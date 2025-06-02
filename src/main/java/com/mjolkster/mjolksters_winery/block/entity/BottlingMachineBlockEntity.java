package com.mjolkster.mjolksters_winery.block.entity;

import com.mjolkster.mjolksters_winery.registry.ModFluids;
import com.mjolkster.mjolksters_winery.registry.ModItems;
import com.mjolkster.mjolksters_winery.registry.ModBlockEntities;
import com.mjolkster.mjolksters_winery.screen.BottlingMachineMenu;
import com.mjolkster.mjolksters_winery.tag.CommonTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static com.mjolkster.mjolksters_winery.block.BottlingMachineBlock.FACING;
import static com.mjolkster.mjolksters_winery.block.BottlingMachineBlock.IN_USE;
import static com.mjolkster.mjolksters_winery.registry.ModDataComponents.WINE_AGE;
import static com.mjolkster.mjolksters_winery.registry.ModDataComponents.WOOD_TYPE;
import static com.mjolkster.mjolksters_winery.registry.ModFluids.RED_WINE;
import static com.mjolkster.mjolksters_winery.registry.ModFluids.WHITE_WINE;

public class BottlingMachineBlockEntity extends BlockEntity implements MenuProvider {
    public final FluidTank fluidTank = new FluidTank(FluidType.BUCKET_VOLUME, this::isFluidValid) {
        protected void onContentsChanged() {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
            ;
        }
    };

