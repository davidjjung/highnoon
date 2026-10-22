package com.davigj.high_noon.core.mixin;

import com.davigj.high_noon.core.HNConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.ninni.spawn.server.entity.mob.fish.Sunfish;
import com.teamabnormals.buzzier_bees.core.registry.BBMobEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(Sunfish.SunfishBaskingGoal.class)
public class SunfishBaskingGoalMixin {
    @Shadow @Final private Sunfish sunfish;

    @ModifyExpressionValue(method = "canUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isDay()Z"))
    private boolean baskinRobbins(boolean original) {
        if (this.sunfish.hasEffect(BBMobEffects.SUNNY) && HNConfig.COMMON.sunfishBask.get()) {
            return true;
        }
        return original;
    }
}
