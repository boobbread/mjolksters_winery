package com.mjolkster.mjolksters_winery.datagen;

import com.mjolkster.mjolksters_winery.Winery;
import com.mjolkster.mjolksters_winery.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemsModelProvider extends ItemModelProvider {
    public ModItemsModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Winery.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.PINOT_NOIR.get());
        basicItem(ModItems.SANGIOVESE.get());
        basicItem(ModItems.CABERNET_SAUVIGNON.get());
        basicItem(ModItems.TEMPRANILLO.get());
        basicItem(ModItems.MOONDROP.get());
        basicItem(ModItems.AUTUMN_ROYAL.get());
        basicItem(ModItems.RUBY_ROMAN.get());

        basicItem(ModItems.RIESLING.get());
        basicItem(ModItems.CHARDONNAY.get());
        basicItem(ModItems.SAUVIGNON_BLANC.get());
        basicItem(ModItems.PINOT_GRIGIO.get());
        basicItem(ModItems.COTTON_CANDY.get());
        basicItem(ModItems.GRENACHE_BLANC.get());
        basicItem(ModItems.WATERFALL.get());

        basicItem(ModItems.KOSHU.get());
        basicItem(ModItems.PINOT_DE_LENFER.get());

        basicItem(ModItems.RED_WINE_BUCKET.get());
        basicItem(ModItems.WHITE_WINE_BUCKET.get());

    }
}
