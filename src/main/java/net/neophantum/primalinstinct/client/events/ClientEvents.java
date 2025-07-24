package net.neophantum.primalinstinct.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neophantum.primalinstinct.PrimalInstinct;
import net.neophantum.primalinstinct.api.sanity.ISanityCap;
import net.neophantum.primalinstinct.client.ClientInfo;
import net.neophantum.primalinstinct.client.gui.GuiSanityHUD;
import net.neophantum.primalinstinct.setup.registry.CapabilityRegistry;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

@EventBusSubscriber(modid = PrimalInstinct.MODID)
public class ClientEvents {

    private static PostChain postShader;
    private static PostPass sanityPass;

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiLayerEvent.Pre event) {
        // Only draw when GUI is not hidden and the correct layer is being rendered

        //PrimalInstinct.LOGGER.info("[HUD] Sanity HUD render tick");

        if (!Minecraft.getInstance().options.hideGui && event.getName().equals(VanillaGuiLayers.HOTBAR)) {
            GuiSanityHUD.renderOverlay(event.getGuiGraphics(), event.getPartialTick());
        }
    }

    @Nullable
    private static PostPass getFirstPass(PostChain chain) {
        try {
            var field = PostChain.class.getDeclaredField("passes");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<PostPass> passes = (List<PostPass>) field.get(chain);
            return passes.isEmpty() ? null : passes.get(0);
        } catch (Exception e) {
            PrimalInstinct.LOGGER.error("Failed to reflectively access PostPass list", e);
            return null;
        }
    }

    // Working on making world turn grayscale as sanity drops
    @SubscribeEvent
    public static void onRenderTick(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;

        Minecraft mc = Minecraft.getInstance();

        // Initialize shader only once
        if (postShader == null) {
            try {
                ResourceLocation shaderLoc = ResourceLocation.fromNamespaceAndPath(PrimalInstinct.MODID, "shaders/post/desaturate.json");
                postShader = new PostChain(
                        mc.getTextureManager(),
                        mc.getResourceManager(),
                        mc.getMainRenderTarget(),
                        shaderLoc
                );
                postShader.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());

                // Use reflection to get the first shader pass from the JSON-defined shader
                sanityPass = getFirstPass(postShader);

                PrimalInstinct.LOGGER.info("Sanity shader loaded successfully with {} passes", postShader.getName());

            } catch (IOException e) {
                PrimalInstinct.LOGGER.error("Failed to load sanity shader", e);
            }
        }

        if (postShader != null) {
            ISanityCap sanity = CapabilityRegistry.getSanity(mc.player);
            float ratio = sanity != null
                    ? (float)(sanity.getCurrentSanity() / Math.max(1.0, sanity.getMaxSanity()))
                    : 1.0f;

            // Set the SanityRatio uniform on the stored pass
            if (sanityPass != null) {
                sanityPass.getEffect().getUniform("SanityRatio").set(ratio);
            }

            postShader.process(event.getPartialTick().getGameTimeDeltaPartialTick(true));
        }
    }

}

