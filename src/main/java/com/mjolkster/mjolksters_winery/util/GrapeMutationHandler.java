package com.mjolkster.mjolksters_winery.util;

import com.mjolkster.mjolksters_winery.block.GrapeBushBlock;
import com.mjolkster.mjolksters_winery.item.GrapeSeedItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import java.util.*;

public class GrapeMutationHandler {
    public static GrapeVariety determineOffspring(GrapeVariety thisVariety, GrapeVariety thatVariety, Level level, BlockPos pos, RandomSource random) {
        Pair<GrapeVariety, GrapeVariety> parentVarieties = Pair.of(thisVariety, thatVariety);

        GrapeVariety p1 = parentVarieties.getFirst();
        GrapeVariety p2 = parentVarieties.getSecond();

        if (p1 == p2 && level.random.nextFloat() < 0.1) {
            if (p1 == GrapeVariety.PINOT_NOIR) {
                List<GrapeVariety> potentialMutants = List.of(GrapeVariety.CABERNET_SAUVIGNON, GrapeVariety.AUTUMN_ROYAL);
                int randomChoice = level.random.nextInt(potentialMutants.size());
                return potentialMutants.get(randomChoice);
            } else if (p1 == GrapeVariety.PINOT_GRIGIO) {
                    List<GrapeVariety> potentialMutants = List.of(GrapeVariety.SAUVIGNON_BLANC, GrapeVariety.GRENACHE_BLANC);
                    int randomChoice = level.random.nextInt(potentialMutants.size());
                return potentialMutants.get(randomChoice);
            }
        }

        GrapeVariety mutated = getMutatedVariety(p1, p2);
        float mutationChance = getMutationChance(level, pos);

        if (mutated != null && level.random.nextFloat() < mutationChance) {
            return mutated;
        } else {
            return thisVariety;
        }
    }

    public static GrapeVariety getMutatedVariety(GrapeVariety v1, GrapeVariety v2) {

        if (v1 == v2) {

            if (v1 == GrapeVariety.PINOT_NOIR){

                if (isNight(GrapeBushBlock.grapeLevel)) {
                    return GrapeVariety.MOONDROP;
                } else return GrapeVariety.PINOT_GRIGIO;

            } else if (v1 == GrapeVariety.PINOT_GRIGIO) {

                return GrapeVariety.PINOT_NOIR;
            }

            return GrapeVariety.PINOT_NOIR;

        } else {

            Set<GrapeVariety> parentVarieties = Set.of(v1, v2);

            if (parentVarieties.contains(GrapeVariety.PINOT_GRIGIO) && parentVarieties.contains(GrapeVariety.PINOT_NOIR) && GrapeBushBlock.altitude > 100) {
                return GrapeVariety.SANGIOVESE;
            }

            return MUTATION_MAP.get(parentVarieties);
        }
    }

    public static float getMutationChance(Level level, BlockPos pos) {
        float mutationChance = 0.25f;
        if (isBeehiveNearby(level, pos, 10)) {
            mutationChance = 0.5f;
        }
        return mutationChance;
    }

    public static final Map<Set<GrapeVariety>, GrapeVariety> MUTATION_MAP = Map.of(
            Set.of(GrapeVariety.COTTON_CANDY, GrapeVariety.RUBY_ROMAN), GrapeVariety.KOSHU,
            Set.of(GrapeVariety.MOONDROP, GrapeVariety.PINOT_GRIGIO), GrapeVariety.WATERFALL
    );

    public static boolean isBeehiveNearby(Level level, BlockPos center, int radius) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    mutable.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                    BlockState state = level.getBlockState(mutable);
                    if (state.getBlock() == Blocks.BEEHIVE || state.getBlock() == Blocks.BEE_NEST) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean isNight(Level level) {
        long timeOfDay = level.getDayTime() % 24000;
        return timeOfDay >= 13000 && timeOfDay < 23000;
    }

    public static GrapeVariety determineSpecialOffspring(GrapeVariety thisVariety, Block otherParent, ServerLevel level, BlockPos pos, RandomSource random) {

        float roll = level.random.nextFloat();
        float mutationChance = getMutationChance(level, pos);

        if (thisVariety == GrapeVariety.PINOT_GRIGIO && otherParent.defaultBlockState().is(Blocks.SUGAR_CANE)) {
            return roll < mutationChance ? GrapeVariety.COTTON_CANDY : thisVariety;
        } else if (thisVariety == GrapeVariety.PINOT_GRIGIO && otherParent.defaultBlockState().is(Blocks.SWEET_BERRY_BUSH)) {
            return roll < mutationChance ? GrapeVariety.RUBY_ROMAN : thisVariety;
        } else if (thisVariety == GrapeVariety.PINOT_NOIR && otherParent.defaultBlockState().is(Blocks.PITCHER_PLANT)) {
            return roll < mutationChance ? GrapeVariety.TEMPRANILLO : thisVariety;
        } else if (thisVariety == GrapeVariety.PINOT_NOIR && otherParent.defaultBlockState().is(Blocks.NETHER_WART)) {
            return roll < mutationChance ? GrapeVariety.PINOT_DE_LENFER : thisVariety;
        }

        return thisVariety;
    }
}
