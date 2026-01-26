package com.juiceybeans.chickens_hatch_now.tag;

import com.juiceybeans.chickens_hatch_now.ChickensHatchNow;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static final TagKey<Block> CHICKEN_EGG_BLOCKS = TagKey.create(
            Registries.BLOCK,
            ChickensHatchNow.id("chicken_eggs")
    );

    public static final TagKey<Item> CHICKEN_EGG_ITEMS = TagKey.create(
            Registries.ITEM,
            ChickensHatchNow.id("chicken_eggs")
    );
}
