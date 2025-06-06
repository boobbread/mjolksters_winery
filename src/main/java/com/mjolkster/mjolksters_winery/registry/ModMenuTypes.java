package com.mjolkster.mjolksters_winery.registry;

import com.mjolkster.mjolksters_winery.Winery;
import com.mjolkster.mjolksters_winery.screen.AgingBarrelMenu;
import com.mjolkster.mjolksters_winery.screen.BottlingMachineMenu;
import com.mjolkster.mjolksters_winery.screen.DemijohnMenu;
import com.mjolkster.mjolksters_winery.screen.SommeliersTableMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Winery.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<DemijohnMenu>> DEMIJOHN_MENU =
            registerMenuType("demijohn_menu", DemijohnMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<AgingBarrelMenu>> AGING_BARREL_MENU =
            registerMenuType("aging_barrel_menu", AgingBarrelMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<BottlingMachineMenu>> BOTTLING_MACHINE_MENU =
            registerMenuType("bottling_machine_menu", BottlingMachineMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<SommeliersTableMenu>> SOMMELIERS_TABLE_MENU =
            registerMenuType("sommeliers_table_menu", SommeliersTableMenu::new);

    private static <T extends AbstractContainerMenu>DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {

        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
