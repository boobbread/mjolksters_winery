package com.mjolkster.mjolksters_winery.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents {
    public static final String MODID = "mjolksters_winery";

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MODID);

    public static final Supplier<DataComponentType<Integer>> WINE_AGE = DATA_COMPONENTS.register("wine_age",
            () -> DataComponentType.<Integer>builder()
                    .persistent(ExtraCodecs.NON_NEGATIVE_INT)
                    .networkSynchronized(StreamCodec.of(RegistryFriendlyByteBuf::writeVarInt, RegistryFriendlyByteBuf::readVarInt))
                    .build());

    public static final Supplier<DataComponentType<String>> WOOD_TYPE = DATA_COMPONENTS.register("wood_type",
            () -> DataComponentType.<String>builder()
                    .persistent(ExtraCodecs.NON_EMPTY_STRING)
                    .networkSynchronized(StreamCodec.of(RegistryFriendlyByteBuf::writeUtf, RegistryFriendlyByteBuf::readUtf))
                    .build());

        // Register directly into the built-in registry
        public static void register(IEventBus modEventBus) {
            DATA_COMPONENTS.register(modEventBus);
        }
    }
