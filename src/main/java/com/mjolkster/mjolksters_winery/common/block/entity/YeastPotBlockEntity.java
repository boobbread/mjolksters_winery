package com.mjolkster.mjolksters_winery.common.block.entity;

import com.mjolkster.mjolksters_winery.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class YeastPotBlockEntity extends BlockEntity{
    private int ageTicks = 0;
    private static final int REQUIRED_AGE_TICKS = 20 * 60 * 5;

    public YeastPotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.YEAST_POT_BE.get(), pos, state);
    }

    public void tick() {
        if (level == null || level.isClientSide) return;

        if (hasAgedEnough()) {
            if (level.random.nextFloat() > 0.95) {
                spawnBubbleParticles();
            }
        } else {
            ageTicks++;
        }
    }

    public boolean hasAgedEnough() {
        return ageTicks >= REQUIRED_AGE_TICKS;
    }

    private void spawnBubbleParticles() {
        if (!(level instanceof ServerLevel serverLevel)) return;

        double x = worldPosition.getX() + 0.5;
        double y = worldPosition.getY() + 0.5;
        double z = worldPosition.getZ() + 0.5;

        BlockPos pos = getBlockPos();
        serverLevel.playSound(null, pos, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.BLOCKS, 0.8f, 0.5f);

        serverLevel.sendParticles(
                ParticleTypes.DUST_PLUME,
                x, y, z,
                2,
                0.02, 0.05, 0.02,
                0.05
        );
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
