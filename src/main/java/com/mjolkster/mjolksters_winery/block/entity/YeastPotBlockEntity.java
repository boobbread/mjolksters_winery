package com.mjolkster.mjolksters_winery.block.entity;

import com.mjolkster.mjolksters_winery.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class YeastPotBlockEntity extends BlockEntity{
    private int ageTicks = 0;
    private static final int REQUIRED_AGE_TICKS = 20 * 30; // 5 minutes, for example

    public YeastPotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.YEAST_POT_BE.get(), pos, state);
    }

    public void tick() {
        if (level == null || level.isClientSide) return;
        ageTicks++;
    }

    public boolean hasAgedEnough() {
        return ageTicks >= REQUIRED_AGE_TICKS;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        tag.putInt("Age", this.ageTicks);
        super.saveAdditional(tag, provider);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        this.ageTicks = tag.getInt("Age");
        super.loadAdditional(tag, provider);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

}
