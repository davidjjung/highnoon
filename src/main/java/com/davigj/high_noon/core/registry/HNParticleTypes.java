package com.davigj.high_noon.core.registry;

import com.davigj.high_noon.client.particle.GlintParticle;
import com.davigj.high_noon.core.HighNoon;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = HighNoon.MOD_ID, value = Dist.CLIENT)
public class HNParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, HighNoon.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GLINT = PARTICLE_TYPES.register("glint", () -> new SimpleParticleType(true));

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(HNParticleTypes.GLINT.get(), GlintParticle.Provider::new);
    }
}
