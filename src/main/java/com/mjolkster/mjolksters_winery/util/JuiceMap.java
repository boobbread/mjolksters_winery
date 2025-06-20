package com.mjolkster.mjolksters_winery.util;

import com.mjolkster.mjolksters_winery.common.registry.ModItems;
import com.mjolkster.mjolksters_winery.util.codec.WineData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;

import java.util.HashMap;
import java.util.Map;

public class JuiceMap {
    public static final Map<Item, WineData> JUICE_CREATION_MAP = new HashMap<>();
    static {
        JUICE_CREATION_MAP.put(ModItems.PINOT_NOIR.get(), new WineData(0xFF1E0926, "Pinot Noir", 0, "none", 0, 0.10f));
        JUICE_CREATION_MAP.put(ModItems.SANGIOVESE.get(), new WineData(0xFF200F40, "Sangiovese", 0, "none", 0, 0.12f));
        JUICE_CREATION_MAP.put(ModItems.CABERNET_SAUVIGNON.get(), new WineData(0xFF1C0F2E, "Cabernet Sauvignon", 0, "none", 0, 0.09f));
        JUICE_CREATION_MAP.put(ModItems.TEMPRANILLO.get(), new WineData(0xFF291261, "Tempranillo", 0, "none", 0, 0.08f));
        JUICE_CREATION_MAP.put(ModItems.MOONDROP.get(), new WineData(0xFF1F072E, "Moondrop", 0, "none", 0, 0.09f));
        JUICE_CREATION_MAP.put(ModItems.AUTUMN_ROYAL.get(), new WineData(0xFF1B0D40, "Autumn Royal", 0, "none", 0, 0.09f));
        JUICE_CREATION_MAP.put(ModItems.RUBY_ROMAN.get(), new WineData(0xFF820C1B, "Ruby Roman", 0, "none", 0, 0.17f));

        JUICE_CREATION_MAP.put(ModItems.RIESLING.get(), new WineData(0xFFACB030, "Riesling", 0, "none", 0, 0.07f));
        JUICE_CREATION_MAP.put(ModItems.CHARDONNAY.get(), new WineData(0xFFCFBD48, "Chardonnay", 0, "none", 0, 0.14f));
        JUICE_CREATION_MAP.put(ModItems.SAUVIGNON_BLANC.get(), new WineData(0xFFB8A727, "Sauvignon Blanc", 0, "none", 0, 0.11f));
        JUICE_CREATION_MAP.put(ModItems.PINOT_GRIGIO.get(), new WineData(0xFFD9B841, "Pinot Grigio", 0, "none", 0, 0.08f));
        JUICE_CREATION_MAP.put(ModItems.COTTON_CANDY.get(), new WineData(0xFFFFE9AA, "Cotton Candy", 0, "none", 0, 0.16f));
        JUICE_CREATION_MAP.put(ModItems.GRENACHE_BLANC.get(), new WineData(0xFFD6D060, "Grenache Blanc", 0, "none", 0, 0.07f));
        JUICE_CREATION_MAP.put(ModItems.WATERFALL.get(), new WineData(0xFF8DBD4A, "Waterfall", 0, "none", 0, 0.13f));

        JUICE_CREATION_MAP.put(ModItems.KOSHU.get(), new WineData(0xFFFF8797, "Koshu", 0, "none", 0, 0.11f));
        JUICE_CREATION_MAP.put(ModItems.PINOT_DE_LENFER.get(), new WineData(0xFF33060C, "Pinot De l'Enfer", 0, "none", 0, 0.16f));

        JUICE_CREATION_MAP.put(Items.SWEET_BERRIES, new WineData(0xFFFFE9AA, "Sweet Berry", 0, "none", 0, 0.12f));
        JUICE_CREATION_MAP.put(Items.GLOW_BERRIES, new WineData(0xFFE6922C, "Glow Berry", 0, "none", 0, 0.14f));
        JUICE_CREATION_MAP.put(Items.CHORUS_FRUIT, new WineData(0xFF8F5CB5, "Chorus", 0, "none", 0, 0.10f));

        JUICE_CREATION_MAP.put(Items.HONEYCOMB, new WineData(0xFFEB8D00, "Honey", 0, "none", 0, 0));
    }

