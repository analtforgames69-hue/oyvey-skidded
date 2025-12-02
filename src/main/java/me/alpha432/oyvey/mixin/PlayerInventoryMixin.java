package me.alpha432.oyvey.mixin;

import me.alpha432.oyvey.mixininterface.IPlayerInventory;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements IPlayerInventory {

    @Shadow private int selectedSlot;

    @Override
    public int getSelectedSlot() {
        return selectedSlot;
    }

    @Override
    public void setSelectedSlot(int slot) {
        this.selectedSlot = slot;
    }
}
