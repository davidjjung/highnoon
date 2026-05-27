package com.davigj.high_noon.core.mixin;

import com.davigj.high_noon.core.HNConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

import java.util.List;


@Mixin(BeehiveBlockEntity.class)
public class BeehiveBlockEntityMixin {

    @ModifyExpressionValue(method = "releaseOccupant", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isNight()Z"))
    private static boolean allowSunnyRelease(boolean original, Level level, BlockPos pos, BlockState state, BeehiveBlockEntity.Occupant occupant,
                                             @Nullable List<Entity> storedInHives, BeehiveBlockEntity.BeeReleaseStatus releaseStatus, @Nullable BlockPos storedFlowerPos) {
        if (original && hn$isSunny(occupant) && HNConfig.COMMON.sunnyBees.get()) return false;
        return original;
    }

    @Unique
    private static boolean hn$isSunny(BeehiveBlockEntity.Occupant occupant) {
        CompoundTag tag = occupant.entityData().copyTag();
        if (!tag.contains("active_effects", Tag.TAG_LIST)) return false;
        ListTag effects = tag.getList("active_effects", Tag.TAG_COMPOUND);
        for (Tag t : effects) {
            CompoundTag effect = (CompoundTag) t;
            if (effect.getString("id").equals("buzzier_bees:sunny")) {
                return true;
            }
        }
        return false;
    }
}