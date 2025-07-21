package net.neophantum.primalinstinct.client;

import net.minecraft.client.Minecraft;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neophantum.primalinstinct.PrimalInstinct;

public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PrimalInstinct.LOGGER.info("HELLO FROM CLIENT SETUP");
        PrimalInstinct.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        // You can do keybinding init, GUI overlay registration, etc here later.
    }
}