package com.mjolkster.mjolksters_winery.compat;

import com.mjolkster.mjolksters_winery.MjolkstersWinery;
import com.mjolkster.mjolksters_winery.client.screen.DemijohnScreen;
import com.mjolkster.mjolksters_winery.common.recipe.DemijohnRecipe;
import com.mjolkster.mjolksters_winery.common.registry.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEIMjolkstersWineryPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(MjolkstersWinery.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new DemijohnRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()
        ));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<DemijohnRecipe> demijohnRecipes = recipeManager
                .getAllRecipesFor(ModRecipes.DEMIJOHN_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(DemijohnRecipeCategory.DEMIJOHN_RECIPE_TYPE, demijohnRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(DemijohnScreen.class, 114, 8, 38,66,
                DemijohnRecipeCategory.DEMIJOHN_RECIPE_TYPE);
    }
}