    public final ItemStackHandler inventory = new ItemStackHandler(3) {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                if (slot == BOTTLE_OUTPUT_SLOT) {
                    // Immediately update IN_USE state on output slot change
                    BlockState state = level.getBlockState(getBlockPos());
                    boolean outputIsEmpty = inventory.getStackInSlot(BOTTLE_OUTPUT_SLOT).isEmpty();
                    boolean inUse = state.getValue(IN_USE);
                    if (outputIsEmpty && inUse) {
                        level.setBlock(getBlockPos(), state.setValue(IN_USE, false).setValue(FACING, state.getValue(FACING)), 3);
                    } else if (!outputIsEmpty && !inUse) {
                        level.setBlock(getBlockPos(), state.setValue(IN_USE, true).setValue(FACING, state.getValue(FACING)), 3);
                    }
                }
            }
        }
    };

    private static final int INFUSION_SLOT = 0;
    private static final int BOTTLE_INPUT_SLOT = 1;
    private static final int BOTTLE_OUTPUT_SLOT = 2;

    private String woodType = "";
    private int wineAge = 0;

    public float fillProgress = 0;

    protected final ContainerData data;

    public BottlingMachineBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.BOTTLING_MACHINE_BE.get(), pos, blockState);

        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> fluidTank.getFluidAmount();
                    case 1 -> fluidTank.getCapacity();
                    case 2 -> {
                        FluidStack fluid = fluidTank.getFluid();
                        if (!fluid.isEmpty()) {
                            FluidType type = fluid.getFluid().getFluidType();
                            IClientFluidTypeExtensions cExt = IClientFluidTypeExtensions.of(type);
                            yield cExt.getTintColor();
                        }
                        yield 0xFFFFFFFF;

                    }
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    public ItemInteractionResult onBucketUse(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        Optional<FluidStack> contained = FluidUtil.getFluidContained(heldItem);

        if (contained.isPresent()) {
            this.wineAge = heldItem.getOrDefault(WINE_AGE.get(),0);
            this.woodType = heldItem.getOrDefault(WOOD_TYPE.get(), "none");
        }

        System.out.print("Wine Age " + wineAge + "Wood Type " + woodType);
        // 1. Handle extracting fluid into an empty bucket
        if (heldItem.is(Items.BUCKET)) {
            if (!fluidTank.isEmpty()) {
                FluidStack drained = fluidTank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);
                if (!drained.isEmpty()) {
                    return ItemInteractionResult.FAIL;
                }
            }
        } else if (contained.isPresent()) {
            FluidStack fluidStack = contained.get();
            if (fluidTank.isFluidValid(fluidStack)) {
                int filledAmount = fluidTank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                if (filledAmount == FluidType.BUCKET_VOLUME) {

                    heldItem.shrink(1);
                    ItemStack emptyBucket = new ItemStack(Items.BUCKET);

                    if (!player.getInventory().add(emptyBucket)) {
                        player.drop(emptyBucket, false);
                    }

                    setChanged();
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide());
                }
            }
        }

        return ItemInteractionResult.FAIL;
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        boolean inUse = state.getValue(IN_USE);

        if (!level.isClientSide) {
            if (canBottle()) {
                if (fluidTank.getFluidAmount() > 0) {
                    fillProgress += 0.01f; // Increment progress by some amount per tick
                    if (fillProgress > 1f) fillProgress = 1f;

                } else {
                    fillProgress = 0f;
                }

                if (!inUse) {
                    level.setBlock(pos, state.setValue(IN_USE, true).setValue(FACING, state.getValue(FACING)), 3);
                }

                // Only set inUse = true **after** output is removed
                if (fillProgress >= 1.0f) {
                    doBottling();
                    fillProgress = 0f;


                } else {
                    // Still filling — keep inUse false
                    if (inUse) {
                        level.setBlock(pos, state.setValue(IN_USE, true).setValue(FACING, state.getValue(FACING)), 3);
                    }
                }
            } else {
                fillProgress = 0f;

                // Check if output slot is empty (replace this check with your inventory check)
                boolean outputIsEmpty = isOutputEmpty();

                if (outputIsEmpty) {
                    if (!inUse) {
                        level.setBlock(pos, state.setValue(IN_USE, false).setValue(FACING, state.getValue(FACING)), 3);
                    }
                } else {
                    // If output is still there, inUse stays false
                    if (inUse) {
                        level.setBlock(pos, state.setValue(IN_USE, true).setValue(FACING, state.getValue(FACING)), 3);
                    }
                }
            }

            setFillProgress(fillProgress);
        }
    }

    private static final Map<Fluid, Supplier<Item>> FLUID_TO_BOTTLE = Map.of(
            RED_WINE.source().get(), ModItems.RED_WINE_BOTTLE,
            ModFluids.WHITE_WINE.source().get(), ModItems.WHITE_WINE_BOTTLE
    );

    private boolean isOutputEmpty() {
        ItemStack output = this.inventory.getStackInSlot(BOTTLE_OUTPUT_SLOT);
        return output.isEmpty();
    }

    private void doBottling() {
        ItemStack input = inventory.getStackInSlot(BOTTLE_INPUT_SLOT);
        ItemStack output = inventory.getStackInSlot(BOTTLE_OUTPUT_SLOT);
        FluidStack fluid = fluidTank.getFluid();

        DataComponentPatch agedComponents = DataComponentPatch.builder()
                .set(WINE_AGE.get(), wineAge)
                .set(WOOD_TYPE.get(), woodType)
                .build();

        if (fluid.isEmpty() || input.isEmpty() || output.getCount() >= output.getMaxStackSize()) {
            // Cannot bottle: no fluid, no input, or output slot full
            return;
        }

        Item resultItem = FLUID_TO_BOTTLE.get(fluid.getFluid()).get();
        ItemStack filledBottle = new ItemStack(resultItem);
        filledBottle.applyComponents(agedComponents);

        // Remove 1 glass bottle
        input.shrink(1);

        // Drain 250 mB of fluid
        fluidTank.drain(250, IFluidHandler.FluidAction.EXECUTE);

        // Insert output
        if (output.isEmpty()) {
            inventory.setStackInSlot(BOTTLE_OUTPUT_SLOT, filledBottle);
        } else {
            output.grow(1);
        }

        setChanged();
        if (!level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }// Mark block entity as modified for saving
    }

    public void setFillProgress(float progress) {
        this.fillProgress = progress;
        setChanged();  // Mark block entity as changed

        if (level != null && !level.isClientSide) {
            BlockState state = getBlockState();
            level.sendBlockUpdated(worldPosition, state, state, 3);
        }
    }

    private boolean canBottle() {
        ItemStack input = inventory.getStackInSlot(BOTTLE_INPUT_SLOT);
        ItemStack output = inventory.getStackInSlot(BOTTLE_OUTPUT_SLOT);
        FluidStack fluid = fluidTank.getFluid();

        // Must have an empty bottle
        if (!input.is(Items.GLASS_BOTTLE)) {
            return false;
        }

        // Must have enough fluid (e.g., 250 mB)
        if (fluid.isEmpty() || fluid.getAmount() < 250) {
            return false;
        }

        // Must be a fluid we can bottle
        if (!FLUID_TO_BOTTLE.containsKey(fluid.getFluid())) {
            return false;
        }

        // Output slot must be empty or stackable with the result
        Item resultItem = FLUID_TO_BOTTLE.get(fluid.getFluid()).get();
        if (output.isEmpty()) return true;
        if (!output.is(resultItem)) return false;
        return output.getCount() < output.getMaxStackSize();
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("block.mjolksters_winery.bottling_machine");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BottlingMachineMenu(i, inventory, this, this.data);
    }

    private boolean isFluidValid(FluidStack stack) {
        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {

        pTag.put("inventory", inventory.serializeNBT(pRegistries));

        CompoundTag fluidTag = new CompoundTag();
        fluidTank.writeToNBT(pRegistries, fluidTag);
        pTag.put("fluidTank", fluidTag);
        pTag.putFloat("bottling_machine.fill_progress", fillProgress);
        pTag.putInt("bottling_machine.wine_age", wineAge);
        pTag.putString("bottling_machine.wood_type", woodType);

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        inventory.deserializeNBT(pRegistries, pTag.getCompound("inventory"));

        if (pTag.contains("fluidTank")) {
            fluidTank.readFromNBT(pRegistries, pTag.getCompound("fluidTank"));
        }

        fillProgress = pTag.getFloat("bottling_machine.fill_progress");
        wineAge = pTag.getInt("bottling_machine.wine_age");
        woodType = pTag.getString("bottling_machine.wood_type");
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
