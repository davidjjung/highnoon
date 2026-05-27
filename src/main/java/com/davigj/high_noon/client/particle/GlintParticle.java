package com.davigj.high_noon.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class GlintParticle extends SuspendedTownParticle {
    protected GlintParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.lifetime = 12 + level.getRandom().nextInt(10);
    }

//    @Override
//    public @NotNull ParticleRenderType getRenderType() {
//        return ParticleRenderType.PARTICLE_SHEET_LIT;
//    }

//    @Override
//    public int getLightColor(float tint) {
//        return 15189611;
//    }


    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprites) {
            this.sprite = sprites;
        }

        public Particle createParticle(
                SimpleParticleType type,
                ClientLevel level,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed
        ) {
            GlintParticle suspendedtownparticle = new GlintParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
            suspendedtownparticle.pickSprite(this.sprite);
            return suspendedtownparticle;
        }
    }
}