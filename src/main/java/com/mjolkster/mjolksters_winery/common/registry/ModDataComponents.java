package com.mjolkster.mjolksters_winery.common.registry;

import com.mjolkster.mjolksters_winery.util.codec.WineData;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents {
    public static final String MODID = "mjolksters_winery";

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MODID);

    public static final Supplier<DataComponentType<WineData>> WINE_DATA = DATA_COMPONENTS.register("wine_data",
            () -> DataComponentType.<WineData>builder()
                    .persistent(WineData.CODEC)
                    .networkSynchronized(new StreamCodec<>() {
                        @Override
                        public WineData decode(RegistryFriendlyByteBuf buf) {
                            int colour = buf.readInt();
                            String type = buf.readUtf();
                            int barrelAge = buf.readInt();
                            String barrelType = buf.readUtf();
                            float alcoholPerc = buf.readFloat();
                            float wineSweetness = buf.readFloat();
                            return new WineData(colour, type, barrelAge, barrelType, alcoholPerc, wineSweetness);
                        }

                        @Override
                        public void encode(RegistryFriendlyByteBuf buf, WineData data) {
                            buf.writeInt(data.colour());
                            buf.writeUtf(data.name());
                            buf.writeInt(data.barrelAge());
                            buf.writeUtf(data.barrelType());
                            buf.writeFloat(data.alcoholPercentage());
                            buf.writeFloat(data.wineSweetness());
                        }
                    })
                    .build()
    );

    public static final Supplier<DataComponentType<Integer>> WINE_QUALITY = DATA_COMPONENTS.register("wine_quality",
            () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .build());
}
