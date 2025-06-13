package com.mjolkster.mjolksters_winery.block;

import com.mjolkster.mjolksters_winery.block.entity.TrellisBlockEntity;
import com.mjolkster.mjolksters_winery.registry.ModBlockEntities;
import com.mjolkster.mjolksters_winery.util.TrellisConnections;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TrellisBlock extends BaseEntityBlock {

    public static final EnumProperty<TrellisConnections> CONNECTION = EnumProperty.create("connection", TrellisConnections.class);
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final MapCodec<TrellisBlock> CODEC = simpleCodec(TrellisBlock::new);

    public static final VoxelShape T_NONE = Block.box(7,0.000001,7,9,16,9);
    public static final VoxelShape T_EAST = Block.box(7,0.000001,7,16,16,9);
    public static final VoxelShape T_WEST = Block.box(0,0.000001,7,9,16,9);
    public static final VoxelShape T_BOTH = Block.box(0,0.000001,7,16,16,9);

    public TrellisBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(defaultBlockState()
                .setValue(CONNECTION, TrellisConnections.NONE)
                .setValue(TOP, false));
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
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(CONNECTION) == TrellisConnections.EAST_WEST) {
            return T_BOTH;
        } else if (state.getValue(CONNECTION) == TrellisConnections.EAST) {
            return T_EAST;
        } else if (state.getValue(CONNECTION) == TrellisConnections.WEST) {
            return T_WEST;
        } else return T_NONE;
    }


    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);

        // Update self and block above
        updateTopState(level, pos);
        updateTopState(level, pos.above());
    }

    private void updateTopState(LevelAccessor level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof TrellisBlock) {
            boolean top = level.getBlockState(pos.below()).getBlock() instanceof TrellisBlock;
            level.setBlock(pos, state.setValue(TOP, top), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction.getAxis().isHorizontal()) {
            return state.setValue(CONNECTION, getConnectionState(level, pos));
        } else if (direction == Direction.DOWN) {
            return state.setValue(TOP, level.getBlockState(pos.below()).is(this));
        }
        return state;
    }

    private TrellisConnections getConnectionState(LevelAccessor level, BlockPos pos) {
        boolean connectsEast = canConnectTo(level.getBlockState(pos.east()));
        boolean connectsWest = canConnectTo(level.getBlockState(pos.west()));

        if (connectsEast && connectsWest) return TrellisConnections.EAST_WEST;
        if (connectsEast) return TrellisConnections.EAST;
        if (connectsWest) return TrellisConnections.WEST;
        return TrellisConnections.NONE;
    }

    private boolean canConnectTo(BlockState state) {
        return state.getBlock() instanceof TrellisBlock;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONNECTION, TOP);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos,
                                              Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {

        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof TrellisBlockEntity trellis) {
            ItemInteractionResult result = trellis.useOn(pState, pLevel, pPos, pPlayer, pHand, pHitResult);
            if (result.consumesAction()) {
                return result;
            }
            return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
        }

        return ItemInteractionResult.FAIL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TrellisBlockEntity(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.TRELLIS_BE.get(), TrellisBlockEntity::tick);
    }
}
