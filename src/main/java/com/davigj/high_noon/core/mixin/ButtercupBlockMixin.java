package com.davigj.high_noon.core.mixin;

import com.teamabnormals.blueprint.core.util.MathUtil;
import com.teamabnormals.buzzier_bees.common.block.ButtercupBlock;
import com.teamabnormals.buzzier_bees.core.registry.BBParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ButtercupBlock.class)
public class ButtercupBlockMixin {
    @Inject(method = "animateTick", at = @At("HEAD"), cancellable = true)
    private void animateOffset(BlockState state, Level level, BlockPos pos, RandomSource rand, CallbackInfo ci){
        if (level.getGameTime() % 2L == 0L) {
            for(int i = 0; i < 2 + rand.nextInt(5); ++i) {
                double x = (double)pos.getX() + 0.5 + state.getOffset(level, pos).x + MathUtil.makeNegativeRandomly((double)(rand.nextFloat() * 0.25F), rand);
                double y = (double)pos.getY() + 0.5;
                double z = (double)pos.getZ() + 0.5 + state.getOffset(level, pos).z + MathUtil.makeNegativeRandomly((double)(rand.nextFloat() * 0.05F), rand);
                level.addParticle((ParticleOptions) BBParticleTypes.BUTTERCUP_BLOOM.get(), x, y, z, MathUtil.makeNegativeRandomly(0.01, rand), 0.05 + MathUtil.makeNegativeRandomly(0.02, rand), MathUtil.makeNegativeRandomly(0.01, rand));
            }
        }
        ci.cancel();
    }
}
