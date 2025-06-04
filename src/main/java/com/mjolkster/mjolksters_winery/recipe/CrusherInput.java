package com.mjolkster.mjolksters_winery.recipe;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class CrusherInput implements RecipeInput {
    public static final CrusherInput EMPTY = new CrusherInput(List.of());
    private final List<ItemStack> items;
    private final StackedContents stackedContents = new StackedContents();
    private int totalCount = 0;

    public CrusherInput(List<ItemStack> items) {
        this.items = items;

        for (ItemStack stack :  items) {
            if (!stack.isEmpty()) {
                totalCount += stack.getCount();
                this.stackedContents.accountStack(stack, stack.getCount());
            }
        }
    }

    public ItemStack getItem(int index) {
        return items.get(index);
    }

    public int size() {
        return this.items.size();
    }

    public boolean isEmpty() {
        return this.totalCount == 0;
    }

    public StackedContents stackedContents() {
        return this.stackedContents;
    }

    public List<ItemStack> items() {
        return this.items;
    }

    public int totalCount() {
        return this.totalCount;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else {
            if (other instanceof CrusherInput) {
                CrusherInput craftingInput = (CrusherInput) other;
                return ItemStack.listMatches(this.items, craftingInput.items);
            }
            return false;
        }
    }

    public int hashCode() {
        return ItemStack.hashStackList(this.items);
    }
}
