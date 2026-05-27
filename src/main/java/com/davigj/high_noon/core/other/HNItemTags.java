package com.davigj.high_noon.core.other;

import com.davigj.high_noon.core.HighNoon;
import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class HNItemTags {
    public static final TagKey<Item> YOUTH_EXTENDERS = itemTag("youth_extenders");
    public static final TagKey<Item> YOUTH_RESUMERS = itemTag("youth_resumers");
    public static final TagKey<Item> SUN_FEED = itemTag("sun_feed");

    private static TagKey<Item> itemTag(String name) {
        return TagUtil.itemTag(HighNoon.MOD_ID, name);
    }
}
