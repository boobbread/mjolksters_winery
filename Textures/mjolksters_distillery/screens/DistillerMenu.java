package com.mjolkster.mjolksters_winery.screen;

import com.mjolkster.mjolksters_winery.block.entity.DistillerBlockEntity;
import com.mjolkster.mjolksters_winery.registry.ModBlocks;
import com.mjolkster.mjolksters_winery.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DistillerMenu extends AbstractContainerMenu {
    public final DistillerBlockEntity blockEntity;
    private final Level level;
    final ContainerData data;

    public DistillerMenu(int pContainerID, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerID, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(5));
    }

    public DistillerMenu(int pContainerID, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.DISTILLER_MENU.get(), pContainerID);
        this.blockEntity = ((DistillerBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        addDataSlots(data);
    }

    public int getInputVolume() {
        int inputVolume = this.data.get(0);
        int maxInputVolume = this.data.get(1);
        return maxInputVolume != 0 ?
                (int) ((float) inputVolume / maxInputVolume * 31) : 0;
    }

    public int getOutputVolume() {
        int outputVolume = this.data.get(2);
        int maxOutputVolume = this.data.get(3);
        return maxOutputVolume != 0 ?
                (int) ((float) outputVolume / maxOutputVolume * 56) : 0;
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 0;

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, ModBlocks.DISTILLER.get());
    }
}
