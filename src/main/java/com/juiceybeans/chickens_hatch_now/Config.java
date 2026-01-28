package com.juiceybeans.chickens_hatch_now;

import net.neoforged.neoforge.common.ModConfigSpec;

import org.apache.commons.lang3.tuple.Pair;

public class Config {

    public static final Config INSTANCE;
    public static final ModConfigSpec CONFIG_SPEC;

    public final ModConfigSpec.ConfigValue<Integer> HATCH_PROGRESS_UPDATE;
    public final ModConfigSpec.ConfigValue<Boolean> DISABLE_THROWN_EGG_SPAWNS;

    private Config(ModConfigSpec.Builder builder) {
        HATCH_PROGRESS_UPDATE = builder
                .translation("config.chickens_hatch_now.hatch_progress_update")
                .comment("Time taken to update hatching progress in seconds (default: 180)")
                .gameRestart()
                .define("hatch_progress_update", 180);

        DISABLE_THROWN_EGG_SPAWNS = builder
                .translation("config.chickens_hatch_now.disable_thrown_egg_spawns")
                .comment("Disable chickens spawning from thrown eggs (default: true)")
                .gameRestart()
                .define("disable_thrown_egg_spawns", true);
    }

    static {
        Pair<Config, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Config::new);

        INSTANCE = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }
}
