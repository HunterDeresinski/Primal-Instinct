package net.neophantum.primalinstinct.api.sanity;

import net.minecraft.world.item.ItemStack;

public interface ISanityDiscountEquipment {
    default int getSanityModifier(ItemStack stack) {
        return 0;
    }
}
