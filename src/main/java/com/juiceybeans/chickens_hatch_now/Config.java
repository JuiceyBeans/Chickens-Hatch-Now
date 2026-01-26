package com.juiceybeans.chickens_hatch_now;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.ConfigValue<Integer> HATCH_PROGRESS_UPDATE = BUILDER
            .comment("Time taken to update hatching progress in seconds (default: 180)")
            .define("hatch_progress_update", 180);

    private static final ModConfigSpec.ConfigValue<Boolean> DISABLE_THROWN_EGG_SPAWNS = BUILDER
            .comment("Disable chickens spawning from thrown eggs (default: true)")
            .define("disable_thrown_egg_spawns", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int hatchProgressUpdate;
    public static boolean disableThrownEggSpawns;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        hatchProgressUpdate = HATCH_PROGRESS_UPDATE.get();
        disableThrownEggSpawns = DISABLE_THROWN_EGG_SPAWNS.get();
    }
}
