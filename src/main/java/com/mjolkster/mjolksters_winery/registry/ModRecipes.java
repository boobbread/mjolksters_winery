package com.mjolkster.mjolksters_winery.registry;

import com.mjolkster.mjolksters_winery.Winery;
import com.mjolkster.mjolksters_winery.recipe.CrusherRecipe;
import com.mjolkster.mjolksters_winery.recipe.DemijohnRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALISERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Winery.MODID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Winery.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DemijohnRecipe>> DEMIJOHN_SERIALISER =
            SERIALISERS.register("demijohn", DemijohnRecipe.Serialiser::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<DemijohnRecipe>> DEMIJOHN_TYPE =
            TYPES.register("demijohn", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "demijohn";
        }

    });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CrusherRecipe>> CRUSHER_SERIALISER =
            SERIALISERS.register("crusher", CrusherRecipe.Serialiser::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<CrusherRecipe>> CRUSHER_TYPE =
            TYPES.register("crusher", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "crusher";
                }

            });

    public static void register(IEventBus eventBus) {
        SERIALISERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
