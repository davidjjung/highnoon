package com.davigj.high_noon.core.other;


import com.davigj.high_noon.core.HNConfig;
import com.davigj.high_noon.core.HighNoon;
import com.davigj.high_noon.core.other.compat.SpawnCompat;
import com.davigj.high_noon.core.registry.HNCriteriaTriggers;
import com.davigj.high_noon.core.registry.HNParticleTypes;
import com.davigj.high_noon.core.registry.HNSoundEvents;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.buzzier_bees.common.entity.animal.Moobloom;
import com.teamabnormals.buzzier_bees.core.registry.BBEntityTypes;
import com.teamabnormals.buzzier_bees.core.registry.BBMobEffects;
import com.teamabnormals.buzzier_bees.core.registry.BBParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import static com.davigj.high_noon.core.other.compat.ModConstants.SPAWN;


@EventBusSubscriber(modid = HighNoon.MOD_ID)
public class HNEvents {
    @SubscribeEvent
    public static void onFinalizeSpawn(FinalizeSpawnEvent event) {
        if (!HNConfig.COMMON.worldgenBabies.get()) return;
        if (event.getEntity().getType() == BBEntityTypes.MOOBLOOM.get()) {
            Moobloom moo = (Moobloom) event.getEntity();
            if (event.getSpawnType() == MobSpawnType.CHUNK_GENERATION) {
                moo.setBaby(true);
                TrackedDataManager.INSTANCE.setValue(moo, HighNoon.BABIED, true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void youthIntxns(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!HNConfig.COMMON.buttercupAgeless.get()) return;
        ItemStack stack = event.getItemStack();
        Entity target = event.getTarget();
        if (!(target instanceof AgeableMob ageable) || !ageable.isBaby()) return;

        TrackedDataManager manager = TrackedDataManager.INSTANCE;
        Player player = event.getEntity();

        boolean babied = manager.getValue(target, HighNoon.BABIED);
        if (stack.is(HNItemTags.YOUTH_EXTENDERS) && !babied) {
            applyYouthState(event, player, stack, target, manager, false);
            return;
        }
        if (stack.is(HNItemTags.YOUTH_RESUMERS) && babied) {
            applyYouthState(event, player, stack, target, manager, true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void sunnyIntxns(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!HNConfig.COMMON.sunFeed.get()) return;
        ItemStack stack = event.getItemStack();
        Entity target = event.getTarget();
        if (stack.is(HNItemTags.SUN_FEED) && target.getType() == BBEntityTypes.MOOBLOOM.get()) {
            TrackedDataManager manager = TrackedDataManager.INSTANCE;
            Player player = event.getEntity();
            target.playSound(SoundEvents.GENERIC_EAT, 0.5F, 1.0F + (target.getRandom().nextFloat() * 0.2F));
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            if (target.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(HNParticleTypes.GLINT.get(), target.getX(),
                        target.getEyeY(), target.getZ(), 8, 0.65, 0.4, 0.65, 0.01);
            }
            player.swing(event.getHand());
            manager.setValue(target, HighNoon.SUNNY_TIMER, HNConfig.COMMON.sunDuration.get());
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    private static void applyYouthState(PlayerInteractEvent.EntityInteractSpecific event, Player player, ItemStack stack,
                                        Entity target, TrackedDataManager manager, boolean babied) {
        if (!player.getAbilities().instabuild && HNConfig.COMMON.stackShrink.get()) {
            stack.shrink(1);
        }
        if (HNConfig.COMMON.eatingSound.get()) target.playSound(SoundEvents.GENERIC_EAT, 0.5F, 1.2F + (target.getRandom().nextFloat() * 0.2F));
        target.playSound(babied ? HNSoundEvents.YOUTH_RESUMED.get() : HNSoundEvents.YOUTH_PAUSED.get(),
                0.6F, 0.9F + (target.getRandom().nextFloat() * 0.2F));
        player.swing(event.getHand());
        manager.setValue(target, HighNoon.BABIED, !babied);
        manager.setValue(target, babied ? HighNoon.BUTTER_UP : HighNoon.BUTTER_DOWN, 25);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void entityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity living && HNConfig.COMMON.sunnyParticle.get() && living.level() instanceof ServerLevel server) {
            if (living.hasEffect(BBMobEffects.SUNNY) && living.tickCount % 16 == 0) {
                server.sendParticles(BBParticleTypes.BUTTERCUP_BLOOM.get(),
                        entity.getX(), entity.getY() + (entity.getBbHeight() * 0.5), entity.getZ(),
                        2, 0.3, 0.3, 0.3, 0);
            }
        }
        if (entity instanceof LivingEntity living && living.tickCount % 20 == 0 && HNConfig.COMMON.sunstoneSunny.get()) {
            if (SPAWN) {
                if (SpawnCompat.isStandingOnSunstone(living)) {
                    living.addEffect(new MobEffectInstance(BBMobEffects.SUNNY, 100, 0, false, false));
                }
            }
        }
        if (!(entity instanceof AgeableMob ageable) || !ageable.isBaby()) return;
        if (!(entity.level() instanceof ClientLevel level)) return;

        TrackedDataManager manager = TrackedDataManager.INSTANCE;
        RandomSource random = entity.getRandom();

        boolean babied = manager.getValue(entity, HighNoon.BABIED);

        tickTransitionParticles(entity, level, random, manager, babied);

        if (entity.tickCount % 15 != 0) return;
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        boolean holdingExtender = player.isHolding(item -> item.is(HNItemTags.YOUTH_EXTENDERS));
        boolean holdingResumer = player.isHolding(item -> item.is(HNItemTags.YOUTH_RESUMERS));

        boolean showExtendHint = holdingExtender && !babied && HNConfig.CLIENT.youthParticle.get();
        boolean showResumeHint = holdingResumer && babied && HNConfig.CLIENT.youthParticleDeDeAged.get();

        if (!showExtendHint && !showResumeHint) return;

        spawnYouthParticle(level, entity, random, showResumeHint ? 1.0 : -1.0);
    }

    private static void tickTransitionParticles(Entity entity, ClientLevel level, RandomSource random,
                                                TrackedDataManager manager, boolean babied) {
        int butterUp = manager.getValue(entity, HighNoon.BUTTER_UP);
        int butterDown = manager.getValue(entity, HighNoon.BUTTER_DOWN);

        if (!babied && butterUp >= 0) {
            manager.setValue(entity, HighNoon.BUTTER_UP, butterUp - 1);
            if (butterUp % 4 == 0) {
                spawnYouthParticle(level, entity, random, 1.0);
            }
            return;
        }

        if (babied && butterDown >= 0) {
            manager.setValue(entity, HighNoon.BUTTER_DOWN, butterDown - 1);
            if (butterDown % 4 == 0) {
                spawnYouthParticle(level, entity, random, -1.0);
            }
        }
    }

    private static void spawnYouthParticle(ClientLevel level, Entity entity, RandomSource random, double direction) {
        level.addParticle(HNParticleTypes.GLINT.get(),
                entity.getX() + (random.nextGaussian() * 0.3) - (entity.getBbWidth() * 0.4),
                entity.getEyeY() + (direction == -1.0 ? .35 : 0) + (random.nextDouble() * 0.3) + (direction * 0.125),
                entity.getZ() + (random.nextGaussian() * 0.3) - (entity.getBbWidth() * 0.4),
                0, (0.02 + (random.nextDouble() * 0.015)) * direction, 0);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void highNoon(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof Player player && player.tickCount % 30 == 0) {
            if (player.isScoping() && !(player.isInWaterOrRain() ||
                    !player.level().getBlockState(new BlockPos((int) player.getX(), (int) player.getEyeY(), (int) player.getZ())).isAir())) {
                float timeOfDay = player.level().getTimeOfDay(1.0F);
                if ((timeOfDay <= 0.015 || timeOfDay >= 0.985) && player.getLookAngle().y > 0.997) {
                    if (player.level().isClientSide && !player.hasEffect(MobEffects.BLINDNESS)) {
                        player.level().playSound(player, player.blockPosition(), SoundEvents.PLAYER_HURT_ON_FIRE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    } else {
                        if (player instanceof ServerPlayer serverPlayer) {
                            HNCriteriaTriggers.HIGH_NOON.get().trigger(serverPlayer);
                        }
                        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 400, 0, false, false));
                    }
                }
            }
        }
    }
}