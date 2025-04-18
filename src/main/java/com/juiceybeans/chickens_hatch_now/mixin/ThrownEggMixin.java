package com.juiceybeans.chickens_hatch_now.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ThrownEgg.class)
public abstract class ThrownEggMixin extends ThrowableItemProjectile {

    public ThrownEggMixin(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    /**
     * @author JuiceyBeans
     * @reason Temp implementation, remember to change this if releasing publicly
     */
    @Overwrite
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
    }
}