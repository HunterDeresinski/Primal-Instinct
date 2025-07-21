package net.neophantum.primalinstinct.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neophantum.primalinstinct.PrimalInstinct;
import net.neophantum.primalinstinct.block.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PrimalInstinct.MODID);

    public static final Supplier<CreativeModeTab> PRIMAL_INSTINCT_TAB =
            CREATIVE_MODE_TAB.register("primal_instinct_tab",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("creativetab.primalinstinct.primal_instinct_items"))
                            .icon(() -> new ItemStack(ModItems.KHAORITE.get()))
                            .displayItems((params, output) -> {
                                output.accept(ModItems.KHAORITE);
                                output.accept(ModItems.RAW_KHAORITE);
                                output.accept(ModBlocks.KHAORITE_BLOCK);
                                output.accept(ModBlocks.KHAORITE_ORE);
                            })
                            .build()
            );

//    public static final Supplier<CreativeModeTab> TEST_TAB =
//            CREATIVE_MODE_TAB.register("test_tab",
//                    () -> CreativeModeTab.builder()
//                            .title(Component.translatable("creativetab.primalinstinct.test_items"))
//                            .icon(() -> new ItemStack(ModItems.KHAORITE.get()))
//                            .displayItems((params, output) -> {
//                                output.accept(ModItems.KHAORITE);
//                                output.accept(ModItems.RAW_KHAORITE);
//                                output.accept(ModBlocks.KHAORITE_BLOCK);
//                            })
//                            .build()
//            );

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }

}
