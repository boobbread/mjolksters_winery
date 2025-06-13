package com.mjolkster.mjolksters_winery.registry;

import com.google.common.collect.ImmutableSet;
import com.mjolkster.mjolksters_winery.Winery;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModVillagers {

    public static final DeferredRegister<PoiType> POI_TYPE =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, Winery.MODID);

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSION =
            DeferredRegister.create(Registries.VILLAGER_PROFESSION, Winery.MODID);

    public static Supplier<PoiType> registerPOI(final String name, final Supplier<PoiType> supplier) {
        return POI_TYPE.register(name, supplier);
    }

    public static Supplier<VillagerProfession> registerProf(final String name, final Supplier<VillagerProfession> supplier) {
        return VILLAGER_PROFESSION.register(name, supplier);
    }

    // POI type

    public static final Supplier<PoiType> VINTNER_POI = registerPOI("vintner_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.OAK_AGING_BARREL.get().getStateDefinition().getPossibleStates()), 1, 1));

    // Villager type

    public static final Supplier<VillagerProfession> VINTNER = registerProf("vintner",
            () -> new VillagerProfession("vintner", poiTypeHolder -> poiTypeHolder.value() == VINTNER_POI.get(),
                    holder -> holder.value() == VINTNER_POI.get(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.BARREL_OPEN));

}
