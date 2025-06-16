package com.mjolkster.mjolksters_winery.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Objects;

public class DistillerContext implements RecipeInput {
    private final FluidStack fluid;

    public DistillerContext(FluidStack fluid) {
        this.fluid = fluid;
    }

    public FluidStack getFluid() {
        return this.fluid;
    }

    @Override
    public ItemStack getItem(int i) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return this.fluid.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DistillerContext that)) return false;
        return this.fluid.getFluid().isSame(that.fluid.getFluid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(fluid.getFluid(), fluid.getAmount());
    }
}
