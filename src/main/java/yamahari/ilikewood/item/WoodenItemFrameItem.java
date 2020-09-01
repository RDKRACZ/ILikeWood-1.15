package yamahari.ilikewood.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.LazyValue;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import yamahari.ilikewood.entity.WoodenItemFrameEntity;
import yamahari.ilikewood.registry.WoodenEntityTypes;
import yamahari.ilikewood.util.WoodType;
import yamahari.ilikewood.util.WoodenObjectType;

public final class WoodenItemFrameItem extends WoodenItem {
    private final LazyValue<EntityType<? extends ItemFrameEntity>> entityType;

    @SuppressWarnings("unchecked")
    public WoodenItemFrameItem(final WoodType woodType) {
        super(woodType, WoodenObjectType.ITEM_FRAME, new Item.Properties().group(ItemGroup.DECORATIONS));
        this.entityType = new LazyValue<>(() ->
                (EntityType<? extends ItemFrameEntity>) WoodenEntityTypes.getEntityType(WoodenObjectType.ITEM_FRAME, this.getWoodType()));
    }

    @Override
    public ActionResultType onItemUse(final ItemUseContext context) {
        final BlockPos blockPos = context.getPos();
        final Direction direction = context.getFace();
        final BlockPos offsetPos = blockPos.offset(direction);
        final PlayerEntity player = context.getPlayer();
        final ItemStack itemStack = context.getItem();

        if (player != null && !this.canPlace(player, direction, itemStack, offsetPos)) {
            return ActionResultType.FAIL;
        } else {
            final World world = context.getWorld();
            final HangingEntity hangingEntity =
                    new WoodenItemFrameEntity(this.getWoodType(), this.entityType.getValue(), world, offsetPos, direction);

            final CompoundNBT compoundNBT = itemStack.getTag();
            if (compoundNBT != null) {
                EntityType.applyItemNBT(world, player, hangingEntity, compoundNBT);
            }

            if (hangingEntity.onValidSurface()) {
                if (!world.isRemote) {
                    hangingEntity.playPlaceSound();
                    world.addEntity(hangingEntity);
                }

                itemStack.shrink(1);
                return ActionResultType.func_233537_a_(world.isRemote);
            } else {
                return ActionResultType.CONSUME;
            }
        }
    }

    private boolean canPlace(final PlayerEntity player, final Direction direction, final ItemStack itemStack, final BlockPos blockPos) {
        // return !direction.getAxis().isVertical() && player.canPlayerEdit(blockPos, direction, itemStack);
        return !World.isOutsideBuildHeight(blockPos) && player.canPlayerEdit(blockPos, direction, itemStack);
    }

    @Override
    public int getBurnTime(final ItemStack itemStack) {
        return -1;
    }
}
