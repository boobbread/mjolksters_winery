package com.mjolkster.mjolksters_winery.util;

import com.mjolkster.mjolksters_winery.registry.ModItems;
import com.mjolkster.mjolksters_winery.util.codec.WineData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.mjolkster.mjolksters_winery.registry.ModDataComponents.WINE_DATA;
import static com.mjolkster.mjolksters_winery.tag.CommonTags.BOTTLE;
import static java.lang.Math.*;

public class WineQualityChecker {

    public static final Map<Item, WineData> idealValues = new HashMap<>();
    static {
        idealValues.put(ModItems.PINOT_NOIR_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));
        idealValues.put(ModItems.SANGIOVESE_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));
        idealValues.put(ModItems.CABERNET_SAUVIGNON_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));
        idealValues.put(ModItems.TEMPRANILLO_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));
        idealValues.put(ModItems.MOONDROP_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));
        idealValues.put(ModItems.RUBY_ROMAN_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));
        idealValues.put(ModItems.AUTUMN_ROYAL_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));

        idealValues.put(ModItems.RIESLING_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));
        idealValues.put(ModItems.CHARDONNAY_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));
        idealValues.put(ModItems.SAUVIGNON_BLANC_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));
        idealValues.put(ModItems.PINOT_GRIGIO_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));
        idealValues.put(ModItems.COTTON_CANDY_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));
        idealValues.put(ModItems.GRENACHE_BLANC_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));
        idealValues.put(ModItems.WATERFALL_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));

        idealValues.put(ModItems.KOSHU_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));
        idealValues.put(ModItems.PINOT_DE_LENFER_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));

        idealValues.put(ModItems.SWEET_BERRY_WINE_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));
        idealValues.put(ModItems.GLOW_BERRY_WINE_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));
        idealValues.put(ModItems.CHORUS_FRUIT_WINE_BOTTLE.get(), new WineData(0xFFFFFFFF, "", 0, "spruce", 0.0f, 0));
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
        float ageRating = idealAge == 0 ? 0.0f : clamp(1 - abs((idealAge - actualAge) / (float) idealAge), 0.0f, 1.0f);
        float alcoholRating = idealAlcohol == 0 ? 0.0f : clamp(1 - abs((idealAlcohol - actualAlcohol) / idealAlcohol), 0.0f, 1.0f);
        float sweetnessRating = idealSweetness == 0 ? 0.0f : clamp(1 - abs((idealSweetness - actualSweetness) / idealSweetness), 0.0f, 1.0f);
        float overallRating = (barrelRating + ageRating + alcoholRating) / 3.0f;

        return new WineRating(barrelRating, ageRating, alcoholRating, sweetnessRating, overallRating);
    }
}
