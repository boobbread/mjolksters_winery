package com.mjolkster.mjolksters_winery.block.entity;

import com.mjolkster.mjolksters_winery.codec.JuiceType;
import com.mjolkster.mjolksters_winery.item.JuiceBucketItem;
import com.mjolkster.mjolksters_winery.registry.ModBlockEntities;
import com.mjolkster.mjolksters_winery.registry.ModFluids;
import com.mjolkster.mjolksters_winery.registry.ModItems;
import com.mjolkster.mjolksters_winery.tag.CommonTags;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import static com.mjolkster.mjolksters_winery.registry.ModDataComponents.JUICE_TYPE;

public class CrusherBlockEntity extends BlockEntity {
    private static final int grapeSlot = 0;
    private static final int juicePerGrape = 200;

    private String grapeType = "";
    private int grapeColour = 0xFFFFFFFF;

    private static final Map<Item, JuiceType> GRAPE_JUICE_MAP = new HashMap<>();
    static {
        GRAPE_JUICE_MAP.put(ModItems.PINOT_NOIR.get(), new JuiceType(0xFF1E0926, "Pinot Noir"));
        GRAPE_JUICE_MAP.put(ModItems.SANGIOVESE.get(), new JuiceType(0xFF200F40, "Sangiovese"));
        GRAPE_JUICE_MAP.put(ModItems.CABERNET_SAUVIGNON.get(), new JuiceType(0xFF1C0F2E, "Cabernet Sauvignon"));
        GRAPE_JUICE_MAP.put(ModItems.TEMPRANILLO.get(), new JuiceType(0xFF291261, "Tempranillo"));
        GRAPE_JUICE_MAP.put(ModItems.MOONDROP.get(), new JuiceType(0xFF1F072E, "Moondrop"));
        GRAPE_JUICE_MAP.put(ModItems.AUTUMN_ROYAL.get(), new JuiceType(0xFF1B0D40, "Autumn Royal"));
        GRAPE_JUICE_MAP.put(ModItems.RUBY_ROMAN.get(), new JuiceType(0xFF820C1B, "Ruby Roman"));

        GRAPE_JUICE_MAP.put(ModItems.RIESLING.get(), new JuiceType(0xFFACB030, "Riesling"));
        GRAPE_JUICE_MAP.put(ModItems.CHARDONNAY.get(), new JuiceType(0xFFCFBD48, "Chardonnay"));
        GRAPE_JUICE_MAP.put(ModItems.SAUVIGNON_BLANC.get(), new JuiceType(0xFFB8A727, "Sauvignon Blanc"));
        GRAPE_JUICE_MAP.put(ModItems.PINOT_GRIGIO.get(), new JuiceType(0xFFD9B841, "Pinot Grigio"));
        GRAPE_JUICE_MAP.put(ModItems.COTTON_CANDY.get(), new JuiceType(0xFFFFE9AA, "Cotton Candy"));
        GRAPE_JUICE_MAP.put(ModItems.GRENACHE_BLANC.get(), new JuiceType(0xFFD6D060, "Grenache Blanc"));
        GRAPE_JUICE_MAP.put(ModItems.WATERFALL.get(), new JuiceType(0xFF8DBD4A, "Waterfall"));

        GRAPE_JUICE_MAP.put(ModItems.KOSHU.get(), new JuiceType(0xFFFF8797, "Koshu"));
        GRAPE_JUICE_MAP.put(ModItems.PINOT_DE_LENFER.get(), new JuiceType(0xFF33060C, "Pinot De l'Enfer"));

        GRAPE_JUICE_MAP.put(Items.SWEET_BERRIES, new JuiceType(0xFFFFE9AA, "Sweet Berry"));
        GRAPE_JUICE_MAP.put(Items.GLOW_BERRIES, new JuiceType(0xFFE6922C, "Glow Berry"));
        GRAPE_JUICE_MAP.put(Items.CHORUS_FRUIT, new JuiceType(0xFF8F5CB5, "Chorus"));
    }

