package com.mjolkster.mjolksters_winery.screen;

import com.mjolkster.mjolksters_winery.Winery;
import com.mjolkster.mjolksters_winery.registry.ModBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class AgingBarrelScreen extends AbstractContainerScreen<AgingBarrelMenu> {
    private static final ResourceLocation OAK_GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Winery.MODID,"textures/gui/oak_aging_barrel_gui.png");
    private static final ResourceLocation SPRUCE_GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Winery.MODID,"textures/gui/spruce_aging_barrel_gui.png");
    private static final ResourceLocation ACACIA_GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Winery.MODID,"textures/gui/acacia_aging_barrel_gui.png");
    private static final ResourceLocation GUI_BOTTLE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Winery.MODID,"textures/gui/aging_barrel_fullness.png");


    public AgingBarrelScreen(AgingBarrelMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        ResourceLocation texture = getTextureForBarrelType(menu.getBlockState());
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(texture, x, y, 0, 0, imageWidth, imageHeight);
        renderBottleFullness(guiGraphics, x, y);
    }

    private void renderBottleFullness(GuiGraphics guiGraphics, int x, int y) {

        int bottleFullness = menu.getBottleFullness();
        int v = 67 - bottleFullness;
        int yPos = y + 12 + v;

        int argb = menu.data.get(4);
        float r = ((argb >> 16) & 0xFF) / 255.0f;
        float g = ((argb >> 8) & 0xFF) / 255.0f;
        float b = (argb & 0xFF) / 255.0f;
        float a = 0.8f;

        guiGraphics.setColor(r,g,b,a);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(GUI_BOTTLE_TEXTURE, x + 69, yPos, 0,v, 38, bottleFullness, 38, 67);
        guiGraphics.setColor(1,1,1,1);
        RenderSystem.disableBlend();
    }

    private ResourceLocation getTextureForBarrelType(BlockState state) {
        Block block = state.getBlock();
        if (block == ModBlocks.OAK_AGING_BARREL.get()) {
            return OAK_GUI_TEXTURE;
        } else if (block == ModBlocks.SPRUCE_AGING_BARREL.get()) {
            return SPRUCE_GUI_TEXTURE;
        } else {
            return ACACIA_GUI_TEXTURE;
    }}

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        pGuiGraphics.drawString(font, title, leftPos + 8, topPos + 6, 0xFFD3D3D3, false);
        pGuiGraphics.drawString(font, playerInventoryTitle, leftPos + 8, topPos + 72, 0xFFD3D3D3, false);
    }
}
