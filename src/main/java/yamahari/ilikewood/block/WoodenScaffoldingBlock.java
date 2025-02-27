package yamahari.ilikewood.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.EntitySelectionContext;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import yamahari.ilikewood.data.tag.ILikeWoodItemTags;
import yamahari.ilikewood.item.WoodenScaffoldingItem;
import yamahari.ilikewood.registry.woodtype.IWoodType;
import yamahari.ilikewood.util.IWooden;

import javax.annotation.Nonnull;
import java.util.Random;

public final class WoodenScaffoldingBlock extends ScaffoldingBlock implements IWooden {
    final IWoodType woodType;

    public WoodenScaffoldingBlock(final IWoodType woodType) {
        super(Block.Properties.from(Blocks.SCAFFOLDING));
        this.woodType = woodType;
    }

    public static int getDistance(final IBlockReader reader, final BlockPos pos) {
        final BlockPos.Mutable mutable =
            (new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ())).move(Direction.DOWN);
        final BlockState state = reader.getBlockState(mutable);
        int distance = 7;
        if (state.getBlock() instanceof ScaffoldingBlock) {
            distance = state.get(DISTANCE);
        } else if (state.isSolidSide(reader, mutable, Direction.UP)) {
            return 0;
        }

        for (final Direction direction : Direction.Plane.HORIZONTAL) {
            final BlockState translatedState = reader.getBlockState(mutable.setPos(pos).move(direction));
            if (translatedState.getBlock() instanceof ScaffoldingBlock) {
                distance = Math.min(distance, translatedState.get(DISTANCE) + 1);
                if (distance == 1) {
                    break;
                }
            }
        }
        return distance;
    }

    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull final BlockState state, @Nonnull final IBlockReader reader,
                               @Nonnull final BlockPos pos, @Nonnull final ISelectionContext context) {
        final boolean flag;
        if (context instanceof EntitySelectionContext) {
            flag = ((EntitySelectionContext) context).item instanceof WoodenScaffoldingItem;
        } else {
            flag = ILikeWoodItemTags.SCAFFOLDINGS.getAllElements().stream().anyMatch(context::hasItem);
        }
        if (flag) {
            return VoxelShapes.fullCube();
        }
        return state.get(BOTTOM) ? FULL_SHAPE : TOP_SLAB_SHAPE;
    }

    @Override
    public boolean isReplaceable(@Nonnull final BlockState state, final BlockItemUseContext context) {
        return context.getItem().getItem() instanceof WoodenScaffoldingItem;
    }

    @Override
    public void tick(final BlockState state, @Nonnull final ServerWorld world, @Nonnull final BlockPos pos,
                     @Nonnull final Random rand) {
        final int distance = getDistance(world, pos);
        final BlockState blockState =
            state.with(DISTANCE, distance).with(BOTTOM, this.hasScaffoldingBelow(world, pos, distance));
        if (blockState.get(DISTANCE) == 7) {
            if (state.get(DISTANCE) == 7) {
                world.addEntity(new FallingBlockEntity(world,
                    (double) pos.getX() + 0.5D,
                    pos.getY(),
                    (double) pos.getZ() + 0.5D,
                    blockState.with(WATERLOGGED, Boolean.FALSE)));
            } else {
                world.destroyBlock(pos, true);
            }
        } else if (state != blockState) {
            world.setBlockState(pos, blockState, 3);
        }
    }

    @Override
    public boolean isValidPosition(@Nonnull final BlockState state, @Nonnull final IWorldReader world,
                                   @Nonnull final BlockPos pos) {
        return getDistance(world, pos) < 7;
    }

    @Override
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        final BlockPos blockpos = context.getPos();
        final World world = context.getWorld();
        final int distance = getDistance(world, blockpos);
        return this
            .getDefaultState()
            .with(WATERLOGGED, world.getFluidState(blockpos).getFluid() == Fluids.WATER)
            .with(DISTANCE, distance)
            .with(BOTTOM, this.hasScaffoldingBelow(world, blockpos, distance));
    }

    @Override
    public boolean isScaffolding(final BlockState state, final IWorldReader world, final BlockPos pos,
                                 final LivingEntity entity) {
        return true;
    }

    @Override
    public IWoodType getWoodType() {
        return this.woodType;
    }
}
