package com.mjolkster.mjolksters_winery.registry;

import com.google.common.collect.Sets;
import com.mjolkster.mjolksters_winery.Winery;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, Winery.MODID);
    public static LinkedHashSet<Supplier<Item>> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();

    public static Supplier<Item> registerWithTab(final String name, final Supplier<Item> supplier) {
        Supplier<Item> block = ITEMS.register(name, supplier);
        CREATIVE_TAB_ITEMS.add(block);
        return block;
    }

    // utils

    public static Item.Properties basicItem() {
        return new Item.Properties();
    }
    public static Item.Properties drinkableItem() {
        return new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16);
    }

    // block

    public static final Supplier<Item> DEMIJOHN = registerWithTab("demijohn",
            () -> new BlockItem(ModBlocks.DEMIJOHN.get(), basicItem()));
    public static final Supplier<Item> CRUSHER = registerWithTab("crusher",
            () -> new BlockItem(ModBlocks.CRUSHER.get(), basicItem()));
    public static final Supplier<Item> OAK_AGING_BARREL = registerWithTab("oak_aging_barrel",
            () -> new BlockItem(ModBlocks.OAK_AGING_BARREL.get(), basicItem()));
    public static final Supplier<Item> SPRUCE_AGING_BARREL = registerWithTab("spruce_aging_barrel",
            () -> new BlockItem(ModBlocks.SPRUCE_AGING_BARREL.get(), basicItem()));
    public static final Supplier<Item> ACACIA_AGING_BARREL = registerWithTab("acacia_aging_barrel",
            () -> new BlockItem(ModBlocks.ACACIA_AGING_BARREL.get(), basicItem()));
    public static final Supplier<Item> BOTTLING_MACHINE = registerWithTab("bottling_machine",
            () -> new BlockItem(ModBlocks.BOTTLING_MACHINE.get(), basicItem()));

    // buckets

    public static final Supplier<Item> RED_WINE_BUCKET = registerWithTab("red_wine_bucket",
            () -> new BucketItem(ModFluids.RED_WINE.source().get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final Supplier<Item> WHITE_WINE_BUCKET = registerWithTab("white_wine_bucket",
            () -> new BucketItem(ModFluids.WHITE_WINE.source().get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final Supplier<Item> POTATO_WASH_BUCKET = registerWithTab("potato_wash_bucket",
            () -> new BucketItem(ModFluids.POTATO_WASH.source().get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    // bottles

    public static final Supplier<Item> RED_WINE_BOTTLE = registerWithTab("red_wine_bottle",
            () -> new Item(drinkableItem()));
    public static final Supplier<Item> WHITE_WINE_BOTTLE = registerWithTab("white_wine_bottle",
            () -> new Item(drinkableItem()));
}
