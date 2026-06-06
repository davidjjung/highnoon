package com.davigj.high_noon.core;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class HNConfig {
    public static class Common {
        public final ModConfigSpec.ConfigValue<Double> babySunRadius;
        public final ModConfigSpec.ConfigValue<Double> babySunRadiusPassive;
        public final ModConfigSpec.ConfigValue<Double> babySunRadiusY;
        public final ModConfigSpec.ConfigValue<Double> adultSunRadius;
        public final ModConfigSpec.ConfigValue<Double> adultSunRadiusPassive;
        public final ModConfigSpec.ConfigValue<Double> adultSunRadiusY;
        public final ModConfigSpec.ConfigValue<Boolean> worldgenBabies;
        public final ModConfigSpec.ConfigValue<Boolean> hostilesOnlyRetaliate;
        public final ModConfigSpec.ConfigValue<Boolean> lightLevelValue;
        public final ModConfigSpec.ConfigValue<Boolean> phantomsAttackSunnyPlayers;
        public final ModConfigSpec.ConfigValue<Boolean> sunnyBees;
        public final ModConfigSpec.ConfigValue<Boolean> iguanasBask;
        public final ModConfigSpec.ConfigValue<Boolean> sunfishBask;
        public final ModConfigSpec.ConfigValue<Boolean> sunstoneSunny;
        public final ModConfigSpec.ConfigValue<Boolean> eyeblossomShut;
        public final ModConfigSpec.ConfigValue<Boolean> sunFeed;
        public final ModConfigSpec.ConfigValue<Boolean> sunnyParticle;
        public final ModConfigSpec.ConfigValue<Integer> sunDuration;
        public final ModConfigSpec.ConfigValue<Boolean> buttercupAgeless;
        public final ModConfigSpec.ConfigValue<Boolean> stackShrink;
        public final ModConfigSpec.ConfigValue<Boolean> eatingSound;
        public final ModConfigSpec.ConfigValue<Boolean> sunflowersFollowMooblooms;

        Common (ModConfigSpec.Builder builder) {
            builder.push("common");
            builder.push("effect_radius");
            babySunRadius = builder.comment("XZ radius where baby mooblooms grant the Sunny effect").defineInRange("Baby XZ sunny radius", 2.0D, 0.0D, 12.0D);
            babySunRadiusPassive = builder.comment("Reduced XZ radius where baby mooblooms passively grant the Sunny effect, if sun_feed required").defineInRange("Baby passive XZ sunny radius", 0.0D, 0.0D, 12.0D);
            babySunRadiusY = builder.comment("Vertical radius where baby mooblooms grant the Sunny effect").defineInRange("Baby Y sunny radius", 1.0D, 0.0D, 12.0D);
            adultSunRadius = builder.comment("XZ radius where adult mooblooms grant the Sunny effect").defineInRange("Adult XZ sunny radius", 7.0D, 0.0D, 12.0D);
            adultSunRadiusPassive = builder.comment("XZ radius where adult mooblooms grant the Sunny effect, if sun_feed required").defineInRange("Adult passive XZ sunny radius", 3.0D, 0.0D, 12.0D);
            adultSunRadiusY = builder.comment("Vertical radius where adult mooblooms grant the Sunny effect").defineInRange("Adult Y sunny radius", 3.0D, 0.0D, 12.0D);
            builder.pop();
            builder.push("sunny_behaviors");
            hostilesOnlyRetaliate = builder.comment("Only mobs actively 'burned' by a moobloom in the moobloom_hostiles tag will aggro on mooblooms").define("Hostiles only retaliate", true);
            lightLevelValue = builder.comment("Mobs afflicted with Sunny use behaviors affected by skylight, like spiders and endermen").define("Light level dependent magic value is high when sunny", true);
            phantomsAttackSunnyPlayers = builder.comment("Phantoms can still attack players afflicted with Sunny").define("Phantoms can attack sunny players", true);
            sunnyBees = builder.comment("Bees afflicted with Sunny can work even at nighttime").define("Busy bees on sunny nights", true);
            sunFeed = builder.comment("Mooblooms must be fed items from the sun_feed tag to induce Sunny in its typical radius").define("Sunflower feed", true);
            sunDuration = builder.comment("Duration for which mooblooms fed sun_feed are able to induce Sunny").define("Sunflower feed duration", 2400);
            sunnyParticle = builder.comment("Mobs afflicted by Sunny exude buttercup particles").define("Sunny particle indicator", true);
            builder.pop();
            builder.push("eternal_youth");
            worldgenBabies = builder.comment("Mooblooms always spawn as ageless babies during world generation").define("Mooblooms worldgen as ageless babies", true);
            buttercupAgeless = builder.comment("Players can use youth extenders/resumers on baby mob to control their aging, like 26.1's golden dandelions").define("Buttercup ageless", true);
            stackShrink = builder.comment("Using an extender or resumer depletes the stack by one, using the item.").define("ItemStack shrinkage", true);
            eatingSound = builder.comment("Using an extender or resumer plays an eating sound as well as the usual SoundEvent.").define("Eating sound", true);
            builder.pop();
            builder.push("compat");
            builder.push("spawn");
            iguanasBask = builder.comment("Iguanas afflicted with Sunny can bask even during nighttime").define("Iguanas bask on sunny nights", true);
            sunfishBask = builder.comment("Sunfish afflicted with Sunny can bask even during nighttime").define("Sunfish bask on sunny nights", true);
            sunstoneSunny = builder.comment("Entities standing on a sunstone block from Spawn incur the Sunny effect").define("Sunstones sunny", true);
            sunflowersFollowMooblooms = builder.comment("Sunflowers from Spawn follow Mooblooms").define("Sunflowers follow mooblooms", true);
            builder.pop();
            builder.push("vanillabackport");
            eyeblossomShut = builder.comment("Eyeblossoms shut near Mooblooms").define("Eyeblossoms shut", true);
            builder.pop();
            builder.pop();
            builder.pop();
        }
    }

    public static class Client {
        public final ModConfigSpec.ConfigValue<Boolean> youthParticle;
        public final ModConfigSpec.ConfigValue<Boolean> youthParticleDeDeAged;

        public Client(ModConfigSpec.Builder builder) {
            builder.push("client");
            youthParticle = builder.comment("Mobs that can use a youth extender to stop growth let off a buttercup particle when a player holds a youth extender nearby").define("Buttercup particle youth pausation validity indicator", false);
            youthParticleDeDeAged = builder.comment("Mobs that can use a youth resumer to resume growth let off a buttercup particle when a player holds a youth resumer nearby").define("Buttercup particle youth resumption validity indicator", false);
            builder.pop();
        }
    }

    static final ModConfigSpec COMMON_SPEC;
    public static final HNConfig.Common COMMON;
    static final ModConfigSpec CLIENT_SPEC;
    public static final HNConfig.Client CLIENT;

    static {
        final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(HNConfig.Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();

        Pair<Client, ModConfigSpec> clientSpecPair = new ModConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }
}

