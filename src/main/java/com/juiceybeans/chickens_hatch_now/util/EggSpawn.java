package com.juiceybeans.chickens_hatch_now.util;

import com.juiceybeans.chickens_hatch_now.ChickensHatchNow;
import com.juiceybeans.chickens_hatch_now.Config;

import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

@EventBusSubscriber(modid = ChickensHatchNow.MOD_ID)
public class EggSpawn {

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        var conf = Config.INSTANCE.DISABLE_THROWN_EGG_SPAWNS.get();

        if (event.getProjectile() instanceof ThrownEgg egg && conf) {
            if (egg.level().isClientSide()) return;

            egg.level().broadcastEntityEvent(egg, (byte) 3);
            egg.discard();
            event.setCanceled(true);

        }
    }
}
