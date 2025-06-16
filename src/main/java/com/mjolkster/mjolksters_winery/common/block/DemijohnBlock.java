package com.mjolkster.mjolksters_winery.common.block;

import com.mjolkster.mjolksters_winery.common.block.entity.DemijohnBlockEntity;
import com.mjolkster.mjolksters_winery.common.registry.ModBlockEntities;
import com.mjolkster.mjolksters_winery.common.registry.ModSounds;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DemijohnBlock extends BaseEntityBlock {

    public static final VoxelShape SHAPE = Block.box(4,0,4,12,12,12);
    public static final MapCodec<DemijohnBlock> CODEC = simpleCodec(DemijohnBlock::new);

    public DemijohnBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DemijohnBlockEntity(blockPos, blockState);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(state.getBlock() != newState.getBlock()) {
            if(level.getBlockEntity(pos) instanceof DemijohnBlockEntity demijohnBlockEntity) {
                demijohnBlockEntity.drops();
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos,
                                              Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {

            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof DemijohnBlockEntity demijohn) {

                ItemInteractionResult bucketResult = demijohn.onBucketUse(pPlayer, pHand);
                if (bucketResult.consumesAction()) {
                    pLevel.playSound(pPlayer, pPos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS);
                    return bucketResult;
                }

                if(!pLevel.isClientSide()); {
                    pLevel.playSound(pPlayer, pPos, ModSounds.DEMIJOHN_OPEN.get(), SoundSource.BLOCKS);
                    pPlayer.openMenu(new SimpleMenuProvider(demijohn, Component.literal("Demijohn")), pPos);
                }
                return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());

            }

        return ItemInteractionResult.FAIL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) {
            return null;
        }

        return createTickerHelper(blockEntityType, ModBlockEntities.DEMIJOHN_BE.get(),
                (level1, blockPos, blockState, blockEntity) -> blockEntity.tick(level1, blockPos, blockState));
    }
}
