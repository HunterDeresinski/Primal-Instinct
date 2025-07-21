package net.neophantum.primalinstinct.client.events;

import net.minecraft.client.Minecraft;
import net.neophantum.primalinstinct.PrimalInstinct;
import net.neophantum.primalinstinct.client.gui.GuiSanityHUD;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiOverlayEvent;

@EventBusSubscriber(modid = PrimalInstinct.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        // Only render on the right layer
        if (event.getOverlay().id().toString().equals("minecraft:crosshair")) {
            GuiSanityHUD.render(event.getGuiGraphics(), event.getPartialTick());
        }
    }
}