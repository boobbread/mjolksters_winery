package com.mjolkster.mjolksters_winery.common.registry;

import com.mjolkster.mjolksters_winery.MjolkstersWinery;
import com.mjolkster.mjolksters_winery.common.recipe.CrusherRecipe;
import com.mjolkster.mjolksters_winery.common.recipe.DemijohnRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALISERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, MjolkstersWinery.MODID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, MjolkstersWinery.MODID);

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
}
