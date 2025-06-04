package com.mjolkster.mjolksters_winery.block.entity;

import com.mjolkster.mjolksters_winery.recipe.DistillerContext;
import com.mjolkster.mjolksters_winery.recipe.DistillerRecipe;
import com.mjolkster.mjolksters_winery.registry.ModBlockEntities;
import com.mjolkster.mjolksters_winery.registry.ModRecipes;
import com.mjolkster.mjolksters_winery.screen.DistillerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;

import java.util.Optional;

public class DistillerBlockEntity extends BlockEntity implements MenuProvider {
    public final FluidTank inputTank = new FluidTank(FluidType.BUCKET_VOLUME, this::isFluidValid) {
        protected void onContentsChanged() {
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            };
        }
    };

    public final FluidTank outputTank = new FluidTank(FluidType.BUCKET_VOLUME, this::isFluidValid) {
        protected void onContentsChanged() {
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            };
        }
    };

    public DistillerContext getContext() {
        return new DistillerContext(inputTank.getFluid());
    }

    protected final ContainerData data;

    public int gameTime = 0;

    public DistillerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DISTILLER_BE.get(), pos, blockState);

        data = new ContainerData() {

            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> inputTank.getFluidAmount();
                    case 1 -> inputTank.getCapacity();
                    case 2 -> outputTank.getFluidAmount();
                    case 3 -> outputTank.getCapacity();
                    case 4 -> {
                        FluidStack fluid = inputTank.getFluid();
                        if (!fluid.isEmpty()) {
                            FluidType type = fluid.getFluid().getFluidType();
                            IClientFluidTypeExtensions cExt = IClientFluidTypeExtensions.of(type);
                            yield cExt.getTintColor();
                        }
                        yield 0xFFFFFFFF; // Default to white if no fluid
                    }
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
            }

            @Override
            public int getCount() {
                return 5;
            }
        };
    }

    private int totalCraftTime = 100; // in ticks

    private int inputPerTick;
    private float outputPerTick;

    private int ticksProgressed = 0;
    private float outputAccumulator = 0;

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.mjolksters_winery.distiller");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new DistillerMenu(i, inventory, this, this.data);
    }

    public ItemInteractionResult onBucketUse(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        Optional<FluidStack> contained = FluidUtil.getFluidContained(heldItem);
        // 1. Handle extracting fluid into an empty bucket
        if (heldItem.is(Items.BUCKET)) {
            if (!outputTank.isEmpty()) {
                FluidStack drained = outputTank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);
                if (!drained.isEmpty()) {

                    // Perform extraction;
                    if (!player.isCreative()) {
                        heldItem.shrink(1);
                    }

                    // Give player the filled bucket
                    ItemStack filledBucket = drained.getFluid().getBucket().getDefaultInstance();
                    if (!player.getInventory().add(filledBucket)) {
                        player.drop(filledBucket, false);
                    }
                    outputTank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);

                    setChanged();
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide());
                }
            }
        } else if (contained.isPresent()) {
            FluidStack fluidStack = contained.get();
            if (inputTank.isFluidValid(fluidStack)) {
                int filledAmount = inputTank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
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

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {

        CompoundTag outputTag = new CompoundTag();
        outputTank.writeToNBT(pRegistries, outputTag);
        pTag.put("outputTank", outputTag);

        CompoundTag inputTag = new CompoundTag();
        inputTank.writeToNBT(pRegistries, inputTag);
        pTag.put("inputTank", inputTag);

        pTag.putInt("Progress", ticksProgressed);
        pTag.putFloat("OutputAccumulator", outputAccumulator);

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        if (pTag.contains("inputTank")) {
            inputTank.readFromNBT(pRegistries, pTag.getCompound("inputTank"));
        }
        if (pTag.contains("outputTank")) {
            outputTank.readFromNBT(pRegistries, pTag.getCompound("outputTank"));
        }

        ticksProgressed = pTag.getInt("Progress");
        outputAccumulator = pTag.getFloat("OutputAccumulator");
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (outputTank.getFluidAmount() >= 1000){
            return;
        }

        if (hasRecipe()) {
            setChanged();
            processCraftingTick();
        } else {
            ticksProgressed = 0;
            outputAccumulator = 0;
        }
    }

    private void processCraftingTick() {
        Optional<RecipeHolder<DistillerRecipe>> recipeOpt = getCurrentRecipe();
        if (recipeOpt.isEmpty()) return;

        DistillerRecipe recipe = recipeOpt.get().value();

        // Calculate per-tick amounts from recipe
        inputPerTick = 1000 / totalCraftTime;
        outputPerTick = (float) recipe.output().getAmount() / totalCraftTime;

        FluidStack input = inputTank.getFluid();
        FluidStack output = recipe.output();

        // Validate input
        if (input.getAmount() < inputPerTick) return;
        if (!recipe.input().test(input)) return;

        // Handle fractional output
        outputAccumulator += outputPerTick;
        int outputThisTick = (int) outputAccumulator;
        outputAccumulator -= outputThisTick;

        if (outputThisTick == 0) return; // Nothing to add this tick

        // Check output capacity
        FluidStack simulatedOutput = output.copy();
        simulatedOutput.setAmount(outputThisTick);
        if (outputTank.fill(simulatedOutput, IFluidHandler.FluidAction.SIMULATE) < outputThisTick) {
            return;
        }

        // Perform transfer
        inputTank.drain(inputPerTick, IFluidHandler.FluidAction.EXECUTE);
        outputTank.fill(simulatedOutput, IFluidHandler.FluidAction.EXECUTE);

        ticksProgressed++;

        // Handle completion
        if (ticksProgressed >= totalCraftTime) {
            level.playSound(null, worldPosition, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS);
            ticksProgressed = 0;
            outputAccumulator = 0;
        }
    }

    private boolean isFluidValid(FluidStack stack) {
        return true;
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<DistillerRecipe>> recipeOutput = getCurrentRecipe();
        if (recipeOutput.isEmpty()) return false;

        DistillerRecipe recipe = recipeOutput.get().value();

        // Check input fluid and amount
        FluidStack input = inputTank.getFluid();
        if (input.getAmount() < 1000) return false;
        if (!recipe.input().test(input)) return false;

        // Check output capacity
        return outputTank.fill(recipe.output().copy(), IFluidHandler.FluidAction.SIMULATE) > 0;
    }

    private Optional<RecipeHolder<DistillerRecipe>> getCurrentRecipe() {
        FluidStack fluid = inputTank.getFluid();

        DistillerContext context = new DistillerContext(fluid);
        Optional<RecipeHolder<DistillerRecipe>> recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipes.DISTILLER_TYPE.get(), context, level);
        return recipe;
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
