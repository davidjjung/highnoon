package com.davigj.high_noon.core.other.compat;

import com.ninni.spawn.registry.SpawnBlocks;
import net.minecraft.world.entity.LivingEntity;

public class SpawnCompat {
    public static boolean isStandingOnSunstone (LivingEntity living) {
        return living.level().getBlockState(living.blockPosition()).is(SpawnBlocks.SUNSTONE);
    }
}
