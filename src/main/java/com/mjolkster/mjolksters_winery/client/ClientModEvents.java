package com.mjolkster.mjolksters_winery.client;

import com.mjolkster.mjolksters_winery.MjolkstersWinery;
import com.mjolkster.mjolksters_winery.client.screen.AgingBarrelScreen;
import com.mjolkster.mjolksters_winery.client.screen.BottlingMachineScreen;
import com.mjolkster.mjolksters_winery.client.screen.DemijohnScreen;
import com.mjolkster.mjolksters_winery.client.screen.SommeliersTableScreen;
import com.mjolkster.mjolksters_winery.common.registry.ModMenuTypes;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import static com.mjolkster.mjolksters_winery.MjolkstersWinery.LOGGER;

@EventBusSubscriber(modid = MjolkstersWinery.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents
{
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        LOGGER.info("HELLO FROM CLIENT SETUP");
        LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.DEMIJOHN_MENU.get(), DemijohnScreen::new);
        event.register(ModMenuTypes.AGING_BARREL_MENU.get(), AgingBarrelScreen::new);
        event.register(ModMenuTypes.BOTTLING_MACHINE_MENU.get(), BottlingMachineScreen::new);
        event.register(ModMenuTypes.SOMMELIERS_TABLE_MENU.get(), SommeliersTableScreen::new);
    }
}