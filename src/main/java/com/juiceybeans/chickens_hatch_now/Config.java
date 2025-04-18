package com.juiceybeans.chickens_hatch_now;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = ChickensHatchNow.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.ConfigValue<Integer> HATCH_PROGRESS_UPDATE =
            BUILDER.comment("Time taken to update hatching progress in seconds (default: 180)")
                    .define("hatch_progress_update", 180);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int hatchProgressUpdate;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        hatchProgressUpdate = HATCH_PROGRESS_UPDATE.get();
    }
}
