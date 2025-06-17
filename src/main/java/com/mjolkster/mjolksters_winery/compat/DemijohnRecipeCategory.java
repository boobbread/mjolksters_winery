package com.mjolkster.mjolksters_winery.compat;

import com.mjolkster.mjolksters_winery.MjolkstersWinery;
import com.mjolkster.mjolksters_winery.common.block.entity.CrusherBlockEntity;
import com.mjolkster.mjolksters_winery.common.registry.ModDataComponents;
import com.mjolkster.mjolksters_winery.common.recipe.DemijohnRecipe;
import com.mjolkster.mjolksters_winery.common.registry.ModBlocks;
import com.mjolkster.mjolksters_winery.common.registry.ModItems;
import com.mjolkster.mjolksters_winery.util.codec.WineData;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DemijohnRecipeCategory implements IRecipeCategory<DemijohnRecipe> {

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(MjolkstersWinery.MODID, "demijohn");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MjolkstersWinery.MODID, "textures/gui/demijohn_gui.png");

    public static final RecipeType<DemijohnRecipe> DEMIJOHN_RECIPE_TYPE =
            new RecipeType<>(UID, DemijohnRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public DemijohnRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE,0,0,176,85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.DEMIJOHN.get()));
    }

    public static ItemStack createJuiceBucket(int colour, String name, int barrelAge, String barrelType, float alc, float sweet) {
        ItemStack stack = new ItemStack(ModItems.JUICE_BUCKET.get());
        stack.set(ModDataComponents.WINE_DATA, new WineData(colour, name, barrelAge, barrelType, alc, sweet));
        return stack;
    }

    @Override
    public RecipeType<DemijohnRecipe> getRecipeType() {
        return DEMIJOHN_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Demijohn");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DemijohnRecipe recipe, IFocusGroup focuses) {

        List<ItemStack> juiceBuckets = CrusherBlockEntity.JUICE_MAP.values().stream()
                        .map(data -> createJuiceBucket(data.colour(), data.name(), 0,"none", 0,0))
                        .toList();

        builder.addSlot(RecipeIngredientRole.INPUT, 34, 34).addItemStacks(juiceBuckets);
        builder.addSlot(RecipeIngredientRole.INPUT, 54, 34).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 74, 34).addIngredients(recipe.getIngredients().get(2));
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return background;
    }
}
