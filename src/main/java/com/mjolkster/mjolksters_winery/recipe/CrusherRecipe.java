package com.mjolkster.mjolksters_winery.recipe;

import com.mjolkster.mjolksters_winery.registry.ModRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public record CrusherRecipe(Ingredient ingredient, int ingredientCount, FluidStack output)
        implements Recipe<CrusherInput> {

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(ingredient);
        return list;
    }

    @Override
    public boolean matches(CrusherInput container, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        // Check if container has enough of the ingredient
        int foundCount = 0;
        for (ItemStack stack : container.items()) {  // Iterate through all items
            if (ingredient.test(stack)) {
                foundCount += stack.getCount();
                if (foundCount >= ingredientCount) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(CrusherInput container, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CRUSHER_SERIALISER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CRUSHER_TYPE.get();
    }

    public static class Serialiser implements RecipeSerializer<CrusherRecipe> {
        public static final MapCodec<CrusherRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(CrusherRecipe::ingredient),
                Codec.INT.fieldOf("ingredientCount").forGetter(CrusherRecipe::ingredientCount),
                FluidStack.CODEC.fieldOf("result").forGetter(CrusherRecipe::output)
        ).apply(inst, CrusherRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, CrusherRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC,
                        CrusherRecipe::ingredient,
                        ByteBufCodecs.VAR_INT,  // Use VAR_INT for integers
                        CrusherRecipe::ingredientCount,
                        FluidStack.STREAM_CODEC,
                        CrusherRecipe::output,
                        CrusherRecipe::new
                );

        @Override
        public MapCodec<CrusherRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CrusherRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}