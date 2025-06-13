package com.mjolkster.mjolksters_winery.registry;


import com.mjolkster.mjolksters_winery.Winery;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;

@EventBusSubscriber(modid = Winery.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if(event.getType() == ModVillagers.VINTNER.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 3),
                    new ItemStack(ModItems.DEMIJOHN.get(), 1), 6, 3, 0.05f
            ));
        }

        if(event.getType() == VillagerProfession.FARMER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 2),
                    new ItemStack(ModItems.RIESLING.get(), 1), 6, 3, 0.05f
            ));
        }
    }
}
