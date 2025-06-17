package com.mjolkster.mjolksters_winery.common.handler;

import com.mjolkster.mjolksters_winery.common.registry.ModDataComponents;
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
            ageRounded = ageRounded.setScale(2, RoundingMode.HALF_UP);
            event.getToolTip().add(Component.literal("Wine Age: " + ageRounded + " Years").withStyle(ChatFormatting.GOLD));

            if (barrelType.contains("oak")) {
                event.getToolTip().add(Component.literal("Wood Type: Oak").withStyle(ChatFormatting.GOLD));
            } else if (barrelType.contains("spruce")) {
                event.getToolTip().add(Component.literal("Wood Type: Spruce").withStyle(ChatFormatting.GOLD));
            } else if (barrelType.contains("cherry")) {
                event.getToolTip().add(Component.literal("Wood Type: Cherry").withStyle(ChatFormatting.GOLD));
            } else if (barrelType.contains("crimson")) {
                event.getToolTip().add(Component.literal("Wood Type: Crimson").withStyle(ChatFormatting.GOLD));
            } else {
                event.getToolTip().add(Component.literal("Wood Type: None").withStyle(ChatFormatting.GOLD));
            }

            event.getToolTip().add(Component.literal("Alcohol Percentage: " + (int)(alcoholPerc * 100) + "%").withStyle(ChatFormatting.GOLD));

            if (wineSweetness <= 0.02) {
                event.getToolTip().add(Component.literal("Wine Sweetness: Extra Dry").withStyle(ChatFormatting.GOLD));
            } else if (wineSweetness <= 0.04 && wineSweetness > 0.02) {
                event.getToolTip().add(Component.literal("Wine Sweetness: Dry").withStyle(ChatFormatting.GOLD));
            } else if (wineSweetness <= 0.06 && wineSweetness > 0.04) {
                event.getToolTip().add(Component.literal("Wine Sweetness: Semi Dry").withStyle(ChatFormatting.GOLD));
            } else if (wineSweetness <= 0.08 && wineSweetness > 0.06) {
                event.getToolTip().add(Component.literal("Wine Sweetness: Sweet").withStyle(ChatFormatting.GOLD));
            } else {
                event.getToolTip().add(Component.literal("Wine Sweetness: Extra Sweet").withStyle(ChatFormatting.GOLD));
            }
        }
    }
}

