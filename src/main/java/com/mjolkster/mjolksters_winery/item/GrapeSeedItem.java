package com.mjolkster.mjolksters_winery.item;

import com.mjolkster.mjolksters_winery.block.GrapeBushBlock;
import com.mjolkster.mjolksters_winery.registry.ModBlocks;
import com.mjolkster.mjolksters_winery.util.GrapeMutationHandler;
import com.mjolkster.mjolksters_winery.util.GrapeVariety;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class GrapeSeedItem extends Item {
    private final GrapeVariety variety;

    public GrapeSeedItem(GrapeVariety variety, Properties properties) {
        super(properties);
        this.variety = variety;
    }

    public GrapeVariety getVariety() {
        return this.variety;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState clickedState = level.getBlockState(clickedPos);
        ItemStack stack = context.getItemInHand();
        BlockPos placePos = clickedPos;

        if (clickedState.canBeReplaced()) {
            if (tryPlaceBush(level, placePos, player, stack, variety)) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        if (context.getClickedFace() == net.minecraft.core.Direction.UP) {
            Block clickedBlock = clickedState.getBlock();
            boolean isValidSoil = (
                    clickedBlock == Blocks.GRASS_BLOCK ||
                            clickedBlock == Blocks.DIRT ||
                            clickedBlock == Blocks.COARSE_DIRT ||
                            clickedBlock == Blocks.PODZOL ||
                            clickedBlock == Blocks.FARMLAND
            );
            if (isValidSoil) {
                BlockPos above = clickedPos.above();
                BlockState aboveState = level.getBlockState(above);
                if (aboveState.canBeReplaced()) {
                    placePos = above;
                    return tryPlaceBush(level, placePos, player, stack, variety)
                            ? InteractionResult.SUCCESS : InteractionResult.PASS;
                }
            }
        }

        return super.useOn(context);
    }

    private boolean tryPlaceBush(Level level, BlockPos pos, Player player, ItemStack stack, GrapeVariety varietyToPlace) {
        if (!level.getBlockState(pos).canBeReplaced()) return false;

        level.setBlock(pos,
                ModBlocks.GRAPE_BUSH_BLOCK.get()
                        .defaultBlockState()
                        .setValue(GrapeBushBlock.VARIETY, varietyToPlace),
                3
        );

        if (player != null && !player.isCreative()) {
            stack.shrink(1);
        }
        return true;
    }
}
