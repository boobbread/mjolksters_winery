package com.mjolkster.mjolksters_winery.util;

import com.mjolkster.mjolksters_winery.registry.ModDataComponents;
import com.mjolkster.mjolksters_winery.util.codec.WineData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TooltipHandler {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        WineData stackData = stack.get(ModDataComponents.WINE_DATA.get());
        if (stackData != null) {
            int wineAge = stackData.barrelAge();
            String barrelType = stackData.barrelType();
            float alcoholPerc = stackData.alcoholPercentage();
            float wineSweetness = stackData.wineSweetness();

            double ageInMCDays = (wineAge / 24000.0);
            BigDecimal ageRounded = new BigDecimal(Double.toString(ageInMCDays));
            ageRounded = ageRounded.setScale(1, RoundingMode.HALF_UP);
            event.getToolTip().add(Component.literal("Wine Age: " + ageRounded + " years").withStyle(ChatFormatting.GOLD));

            if (barrelType.contains("oak")) {
                event.getToolTip().add(Component.literal("Wood Type: Oak").withStyle(ChatFormatting.GOLD));
            } else if (barrelType.contains("spruce")) {
                event.getToolTip().add(Component.literal("Wood Type: Spruce").withStyle(ChatFormatting.GOLD));
            } else if (barrelType.contains("acacia")) {
                event.getToolTip().add(Component.literal("Wood Type: Acacia").withStyle(ChatFormatting.GOLD));
            } else {
                event.getToolTip().add(Component.literal("Wood Type: None").withStyle(ChatFormatting.GOLD));
            }

            event.getToolTip().add(Component.literal("Alcohol Percentage: " + (int)(alcoholPerc * 100) + "%").withStyle(ChatFormatting.GOLD));
            event.getToolTip().add(Component.literal("Wine Sweetness: " + (int)(wineSweetness * 100) + "%").withStyle(ChatFormatting.GOLD));
        }
    }
}

