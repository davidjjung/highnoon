package com.davigj.high_noon.core.registry;

import com.davigj.high_noon.core.HighNoon;
import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

public class HNSoundEvents {
    public static final SoundSubRegistryHelper SOUNDS = HighNoon.REGISTRY_HELPER.getSoundSubHelper();

    public static final DeferredHolder<SoundEvent, SoundEvent> YOUTH_PAUSED = SOUNDS.createSoundEvent("item.buttercup.pause");
    public static final DeferredHolder<SoundEvent, SoundEvent> YOUTH_RESUMED = SOUNDS.createSoundEvent("item.buttercup.resume");
}
