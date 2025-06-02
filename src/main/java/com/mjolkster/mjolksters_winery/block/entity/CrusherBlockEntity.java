package com.mjolkster.mjolksters_winery.block.entity;

import com.mjolkster.mjolksters_winery.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CrusherBlockEntity extends BlockEntity {
    public CrusherBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.CRUSHER_BE.get(), pos, blockState);
    }
}
