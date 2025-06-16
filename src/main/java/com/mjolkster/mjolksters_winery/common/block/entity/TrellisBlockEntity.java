package com.mjolkster.mjolksters_winery.common.block.entity;

import com.mjolkster.mjolksters_winery.common.item.GrapeSeedItem;
import com.mjolkster.mjolksters_winery.common.registry.ModBlockEntities;
import com.mjolkster.mjolksters_winery.common.registry.ModItems;
import com.mjolkster.mjolksters_winery.util.GrapeVariety;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class TrellisBlockEntity extends BlockEntity {

    private static final Map<Item, Integer> GRAPE_COLOUR_MAP = new HashMap<>();
    static {
        GRAPE_COLOUR_MAP.put(ModItems.PINOT_NOIR.get(), 0xFF1E0926);
        GRAPE_COLOUR_MAP.put(ModItems.SANGIOVESE.get(), 0xFF200F40);
        GRAPE_COLOUR_MAP.put(ModItems.CABERNET_SAUVIGNON.get(), 0xFF1C0F2E);
        GRAPE_COLOUR_MAP.put(ModItems.TEMPRANILLO.get(), 0xFF291261);
        GRAPE_COLOUR_MAP.put(ModItems.MOONDROP.get(), 0xFF1F072E);
        GRAPE_COLOUR_MAP.put(ModItems.AUTUMN_ROYAL.get(), 0xFF1B0D40);
        GRAPE_COLOUR_MAP.put(ModItems.RUBY_ROMAN.get(),0xFF820C1B);

        GRAPE_COLOUR_MAP.put(ModItems.RIESLING.get(), 0xFFACB030);
        GRAPE_COLOUR_MAP.put(ModItems.CHARDONNAY.get(), 0xFFCFBD48);
        GRAPE_COLOUR_MAP.put(ModItems.SAUVIGNON_BLANC.get(), 0xFFB8A727);
        GRAPE_COLOUR_MAP.put(ModItems.PINOT_GRIGIO.get(), 0xFFD9B841);
        GRAPE_COLOUR_MAP.put(ModItems.COTTON_CANDY.get(), 0xFFFFE9AA);
        GRAPE_COLOUR_MAP.put(ModItems.GRENACHE_BLANC.get(), 0xFFD6D060);
        GRAPE_COLOUR_MAP.put(ModItems.WATERFALL.get(), 0xFF8DBD4A);

        GRAPE_COLOUR_MAP.put(ModItems.KOSHU.get(), 0xFFFF8797);
        GRAPE_COLOUR_MAP.put(ModItems.PINOT_DE_LENFER.get(), 0xFF33060C);

    }

    private static final Map<GrapeVariety, Item> GRAPE_SEED_MAP = new HashMap<>();
    static {
        GRAPE_SEED_MAP.put(GrapeVariety.PINOT_NOIR, ModItems.PINOT_NOIR.get());
        GRAPE_SEED_MAP.put(GrapeVariety.SANGIOVESE, ModItems.SANGIOVESE.get());
        GRAPE_SEED_MAP.put(GrapeVariety.CABERNET_SAUVIGNON, ModItems.CABERNET_SAUVIGNON.get());
        GRAPE_SEED_MAP.put(GrapeVariety.TEMPRANILLO, ModItems.TEMPRANILLO.get());
        GRAPE_SEED_MAP.put(GrapeVariety.MOONDROP, ModItems.MOONDROP.get());
        GRAPE_SEED_MAP.put(GrapeVariety.AUTUMN_ROYAL, ModItems.AUTUMN_ROYAL.get());
        GRAPE_SEED_MAP.put(GrapeVariety.RUBY_ROMAN, ModItems.RUBY_ROMAN.get());

        GRAPE_SEED_MAP.put(GrapeVariety.RIESLING, ModItems.RIESLING.get());
        GRAPE_SEED_MAP.put(GrapeVariety.CHARDONNAY, ModItems.CHARDONNAY.get());
        GRAPE_SEED_MAP.put(GrapeVariety.SAUVIGNON_BLANC, ModItems.SAUVIGNON_BLANC.get());
        GRAPE_SEED_MAP.put(GrapeVariety.PINOT_GRIGIO, ModItems.PINOT_GRIGIO.get());
        GRAPE_SEED_MAP.put(GrapeVariety.COTTON_CANDY, ModItems.COTTON_CANDY.get());
        GRAPE_SEED_MAP.put(GrapeVariety.GRENACHE_BLANC, ModItems.GRENACHE_BLANC.get());
        GRAPE_SEED_MAP.put(GrapeVariety.WATERFALL, ModItems.WATERFALL.get());

        GRAPE_SEED_MAP.put(GrapeVariety.KOSHU, ModItems.KOSHU.get());
        GRAPE_SEED_MAP.put(GrapeVariety.PINOT_DE_LENFER, ModItems.PINOT_DE_LENFER.get());

    }

    public TrellisBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TRELLIS_BE.get(), pos, blockState);
    }

    public GrapeVariety grapeVariety;
    public int grapeColour;
    public boolean hasBush;
    public GrapeSeedItem grapeSeed;
    public int bushAge;

    public ItemInteractionResult useOn(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);

        if (heldItem.getItem() instanceof GrapeSeedItem seedItem) {
           return placeBush(seedItem, heldItem, player, pos);
        }

        if (heldItem.is(Items.SHEARS)) {
            return removeBush(player, grapeVariety, pos);
        }

        if (heldItem.is(Items.BONE_MEAL)) {
            float chance = level.random.nextFloat();
            heldItem.shrink(1);

            if (chance <= 0.5) {
                increaseAge();
                level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS);
            }
        }

        if (heldItem.isEmpty()) {
            return harvestBush(player, grapeVariety, pos);
        }

        return ItemInteractionResult.FAIL;
    }

    private ItemInteractionResult harvestBush(Player player, GrapeVariety variety, BlockPos pos) {
        if (hasBush && bushAge == 3) {

            bushAge = 2;

            assert level != null;
            int grapeCount = level.random.nextInt(1,4);

            ItemStack seedStack = new ItemStack(GRAPE_SEED_MAP.get(grapeVariety), grapeCount);
            player.getInventory().add(seedStack);

            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;

        } else return ItemInteractionResult.FAIL;
    }

    private ItemInteractionResult removeBush(Player player, GrapeVariety variety, BlockPos pos) {
        if (hasBush) {
            bushAge = 0;
            hasBush = false;

            ItemStack seedStack = new ItemStack(GRAPE_SEED_MAP.get(grapeVariety));
            player.getInventory().add(seedStack);

            level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;

        } else return ItemInteractionResult.FAIL;
    }

    private ItemInteractionResult placeBush(GrapeSeedItem seed, ItemStack stack, Player player, BlockPos pos) {
        if (!hasBush) {
            grapeVariety = seed.getVariety();
            grapeColour = GRAPE_COLOUR_MAP.get(seed);
            hasBush = true;
            bushAge = 0;

            if (!player.isCreative()) {
                stack.shrink(1);
            }

            level.playSound(null, pos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        } else return ItemInteractionResult.FAIL;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TrellisBlockEntity be) {
        if (!level.isClientSide && level.random.nextInt(1000) == 0) {
            be.increaseAge();
        }
    }

    public void increaseAge() {
        if(hasBush) {
            if (bushAge < 3) {
                bushAge++;

                if (level != null && !level.isClientSide) {
                    setChanged();
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        tag.putInt("grapeColour", grapeColour);
        tag.putBoolean("hasBush", hasBush);
        tag.putInt("bushAge", bushAge);

        if (grapeVariety != null) {
            tag.putString("grapeVariety", grapeVariety.name());
        }

        super.saveAdditional(tag, provider);

    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        grapeColour = tag.getInt("grapeColour");
        hasBush = tag.getBoolean("hasBush");
        bushAge = tag.getInt("bushAge");

        if (tag.contains("grapeVariety")) {
            try {
                grapeVariety = GrapeVariety.valueOf(tag.getString("grapeVariety"));
            } catch (IllegalArgumentException e) {
                grapeVariety = null;
            }
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
