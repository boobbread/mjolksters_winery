package com.mjolkster.mjolksters_winery.util;

import com.mjolkster.mjolksters_winery.registry.ModDataComponents;
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
        if (stack.has(ModDataComponents.WINE_AGE.get())) {
            int age = stack.get(ModDataComponents.WINE_AGE.get());
            double ageInMCDays = (age / 24000.0);
            BigDecimal ageRounded = new BigDecimal(Double.toString(ageInMCDays));
            ageRounded = ageRounded.setScale(1, RoundingMode.HALF_UP);
            event.getToolTip().add(Component.literal("Wine Age: " + ageRounded + " years").withStyle(ChatFormatting.GOLD));
        }
        if (stack.has(ModDataComponents.WOOD_TYPE.get())) {
            String woodTypeRaw = stack.get(ModDataComponents.WOOD_TYPE.get());
            if (woodTypeRaw.contains("oak")) {
                event.getToolTip().add(Component.literal("Wood Type: Oak").withStyle(ChatFormatting.GOLD));
            } else if (woodTypeRaw.contains("spruce")) {
                event.getToolTip().add(Component.literal("Wood Type: Spruce").withStyle(ChatFormatting.GOLD));
            } else {
                event.getToolTip().add(Component.literal("Wood Type: Acacia").withStyle(ChatFormatting.GOLD));
            }


        }
    }
}

