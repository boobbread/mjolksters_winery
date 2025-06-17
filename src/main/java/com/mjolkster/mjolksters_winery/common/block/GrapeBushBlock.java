package com.mjolkster.mjolksters_winery.common.block;

import com.mjolkster.mjolksters_winery.common.handler.GrapeMutationHandler;
import com.mjolkster.mjolksters_winery.common.item.GrapeSeedItem;
import com.mjolkster.mjolksters_winery.common.registry.ModBlocks;
import com.mjolkster.mjolksters_winery.common.registry.ModItems;
import com.mjolkster.mjolksters_winery.util.GrapeVariety;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.*;

public class GrapeBushBlock extends SweetBerryBushBlock {
    public static final EnumProperty<GrapeVariety> VARIETY = EnumProperty.create("variety", GrapeVariety.class);
    public static Level grapeLevel;
    public static int altitude;

    public GrapeBushBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AGE, 0)
                .setValue(VARIETY, GrapeVariety.PINOT_NOIR));
    }
    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        GrapeVariety variety = state.getValue(VARIETY);

        return switch (variety) {
            case PINOT_NOIR -> new ItemStack(ModItems.PINOT_NOIR.get());
            case SANGIOVESE -> new ItemStack(ModItems.SANGIOVESE.get());
            case CABERNET_SAUVIGNON -> new ItemStack(ModItems.CABERNET_SAUVIGNON.get());
            case TEMPRANILLO -> new ItemStack(ModItems.TEMPRANILLO.get());
            case MOONDROP -> new ItemStack(ModItems.MOONDROP.get());
            case RUBY_ROMAN -> new ItemStack(ModItems.RUBY_ROMAN.get());
            case AUTUMN_ROYAL -> new ItemStack(ModItems.AUTUMN_ROYAL.get());

            case RIESLING -> new ItemStack(ModItems.RIESLING.get());
            case CHARDONNAY -> new ItemStack(ModItems.CHARDONNAY.get());
            case SAUVIGNON_BLANC -> new ItemStack(ModItems.SAUVIGNON_BLANC.get());
            case PINOT_GRIGIO -> new ItemStack(ModItems.PINOT_GRIGIO.get());
            case COTTON_CANDY -> new ItemStack(ModItems.COTTON_CANDY.get());
            case GRENACHE_BLANC -> new ItemStack(ModItems.GRENACHE_BLANC.get());
            case WATERFALL -> new ItemStack(ModItems.WATERFALL.get());

            case KOSHU -> new ItemStack(ModItems.KOSHU.get());
            case PINOT_DE_LENFER -> new ItemStack(ModItems.PINOT_DE_LENFER.get());
        };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        int age = state.getValue(AGE);
        GrapeVariety variety = state.getValue(VARIETY);
        boolean fullyGrown = age == 3;

        if (age > 1) {
            int dropCount = 1 + level.random.nextInt(2);
            if (fullyGrown) dropCount++;

            Item grapeItem = switch (variety) {
                case PINOT_NOIR -> ModItems.PINOT_NOIR.get();
                case SANGIOVESE -> ModItems.SANGIOVESE.get();
                case CABERNET_SAUVIGNON -> ModItems.CABERNET_SAUVIGNON.get();
                case TEMPRANILLO -> ModItems.TEMPRANILLO.get();
                case MOONDROP -> ModItems.MOONDROP.get();
                case RUBY_ROMAN -> ModItems.RUBY_ROMAN.get();
                case AUTUMN_ROYAL -> ModItems.AUTUMN_ROYAL.get();

                case RIESLING -> ModItems.RIESLING.get();
                case CHARDONNAY -> ModItems.CHARDONNAY.get();
                case SAUVIGNON_BLANC -> ModItems.SAUVIGNON_BLANC.get();
                case PINOT_GRIGIO -> ModItems.PINOT_GRIGIO.get();
                case COTTON_CANDY -> ModItems.COTTON_CANDY.get();
                case GRENACHE_BLANC -> ModItems.GRENACHE_BLANC.get();
                case WATERFALL -> ModItems.WATERFALL.get();

                case KOSHU -> ModItems.KOSHU.get();
                case PINOT_DE_LENFER -> ModItems.PINOT_DE_LENFER.get();
            };

            popResource(level, pos, new ItemStack(grapeItem, dropCount));
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            level.setBlock(pos, state.setValue(AGE, 1), 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, VARIETY);
    }

    public static Map<GrapeVariety, Integer> VARIETY_COLORS = new HashMap<>();
            static {
                VARIETY_COLORS.put(GrapeVariety.PINOT_NOIR, 0xFF1E0926);
                VARIETY_COLORS.put(GrapeVariety.SANGIOVESE, 0xFF200F40);
                VARIETY_COLORS.put(GrapeVariety.CABERNET_SAUVIGNON, 0xFF1C0F2E);
                VARIETY_COLORS.put(GrapeVariety.TEMPRANILLO, 0xFF291261);
                VARIETY_COLORS.put(GrapeVariety.MOONDROP, 0xFF1F072E);
                VARIETY_COLORS.put(GrapeVariety.AUTUMN_ROYAL, 0xFF1B0D40);
                VARIETY_COLORS.put(GrapeVariety.RUBY_ROMAN, 0xFF820C1B);

                VARIETY_COLORS.put(GrapeVariety.RIESLING, 0xFFACB030);
                VARIETY_COLORS.put(GrapeVariety.CHARDONNAY, 0xFFCFBD48);
                VARIETY_COLORS.put(GrapeVariety.SAUVIGNON_BLANC, 0xFFB8A727);
                VARIETY_COLORS.put(GrapeVariety.PINOT_GRIGIO, 0xFFD9B841);
                VARIETY_COLORS.put(GrapeVariety.COTTON_CANDY, 0xFFFFE9AA);
                VARIETY_COLORS.put(GrapeVariety.GRENACHE_BLANC, 0xFFD6D060);
                VARIETY_COLORS.put(GrapeVariety.WATERFALL, 0xFF8DBD4A);

                VARIETY_COLORS.put(GrapeVariety.KOSHU, 0xFFFF8797);
                VARIETY_COLORS.put(GrapeVariety.PINOT_DE_LENFER, 0xFF33060C);
    }


    public static int getColorForVariety(GrapeVariety variety) {
        return VARIETY_COLORS.getOrDefault(variety, 0xFFFFFFFF);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        ItemStack stack = context.getItemInHand();
        if (stack.getItem() instanceof GrapeSeedItem grapeSeedItem) {
            return this.defaultBlockState()
                    .setValue(AGE, 0)
                    .setValue(VARIETY, grapeSeedItem.getVariety());
        }

        return this.defaultBlockState().setValue(AGE, 0).setValue(VARIETY, GrapeVariety.PINOT_NOIR);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!(state.getBlock() instanceof GrapeBushBlock) || state.getValue(AGE) < 2) return;

        GrapeVariety thisVariety = state.getValue(VARIETY);
        System.out.println("Parent variety: " + thisVariety);
        grapeLevel = level;

        List<BlockPos> validAirPositions = new ArrayList<>();
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos target = pos.relative(dir);
            if (level.getBlockState(target).isAir()) {
                validAirPositions.add(target);
            }
        }

        if (validAirPositions.isEmpty()) return;

        BlockPos[] diagonals = {
                pos.north().west(),
                pos.north().east(),
                pos.south().west(),
                pos.south().east()
        };

        GrapeVariety childVariety = thisVariety;

        List<BlockPos> shuffledDiagonals = Arrays.asList(diagonals);
        Collections.shuffle(shuffledDiagonals, new Random(random.nextLong()));

        for (BlockPos diagonal : shuffledDiagonals) {
            BlockState neighborState = level.getBlockState(diagonal);

            if (neighborState.getBlock() instanceof GrapeBushBlock && neighborState.getValue(AGE) >= 2) {
                childVariety = GrapeMutationHandler.determineOffspring(
                        thisVariety,
                        neighborState.getValue(VARIETY),
                        level,
                        pos,
                        random
                );
                break;
            } else {
                GrapeVariety potentialVariety = GrapeMutationHandler.determineSpecialOffspring(
                        thisVariety,
                        neighborState.getBlock(),
                        level,
                        pos,
                        random
                );

                if (potentialVariety != thisVariety) {
                    childVariety = potentialVariety;
                    break;
                }
            }
        }

        BlockPos placementPos = validAirPositions.get(random.nextInt(validAirPositions.size()));
        BlockState newState = ModBlocks.GRAPE_BUSH_BLOCK.get().defaultBlockState()
                .setValue(AGE, 0)
                .setValue(VARIETY, childVariety);

        level.setBlock(placementPos, newState, Block.UPDATE_ALL);
        System.out.println("Placed new bush with variety: " + childVariety);
    }
}
