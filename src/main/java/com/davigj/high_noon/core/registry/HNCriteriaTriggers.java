package com.davigj.high_noon.core.registry;

import com.davigj.high_noon.core.HighNoon;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public class HNCriteriaTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, HighNoon.MOD_ID);

    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> HIGH_NOON = TRIGGERS.register("high_noon", PlayerTrigger::new);

    public static Criterion<PlayerTrigger.TriggerInstance> highNoon() {
        return HIGH_NOON.get().createCriterion(new PlayerTrigger.TriggerInstance(EntityPredicate.wrap(Optional.empty())));
    }
}