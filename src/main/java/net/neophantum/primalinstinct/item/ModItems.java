package net.neophantum.primalinstinct.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neophantum.primalinstinct.PrimalInstinct;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PrimalInstinct.MODID);

    public static final DeferredItem<Item> KHAORITE = ITEMS.register("khaorite",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAW_KHAORITE = ITEMS.register("raw_khaorite",
            () -> new Item(new Item.Properties()));



    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

}
