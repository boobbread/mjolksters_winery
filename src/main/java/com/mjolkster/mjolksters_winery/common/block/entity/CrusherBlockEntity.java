package com.mjolkster.mjolksters_winery.common.block.entity;

import com.mjolkster.mjolksters_winery.common.CommonTags;
import com.mjolkster.mjolksters_winery.common.registry.ModBlockEntities;
import com.mjolkster.mjolksters_winery.common.registry.ModFluids;
import com.mjolkster.mjolksters_winery.common.registry.ModItems;
import com.mjolkster.mjolksters_winery.util.codec.WineData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.mjolkster.mjolksters_winery.common.registry.ModDataComponents.WINE_DATA;
import static com.mjolkster.mjolksters_winery.util.JuiceMap.JUICE_CREATION_MAP;

public class CrusherBlockEntity extends BlockEntity {
    private static final int grapeSlot = 0;
    private static final int juicePerGrape = 200;

    private String grapeType = "";
    private int grapeColour = 0xFFFFFFFF;
    private float grapeSweetness = 0.0f;

    public final FluidTank fluidTank = new FluidTank(FluidType.BUCKET_VOLUME, fluidStack ->
            fluidStack.getFluid() == ModFluids.JUICE.source().get()) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public CrusherBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.CRUSHER_BE.get(), pos, blockState);
    }

    public ItemInteractionResult onUse(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);

        if (isCrushableItem(heldItem)) {
            return insertGrape(player, heldItem);
        }

        if(heldItem.isEmpty()) {
            return crushGrapes(player);
        }

        if(isExtractableContainer(heldItem)) {
            return extractJuice(player, hand);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private ItemInteractionResult insertGrape(Player player, @NotNull ItemStack grapeStack) {
        if (grapeStack.getCount() >= 5 ){
            ItemStack currentGrapes = inventory.getStackInSlot(grapeSlot);

            WineData newType = JUICE_CREATION_MAP.get(grapeStack.getItem());
            if (newType != null && !grapeType.isEmpty() && !newType.name().equals(grapeType)) {
                return ItemInteractionResult.FAIL;
            }

            if (!currentGrapes.isEmpty() &&
                    (!ItemStack.isSameItem(currentGrapes, grapeStack) ||
                            currentGrapes.getCount() >= currentGrapes.getMaxStackSize())) {
                return ItemInteractionResult.FAIL;
            }

            if (currentGrapes.isEmpty()) {
                WineData info = JUICE_CREATION_MAP.get(grapeStack.getItem());
                if (info != null) {
                    this.grapeType = info.name();
                    this.grapeColour = info.colour();
                    this.grapeSweetness = info.wineSweetness();
                }
            }

            ItemStack toInsert = grapeStack.copyWithCount(5);
            if (currentGrapes.isEmpty()) {
                inventory.setStackInSlot(grapeSlot, toInsert);
            } else {
                return ItemInteractionResult.sidedSuccess(level.isClientSide());
            }

            if (!player.isCreative()) {
                grapeStack.shrink(5);
            }

            assert level != null;
            level.playSound(null, worldPosition, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 0.8f, 1.2f);
        }



        setChanged();
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    private ItemInteractionResult crushGrapes(Player player) {
        ItemStack grapes = inventory.getStackInSlot(grapeSlot);

        if (grapes.isEmpty()) {
            return ItemInteractionResult.FAIL;
        }

        int grapeCount = grapes.getCount();

        if (grapeCount >= 5){
            int maxJuiceSpace = fluidTank.getSpace();
            int maxCrushable = maxJuiceSpace / juicePerGrape;
            int toCrush = Math.min(grapeCount, maxCrushable);

            if (toCrush <= 0) {
                return ItemInteractionResult.FAIL;
            }

            grapes.shrink(toCrush);
            fluidTank.fill(new FluidStack(ModFluids.JUICE.source().get(), toCrush * juicePerGrape), IFluidHandler.FluidAction.EXECUTE);

            assert level != null;
            level.playSound(null, worldPosition, SoundEvents.HONEY_BLOCK_BREAK,
                    SoundSource.BLOCKS, 0.8f, 0.9f);
        }

        setChanged();
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    private ItemInteractionResult extractJuice(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);

        if (!fluidTank.isEmpty() && fluidTank.getFluidAmount() >= FluidType.BUCKET_VOLUME) {
            ItemStack filledContainer = fillContainer(heldItem);
            if (filledContainer.isEmpty()) {
                return ItemInteractionResult.FAIL;
            }

            if (!player.isCreative()) {
                heldItem.shrink(1);
                if (heldItem.isEmpty()) {
                    player.setItemInHand(hand, filledContainer);
                } else {
                    player.getInventory().add(filledContainer);
                }
            } else {
                player.getInventory().add(filledContainer.copy());
            }

            this.grapeColour = 0;
            this.grapeType = "";
            this.grapeSweetness = 0.0f;

            level.playSound(null, worldPosition, SoundEvents.BUCKET_FILL,
                    SoundSource.BLOCKS, 0.8f, 1.0f);
            setChanged();
            onDrained();
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }
        return ItemInteractionResult.FAIL;
    }

    private ItemStack fillContainer(ItemStack heldItem) {
        if (grapeColour != 0 && !grapeType.isBlank()) {
            if (heldItem.is(Items.BUCKET)) {
                FluidStack drained = fluidTank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);
                if (drained.getAmount() == FluidType.BUCKET_VOLUME) {
                    fluidTank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
                    ItemStack filledBucket = new ItemStack(ModItems.JUICE_BUCKET.get());

                    WineData wineData = new WineData(grapeColour, grapeType, 0, "none", 0, grapeSweetness);
                    filledBucket.set(WINE_DATA.get(), wineData);

                    return filledBucket;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private boolean isCrushableItem(ItemStack item) {
        if (!item.is(CommonTags.GRAPE) && !item.is(Items.HONEYCOMB)) {
            return false;
        }

        return JUICE_CREATION_MAP.containsKey(item.getItem());
    }

    private boolean isExtractableContainer(ItemStack stack) {
        return stack.is(Items.BUCKET);
    }

    private void onDrained() {
        boolean isCrusherEmpty = fluidTank.isEmpty();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        tag.put("inventory", inventory.serializeNBT(provider));
        CompoundTag fluidTag = new CompoundTag();
        fluidTank.writeToNBT(provider, fluidTag);
        tag.put("fluidTank", fluidTag);
        tag.putString("GrapeType", grapeType);
        tag.putInt("GrapeColour", grapeColour);

        super.saveAdditional(tag, provider);

    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        inventory.deserializeNBT(provider, tag.getCompound("inventory"));
        if (tag.contains("fluidTank")) {
            fluidTank.readFromNBT(provider, tag.getCompound("fluidTank"));
        }
        if (tag.contains("GrapeType")) {
            grapeType = tag.getString("GrapeType");
        }
        if (tag.contains("GrapeColour")) {
            grapeColour = tag.getInt("GrapeColour");
        }
        super.loadAdditional(tag, provider);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
