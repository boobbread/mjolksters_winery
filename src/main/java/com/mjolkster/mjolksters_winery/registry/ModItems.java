package com.mjolkster.mjolksters_winery.registry;

import com.google.common.collect.Sets;
import com.mjolkster.mjolksters_winery.item.JuiceBucketItem;
import com.mjolkster.mjolksters_winery.Winery;
import com.mjolkster.mjolksters_winery.item.WineBucketItem;
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

    public static Supplier<Item> registerWithoutTab(final String name, final Supplier<Item> supplier) {
        Supplier<Item> block = ITEMS.register(name, supplier);
        return block;
    }

    // utils

    public static Item.Properties basicItem() {

        return new Item.Properties();
    }
    public static Item.Properties drinkableItem() {
        return new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16);
    }
    public static Item.Properties grapeItem() {
        return new Item.Properties().stacksTo(64);
    }
    public static Item.Properties yeastItem() {
        return new Item.Properties().stacksTo(1);
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
    public static final Supplier<Item> YEAST_POT = registerWithTab("yeast_pot",
            () -> new BlockItem(ModBlocks.YEAST_POT.get(), basicItem()));
    public static final Supplier<Item> SOMMELIERS_TABLE = registerWithTab("sommeliers_table",
            () -> new BlockItem(ModBlocks.SOMMELIERS_TABLE.get(), basicItem()));

    // buckets

    public static final Supplier<Item> JUICE_BUCKET = registerWithoutTab("juice_bucket",
            () -> new JuiceBucketItem(ModFluids.JUICE.source().get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final Supplier<Item> WINE_BUCKET = registerWithoutTab("wine_bucket",
            () -> new WineBucketItem(ModFluids.WINE.source().get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    // bottles

    public static final Supplier<Item> RED_WINE_BOTTLE = registerWithTab("red_wine_bottle",
            () -> new Item(drinkableItem()));

    public static final Supplier<Item> PINOT_NOIR_BOTTLE = registerWithTab("pinot_noir_wine_bottle",
            () -> new Item(drinkableItem()));
    public static final Supplier<Item> SANGIOVESE_BOTTLE = registerWithTab("sangiovese_wine_bottle",
            () -> new Item(drinkableItem()));
    public static final Supplier<Item> CABERNET_SAUVIGNON_BOTTLE = registerWithTab("cabernet_sauvignon_wine_bottle",
            () -> new Item(drinkableItem()));
    public static final Supplier<Item> TEMPRANILLO_BOTTLE = registerWithTab("tempranillo_wine_bottle",
            () -> new Item(drinkableItem()));
    public static final Supplier<Item> MOONDROP_BOTTLE = registerWithTab("moondrop_wine_bottle",
            () -> new Item(drinkableItem()));
    public static final Supplier<Item> RUBY_ROMAN_BOTTLE = registerWithTab("ruby_roman_wine_bottle",
            () -> new Item(drinkableItem()));
    public static final Supplier<Item> AUTUMN_ROYAL_BOTTLE = registerWithTab("autumn_royal_wine_bottle",
            () -> new Item(drinkableItem()));

    public static final Supplier<Item> RIESLING_BOTTLE = registerWithTab("riesling_wine_bottle",
            () -> new Item(drinkableItem()));
    public static final Supplier<Item> CHARDONNAY_BOTTLE = registerWithTab("chardonnay_wine_bottle",
            () -> new Item(drinkableItem()));
    public static final Supplier<Item> SAUVIGNON_BLANC_BOTTLE = registerWithTab("sauvignon_blanc_wine_bottle",
            () -> new Item(drinkableItem()));
    public static final Supplier<Item> PINOT_GRIGIO_BOTTLE = registerWithTab("pinot_grigio_wine_bottle",
            () -> new Item(drinkableItem()));
    public static final Supplier<Item> COTTON_CANDY_BOTTLE = registerWithTab("cotton_candy_wine_bottle",
            () -> new Item(drinkableItem()));
    public static final Supplier<Item> GRENACHE_BLANC_BOTTLE = registerWithTab("grenache_blanc_wine_bottle",
            () -> new Item(drinkableItem()));
    public static final Supplier<Item> WATERFALL_BOTTLE = registerWithTab("waterfall_wine_bottle",
            () -> new Item(drinkableItem()));

    public static final Supplier<Item> KOSHU_BOTTLE = registerWithTab("koshu_wine_bottle",
            () -> new Item(drinkableItem()));
    public static final Supplier<Item> PINOT_DE_LENFER_BOTTLE = registerWithTab("pinot_de_lenfer_wine_bottle",
            () -> new Item(drinkableItem()));

    public static final Supplier<Item> SWEET_BERRY_WINE_BOTTLE = registerWithTab("sweet_berry_wine_bottle",
            () -> new Item(drinkableItem()));
    public static final Supplier<Item> GLOW_BERRY_WINE_BOTTLE = registerWithTab("glow_berry_wine_bottle",
            () -> new Item(drinkableItem()));
    public static final Supplier<Item> CHORUS_FRUIT_WINE_BOTTLE = registerWithTab("chorus_fruit_wine_bottle",
            () -> new Item(drinkableItem()));

    // grapes - red

    public static final Supplier<Item> PINOT_NOIR = registerWithTab("pinot_noir_grapes",
            () -> new Item(grapeItem()));
    public static final Supplier<Item> SANGIOVESE = registerWithTab("sangiovese_grapes",
            () -> new Item(grapeItem()));
    public static final Supplier<Item> CABERNET_SAUVIGNON = registerWithTab("cabernet_sauvignon_grapes",
            () -> new Item(grapeItem()));
    public static final Supplier<Item> TEMPRANILLO = registerWithTab("tempranillo_grapes",
            () -> new Item(grapeItem()));
    public static final Supplier<Item> MOONDROP = registerWithTab("moondrop_grapes",
            () -> new Item(grapeItem()));
    public static final Supplier<Item> RUBY_ROMAN = registerWithTab("ruby_roman_grapes",
            () -> new Item(grapeItem()));
    public static final Supplier<Item> AUTUMN_ROYAL = registerWithTab("autumn_royal_grapes",
            () -> new Item(grapeItem()));

    // grapes - white

    public static final Supplier<Item> RIESLING = registerWithTab("riesling_grapes",
            () -> new Item(grapeItem()));
    public static final Supplier<Item> CHARDONNAY = registerWithTab("chardonnay_grapes",
            () -> new Item(grapeItem()));
    public static final Supplier<Item> SAUVIGNON_BLANC = registerWithTab("sauvignon_blanc_grapes",
            () -> new Item(grapeItem()));
    public static final Supplier<Item> PINOT_GRIGIO = registerWithTab("pinot_grigio_grapes",
            () -> new Item(grapeItem()));
    public static final Supplier<Item> COTTON_CANDY = registerWithTab("cotton_candy_grapes",
            () -> new Item(grapeItem()));
    public static final Supplier<Item> GRENACHE_BLANC = registerWithTab("grenache_blanc_grapes",
            () -> new Item(grapeItem()));
    public static final Supplier<Item> WATERFALL = registerWithTab("waterfall_grapes",
            () -> new Item(grapeItem()));

    // grapes - special

    public static final Supplier<Item> KOSHU = registerWithTab("koshu_grapes",
            () -> new Item(grapeItem()));
    public static final Supplier<Item> PINOT_DE_LENFER = registerWithTab("pinot_de_lenfer_grapes",
            () -> new Item(grapeItem()));

    // yeasts

    public static final Supplier<Item> PASTEUR_RED = registerWithTab("pasteur_red_yeast",
            () -> new Item(yeastItem()));
    public static final Supplier<Item> MONTRACHE = registerWithTab("montrachet_yeast",
            () -> new Item(yeastItem()));
    public static final Supplier<Item> COTE_DES_BLANCS = registerWithTab("cote_des_blancs_yeast",
            () -> new Item(yeastItem()));
    public static final Supplier<Item> PREMIER_CUVEE = registerWithTab("premier_cuvee_yeast",
            () -> new Item(yeastItem()));
    public static final Supplier<Item> CHAMPAGNE = registerWithTab("champagne_yeast",
            () -> new Item(yeastItem()));

}
