package com.mjolkster.mjolksters_winery.common.block;

import com.mjolkster.mjolksters_winery.common.block.entity.YeastPotBlockEntity;
import com.mjolkster.mjolksters_winery.common.registry.ModBlockEntities;
import com.mjolkster.mjolksters_winery.common.registry.ModItems;
import com.mjolkster.mjolksters_winery.util.BiomeType;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class YeastPotBlock extends BaseEntityBlock {
    public static final MapCodec<YeastPotBlock> CODEC = simpleCodec(YeastPotBlock::new);
    public static final VoxelShape SHAPE = Block.box(5.5,0,5.5,10.5,7.5,10.5);
    public static final EnumProperty<BiomeType> BIOME_TYPE = EnumProperty.create("biome_type", BiomeType.class);

    public YeastPotBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(BIOME_TYPE, BiomeType.MILD));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BIOME_TYPE);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!world.isClientSide) {
            BiomeType type = getBiomeType(world, pos);
            world.setBlock(pos, state.setValue(BIOME_TYPE, type), 2);
        }
        super.onPlace(state, world, pos, oldState, isMoving);
    }

    private BiomeType getBiomeType(Level level, BlockPos pos) {
        Holder<Biome> biomeHolder = level.getBiome(pos);

        float temperature = biomeHolder.value().getBaseTemperature();

        if (temperature < 0.0f) {
            return BiomeType.FREEZING;
        } else if (temperature < 0.3f) {
            return BiomeType.COLD;
        } else if (temperature < 0.8f) {
            return BiomeType.MILD;
        } else if (temperature < 1.5f) {
            return BiomeType.WARM;
        } else {
            return BiomeType.HOT;
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) {
            return null;
        }

        return createTickerHelper(blockEntityType, ModBlockEntities.YEAST_POT_BE.get(),
                (level1, blockPos, blockState, blockEntity) -> {
                    blockEntity.tick();
                });
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getParameter(LootContextParams.BLOCK_ENTITY);
        if (!(blockEntity instanceof YeastPotBlockEntity yeastPot)) {
            return List.of();
        }

        if (!yeastPot.hasAgedEnough()) {
            return List.of();
        }

        System.out.println("hasAgedEnough: " + yeastPot.hasAgedEnough());

        BiomeType temp = state.getValue(BIOME_TYPE);
        Item item;

        switch (temp) {
            case FREEZING -> item = ModItems.PREMIER_CUVEE.get();
            case COLD     -> item = ModItems.MONTRACHE.get();
            case MILD     -> item = ModItems.CHAMPAGNE.get();
            case WARM     -> item = ModItems.PASTEUR_RED.get();
            case HOT      -> item = ModItems.COTE_DES_BLANCS.get();
            default       -> item = Items.AIR;
        }

        return item == Items.AIR ? List.of() : List.of(new ItemStack(item));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new YeastPotBlockEntity(blockPos, blockState);
    }
}
