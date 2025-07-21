package net.neophantum.primalinstinct.client.events;

import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neophantum.primalinstinct.PrimalInstinct;
import net.neophantum.primalinstinct.client.ClientInfo;
import net.neophantum.primalinstinct.client.gui.GuiSanityHUD;

@EventBusSubscriber(modid = PrimalInstinct.MODID)
public class ClientEvents {

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiLayerEvent.Pre event) {
        if (event.getName().equals(VanillaGuiLayers.HOTBAR)) {
            GuiSanityHUD.render(event.getGuiGraphics(), ClientInfo.partialTicks);
            event.getGuiGraphics().drawString(
                    Minecraft.getInstance().font,"Debug render",10, 10,0xFF00FF);  // Magenta for visibility
        }
    }
}
