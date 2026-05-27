package com.davigj.high_noon.core.mixin;

import com.davigj.high_noon.core.HNConfig;
import com.davigj.high_noon.core.HighNoon;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableMob.class)
public abstract class AgeableMobMixin extends PathfinderMob {
    protected AgeableMobMixin(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "setAge", at = @At("HEAD"), cancellable = true)
    private void peterPan(int age, CallbackInfo ci){
        if (TrackedDataManager.INSTANCE.getValue(this, HighNoon.BABIED) && HNConfig.COMMON.buttercupAgeless.get()) {
            ci.cancel();
        }
    }
}
