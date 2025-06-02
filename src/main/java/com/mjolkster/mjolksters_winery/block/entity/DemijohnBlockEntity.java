package com.mjolkster.mjolksters_winery.block.entity;

import com.mjolkster.mjolksters_winery.recipe.DemijohnRecipe;
import com.mjolkster.mjolksters_winery.recipe.DemijohnRecipeInput;
import com.mjolkster.mjolksters_winery.registry.ModRecipes;
import com.mjolkster.mjolksters_winery.registry.ModBlockEntities;
import com.mjolkster.mjolksters_winery.screen.DemijohnMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;


public class DemijohnBlockEntity extends BlockEntity implements MenuProvider {
    public final FluidTank fluidTank = new FluidTank(FluidType.BUCKET_VOLUME, this::isFluidValid) {
        protected void onContentsChanged() {

        }
    };
    public final ItemStackHandler inventory = new ItemStackHandler(3) {

        @Override
        protected void onContentsChanged(int slot) {
            if (hasRecipe()) {
                level.playSound(null, getBlockPos(), SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS);
            }
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            };
        }
    };

    private static final int INPUT_SLOT1 = 0;
    private static final int INPUT_SLOT2 = 1;
    private static final int INPUT_SLOT3 = 2;


    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 600;

    private boolean hasInitialItem = false;
    private boolean hasCraftingFinished = false;

    public DemijohnBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DEMIJOHN_BE.get(), pos, blockState);
        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> DemijohnBlockEntity.this.progress;
                    case 1 -> DemijohnBlockEntity.this.maxProgress;
                    case 2 -> fluidTank.getFluidAmount();
                    case 3 -> fluidTank.getCapacity();
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0: DemijohnBlockEntity.this.progress = value;
                    case 1: DemijohnBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.mjolksters_winery.demijohn");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new DemijohnMenu(i, inventory, this, this.data);
    }

    public ItemInteractionResult onBucketUse(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);

        // Try to fill a bucket
        // Early exit if not a bucket
        if (!heldItem.is(Items.BUCKET)) {
            return ItemInteractionResult.FAIL;
        }

        // Check if we can extract fluid
        if (!fluidTank.isEmpty() && hasCraftingFinished) {
            FluidStack drained = fluidTank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);
            if (!drained.isEmpty()) {

                // Perform extraction
                fluidTank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
                heldItem.shrink(1);

                // Give filled bucket
                ItemStack filledBucket = drained.getFluid().getBucket().getDefaultInstance();
                if (!player.getInventory().add(filledBucket)) {
                    player.drop(filledBucket, false);
                }

                setChanged();
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                onFluidDrained();
                return ItemInteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return ItemInteractionResult.FAIL;
    }

    private void fillToMax() {
        if (fluidTank.isEmpty()) {
            Optional<RecipeHolder<DemijohnRecipe>> recipe = getCurrentRecipe();
            if (recipe.isPresent()) {
                FluidStack maxFill = recipe.get().value().output().copy();
                maxFill.setAmount(fluidTank.getCapacity());
                fluidTank.fill(maxFill, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    public void onFluidDrained() {
        if (fluidTank.isEmpty()) {
            hasInitialItem = false;
            hasCraftingFinished = false;
        }
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for(int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {

        pTag.put("inventory", inventory.serializeNBT(pRegistries));

        CompoundTag fluidTag = new CompoundTag();
        fluidTank.writeToNBT(pRegistries, fluidTag);
        pTag.put("fluidTank", fluidTag);

        pTag.putInt("demijohn.progress", progress);
        pTag.putInt("demijohn.max_progress", maxProgress);

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        inventory.deserializeNBT(pRegistries, pTag.getCompound("inventory"));

        if (pTag.contains("fluidTank")) {
            fluidTank.readFromNBT(pRegistries, pTag.getCompound("fluidTank"));
        }

        progress = pTag.getInt("demijohn.progress");
        maxProgress = pTag.getInt("demijohn.max_progress");
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if(hasCraftingFinished && !fluidTank.isEmpty()) return;

        if(hasRecipe()) {
            if(!hasInitialItem) {
                hasInitialItem = true;
                setChanged();
                fillToMax();
                if(!level.isClientSide()){
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                };
            }

            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);

            if(hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void craftItem() {
        Optional<RecipeHolder<DemijohnRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return;

        FluidStack output = recipe.get().value().output().copy();

        for (int i = 0; i < 3; i++) {
            ItemStack input = inventory.getStackInSlot(i);
            if (!input.isEmpty()) {
                BlockPos pos = getBlockPos();
                level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS);
                ItemStack container = input.getCraftingRemainingItem();
                inventory.extractItem(i,1,false);

                if (!container.isEmpty()){
                    inventory.setStackInSlot(i, container);
                } else {
                    if (!level.isClientSide && level != null){
                        Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), container);
                    }
                }
            }
        }

        hasCraftingFinished = true;
        setChanged();

        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }


    }

    private void resetProgress() {
        progress = 0;
        maxProgress = 600;
        hasInitialItem = false;
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private boolean isFluidValid(FluidStack stack) {
        // Add any fluid validation logic here
        return true;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<DemijohnRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return false;

        for (int i = 0; i < 3; i++) {
            if (inventory.getStackInSlot(i).getCount() < 1) {
                return false;
            }
        }

        if(hasCraftingFinished && !fluidTank.isEmpty()) return false;

        FluidStack output = recipe.get().value().output();
        if (fluidTank.isEmpty()) {
            int fillableAmount = fluidTank.fill(output, IFluidHandler.FluidAction.SIMULATE);
            return fillableAmount == output.getAmount();
        }
        return true;
    }

    private Optional<RecipeHolder<DemijohnRecipe>> getCurrentRecipe() {

        // List of items that are in the demijohn
        List<ItemStack> items = List.of(
                inventory.getStackInSlot(INPUT_SLOT1),
                inventory.getStackInSlot(INPUT_SLOT2),
                inventory.getStackInSlot(INPUT_SLOT3)
        );

        DemijohnRecipeInput input = new DemijohnRecipeInput(3, 1, items);
        Optional<RecipeHolder<DemijohnRecipe>> recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipes.DEMIJOHN_TYPE.get(), input, level);
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

