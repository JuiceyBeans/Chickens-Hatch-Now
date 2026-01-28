package com.juiceybeans.chickens_hatch_now.integration.jade;

import com.juiceybeans.chickens_hatch_now.ChickensHatchNow;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum EggInfoProvider implements IBlockComponentProvider {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        var hatchProgress = blockAccessor.getBlockState().getOptionalValue(BlockStateProperties.HATCH);

        hatchProgress.ifPresent(progress -> {
            tooltip.add(Component.translatable("chicken_hatch_now.jade.hatch_progress", progress));
        });
    }

    @Override
    public ResourceLocation getUid() {
        return ChickensHatchNow.id("egg_info");
    }
}
