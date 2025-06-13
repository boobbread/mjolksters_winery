package com.mjolkster.mjolksters_winery.registry;

import com.mjolkster.mjolksters_winery.Winery;
import com.mjolkster.mjolksters_winery.block.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
    public static final Supplier<Block> CHERRY_AGING_BARREL = BLOCKS.register("cherry_aging_barrel",
            () -> new AgingBarrelBlock(BlockBehaviour.Properties.of()));
    public static final Supplier<Block> CRIMSON_AGING_BARREL = BLOCKS.register("crimson_aging_barrel",
            () -> new AgingBarrelBlock(BlockBehaviour.Properties.of()));

    public static final Supplier<Block> BOTTLING_MACHINE = BLOCKS.register("bottling_machine",
            () -> new BottlingMachineBlock(BlockBehaviour.Properties.of()));

    public static final Supplier<Block> YEAST_POT = BLOCKS.register("yeast_pot",
            () -> new YeastPotBlock(BlockBehaviour.Properties.of()));

    public static final Supplier<Block> SOMMELIERS_TABLE = BLOCKS.register("sommeliers_table",
            () -> new SommeliersTableBlock(BlockBehaviour.Properties.of()));

    public static final Supplier<Block> GRAPE_BUSH_BLOCK = BLOCKS.register("grape_bush_block",
            () -> new GrapeBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).randomTicks()));

    public static final Supplier<Block> TRELLIS = BLOCKS.register("trellis",
            () -> new TrellisBlock(BlockBehaviour.Properties.of()));

}
