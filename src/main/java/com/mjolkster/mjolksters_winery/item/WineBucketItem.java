package com.mjolkster.mjolksters_winery.item;

import com.mjolkster.mjolksters_winery.registry.ModDataComponents;
import com.mjolkster.mjolksters_winery.util.codec.WineData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

public class WineBucketItem extends BucketItem {
    public WineBucketItem(Fluid content, Properties properties) {
        super(content, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        WineData data = stack.get(ModDataComponents.WINE_DATA.get());
        return data != null ?
                Component.translatable("item.mjolksters_winery.wine_bucket", data.name()) :
                Component.translatable("item.mjolksters_winery.wine_bucket_empty");
    }
}
