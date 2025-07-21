package net.neophantum.primalinstinct.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neophantum.primalinstinct.PrimalInstinct;
import net.neophantum.primalinstinct.api.sanity.ISanityCap;
import net.neophantum.primalinstinct.client.ClientInfo;
import net.neophantum.primalinstinct.setup.registry.CapabilityRegistry;

public class GuiSanityHUD {

    private static final Minecraft minecraft = Minecraft.getInstance();

    private static final ResourceLocation BORDER = ResourceLocation.fromNamespaceAndPath(PrimalInstinct.MODID,"textures/gui/sanitybar_border.png");
    private static final ResourceLocation FILL = new ResourceLocation.fromNamespaceAndPath(PrimalInstinct.MODID, "textures/gui/sanitybar_fill.png");

    public static void render(GuiGraphics guiGraphics, float partialTicks) {
        if (minecraft.player == null || minecraft.options.hideGui) return;

        ISanityCap sanity = CapabilityRegistry.getSanity(minecraft.player);
        if (sanity == null || sanity.getMaxSanity() <= 0) return;

        int current = sanity.getCurrentSanity();
        int max = sanity.getMaxSanity();

        int width = 100;
        int height = 10;
        int left = 10;
        int top = minecraft.getWindow().getGuiScaledHeight() - 30;

        float ratio = Mth.clamp((float) current / max, 0.0f, 1.0f);
        int filled = (int) (width * ratio);

        PoseStack pose = guiGraphics.pose();
        pose.pushPose();

        // Border
        guiGraphics.blit(BORDER, left, top, 0, 0, width, height, 128, 32);

        // Fill
        RenderSystem.setShaderTexture(0, FILL);
        guiGraphics.blit(FILL, left, top, 0, 0, filled, height, 128, 32);

        pose.popPose();

        // Debug numbers
        if (minecraft.options.renderDebug) {
            String text = current + " / " + max;
            guiGraphics.drawString(minecraft.font, text, left + width + 5, top + 1, 0xFFFFFF);
        }
    }
}
