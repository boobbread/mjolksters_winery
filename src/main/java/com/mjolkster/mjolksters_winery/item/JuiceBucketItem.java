package com.mjolkster.mjolksters_winery.item;

import com.mjolkster.mjolksters_winery.codec.JuiceType;
import com.mjolkster.mjolksters_winery.registry.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;


public class JuiceBucketItem extends BucketItem{
    public JuiceBucketItem(Fluid content, Properties properties) {
        super(content, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        JuiceType data = stack.get(ModDataComponents.JUICE_TYPE.get());
        return data != null ?
                Component.translatable("item.mjolksters_winery.juice_bucket", data.name()) :
                super.getName(stack);
    }
}
