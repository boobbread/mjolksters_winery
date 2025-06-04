package com.mjolkster.mjolksters_winery.recipe;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class DemijohnRecipeInput implements RecipeInput {
    public static final DemijohnRecipeInput EMPTY = new DemijohnRecipeInput(3, 1, List.of());
    private final int width;
    private final int height;
    private final List<ItemStack> items;
    private final StackedContents stackedContents = new StackedContents();
    private final int ingredientCount;

    public DemijohnRecipeInput(int width, int height, List<ItemStack> item) {
        this.width = width;
        this.height = height;
        this.items = item;
        int i = 0;

        for(ItemStack itemstack : item) {
            if (!itemstack.isEmpty()) {
                ++i;
                this.stackedContents.accountStack(itemstack, 1);
            }
        }

        this.ingredientCount = i;
    }

    public static DemijohnRecipeInput of(int width, int height, List<ItemStack> items) {
        return ofPositioned(width, height, items).input();
    }

    public static DemijohnRecipeInput.Positioned ofPositioned(int width, int height, List<ItemStack> items) {
        if (width != 0 && height != 0) {
            int i = width - 1;
            int j = 0;
            int k = height - 1;
            int l = 0;

            for(int i1 = 0; i1 < height; ++i1) {
                boolean flag = true;

                for(int j1 = 0; j1 < width; ++j1) {
                    ItemStack itemstack = (ItemStack)items.get(j1 + i1 * width);
                    if (!itemstack.isEmpty()) {
                        i = Math.min(i, j1);
                        j = Math.max(j, j1);
                        flag = false;
                    }
                }

                if (!flag) {
                    k = Math.min(k, i1);
                    l = Math.max(l, i1);
                }
            }

            int i2 = j - i + 1;
            int j2 = l - k + 1;
            if (i2 > 0 && j2 > 0) {
                if (i2 == width && j2 == height) {
                    return new DemijohnRecipeInput.Positioned(new DemijohnRecipeInput(width, height, items), i, k);
                } else {
                    List<ItemStack> list = new ArrayList(i2 * j2);

                    for(int k2 = 0; k2 < j2; ++k2) {
                        for(int k1 = 0; k1 < i2; ++k1) {
                            int l1 = k1 + i + (k2 + k) * width;
                            list.add((ItemStack)items.get(l1));
                        }
                    }

                    return new DemijohnRecipeInput.Positioned(new DemijohnRecipeInput(i2, j2, list), i, k);
                }
            } else {
                return DemijohnRecipeInput.Positioned.EMPTY;
            }
        } else {
            return DemijohnRecipeInput.Positioned.EMPTY;
        }
    }

    public ItemStack getItem(int index) {
        return (ItemStack)this.items.get(index);
    }

    public ItemStack getItem(int row, int column) {
        return (ItemStack)this.items.get(row + column * this.width);
    }

    public int size() {
        return this.items.size();
    }

    public boolean isEmpty() {
        return this.ingredientCount == 0;
    }

    public StackedContents stackedContents() {
        return this.stackedContents;
    }

    public List<ItemStack> items() {
        return this.items;
    }

    public int ingredientCount() {
        return this.ingredientCount;
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else {
            boolean var10000;
            if (other instanceof DemijohnRecipeInput) {
                DemijohnRecipeInput craftingInput = (DemijohnRecipeInput)other;
                var10000 = this.width == craftingInput.width && this.height == craftingInput.height && this.ingredientCount == craftingInput.ingredientCount && ItemStack.listMatches(this.items, craftingInput.items);
            } else {
                var10000 = false;
            }

            return var10000;
        }
    }

    public int hashCode() {
        int i = ItemStack.hashStackList(this.items);
        i = 31 * i + this.width;
        return 31 * i + this.height;
    }

    public record Positioned(DemijohnRecipeInput input, int left, int top) {
        public static final DemijohnRecipeInput.Positioned EMPTY;

        static {
            EMPTY = new DemijohnRecipeInput.Positioned(DemijohnRecipeInput.EMPTY, 0, 0);
        }
    }
}
