package net.neophantum.primalinstinct.client;

import com.mojang.blaze3d.pipeline.TextureTarget;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.neophantum.primalinstinct.client.particle.ColorPos;

import java.util.ArrayList;
import java.util.List;

public class ClientInfo {
    private ClientInfo() {}

    public static Component[] storageTooltip = new Component[0];
    public static int ticksInGame = 0;
    public static int redOverlayTicks = 0;
    public static float redOverlayMana = 0;
    public static float reservedOverlayMana = 0.15F;

    public static float partialTicks = 0.0f;
    public static float deltaTicks = 0;
    public static float totalTicks = 0;

    public static List<BlockPos> scryingPositions = new ArrayList<>();
    public static List<ColorPos> highlightPositions = new ArrayList<>();
    public static int highlightTicks;

    public static TextureTarget skyRenderTarget;
    public static ShaderInstance skyShader;
    public static ShaderInstance blameShader;
    public static ShaderInstance rainbowShader;

    public static boolean isSupporter = false;

    public static void setTooltip(Component... tooltip) {
        storageTooltip = tooltip;
    }

    public static void highlightPosition(List<ColorPos> colorPos, int ticks) {
        highlightPositions = colorPos;
        highlightTicks = ticks;
    }

    private static void calcDelta() {
        float oldTotal = ClientInfo.totalTicks;
        totalTicks = ClientInfo.totalTicks + ClientInfo.partialTicks;
        deltaTicks = totalTicks - oldTotal;
    }

    public static void renderTickStart(float pt) {
        partialTicks = pt;
    }

    public static void renderTickEnd() {
        calcDelta();
    }

    public static void endClientTick() {
        ticksInGame++;
        partialTicks = 0f;
        if (redTicks()) {
            redOverlayTicks--;
        }
        calcDelta();
    }

    public static boolean redTicks() {
        return redOverlayTicks > 0;
    }
}
