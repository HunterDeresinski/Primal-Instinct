package net.neophantum.primalinstinct.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neophantum.primalinstinct.PrimalInstinct;
import net.neophantum.primalinstinct.api.sanity.ISanityCap;
import net.neophantum.primalinstinct.client.ClientInfo;
import net.neophantum.primalinstinct.common.capability.SanityData;
import net.neophantum.primalinstinct.setup.registry.CapabilityRegistry;

public class GuiSanityHUD {

    public static final LayeredDraw.Layer OVERLAY = GuiSanityHUD::renderOverlay;
    private static final Minecraft mc = Minecraft.getInstance();

    private static final ResourceLocation BORDER = ResourceLocation.fromNamespaceAndPath(
            PrimalInstinct.MODID, "textures/gui/sanitybar_border.png");
    private static final ResourceLocation FILL = ResourceLocation.fromNamespaceAndPath(
            PrimalInstinct.MODID, "textures/gui/sanitybar_fill.png");

    public static boolean shouldDisplayBar() {
        if (mc.player == null || mc.options.hideGui) return false;

        ISanityCap sanityCap = CapabilityRegistry.getSanity(mc.player);
        if (sanityCap == null) return false;

        boolean isLowSanity = sanityCap.getCurrentSanity() < sanityCap.getMaxSanity();
        return sanityCap.getMaxSanity() > 0 && isLowSanity;
    }


    public static void renderOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!shouldDisplayBar()) return;

        PoseStack poseStack = guiGraphics.pose();
        ISanityCap sanity = CapabilityRegistry.getSanity(mc.player);
        if (sanity == null) return;
        double current = sanity.getCurrentSanity();
        int max = sanity.getMaxSanity();


        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int offsetLeft = 10;
        int barWidth = 96;
        int barHeight = 6;
        int filledWidth = (int) (barWidth * (current / (max * (1.0 + ClientInfo.reservedOverlayMana))));
        int yOffset = screenHeight - 28;

        guiGraphics.blit(BORDER, offsetLeft, yOffset - 9, 0, 0, 108, 18, 256, 256);

        int sanityOffset = (int) (((ClientInfo.ticksInGame + deltaTracker.getGameTimeDeltaTicks()) / 3 % (33))) * 6;
        guiGraphics.blit(FILL, offsetLeft + 9, yOffset, 0, sanityOffset, filledWidth, barHeight, 256, 256);

        renderRedOverlay(poseStack, offsetLeft, yOffset, sanityOffset, max);
        renderReserveOverlay(poseStack, offsetLeft, yOffset, sanityOffset, max);

        String display = (int) current + " / " + max;
        int maxWidth = mc.font.width(display);
        int offset = 67 - maxWidth / 2;
        guiGraphics.drawString(mc.font, display, offset, yOffset - 10, 0xFFFFFF);

        guiGraphics.blit(BORDER, offsetLeft, yOffset - 8, 0, 18, 108, 20, 256, 256);
    }

    public static void renderRedOverlay(PoseStack poseStack, int offsetLeft, int yOffset, int sanityOffset, int maxSanity) {
        if (!ClientInfo.redTicks()) return;
        int redLength = (int) (96F * Mth.clamp(0F, ClientInfo.redOverlayMana / maxSanity, 1F));
        RenderSystem.setShaderTexture(0, BORDER);
        guiColorBlit(poseStack, offsetLeft + 9, yOffset, 0, sanityOffset, redLength, 6, 256, 256, 0xAA0000);
    }

    public static void renderReserveOverlay(PoseStack poseStack, int offsetLeft, int yOffset, int sanityOffset, int maxSanity) {
        if (ClientInfo.reservedOverlayMana <= 0) return;
        int reserveLength = (int) (96F * ClientInfo.reservedOverlayMana);
        int offset = 96 - reserveLength;
        RenderSystem.setShaderTexture(0, FILL);
        guiColorBlit(poseStack, offsetLeft + 9 + offset, yOffset, 0, sanityOffset, reserveLength, 6, 256, 256, 0x232323);
    }

    private static void guiColorBlit(PoseStack poseStack, int x, int y, int u, int v, int width, int height, int texW, int texH, int color) {
        float a = ((color >> 24) & 0xFF) / 255f;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        RenderSystem.setShaderColor(r, g, b, a);
        GuiGraphics.blit(poseStack, x, y, u, v, width, height, texW, texH);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
}
