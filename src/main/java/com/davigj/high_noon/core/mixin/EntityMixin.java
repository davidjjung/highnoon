package com.davigj.high_noon.core.mixin;

import com.davigj.high_noon.core.HNConfig;
import com.teamabnormals.buzzier_bees.core.registry.BBMobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "getLightLevelDependentMagicValue", at = @At("HEAD"), cancellable = true)
    private void sunny(CallbackInfoReturnable<Float> cir){
        if (HNConfig.COMMON.lightLevelValue.get() && (Entity) (Object) this instanceof LivingEntity living && living.hasEffect(BBMobEffects.SUNNY)) {
            cir.setReturnValue(1.0F);
        }
    }
}
