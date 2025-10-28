package com.example.mixin.client;

import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerInventory.class)
public interface PlayerInventoryMixin {
    @Accessor("selectedSlot")
    void setSelectedSlot(int selectedSlot);

    @Accessor("selectedSlot")
    int getSelectedSlot();
}
