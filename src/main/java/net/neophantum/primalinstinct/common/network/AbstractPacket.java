package net.neophantum.primalinstinct.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Base class for custom packet types.
 * Override onClientReceived/onServerReceived depending on direction.
 */
public abstract class AbstractPacket implements CustomPacketPayload {

    public AbstractPacket() {
    }

    /**
     * Encode data to buffer. Override in subclasses if needed.
     */
    public void toBytes(RegistryFriendlyByteBuf buf) {
    }

    /**
     * Called when this packet is received on the client side.
     */
    public void onClientReceived(Minecraft minecraft, Player player) {
    }

    /**
     * Called when this packet is received on the server side.
     */
    public void onServerReceived(MinecraftServer server, ServerPlayer player) {
    }
}