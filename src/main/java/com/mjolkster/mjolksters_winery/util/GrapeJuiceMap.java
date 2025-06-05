package com.mjolkster.mjolksters_winery.util;

import com.mjolkster.mjolksters_winery.registry.ModItems;
import com.mjolkster.mjolksters_winery.util.codec.JuiceType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class GrapeJuiceMap {
    public static final Map<Item, JuiceType> GRAPE_JUICE_MAP = new HashMap<>();
    static {
        GRAPE_JUICE_MAP.put(ModItems.PINOT_NOIR.get(), new JuiceType(0xFF1E0926, "Pinot Noir"));
        GRAPE_JUICE_MAP.put(ModItems.SANGIOVESE.get(), new JuiceType(0xFF200F40, "Sangiovese"));
        GRAPE_JUICE_MAP.put(ModItems.CABERNET_SAUVIGNON.get(), new JuiceType(0xFF1C0F2E, "Cabernet Sauvignon"));
        GRAPE_JUICE_MAP.put(ModItems.TEMPRANILLO.get(), new JuiceType(0xFF291261, "Tempranillo"));
        GRAPE_JUICE_MAP.put(ModItems.MOONDROP.get(), new JuiceType(0xFF1F072E, "Moondrop"));
        GRAPE_JUICE_MAP.put(ModItems.AUTUMN_ROYAL.get(), new JuiceType(0xFF1B0D40, "Autumn Royal"));
        GRAPE_JUICE_MAP.put(ModItems.RUBY_ROMAN.get(), new JuiceType(0xFF820C1B, "Ruby Roman"));

        GRAPE_JUICE_MAP.put(ModItems.RIESLING.get(), new JuiceType(0xFFACB030, "Riesling"));
        GRAPE_JUICE_MAP.put(ModItems.CHARDONNAY.get(), new JuiceType(0xFFCFBD48, "Chardonnay"));
        GRAPE_JUICE_MAP.put(ModItems.SAUVIGNON_BLANC.get(), new JuiceType(0xFFB8A727, "Sauvignon Blanc"));
        GRAPE_JUICE_MAP.put(ModItems.PINOT_GRIGIO.get(), new JuiceType(0xFFD9B841, "Pinot Grigio"));
        GRAPE_JUICE_MAP.put(ModItems.COTTON_CANDY.get(), new JuiceType(0xFFFFE9AA, "Cotton Candy"));
        GRAPE_JUICE_MAP.put(ModItems.GRENACHE_BLANC.get(), new JuiceType(0xFFD6D060, "Grenache Blanc"));
        GRAPE_JUICE_MAP.put(ModItems.WATERFALL.get(), new JuiceType(0xFF8DBD4A, "Waterfall"));

        GRAPE_JUICE_MAP.put(ModItems.KOSHU.get(), new JuiceType(0xFFFF8797, "Koshu"));
        GRAPE_JUICE_MAP.put(ModItems.PINOT_DE_LENFER.get(), new JuiceType(0xFF33060C, "Pinot De l'Enfer"));

        GRAPE_JUICE_MAP.put(Items.SWEET_BERRIES, new JuiceType(0xFFFFE9AA, "Sweet Berry"));
        GRAPE_JUICE_MAP.put(Items.GLOW_BERRIES, new JuiceType(0xFFE6922C, "Glow Berry"));
        GRAPE_JUICE_MAP.put(Items.CHORUS_FRUIT, new JuiceType(0xFF8F5CB5, "Chorus"));
    }
}
