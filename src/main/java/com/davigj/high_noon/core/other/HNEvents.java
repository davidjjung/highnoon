package com.davigj.high_noon.core.other;


import com.davigj.high_noon.core.HNConfig;
import com.davigj.high_noon.core.HighNoon;
import com.davigj.high_noon.core.registry.HNParticleTypes;
import com.davigj.high_noon.core.registry.HNSoundEvents;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.buzzier_bees.common.entity.animal.Moobloom;
import com.teamabnormals.buzzier_bees.core.registry.BBEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;


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
}