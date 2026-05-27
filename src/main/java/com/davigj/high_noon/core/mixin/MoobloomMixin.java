package com.davigj.high_noon.core.mixin;

import com.davigj.high_noon.core.HNConfig;
import com.davigj.high_noon.core.HighNoon;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.buzzier_bees.common.entity.animal.Moobloom;
import com.teamabnormals.buzzier_bees.core.other.tags.BBEntityTypeTags;
import com.teamabnormals.buzzier_bees.core.registry.BBMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(Moobloom.class)
public abstract class MoobloomMixin extends Cow {
    public MoobloomMixin(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "aiStep", at = @At("HEAD"), cancellable = true)
    private void injected(CallbackInfo ci) {
        super.aiStep();
        if (!this.level().isClientSide && this.level().getGameTime() % 20L == 0L) {
            double xzRadius = this.isBaby() ? HNConfig.COMMON.babySunRadius.get() : HNConfig.COMMON.adultSunRadius.get();
            double yRadius = this.isBaby() ? HNConfig.COMMON.babySunRadiusY.get() : HNConfig.COMMON.adultSunRadiusY.get();
            int sunTime = TrackedDataManager.INSTANCE.getValue(this, HighNoon.SUNNY_TIMER);
            if (HNConfig.COMMON.sunFeed.get() && sunTime <= 0) {
                xzRadius = this.isBaby() ? HNConfig.COMMON.babySunRadiusPassive.get() : HNConfig.COMMON.adultSunRadiusPassive.get();
            } else {
                TrackedDataManager.INSTANCE.setValue(this, HighNoon.SUNNY_TIMER, sunTime - 1);
            }
            Iterator<LivingEntity> var1 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(xzRadius, yRadius, xzRadius)).iterator();

            while (var1.hasNext()) {
                LivingEntity living = (LivingEntity) var1.next();
                if (!(living instanceof Moobloom)) {
                    living.addEffect(new MobEffectInstance(BBMobEffects.SUNNY, 100, 0, false, false));
                    if (living.getType().is(BBEntityTypeTags.MOOBLOOM_HOSTILES)) {
                        living.setLastHurtByMob(this);
                        if (living instanceof Mob mob) {
                            mob.setTarget(this);
                        }
                    }
                }
            }
        }
        ci.cancel();
    }
}
