package com.mjolkster.mjolksters_winery.fluid;

import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

public class JuiceFluid extends BaseFluidType{
    public JuiceFluid(ResourceLocation stillTexture, ResourceLocation flowingTexture, ResourceLocation overlayTexture, int tintColor, Vector3f fogColor, Properties properties) {
        super(stillTexture, flowingTexture, overlayTexture, tintColor, fogColor, properties);
    }
}