    public static final Map<String, WineData> JUICE_MAP = new HashMap<>();
    static {
        JUICE_MAP.put("mjolksters_winery:create_source_pinot_noir_juice", new WineData(0xFF1E0926, "Pinot Noir", 0, "none", 0, 0.10f));
        JUICE_MAP.put("mjolksters_winery:create_source_sangiovese_juice", new WineData(0xFF200F40, "Sangiovese", 0, "none", 0, 0.12f));
        JUICE_MAP.put("mjolksters_winery:create_source_cabernet_sauvignon_juice", new WineData(0xFF1C0F2E, "Cabernet Sauvignon", 0, "none", 0, 0.09f));
        JUICE_MAP.put("mjolksters_winery:create_source_pinot_noir_juice", new WineData(0xFF291261, "Tempranillo", 0, "none", 0, 0.08f));
        JUICE_MAP.put("mjolksters_winery:create_source_pinot_noir_juice", new WineData(0xFF1F072E, "Moondrop", 0, "none", 0, 0.09f));
        JUICE_MAP.put("mjolksters_winery:create_source_pinot_noir_juice", new WineData(0xFF1B0D40, "Autumn Royal", 0, "none", 0, 0.09f));
        JUICE_MAP.put("mjolksters_winery:create_source_pinot_noir_juice", new WineData(0xFF820C1B, "Ruby Roman", 0, "none", 0, 0.17f));

        JUICE_MAP.put("mjolksters_winery:create_source_pinot_noir_juice", new WineData(0xFFACB030, "Riesling", 0, "none", 0, 0.07f));
        JUICE_MAP.put("mjolksters_winery:create_source_pinot_noir_juice", new WineData(0xFFCFBD48, "Chardonnay", 0, "none", 0, 0.14f));
        JUICE_MAP.put("mjolksters_winery:create_source_pinot_noir_juice", new WineData(0xFFB8A727, "Sauvignon Blanc", 0, "none", 0, 0.11f));
        JUICE_MAP.put("mjolksters_winery:create_source_pinot_noir_juice", new WineData(0xFFD9B841, "Pinot Grigio", 0, "none", 0, 0.08f));
        JUICE_MAP.put("mjolksters_winery:create_source_pinot_noir_juice", new WineData(0xFFFFE9AA, "Cotton Candy", 0, "none", 0, 0.16f));
        JUICE_MAP.put("mjolksters_winery:create_source_pinot_noir_juice", new WineData(0xFFD6D060, "Grenache Blanc", 0, "none", 0, 0.07f));
        JUICE_MAP.put("mjolksters_winery:create_source_pinot_noir_juice", new WineData(0xFF8DBD4A, "Waterfall", 0, "none", 0, 0.13f));

        JUICE_MAP.put("mjolksters_winery:create_source_pinot_noir_juice", new WineData(0xFFFF8797, "Koshu", 0, "none", 0, 0.11f));
        JUICE_MAP.put("mjolksters_winery:create_source_pinot_noir_juice", new WineData(0xFF33060C, "Pinot De l'Enfer", 0, "none", 0, 0.16f));

        JUICE_MAP.put("mjolksters_winery:create_source_pinot_noir_juice", new WineData(0xFFFFE9AA, "Sweet Berry", 0, "none", 0, 0.12f));
        JUICE_MAP.put("mjolksters_winery:create_source_pinot_noir_juice", new WineData(0xFFE6922C, "Glow Berry", 0, "none", 0, 0.14f));
        JUICE_MAP.put("mjolksters_winery:create_source_pinot_noir_juice", new WineData(0xFF8F5CB5, "Chorus", 0, "none", 0, 0.10f));
    }
}
