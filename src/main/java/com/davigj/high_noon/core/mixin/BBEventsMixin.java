package com.davigj.high_noon.core.mixin;

import com.davigj.high_noon.core.HNConfig;
import com.teamabnormals.buzzier_bees.core.other.BBEvents;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BBEvents.class)
public class BBEventsMixin {
    @Inject(method = "onLivingSpawned", at = @At("HEAD"), cancellable = true)
    private static void neutralizeGoal(EntityJoinLevelEvent event, CallbackInfo ci){
        if (HNConfig.COMMON.hostilesOnlyRetaliate.get()) ci.cancel();
    }

    @Inject(method = "onEntityUpdate", at = @At("HEAD"), cancellable = true)
    private static void neutralizeOtherGoal(EntityTickEvent.Pre event, CallbackInfo ci){
        if (HNConfig.COMMON.phantomsAttackSunnyPlayers.get()) ci.cancel();
    }
}
