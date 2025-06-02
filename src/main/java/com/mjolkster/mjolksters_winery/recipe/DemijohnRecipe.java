package com.mjolkster.mjolksters_winery.recipe;

import com.mjolkster.mjolksters_winery.registry.ModRecipes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public record DemijohnRecipe(Ingredient inputItem1, Ingredient inputItem2, Ingredient inputItem3, FluidStack output) implements Recipe<DemijohnRecipeInput> {
    // inputItem & output ==> Read From JSON File!
    // GrowthChamberRecipeInput --> INVENTORY of the Block Entity

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItem1);
        list.add(inputItem2);
        list.add(inputItem3);
        return list;
    }

    @Override
    public boolean matches(DemijohnRecipeInput demijohnRecipeInput, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        return inputItem1.test(demijohnRecipeInput.getItem(0)) &&
            inputItem2.test(demijohnRecipeInput.getItem(1)) &&
            inputItem3.test(demijohnRecipeInput.getItem(2));
    }

    @Override
    public ItemStack assemble(DemijohnRecipeInput demijohnRecipeInput, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.DEMIJOHN_SERIALISER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.DEMIJOHN_TYPE.get();
    }

    public static class Serialiser implements RecipeSerializer<DemijohnRecipe> {
        public static final MapCodec<DemijohnRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient1").forGetter(DemijohnRecipe::inputItem1),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient2").forGetter(DemijohnRecipe::inputItem2),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient3").forGetter(DemijohnRecipe::inputItem3),
                FluidStack.CODEC.fieldOf("result").forGetter(DemijohnRecipe::output)
        ).apply(inst, DemijohnRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, DemijohnRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, DemijohnRecipe::inputItem1,
                        Ingredient.CONTENTS_STREAM_CODEC, DemijohnRecipe::inputItem2,
                        Ingredient.CONTENTS_STREAM_CODEC, DemijohnRecipe::inputItem3,
                        FluidStack.STREAM_CODEC, DemijohnRecipe::output,
                        DemijohnRecipe::new
                );


        @Override
        public MapCodec<DemijohnRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, DemijohnRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}