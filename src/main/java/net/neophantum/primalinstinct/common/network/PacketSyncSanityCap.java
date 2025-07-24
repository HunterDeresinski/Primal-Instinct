package net.neophantum.primalinstinct.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neophantum.primalinstinct.PrimalInstinct;
import net.neophantum.primalinstinct.api.sanity.ISanityCap;
import net.neophantum.primalinstinct.client.ClientInfo;
import net.neophantum.primalinstinct.common.capability.SanityData;
import net.neophantum.primalinstinct.setup.registry.CapabilityRegistry;

public class PacketSyncSanityCap extends AbstractPacket {

    private final CompoundTag tag;

    // Decoder constructor
    public PacketSyncSanityCap(RegistryFriendlyByteBuf buf) {
        this.tag = buf.readNbt();
    }

    // Encoder
    public void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeNbt(tag);
    }

    public PacketSyncSanityCap(CompoundTag tag) {
        this.tag = tag;
    }

    // Client-side logic
    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        ISanityCap sanity = CapabilityRegistry.getSanity(player);
        if (sanity instanceof SanityData data) {
            data.deserializeNBT(player.registryAccess(), tag);
            System.out.println("[PacketSyncSanityCap] Synced sanity from server: " + tag);
        }
    }

    public static final CustomPacketPayload.Type<PacketSyncSanityCap> TYPE =
            new CustomPacketPayload.Type<>(PrimalInstinct.id("sync_sanity"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketSyncSanityCap> CODEC =
            StreamCodec.ofMember(PacketSyncSanityCap::toBytes, PacketSyncSanityCap::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