    public final FluidTank fluidTank = new FluidTank(3 * FluidType.BUCKET_VOLUME, fluidStack ->
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

        if (isGrapeItem(heldItem)) {
            System.out.print("Grape Event");
            return insertGrape(player, heldItem);
        }

        if(heldItem.isEmpty()) {
            System.out.print("Crush Event");
            return crushGrapes(player);
        }

        if(isExtractableContainer(heldItem)) {
            System.out.print("Bucket Event");
            return extractJuice(player, hand);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private ItemInteractionResult insertGrape(Player player, ItemStack grapeStack) {
        ItemStack currentGrapes = inventory.getStackInSlot(grapeSlot);

        JuiceType newType = GRAPE_JUICE_MAP.get(grapeStack.getItem());
        if (newType != null && !grapeType.isEmpty() && !newType.name().equals(grapeType)) {
            return ItemInteractionResult.FAIL; // Different grape type
        }

        if (!currentGrapes.isEmpty() &&
                (!ItemStack.isSameItem(currentGrapes, grapeStack) ||
                        currentGrapes.getCount() >= currentGrapes.getMaxStackSize())) {
            return ItemInteractionResult.FAIL;
        }

        if (currentGrapes.isEmpty()) {
            JuiceType info = GRAPE_JUICE_MAP.get(grapeStack.getItem());
            if (info != null) {
                this.grapeType = info.name();
                this.grapeColour = info.colour();
            }
        }

        ItemStack toInsert = grapeStack.copyWithCount(1);
        if (currentGrapes.isEmpty()) {
            inventory.setStackInSlot(grapeSlot, toInsert);
        } else {
            currentGrapes.grow(1);
        }

        if (!player.isCreative()) {
            grapeStack.shrink(1);
        }

        level.playSound(null, worldPosition, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 0.8f, 1.2f);
        setChanged();
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    private ItemInteractionResult crushGrapes(Player player) {
        ItemStack grapes = inventory.getStackInSlot(grapeSlot);

        if (grapes.isEmpty()) {
            return ItemInteractionResult.FAIL;
        }

        FluidStack simulatedFill = new FluidStack(ModFluids.JUICE.source().get(), juicePerGrape);
        int filled = fluidTank.fill(simulatedFill, IFluidHandler.FluidAction.SIMULATE);
        if (filled < juicePerGrape) {
            return ItemInteractionResult.FAIL;
        }

        grapes.shrink(1);
        fluidTank.fill(simulatedFill, IFluidHandler.FluidAction.EXECUTE);

        level.playSound(null, worldPosition, SoundEvents.HONEY_BLOCK_BREAK,
                SoundSource.BLOCKS, 0.8f, 0.9f);
        setChanged();
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    private ItemInteractionResult extractJuice(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);

        // Allow any juice bucket (empty)
        if (!(heldItem.getItem() instanceof JuiceBucketItem)) {
            return ItemInteractionResult.FAIL;
        }

        // Check if bucket is empty (no juice type component)
        if (heldItem.get(JUICE_TYPE.get()) != null) {
            return ItemInteractionResult.FAIL;
        }

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

            level.playSound(null, worldPosition, SoundEvents.BUCKET_FILL,
                    SoundSource.BLOCKS, 0.8f, 1.0f);
            setChanged();
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }
        return ItemInteractionResult.FAIL;
    }

    private ItemStack fillContainer(ItemStack heldItem) {
        if (heldItem.is(ModItems.JUICE_BUCKET.get())) {
            FluidStack drained = fluidTank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);
            if (drained.getAmount() == FluidType.BUCKET_VOLUME) {
                fluidTank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
                ItemStack filledBucket = new ItemStack(ModItems.JUICE_BUCKET.get());

                JuiceType juiceType = new JuiceType(grapeColour, grapeType);
                System.out.println("Setting juice type: " + juiceType.name() + ", Color: " + juiceType.colour());
                filledBucket.set(JUICE_TYPE.get(), juiceType);

                return filledBucket;
            }
        }
        return ItemStack.EMPTY;
    }

    private boolean isGrapeItem(ItemStack item) {
        if (!item.is(CommonTags.GRAPE)) {
            return false;
        }

        return GRAPE_JUICE_MAP.containsKey(item.getItem());
    }

    private boolean isExtractableContainer(ItemStack stack) {
        return stack.getItem() instanceof JuiceBucketItem;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        tag.put("inventory", inventory.serializeNBT(provider));
        CompoundTag fluidTag = new CompoundTag();
        fluidTank.writeToNBT(provider, fluidTag);
        tag.put("fluidTank", fluidTag);
        tag.putString("GrapeType", grapeType);
        tag.putInt("GrapeColor", grapeColour);

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
        if (tag.contains("GrapeColor")) {
            grapeColour = tag.getInt("GrapeColor");
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
