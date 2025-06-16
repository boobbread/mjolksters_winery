package com.mjolkster.mjolksters_winery.common.registry;

import com.mjolkster.mjolksters_winery.MjolkstersWinery;
import com.mjolkster.mjolksters_winery.common.block.entity.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MjolkstersWinery.MODID);

    public static final Supplier<BlockEntityType<DemijohnBlockEntity>> DEMIJOHN_BE =
            BLOCK_ENTITIES.register("demijohn_be", () -> BlockEntityType.Builder.of(
                    DemijohnBlockEntity::new, ModBlocks.DEMIJOHN.get()).build(null)
            );

    public static final Supplier<BlockEntityType<CrusherBlockEntity>> CRUSHER_BE =
            BLOCK_ENTITIES.register("crusher_be", () -> BlockEntityType.Builder.of(
                    CrusherBlockEntity::new, ModBlocks.CRUSHER.get()).build(null)
            );

    public static final Supplier<BlockEntityType<AgingBarrelBlockEntity>> AGING_BARREL_BE =
            BLOCK_ENTITIES.register("aging_barrel_be", () -> BlockEntityType.Builder.of(
                    AgingBarrelBlockEntity::new,
                    ModBlocks.OAK_AGING_BARREL.get(),
                    ModBlocks.SPRUCE_AGING_BARREL.get(),
                    ModBlocks.CHERRY_AGING_BARREL.get(),
                    ModBlocks.CRIMSON_AGING_BARREL.get()
                    ).build(null)
            );

    public static final Supplier<BlockEntityType<BottlingMachineBlockEntity>> BOTTLING_MACHINE_BE =
            BLOCK_ENTITIES.register("bottling_machine_be", () -> BlockEntityType.Builder.of(
                    BottlingMachineBlockEntity::new, ModBlocks.BOTTLING_MACHINE.get()).build(null)
            );

    public static final Supplier<BlockEntityType<YeastPotBlockEntity>> YEAST_POT_BE =
            BLOCK_ENTITIES.register("yeast_pot_be", () -> BlockEntityType.Builder.of(
                    YeastPotBlockEntity::new, ModBlocks.YEAST_POT.get()).build(null)
            );

    public static final Supplier<BlockEntityType<SommeliersTableBlockEntity>> SOMMELIERS_TABLE_BE =
            BLOCK_ENTITIES.register("sommeliers_table_be", () -> BlockEntityType.Builder.of(
                    SommeliersTableBlockEntity::new, ModBlocks.SOMMELIERS_TABLE.get()).build(null)
            );

    public static final Supplier<BlockEntityType<TrellisBlockEntity>> TRELLIS_BE =
            BLOCK_ENTITIES.register("trellis_be", () -> BlockEntityType.Builder.of(
                    TrellisBlockEntity::new, ModBlocks.TRELLIS.get()).build(null)
            );
}
