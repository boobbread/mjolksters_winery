package com.mjolkster.mjolksters_winery.common.block.entity;

import com.mjolkster.mjolksters_winery.client.screen.SommeliersTableMenu;
import com.mjolkster.mjolksters_winery.common.registry.ModBlockEntities;
import com.mjolkster.mjolksters_winery.common.registry.ModDataComponents;
import com.mjolkster.mjolksters_winery.util.WineQualityChecker;
import com.mjolkster.mjolksters_winery.util.WineQualityChecker.WineRating;
import com.mjolkster.mjolksters_winery.util.codec.WineData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.mjolkster.mjolksters_winery.common.CommonTags.BOTTLE;
import static com.mjolkster.mjolksters_winery.common.registry.ModDataComponents.WINE_QUALITY;

public class SommeliersTableBlockEntity extends BlockEntity implements MenuProvider {

    float overall = 0;
    float age = 0;
    float alcohol = 0;
    float barrel = 0;
    float sweetness = 0;
    String name = "";

    public final ItemStackHandler inventory = new ItemStackHandler(2) {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int BOTTLE_INPUT_SLOT = 0;
    private static final int BOTTLE_OUTPUT_SLOT = 1;

    protected final ContainerData data;

    public SommeliersTableBlockEntity( BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SOMMELIERS_TABLE_BE.get(), pos, blockState);

        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {

                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("block.mjolksters_winery.sommeliers_table");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new SommeliersTableMenu(i, inventory, this, this.data);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        ItemStack inputBottle = inventory.getStackInSlot(BOTTLE_INPUT_SLOT);

        if (inputBottle.is(BOTTLE)) {
            processBottle();
        }
    }

    private void processBottle() {
        ItemStack inputBottle = inventory.getStackInSlot(BOTTLE_INPUT_SLOT);
        int inputCount = inputBottle.getCount();

        if (!level.isClientSide) {
            WineRating rating = WineQualityChecker.getChecked(inputBottle);
            this.overall = rating.overallRating();
            this.age = rating.ageRating();
            this.alcohol = rating.alcoholRating();
            this.barrel = rating.barrelRating();
            this.sweetness = rating.sweetnessRating();
        }
        WineData wineData = inputBottle.get(ModDataComponents.WINE_DATA.get());

        ItemStack outputBottle = new ItemStack(inputBottle.getItem(), inputCount);
        this.name = wineData.name();

        int numStars = Math.round(overall * 5);
        String star = "★";
        String starRating = star.repeat(numStars);
        outputBottle.set(DataComponents.ITEM_NAME, Component.literal(name + " " + starRating));


        if (wineData != null) {

            List<Component> tooltips = new ArrayList<>();

            tooltips.add(createTooltip("Age", formatAge(wineData.barrelAge()), age));
            tooltips.add(createTooltip("Alcohol", formatPercentage(wineData.alcoholPercentage()), alcohol));
            tooltips.add(createTooltip("Sweetness", formatSweetness(wineData.wineSweetness()), sweetness));
            tooltips.add(createTooltip("Barrel", formatBarrel(wineData.barrelType()), barrel));

            outputBottle.set(DataComponents.LORE, new ItemLore(tooltips));
            outputBottle.set(WINE_QUALITY.get(), numStars);
        }

        inputBottle.shrink(inputCount);
        inventory.setStackInSlot(BOTTLE_OUTPUT_SLOT, outputBottle);
    }

    private Component createTooltip(String label, String value, float rating) {
        return Component.literal(label + ": " + value).withStyle(getColorForRating(rating))
                .withStyle(Style.EMPTY.withItalic(false));
    }

    private ChatFormatting getColorForRating(float rating) {
        int score = Math.round(rating * 10);
        return switch (score) {
            case 0, 1, 2, 3, 4 -> ChatFormatting.RED;
            case 5, 6, 7 -> ChatFormatting.GRAY;
            case 8, 9 -> ChatFormatting.GOLD;
            case 10 -> ChatFormatting.AQUA;
            default -> ChatFormatting.WHITE;
        };
    }

    private String formatAge(int barrelAge) {
        float years = barrelAge / 24000.0f;
        int months = Math.round(years * 12);
        return String.format(months + " Months");
    }

    private String formatPercentage(float value) {
        return (int) (value * 100) + "%";
    }

    private String formatBarrel(String barrelType) {
        if (barrelType.contains("oak")) return "Oak";
        if (barrelType.contains("spruce")) return "Spruce";
        if (barrelType.contains("acacia")) return "Acacia";
        if (barrelType.contains("birch")) return "Birch";
        if (barrelType.contains("cherry")) return "Cherry";
        return "None";
    }

    private String formatSweetness(float value) {
        if (value <= 0.02) return "Extra Dry";
        if (value <= 0.04 && value > 0.02) return "Dry";
        if (value <= 0.06 && value > 0.04) return "Semi Dry";
        if (value <= 0.08 && value > 0.06) return "Sweet";
        return "Extra Sweet";
    }
}
