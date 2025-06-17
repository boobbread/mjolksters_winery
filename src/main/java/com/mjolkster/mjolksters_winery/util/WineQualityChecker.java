package com.mjolkster.mjolksters_winery.util;

import com.mjolkster.mjolksters_winery.common.registry.ModItems;
import com.mjolkster.mjolksters_winery.util.codec.WineData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static com.mjolkster.mjolksters_winery.common.CommonTags.BOTTLE;
import static com.mjolkster.mjolksters_winery.common.registry.ModDataComponents.WINE_DATA;
import static java.lang.Math.abs;
import static java.lang.Math.clamp;

public class WineQualityChecker {

    public static final Map<Item, WineData> idealValues = new HashMap<>();
    static {

        // Reds
        idealValues.put(ModItems.PINOT_NOIR_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 120000, "oak", 0.14f, 0.01f));
        idealValues.put(ModItems.SANGIOVESE_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 24000, "oak", 0.13f, 0.03f));
        idealValues.put(ModItems.CABERNET_SAUVIGNON_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 12000, "spruce", 0.14f, 0.01f));
        idealValues.put(ModItems.TEMPRANILLO_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 48000, "oak", 0.15f, 0.05f));
        idealValues.put(ModItems.MOONDROP_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 24000, "spruce", 0.15f, 0.07f));
        idealValues.put(ModItems.RUBY_ROMAN_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 36000, "spruce", 0.18f, 0.02f));
        idealValues.put(ModItems.AUTUMN_ROYAL_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 12000, "oak", 0.11f, 0.05f));

        // Whites
        idealValues.put(ModItems.RIESLING_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 48000, "oak", 0.12f, 0.02f));
        idealValues.put(ModItems.CHARDONNAY_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 6000, "cherry", 0.15f, 0.03f));
        idealValues.put(ModItems.SAUVIGNON_BLANC_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 24000, "oak", 0.13f, 0.04f));
        idealValues.put(ModItems.PINOT_GRIGIO_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "none", 0.12f, 0.03f));
        idealValues.put(ModItems.COTTON_CANDY_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 12000, "cherry", 0.18f, 0.09f));
        idealValues.put(ModItems.GRENACHE_BLANC_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 20000, "oak", 0.14f, 0.02f));
        idealValues.put(ModItems.WATERFALL_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 20000, "cherry", 0.16f, 0.07f));

        // Special
        idealValues.put(ModItems.KOSHU_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 8000, "cherry", 0.12f, 0.03f));
        idealValues.put(ModItems.PINOT_DE_LENFER_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 60000, "crimson", 0.18f, 0.01f));

        // Vanilla
        idealValues.put(ModItems.SWEET_BERRY_WINE_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 12000, "spruce", 0.12f, 0.06f));
        idealValues.put(ModItems.GLOW_BERRY_WINE_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 40000, "cherry", 0.15f, 0.08f));
        idealValues.put(ModItems.CHORUS_FRUIT_WINE_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 36000, "oak", 0.17f, 0.04f));
    }

    public record WineRating(float barrelRating, float ageRating, float alcoholRating, float sweetnessRating, float overallRating) {}

    public static WineRating getChecked(ItemStack itemStack) {
        if (!itemStack.is(BOTTLE)) {
            return new WineRating(0, 0, 0, 0, 0);
        }

        Item item = itemStack.getItem();
        WineData idealData = idealValues.get(item);
        WineData actualData = itemStack.get(WINE_DATA.get());

        if (idealData == null || actualData == null) {
            return new WineRating(0, 0, 0, 0, 0);
        }

        int idealAge = idealData.barrelAge();
        String idealBarrel = idealData.barrelType();
        float idealAlcohol = idealData.alcoholPercentage();
        float idealSweetness = idealData.wineSweetness();

        int actualAge = actualData.barrelAge();
        String actualBarrel = actualData.barrelType();
        float actualAlcohol = actualData.alcoholPercentage();
        float actualSweetness = actualData.wineSweetness();

        float barrelRating = actualBarrel.contains(idealBarrel) ? 1.0f : 0.0f;
        float ageRating = idealAge == 0 ? 0.0f : clamp((float) Math.exp(-(Math.pow(actualAge - idealAge, 2)) / (4700.0 * (idealAge * 0.63))), 0.0f, 1.0f);
        float alcoholRating = idealAlcohol == 0 ? 0.0f : clamp(1 - abs((idealAlcohol - actualAlcohol) / idealAlcohol), 0.0f, 1.0f);
        float sweetnessRating = idealSweetness == 0 ? 0.0f : clamp(1 - abs((idealSweetness - actualSweetness) / (idealSweetness * 10)), 0.0f, 1.0f);
        float overallRating = (barrelRating + ageRating + alcoholRating + sweetnessRating) / 4.0f;

        return new WineRating(barrelRating, ageRating, alcoholRating, sweetnessRating, overallRating);
    }
}
