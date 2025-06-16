package com.mjolkster.mjolksters_winery.common.registry;

import com.mjolkster.mjolksters_winery.MjolkstersWinery;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MjolkstersWinery.MODID);

    public static final Supplier<SoundEvent> DEMIJOHN_OPEN = registerSoundEvent("demijohn_open");
    public static final Supplier<SoundEvent> YEAST_POT_BUBBLE = registerSoundEvent("yeast_pot_bubble");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MjolkstersWinery.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
