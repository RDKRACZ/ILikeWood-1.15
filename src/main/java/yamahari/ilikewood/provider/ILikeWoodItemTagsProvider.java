package yamahari.ilikewood.provider;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import yamahari.ilikewood.ILikeWood;
import yamahari.ilikewood.data.tag.ILikeWoodItemTags;
import yamahari.ilikewood.item.tiered.IWoodenTieredItem;
import yamahari.ilikewood.plugin.vanilla.VanillaWoodenItemTiers;
import yamahari.ilikewood.registry.objecttype.WoodenBlockType;
import yamahari.ilikewood.registry.objecttype.WoodenItemType;
import yamahari.ilikewood.registry.objecttype.WoodenTieredItemType;
import yamahari.ilikewood.util.Constants;

public final class ILikeWoodItemTagsProvider extends ItemTagsProvider {
    public ILikeWoodItemTagsProvider(final DataGenerator generator, final ILikeWoodBlockTagsProvider blockTagsProvider,
                                     final ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, Constants.MOD_ID, existingFileHelper);
    }

    private void registerTag(final ITag.INamedTag<Item> tag, final WoodenBlockType blockType) {
        if (blockType.equals(WoodenBlockType.WHITE_BED)) {
            this
                .getOrCreateBuilder(tag)
                .add(ILikeWood.BLOCK_ITEM_REGISTRY.getObjects(WoodenBlockType.getBeds()).toArray(Item[]::new));
        } else {
            this.getOrCreateBuilder(tag).add(ILikeWood.BLOCK_ITEM_REGISTRY.getObjects(blockType).toArray(Item[]::new));
        }
    }

    private void registerTag(final ITag.INamedTag<Item> tag, final WoodenItemType itemType) {
        this.getOrCreateBuilder(tag).add(ILikeWood.ITEM_REGISTRY.getObjects(itemType).toArray(Item[]::new));
    }

    private void registerTag(final ITag.INamedTag<Item> tag, final WoodenTieredItemType tieredItemType) {
        this
            .getOrCreateBuilder(tag)
            .add(ILikeWood.TIERED_ITEM_REGISTRY.getObjects(tieredItemType).toArray(Item[]::new));
    }

    @Override
    protected void registerTags() {
        registerTag(ILikeWoodItemTags.BARRELS, WoodenBlockType.BARREL);
        registerTag(ILikeWoodItemTags.CHESTS, WoodenBlockType.CHEST);
        registerTag(Tags.Items.CHESTS, WoodenBlockType.CHEST);
        registerTag(Tags.Items.CHESTS_WOODEN, WoodenBlockType.CHEST);
        registerTag(ILikeWoodItemTags.COMPOSTER, WoodenBlockType.COMPOSTER);
        registerTag(ILikeWoodItemTags.BOOKSHELFS, WoodenBlockType.BOOKSHELF);
        registerTag(ILikeWoodItemTags.PANELS_SLABS, WoodenBlockType.PANELS_SLAB);
        registerTag(ILikeWoodItemTags.PANELS_STAIRS, WoodenBlockType.PANELS_STAIRS);
        registerTag(ILikeWoodItemTags.PANELS, WoodenBlockType.PANELS);
        registerTag(ILikeWoodItemTags.WALLS, WoodenBlockType.WALL);
        registerTag(ILikeWoodItemTags.LADDERS, WoodenBlockType.LADDER);
        registerTag(ILikeWoodItemTags.TORCHES, WoodenBlockType.TORCH);
        registerTag(ILikeWoodItemTags.STICKS, WoodenItemType.STICK);
        registerTag(Tags.Items.RODS, WoodenItemType.STICK);
        registerTag(ILikeWoodItemTags.CRAFTING_TABLES, WoodenBlockType.CRAFTING_TABLE);
        registerTag(ILikeWoodItemTags.SCAFFOLDINGS, WoodenBlockType.SCAFFOLDING);
        registerTag(ILikeWoodItemTags.LECTERNS, WoodenBlockType.LECTERN);
        registerTag(ILikeWoodItemTags.POSTS, WoodenBlockType.POST);
        registerTag(ILikeWoodItemTags.STRIPPED_POSTS, WoodenBlockType.STRIPPED_POST);
        registerTag(ILikeWoodItemTags.BOWS, WoodenItemType.BOW);
        registerTag(ILikeWoodItemTags.CROSSBOWS, WoodenItemType.CROSSBOW);
        registerTag(ILikeWoodItemTags.ITEM_FRAMES, WoodenItemType.ITEM_FRAME);
        registerTag(ILikeWoodItemTags.BEDS, WoodenBlockType.WHITE_BED);
        registerTag(ILikeWoodItemTags.SAWMILLS, WoodenBlockType.SAWMILL);
        registerTag(ILikeWoodItemTags.FISHING_POLES, WoodenItemType.FISHING_ROD);
        registerTag(ILikeWoodItemTags.SOUL_TORCHES, WoodenBlockType.SOUL_TORCH);
        registerTag(ItemTags.PIGLIN_REPELLENTS, WoodenBlockType.SOUL_TORCH);

        registerTag(ILikeWoodItemTags.AXES, WoodenTieredItemType.AXE);
        registerTag(ILikeWoodItemTags.HOES, WoodenTieredItemType.HOE);
        registerTag(ILikeWoodItemTags.PICKAXES, WoodenTieredItemType.PICKAXE);
        registerTag(ILikeWoodItemTags.SHOVELS, WoodenTieredItemType.SHOVEL);
        registerTag(ILikeWoodItemTags.SWORDS, WoodenTieredItemType.SWORD);

        this
            .getOrCreateBuilder(ItemTags.PIGLIN_LOVED)
            .add(WoodenTieredItemType
                .getAll()
                .flatMap(ILikeWood.TIERED_ITEM_REGISTRY::getObjects)
                .filter(item -> ((IWoodenTieredItem) item).getWoodenItemTier().equals(VanillaWoodenItemTiers.GOLDEN))
                .toArray(Item[]::new));
    }

    @Override
    public String getName() {
        return "I Like Wood - Item Tags";
    }
}
