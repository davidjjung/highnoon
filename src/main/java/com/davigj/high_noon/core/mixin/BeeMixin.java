package com.davigj.high_noon.core.mixin;

import com.davigj.high_noon.core.HNConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.teamabnormals.buzzier_bees.core.registry.BBMobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Bee.class)
public abstract class BeeMixin extends Animal {
    protected BeeMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(method = "wantsToEnterHive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isNight()Z"))
    private boolean wantsToEnterHive(boolean original) {
        if (this.hasEffect(BBMobEffects.SUNNY) && original && HNConfig.COMMON.sunnyBees.get()) return false;
        return original;
    }
}
