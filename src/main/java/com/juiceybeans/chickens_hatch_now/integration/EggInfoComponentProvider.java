package com.juiceybeans.chickens_hatch_now.integration;

/*import com.juiceybeans.chickens_hatch_now.ChickensHatchNow;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.Optional;

public class EggInfoComponentProvider implements IBlockComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        Optional<Integer> hatch_progress = EggInfoDataProvider.INSTANCE.decodeFromData(blockAccessor);

        hatch_progress.ifPresent(i -> tooltip.add(Component.translatable("chicken_hatch_now.jade.hatch_progress", i)));
    }

    @Override
    public Identifier getUid() {
        return ChickensHatchNow.id("egg_info");
    }
}*/