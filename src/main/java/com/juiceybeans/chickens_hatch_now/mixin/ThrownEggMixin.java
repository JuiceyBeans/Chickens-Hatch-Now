package com.juiceybeans.chickens_hatch_now.mixin;

import com.juiceybeans.chickens_hatch_now.Config;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThrownEgg.class)
public abstract class ThrownEggMixin extends ThrowableItemProjectile {

    public ThrownEggMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Redirect(
              method = "onHit",
              at = @At(
                       value = "INVOKE",
                       target = "Lnet/minecraft/util/RandomSource;nextInt(I)I",
                       ordinal = 0))
    private int redirectRandomCheck(RandomSource random, int bound) {
        if (Config.CONFIG.DISABLE_THROWN_EGG_SPAWNS.get()) {
            return random.nextInt(bound) + 1;
        } else return random.nextInt(bound);
    }
}
