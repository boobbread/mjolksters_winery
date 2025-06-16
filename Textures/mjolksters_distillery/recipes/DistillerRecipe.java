package com.mjolkster.mjolksters_winery.common.recipe;

import com.mjolkster.mjolksters_winery.common.registry.ModRecipes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

public record DistillerRecipe(FluidIngredient input, FluidStack output) implements Recipe<DistillerContext> {

    @Override
    public boolean matches(DistillerContext context, Level level) {
        if(level.isClientSide()) {
            return false;
        }
        return input.test(context.getFluid()) && context.getFluid().getAmount() >= input.getStacks()[0].getAmount();
    }

    @Override
    public ItemStack assemble(DistillerContext context, HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.DISTILLER_SERIALISER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.DISTILLER_TYPE.get();
    }

    public static class Serialiser implements RecipeSerializer<DistillerRecipe> {

        public static final MapCodec<DistillerRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                FluidIngredient.CODEC_NON_EMPTY.fieldOf("ingredient").forGetter(DistillerRecipe::input),
                FluidStack.CODEC.fieldOf("result").forGetter(DistillerRecipe::output)
        ).apply(inst, DistillerRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, DistillerRecipe> STREAM_CODEC = StreamCodec.composite(
                FluidIngredient.STREAM_CODEC, DistillerRecipe::input,
                FluidStack.STREAM_CODEC, DistillerRecipe::output,
                DistillerRecipe::new
        );

        @Override
        public MapCodec<DistillerRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, DistillerRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}