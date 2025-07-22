package net.neophantum.primalinstinct.client.gui.utils;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neophantum.primalinstinct.client.gui.Color;
import org.joml.Matrix4f;

public class RenderUtils {

    private static final RenderType TRANSLUCENT = RenderType.entityTranslucent(TextureAtlas.LOCATION_BLOCKS);

    private static final Matrix4f SCALE_INVERT_Y = new Matrix4f().scaling(1F, -1F, 1F);

    public static void renderFakeItemTransparent(PoseStack poseStack, ItemStack stack, int x, int y, int scale, int alpha, boolean transparent, int zIndex) {
        if (stack.isEmpty()) return;

        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        BakedModel model = renderer.getModel(stack, null, Minecraft.getInstance().player, 0);
        renderItemModel(poseStack, stack, x, y, scale, alpha, model, renderer, transparent, zIndex);
    }

    public static void renderItemModel(PoseStack poseStack, ItemStack stack, int x, int y, int scale, int alpha, BakedModel model, ItemRenderer renderer, boolean transparent, int zIndex) {
        poseStack.pushPose();
        poseStack.translate(x + 8F, y + 8F, zIndex);
        poseStack.mulPose(SCALE_INVERT_Y);
        poseStack.scale(scale, scale, scale);

        boolean flatLight = !model.usesBlockLight();
        if (flatLight) Lighting.setupForFlatItems();

        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        renderer.render(
                stack,
                ItemDisplayContext.GUI,
                false,
                poseStack,
                transparent ? transparentBuffer(buffer) : buffer,
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY,
                model
        );
        buffer.endBatch();

        RenderSystem.enableDepthTest();
        if (flatLight) Lighting.setupFor3DItems();
        poseStack.popPose();
    }

    private static MultiBufferSource transparentBuffer(MultiBufferSource buffer) {
        return renderType -> new TintedVertexConsumer(buffer.getBuffer(TRANSLUCENT), 1.0f, 1.0f, 1.0f, 0.25f);
    }

    public static void colorBlit(PoseStack mStack, int x, int y, int uOffset, int vOffset, int width, int height, int textureWidth, int textureHeight, Color color) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        Matrix4f matrix = mStack.last().pose();
        int maxX = x + width;
        int maxY = y + height;
        float minU = (float) uOffset / textureWidth;
        float minV = (float) vOffset / textureHeight;
        float maxU = minU + (float) width / textureWidth;
        float maxV = minV + (float) height / textureHeight;
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int alpha = color.getAlpha();
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.addVertex(matrix, (float) x, (float) maxY, 0).setUv(minU, maxV).setColor(r, g, b, alpha);
        bufferBuilder.addVertex(matrix, (float) maxX, (float) maxY, 0).setUv(maxU, maxV).setColor(r, g, b, alpha);
        bufferBuilder.addVertex(matrix, (float) maxX, (float) y, 0).setUv(maxU, minV).setColor(r, g, b, alpha);
        bufferBuilder.addVertex(matrix, (float) x, (float) y, 0).setUv(minU, minV).setColor(r, g, b, alpha);
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
        RenderSystem.disableBlend();
    }

    public static void drawString(String string, GuiGraphics guiGraphics, int positionX, int positionY) {
        guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable(string), positionX, positionY, Color.WHITE.getRGB());
    }
}
