package com.juiceybeans.chickens_hatch_now.integration;

import com.juiceybeans.chickens_hatch_now.ChickensHatchNow;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jspecify.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.StreamServerDataProvider;

public class EggComponentProvider implements StreamServerDataProvider<BlockAccessor, Integer> {
    public static final EggComponentProvider INSTANCE = new EggComponentProvider();

    @Override
    public @Nullable Integer streamData(BlockAccessor blockAccessor) {
        return blockAccessor.getBlockState().getValue(BlockStateProperties.HATCH);
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Integer> streamCodec() {
        return ByteBufCodecs.VAR_INT.cast();
    }

    @Override
    public Identifier getUid() {
        return ChickensHatchNow.id("egg_info");
    }
}
