package net.neophantum.primalinstinct.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neophantum.primalinstinct.PrimalInstinct;
import net.neophantum.primalinstinct.api.sanity.ISanityCap;
import net.neophantum.primalinstinct.client.ClientInfo;
import net.neophantum.primalinstinct.setup.registry.CapabilityRegistry;

public class GuiSanityHUD {

    private static final Minecraft mc = Minecraft.getInstance();

    private static final ResourceLocation BORDER = ResourceLocation.fromNamespaceAndPath(
            PrimalInstinct.MODID, "textures/gui/sanitybar_border.png");
    private static final ResourceLocation FILL = ResourceLocation.fromNamespaceAndPath(
            PrimalInstinct.MODID, "textures/gui/sanitybar_fill.png");

    public static void render(GuiGraphics guiGraphics, float partialTicks) {
        if (mc.player == null || mc.options.hideGui) return;

        ISanityCap sanity = ClientInfo.getSanityCap();
        if (sanity == null) {
            //System.out.println("[HUD] No sanity data available on client.");
            return;
        }

        double current = sanity.getCurrentSanity();
        int max = sanity.getMaxSanity();
        System.out.println("[HUD] Sanity: " + current + " / " + max);
        if (max <= 0) return;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int left = (screenWidth / 2) - (95 / 2) - 20;
        int top = screenHeight - 55;

        float ratio = Mth.clamp((float) current / max, 0.0f, 1.0f);
        int filledWidth = (int) (95 * ratio);

        int fillOffsetX = 25;
        int fillOffsetY = 7;

        // Draw filled portion
        guiGraphics.blit(FILL, left + fillOffsetX, top + fillOffsetY, 0, 0, filledWidth, 5, 95, 5);
        // Draw border overlay
        guiGraphics.blit(BORDER, left, top, 0, 0, 123, 14, 123, 14);

        // Draw centered sanity text
        String display = (int) current + " / " + max;
        int textWidth = (int) (mc.font.width(display) * 0.75f);
        float textScale = 0.75f;

        int textX = (int) ((screenWidth / 2 - textWidth / 2) / textScale);
        int textY = (int) ((top - 10) / textScale);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(textScale, textScale, 1.0f);
        guiGraphics.drawString(mc.font, display, textX, textY, 0xFFFFFF);
        guiGraphics.pose().popPose();
    }
}
