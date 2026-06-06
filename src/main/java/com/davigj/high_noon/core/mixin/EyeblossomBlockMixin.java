package com.davigj.high_noon.core.mixin;

import com.blackgear.vanillabackport.common.level.blocks.EyeblossomBlock;
import com.davigj.high_noon.core.HNConfig;
import com.davigj.high_noon.core.HighNoon;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.buzzier_bees.common.entity.animal.Moobloom;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(EyeblossomBlock.class)
public class EyeblossomBlockMixin {
    @Shadow
    @Final
    private EyeblossomBlock.Type type;

    @Inject(method = "tryChangingState", at = @At("HEAD"), cancellable = true)
    private void shutEye(BlockState state, ServerLevel level, BlockPos origin, RandomSource random, CallbackInfoReturnable<Boolean> cir) {
        if (!HNConfig.COMMON.eyeblossomShut.get()) return;
        List<Moobloom> mooblooms = level.getEntitiesOfClass(Moobloom.class, new AABB(origin).inflate(HNConfig.COMMON.adultSunRadius.get()));
        if (HNConfig.COMMON.sunFeed.get()) {
            List<Moobloom> newBlooms = new ArrayList<>();
            for (Moobloom moo : mooblooms) {
                boolean markToAdd;
                boolean baby = moo.isBaby();
                double dist = moo.distanceToSqr(origin.getX() + 0.5, origin.getY() + 0.5, origin.getZ() + 0.5);
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
        if (this.type == EyeblossomBlock.Type.OPEN) {
            EyeblossomBlock.Type closedType = this.type.transform();
            level.setBlock(origin, closedType.state(), 3);
            level.gameEvent(GameEvent.BLOCK_CHANGE, origin, GameEvent.Context.of(state));
            closedType.spawnTransformParticle(level, origin, random);
            BlockPos.betweenClosed(origin.offset(-3, -2, -3), origin.offset(3, 2, 3)).forEach((pos) -> {
                BlockState closeState = level.getBlockState(pos);
                if (closeState == state) {
                    double distance = Math.sqrt(origin.distSqr(pos));
                    int ticks = random.nextIntBetweenInclusive((int) (distance * 5.0), (int) (distance * 10.0));
                    level.scheduleTick(pos, state.getBlock(), ticks);
                }
            });
            cir.setReturnValue(true);
        } else cir.setReturnValue(false);
    }

    @Inject(method = "entityInside", at = @At("HEAD"))
    private void shutEyeTheSqueakquel(BlockState state, Level level, BlockPos blockPos, Entity entity, CallbackInfo ci) {
        if (entity instanceof Moobloom && this.type == EyeblossomBlock.Type.OPEN && level instanceof ServerLevel serverLevel) {
            EyeblossomBlock.Type type = this.type.transform();
            level.setBlock(blockPos, type.state(), 3);
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(state));
            BlockPos.betweenClosed(blockPos.offset(-3, -2, -3), blockPos.offset(3, 2, 3)).forEach((pos) -> {
                BlockState closeState = level.getBlockState(pos);
                if (closeState == state) {
                    double distance = Math.sqrt(blockPos.distSqr(pos));
                    int ticks = level.random.nextIntBetweenInclusive((int) (distance * 5.0), (int) (distance * 10.0));
                    level.scheduleTick(pos, state.getBlock(), ticks);
                }
            });
            type.spawnTransformParticle(serverLevel, blockPos, level.getRandom());
        }
    }
}
