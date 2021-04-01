package yamahari.ilikewood.provider.recipe.blockitem;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import yamahari.ilikewood.data.tag.ILikeWoodBlockTags;
import yamahari.ilikewood.provider.recipe.AbstractBlockItemRecipeProvider;
import yamahari.ilikewood.registry.WoodenItems;
import yamahari.ilikewood.registry.woodtype.IWoodType;
import yamahari.ilikewood.util.IWooden;
import yamahari.ilikewood.util.WoodenObjectType;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class SoulTorchRecipeProvider extends AbstractBlockItemRecipeProvider {
    public SoulTorchRecipeProvider(final DataGenerator generator) {
        super(generator, WoodenObjectType.SOUL_TORCH);
    }

    @Override
    protected void registerRecipe(final Block block, @Nonnull final Consumer<IFinishedRecipe> consumer) {
        final IWoodType woodType = ((IWooden) block).getWoodType();
        final IItemProvider stick = WoodenItems.getItem(WoodenObjectType.STICK, woodType);
        final Ingredient coals = Ingredient.fromTag(ItemTags.COALS);
        final Ingredient soul = Ingredient.fromTag(ItemTags.SOUL_FIRE_BASE_BLOCKS);

        ShapedRecipeBuilder
            .shapedRecipe(block, 4)
            .key('I', stick)
            .key('#', coals)
            .key('S', soul)
            .patternLine("#")
            .patternLine("I")
            .patternLine("S")
            .addCriterion("has_soul_fire_base", hasItem(ItemTags.SOUL_FIRE_BASE_BLOCKS))
            .setGroup(ILikeWoodBlockTags.SOUL_TORCHES.getName().getPath())
            .build(consumer);
    }
}
