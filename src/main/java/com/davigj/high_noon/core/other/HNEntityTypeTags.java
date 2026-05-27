package com.davigj.high_noon.core.other;

import com.davigj.high_noon.core.HighNoon;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class HNEntityTypeTags {
    public static final TagKey<EntityType<?>> NON_AGEABLE_MOB_BABIES = entityTypeTag("non_ageable_mob_babies");

    private static TagKey<EntityType<?>> entityTypeTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, HighNoon.location(name));
    }
}
