package com.mjolkster.mjolksters_winery.registry;

import com.mjolkster.mjolksters_winery.Winery;
import com.mjolkster.mjolksters_winery.block.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, Winery.MODID);

    public static final Supplier<Block> DEMIJOHN = BLOCKS.register("demijohn",
            () -> new DemijohnBlock(BlockBehaviour.Properties.of().noOcclusion().sound(SoundType.GLASS).strength(1.5f)));

    public static final Supplier<Block> CRUSHER = BLOCKS.register("crusher",
            () -> new CrusherBlock(BlockBehaviour.Properties.of()));

    public static final Supplier<Block> OAK_AGING_BARREL = BLOCKS.register("oak_aging_barrel",
            () -> new AgingBarrelBlock(BlockBehaviour.Properties.of()));
    public static final Supplier<Block> SPRUCE_AGING_BARREL = BLOCKS.register("spruce_aging_barrel",
            () -> new AgingBarrelBlock(BlockBehaviour.Properties.of()));
    public static final Supplier<Block> ACACIA_AGING_BARREL = BLOCKS.register("acacia_aging_barrel",
            () -> new AgingBarrelBlock(BlockBehaviour.Properties.of()));

    public static final Supplier<Block> BOTTLING_MACHINE = BLOCKS.register("bottling_machine",
            () -> new BottlingMachineBlock(BlockBehaviour.Properties.of()));
}
