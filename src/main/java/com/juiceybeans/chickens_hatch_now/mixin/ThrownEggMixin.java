package com.juiceybeans.chickens_hatch_now.mixin;

import com.juiceybeans.chickens_hatch_now.Config;

import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.minecraft.world.phys.HitResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEgg.class)
public class ThrownEggMixin {

    @Inject(method = "onHit", at = @At(value = "HEAD"), cancellable = true)
    private void cancelSpawn(HitResult result, CallbackInfo ci) {
        if (Config.disableThrownEggSpawns) {
            ci.cancel();
        }
    }
}
