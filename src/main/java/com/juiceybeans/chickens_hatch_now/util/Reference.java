package com.juiceybeans.chickens_hatch_now.util;

import com.juiceybeans.chickens_hatch_now.block.ModBlocks;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.animal.chicken.ChickenVariants;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.function.Supplier;

public class Reference {

    public static final Map<Item, Supplier<Block>> EGG_ITEM_TO_BLOCK = Map.of(
            Items.EGG, ModBlocks.CHICKEN_EGG,
            Items.BROWN_EGG, ModBlocks.BROWN_CHICKEN_EGG,
            Items.BLUE_EGG, ModBlocks.BLUE_CHICKEN_EGG);

    public static final Map<ResourceKey<ChickenVariant>, Item> CHICKEN_VARIANT_TO_EGG = Map.of(
            ChickenVariants.TEMPERATE, Items.EGG,
            ChickenVariants.WARM, Items.BROWN_EGG,
            ChickenVariants.COLD, Items.BLUE_EGG);
}
