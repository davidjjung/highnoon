package com.davigj.high_noon.core;

import com.davigj.high_noon.core.registry.HNCriteriaTriggers;
import com.davigj.high_noon.core.registry.HNParticleTypes;
import com.davigj.high_noon.core.registry.HNSoundEvents;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedData;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(HighNoon.MOD_ID)
public class HighNoon {
    public static final String MOD_ID = "high_noon";
    public static final TrackedData<Boolean> BABIED = TrackedData.Builder.create(ByteBufCodecs.BOOL, () -> false).enablePersistence().build();
    public static final TrackedData<Integer> BUTTER_DOWN = TrackedData.Builder.create(ByteBufCodecs.INT, () -> -1).enablePersistence().build();
    public static final TrackedData<Integer> BUTTER_UP = TrackedData.Builder.create(ByteBufCodecs.INT, () -> -1).enablePersistence().build();
    public static final TrackedData<Integer> SUNNY_TIMER = TrackedData.Builder.create(ByteBufCodecs.INT, () -> -1).enablePersistence().build();
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);

    public HighNoon(IEventBus bus, ModContainer container) {
        HNParticleTypes.PARTICLE_TYPES.register(bus);
        HNSoundEvents.SOUNDS.register(bus);
        HNCriteriaTriggers.TRIGGERS.register(bus);
        bus.addListener(this::commonSetup);

        container.registerConfig(ModConfig.Type.COMMON, HNConfig.COMMON_SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, HNConfig.CLIENT_SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        TrackedDataManager.INSTANCE.registerData(HighNoon.location("babied"), BABIED);
        TrackedDataManager.INSTANCE.registerData(HighNoon.location("butter_down"), BUTTER_DOWN);
        TrackedDataManager.INSTANCE.registerData(HighNoon.location("butter_up"), BUTTER_UP);
        TrackedDataManager.INSTANCE.registerData(HighNoon.location("sunny_timer"), SUNNY_TIMER);
    }

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}