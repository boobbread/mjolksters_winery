package com.mjolkster.mjolksters_winery.common.block.entity;

import com.mjolkster.mjolksters_winery.client.screen.AgingBarrelMenu;
import com.mjolkster.mjolksters_winery.common.item.WineBucketItem;
import com.mjolkster.mjolksters_winery.common.registry.ModBlockEntities;
import com.mjolkster.mjolksters_winery.common.registry.ModFluids;
import com.mjolkster.mjolksters_winery.util.codec.WineData;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;

import static com.mjolkster.mjolksters_winery.common.registry.ModDataComponents.WINE_DATA;

public class AgingBarrelBlockEntity extends BlockEntity implements MenuProvider {
    public final FluidTank fluidTank = new FluidTank(4* FluidType.BUCKET_VOLUME, this::isFluidValid) {
        protected void onContentsChanged() {
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    protected final ContainerData data;

    public int gameTime = 0;
    public int inputWineColour = 0;
    public String inputWineName = "";
    public float inputAlcoholPercentage = 0.0f;
    public float inputWineSweetness = 0.0f;

    public AgingBarrelBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.AGING_BARREL_BE.get(), pos, blockState);
        if (level != null) {
            int gameTime = (int)level.getGameTime();
        }
        data = new ContainerData() {

            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> fluidTank.getFluidAmount();
                    case 1 -> fluidTank.getCapacity();
                    case 2 -> gameTime;
                    case 3 -> (int)AgingBarrelBlockEntity.this.insertTime;
                    case 4 -> inputWineColour;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0: gameTime = value;
                    case 1: insertTime = value;

                }
            }

            @Override
            public int getCount() {
                return 5;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.mjolksters_winery.aging_barrel");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new AgingBarrelMenu(i, inventory, this, this.data);
    }



    private long insertTime = 0;

    public ItemInteractionResult onBucketUse(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.is(Items.BUCKET)) {
            if (!fluidTank.isEmpty()) {
                FluidStack drained = fluidTank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);
                if (!drained.isEmpty()) {
                    long ageInTicks = level.getGameTime() - insertTime;
                    String state = getBlockState().toString();
                    WineData data = new WineData(inputWineColour, inputWineName, (int)ageInTicks, state, inputAlcoholPercentage, inputWineSweetness);

                    DataComponentPatch agedComponents = DataComponentPatch.builder()
                            .set(WINE_DATA.get(), data)
                            .build();

                    FluidStack agedDrained = new FluidStack(
                            drained.getFluidHolder(),
                            drained.getAmount(),
                            agedComponents);

                    if (!player.isCreative()) {
                        heldItem.shrink(1);
                    }

                    ItemStack filledBucket = agedDrained.getFluid().getBucket().getDefaultInstance();
                    filledBucket.applyComponents(agedComponents);
                    if (!player.getInventory().add(filledBucket)) {
                        player.drop(filledBucket, false);
                    }
                    fluidTank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);

                    setChanged();
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide());
                }
            }
        } else if (heldItem.getItem() instanceof WineBucketItem) {
            FluidStack fluidStack = new FluidStack(ModFluids.WINE.source().get(), FluidType.BUCKET_VOLUME);
            if (fluidTank.isFluidValid(fluidStack)) {
                int filledAmount = fluidTank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                if (filledAmount == FluidType.BUCKET_VOLUME) {

                    getInfo(heldItem);

                    assert level != null;
                    this.insertTime = level.getGameTime();

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

    private void getInfo(ItemStack heldItem) {
        if (!heldItem.isEmpty()) {
            WineData nullWineData = new WineData(0xFFFFFFFF,"none", 0, "none",0, 0);
            WineData wineData = heldItem.getOrDefault(WINE_DATA.get(), nullWineData);
            this.inputWineColour = wineData.colour();
            this.inputWineName = wineData.name();
            this.inputAlcoholPercentage = wineData.alcoholPercentage();
            this.inputWineSweetness = wineData.wineSweetness();

        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {

        CompoundTag fluidTag = new CompoundTag();
        fluidTank.writeToNBT(pRegistries, fluidTag);
        pTag.put("fluidTank", fluidTag);
        pTag.putInt("inputWineColour", inputWineColour);
        pTag.putString("inputWineName", inputWineName);
        pTag.putFloat("inputAlcohol", inputAlcoholPercentage);
        pTag.putFloat("inputSweetness", inputWineSweetness);

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        if (pTag.contains("fluidTank")) {
            fluidTank.readFromNBT(pRegistries, pTag.getCompound("fluidTank"));
        }

        inputWineColour = pTag.getInt("inputWineColour");
        inputWineName = pTag.getString("inputWineName");
        inputAlcoholPercentage = pTag.getFloat("inputAlcohol");
        inputWineSweetness = pTag.getFloat("inputSweetness");
    }

    private boolean isFluidValid(FluidStack stack) {
        FluidStack fluidInBarrel = this.fluidTank.getFluid();

        if(fluidInBarrel.isEmpty()) {
            return true;
        }
        return fluidInBarrel.getFluid().isSame(stack.getFluid());
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
