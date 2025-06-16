package com.mjolkster.mjolksters_winery.datagen;

import com.mjolkster.mjolksters_winery.MjolkstersWinery;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MjolkstersWinery.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }

}
