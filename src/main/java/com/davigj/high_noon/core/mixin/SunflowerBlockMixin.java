package com.davigj.high_noon.core.mixin;

import com.davigj.high_noon.core.HNConfig;
import com.davigj.high_noon.core.HighNoon;
import com.ninni.spawn.server.block.SunflowerBlock;
import com.ninni.spawn.server.block.state.properties.SunflowerRotation;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.buzzier_bees.common.entity.animal.Moobloom;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.ninni.spawn.server.block.SunflowerBlock.ROTATION;
import static com.ninni.spawn.server.block.SunflowerBlock.SEEDS;
import static net.minecraft.world.level.block.DoublePlantBlock.HALF;

@Pseudo
@Mixin(SunflowerBlock.class)
public class SunflowerBlockMixin {

    @Inject(method = "tryAdvanceRotation", at = @At("HEAD"), cancellable = true)
    private void moobloomSeekingMissile(BlockState state, ServerLevel level, BlockPos pos, CallbackInfo ci) {
        // waiter waiter! more features that literally nobody will ever notice please !!!
        if (!HNConfig.COMMON.sunflowersFollowMooblooms.get()) return;
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) return;
        if (!(level.dimension() == Level.OVERWORLD)) return;

        List<Moobloom> mooblooms = level.getEntitiesOfClass(Moobloom.class, new AABB(pos).inflate(HNConfig.COMMON.adultSunRadius.get()));
        if (HNConfig.COMMON.sunFeed.get()) {
            List<Moobloom> newBlooms = new ArrayList<>();
            for (Moobloom moo: mooblooms) {
                boolean markToAdd;
                boolean baby = moo.isBaby();
                double dist = moo.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                if (TrackedDataManager.INSTANCE.getValue(moo, HighNoon.SUNNY_TIMER) > 0) {
                    markToAdd = !baby || dist <= HNConfig.COMMON.babySunRadius.get();
                } else {
                    markToAdd = baby ? dist <= HNConfig.COMMON.babySunRadiusPassive.get() : dist <= HNConfig.COMMON.adultSunRadiusPassive.get() + 3;
                }
                if (markToAdd) newBlooms.add(moo);
            }
            mooblooms = newBlooms;
        }
        if (mooblooms.isEmpty()) return;

        Moobloom nearest = mooblooms.stream().min(Comparator.comparingDouble( // TIL abt Comparator thx stackoverflow
                m -> m.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5))).orElse(null);

        double dx = nearest.getX() - pos.getX();
        SunflowerRotation target = hn$moobloomChaser(dx);
        SunflowerRotation current = state.getValue(ROTATION);
        if (current != target) {
            boolean seeds = state.getValue(SEEDS) || (current == SunflowerRotation.NIGHT && target == SunflowerRotation.MORNING);
            level.setBlock(pos, state.setValue(ROTATION, target).setValue(SEEDS, seeds), 2);
        }
        ci.cancel();
    }

    @Unique
    private SunflowerRotation hn$moobloomChaser(double dx) {
        if (dx > 0.25) {
            return SunflowerRotation.MORNING;
        } else if (dx < -0.25) {
            return SunflowerRotation.EVENING;
        } else {
            return SunflowerRotation.DAY;
        }
    }
}
