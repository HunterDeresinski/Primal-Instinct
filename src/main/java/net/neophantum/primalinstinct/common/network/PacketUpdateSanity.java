package net.neophantum.primalinstinct.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neophantum.primalinstinct.PrimalInstinct;
import net.neophantum.primalinstinct.api.sanity.ISanityCap;
import net.neophantum.primalinstinct.common.capability.SanityCap;
import net.neophantum.primalinstinct.common.capability.SanityData;
import net.neophantum.primalinstinct.client.ClientInfo;
import net.neophantum.primalinstinct.setup.registry.AttachmentRegistry;
import net.neophantum.primalinstinct.setup.registry.CapabilityRegistry;

public class PacketUpdateSanity extends AbstractPacket {

    private final CompoundTag tag;

    public PacketUpdateSanity(CompoundTag tag) {
        this.tag = tag;
    }

    public PacketUpdateSanity(RegistryFriendlyByteBuf buf) {
        this.tag = buf.readNbt();
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeNbt(this.tag);
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        PrimalInstinct.LOGGER.info("[PACKET] Received sanity update on client");
        SanityData data = new SanityData();
        data.deserializeNBT(player.registryAccess(), this.tag);
        player.setData(AttachmentRegistry.SANITY_ATTACHMENT, data);

        var cap = CapabilityRegistry.getSanity(player);
        if (cap instanceof SanityCap sanityCap) {
            sanityCap.setSanityData(data);
        }
    }

    public static final Type<PacketUpdateSanity> TYPE = new Type<>(PrimalInstinct.id("update_sanity"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketUpdateSanity> CODEC =
            StreamCodec.ofMember(PacketUpdateSanity::toBytes, PacketUpdateSanity::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
