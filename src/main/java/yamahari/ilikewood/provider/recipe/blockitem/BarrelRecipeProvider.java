package yamahari.ilikewood.provider.recipe.blockitem;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.util.IItemProvider;
import yamahari.ilikewood.ILikeWood;
import yamahari.ilikewood.data.tag.ILikeWoodBlockTags;
import yamahari.ilikewood.registry.objecttype.WoodenBlockType;
import yamahari.ilikewood.registry.woodtype.IWoodType;
import yamahari.ilikewood.util.IWooden;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public final class BarrelRecipeProvider extends AbstractBlockItemRecipeProvider {
    public BarrelRecipeProvider(final DataGenerator generator) {
        super(generator, WoodenBlockType.BARREL);
    }

    @Override
    protected void registerRecipe(final Block block, @Nonnull final Consumer<IFinishedRecipe> consumer) {
        final IWoodType woodType = ((IWooden) block).getWoodType();
        final IItemProvider panels = ILikeWood.getBlock(woodType, WoodenBlockType.PANELS);
        final IItemProvider slab = ILikeWood.getBlock(woodType, WoodenBlockType.PANELS_SLAB);

        ShapedRecipeBuilder
            .shapedRecipe(block)
            .key('P', panels)
            .key('S', slab)
            .patternLine("PSP")
            .patternLine("P P")
            .patternLine("PSP")
            .addCriterion("has_panels", hasItem(panels))
            .setGroup(ILikeWoodBlockTags.BARRELS.getName().getPath())
            .build(consumer);
    }
}
