package com.juiceybeans.chickens_hatch_now.block;

import com.juiceybeans.chickens_hatch_now.ChickensHatchNow;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.animal.chicken.ChickenVariants;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ChickensHatchNow.MOD_ID);

    public static final DeferredBlock<Block> CHICKEN_EGG = createChickenEgg(null, ChickenVariants.TEMPERATE, MapColor.COLOR_LIGHT_GRAY);
    public static final DeferredBlock<Block> BLUE_CHICKEN_EGG = createChickenEgg("blue", ChickenVariants.COLD, MapColor.COLOR_LIGHT_BLUE);
    public static final DeferredBlock<Block> BROWN_CHICKEN_EGG = createChickenEgg("brown", ChickenVariants.WARM, MapColor.COLOR_BROWN);

    public static DeferredBlock<Block> createChickenEgg(String color, ResourceKey<ChickenVariant> variant, MapColor mapColor) {
        String id = (color == null) ? "chicken_egg" : color + "_chicken_egg";

        return BLOCKS.register(id,
                registryName -> new ChickenEggBlock(BlockBehaviour.Properties.of()
                        .setId(ResourceKey.create(Registries.BLOCK, registryName))
                        .mapColor(mapColor)
                        .forceSolidOn()
                        .strength(0.5F)
                        .sound(SoundType.METAL)
                        .randomTicks()
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                        variant
                ));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}