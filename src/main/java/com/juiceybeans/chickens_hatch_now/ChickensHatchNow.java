package com.juiceybeans.chickens_hatch_now;

import com.juiceybeans.chickens_hatch_now.block.ModBlocks;

import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

@Mod(ChickensHatchNow.MOD_ID)
public class ChickensHatchNow {

    public static final String MOD_ID = "chickens_hatch_now";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public ChickensHatchNow(IEventBus bus, ModContainer container) {
        ModBlocks.BLOCKS.register(bus);

        container.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
