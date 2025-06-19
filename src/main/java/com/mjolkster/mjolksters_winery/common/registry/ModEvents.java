package com.mjolkster.mjolksters_winery.common.registry;


import com.mjolkster.mjolksters_winery.MjolkstersWinery;
import com.mjolkster.mjolksters_winery.common.CommonTags;
import com.mjolkster.mjolksters_winery.common.item.WineBottleItem;
import com.mjolkster.mjolksters_winery.mixin.VillagerGossipAccessor;
import com.mjolkster.mjolksters_winery.util.WineQualityChecker;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;

import static com.mjolkster.mjolksters_winery.MjolkstersWinery.LOGGER;
import static com.mjolkster.mjolksters_winery.common.registry.ModDataComponents.WINE_QUALITY;

@EventBusSubscriber(modid = MjolkstersWinery.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if(event.getType() == ModVillagers.VINTNER.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            var tagKey = CommonTags.BOTTLE;
            var optionalTag = net.minecraft.core.registries.BuiltInRegistries.ITEM.getTag(tagKey);

            var items = optionalTag.get().stream().toList();

            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 3),
                    new ItemStack(ModItems.DEMIJOHN.get(), 1), 6, 3, 0.05f
            ));

            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 4),
                    new ItemStack(ModItems.OAK_AGING_BARREL.get(), 1), 6, 3, 0.05f
            ));

            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 4),
                    new ItemStack(ModItems.RIESLING.get(), 1), 6, 3, 0.05f
            ));

            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.PINOT_DE_LENFER.get(), 5),
                    new ItemStack(Items.EMERALD, 1), 6, 3, 0.05f
            ));

            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.PINOT_NOIR_BOTTLE.get(), 1),
                    new ItemStack(Items.EMERALD, 5), 10, 3, 0.05f
            ));

            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.KOSHU.get(), 5),
                    new ItemStack(Items.EMERALD, 1), 6, 3, 0.05f
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

    @SubscribeEvent
    public static void onRightClickVillager(PlayerInteractEvent.EntityInteractSpecific event) {
        Player player = event.getEntity();
        Level level = event.getLevel();

        if (!(event.getTarget() instanceof Villager villager)) return;
        if (player.isSpectator()) return;

        ItemStack heldItem = event.getItemStack();

        if (heldItem.getItem() instanceof WineBottleItem) {
            LOGGER.info("Instance of Wine Bottle");

            float wineQuality = WineQualityChecker.getChecked(heldItem).overallRating();
            LOGGER.info("Overall Wine Quality {}", wineQuality);

            if (!level.isClientSide) {
                //.*\n Only apply effect if the villager is unhappy
                    //.*\n Remove gossip penalties
                    if (villager instanceof VillagerGossipAccessor accessor) {
                        GossipContainer gossip = accessor.getGossips();
                        LOGGER.info("Retrieved gossip{}", gossip);

                        if (wineQuality > 0.8) {
                            gossip.remove(player.getUUID(), GossipType.MINOR_NEGATIVE);
                            gossip.remove(player.getUUID(), GossipType.MAJOR_NEGATIVE);
                            gossip.remove(player.getUUID(), GossipType.TRADING);

                            heldItem.shrink(1);
                            level.playSound(null, villager.blockPosition(), SoundEvents.VILLAGER_CELEBRATE, SoundSource.NEUTRAL, 1.0f, 1.0f);
                        } else if (wineQuality < 0.4) {
                            gossip.add(player.getUUID(), GossipType.MINOR_NEGATIVE, 1);

                            heldItem.shrink(1);
                            level.playSound(null, villager.blockPosition(), SoundEvents.VILLAGER_NO, SoundSource.NEUTRAL, 1.0f, 1.0f);
                        }
                    }
            }

            event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
            event.setCanceled(true);
        } else {
            event.setCanceled(false);
        }
    }
}
