package com.mjolkster.mjolksters_winery.registry;

import com.mjolkster.mjolksters_winery.Winery;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Winery.MODID);

    public static final Supplier<CreativeModeTab> TAB_MJOLKSTERS_WINERY = CREATIVE_TABS.register(Winery.MODID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.mjolksters_winery"))
                    .icon(() -> new ItemStack(ModBlocks.DEMIJOHN.get()))
                    .displayItems((parameters, output) -> ModItems.CREATIVE_TAB_ITEMS.forEach((item) -> output.accept(item.get())))
                    .build());
}
