package com.davigj.high_noon.core.mixin;

import com.davigj.high_noon.core.HNConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.ninni.spawn.server.entity.base.AbstractIguana;
import com.teamabnormals.buzzier_bees.core.registry.BBMobEffects;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(AbstractIguana.IguanaPreferredBaskGoal.class)
public abstract class IguanaPreferredBaskGoalMixin extends MoveToBlockGoal {
    public IguanaPreferredBaskGoalMixin(PathfinderMob mob, double speedModifier, int searchRange) {
        super(mob, speedModifier, searchRange);
    }

    @ModifyExpressionValue(method = "canUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isDay()Z"))
    private boolean baskinRobbins(boolean original) {
        if (this.mob.hasEffect(BBMobEffects.SUNNY) && HNConfig.COMMON.iguanasBask.get()) {
            return true;
        }
        return original;
    }
}
