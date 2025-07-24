package net.neophantum.primalinstinct.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neophantum.primalinstinct.PrimalInstinct;
import net.neophantum.primalinstinct.api.sanity.ISanityCap;
import net.neophantum.primalinstinct.common.network.NetworkManager;
import net.neophantum.primalinstinct.common.network.PacketUpdateSanity;
import net.neophantum.primalinstinct.setup.registry.AttachmentRegistry;

public class SanityCap implements ISanityCap {

    private SanityData sanityData;
    private final LivingEntity entity;

    public SanityCap(LivingEntity entity) {
        this.entity = entity;
        this.sanityData = entity.getData(AttachmentRegistry.SANITY_ATTACHMENT);
        //PrimalInstinct.LOGGER.info("[SanityCap] Constructed for: " + entity.getName().getString() +
        //        ", has sanityData? " + (this.sanityData != null));

        if (this.sanityData == null && entity.level().isClientSide()) {
            this.sanityData = new SanityData(); // Prevent NPE on client before sync
            //PrimalInstinct.LOGGER.warn("[SanityCap] sanityData was null on CLIENT — instantiating fallback!");
        }

        // Most likely changing this later. I cannot seem to find where Ars initializes this. REMEMBER TO LOOK INTO THAT!
        if (this.sanityData != null && this.sanityData.getMaxSanity() <= 0) {
            this.sanityData.setMaxSanity(100);
            this.sanityData.setSanity(100);
        }
    }

    private SanityData getDataSafe() {
        return sanityData != null ? sanityData : new SanityData();
    }

    @Override
    public double getCurrentSanity() {
        return sanityData.getSanity();
    }

    @Override
    public int getMaxSanity() {
        return sanityData.getMaxSanity();
    }

    @Override
    public void setMaxSanity(int max) {
        sanityData.setMaxSanity(max);
        entity.setData(AttachmentRegistry.SANITY_ATTACHMENT, sanityData);
    }

    @Override
    public double setSanity(double sanity) {
        double clamped = Math.max(0, Math.min(sanity, getMaxSanity()));
        sanityData.setSanity(clamped);
        entity.setData(AttachmentRegistry.SANITY_ATTACHMENT, sanityData);
        return clamped;
    }

    @Override
    public double addSanity(double sanityToAdd) {
        return setSanity(getCurrentSanity() + sanityToAdd);
    }

    @Override
    public double removeSanity(double sanityToRemove) {
        return setSanity(getCurrentSanity() - Math.max(0, sanityToRemove));
    }

    @Override
    public int getInsightBonus() {
        return sanityData.getInsightBonus();
    }

    @Override
    public void setInsightBonus(int bonus) {
        sanityData.setInsightBonus(bonus);
        entity.setData(AttachmentRegistry.SANITY_ATTACHMENT, sanityData);
    }

    @Override
    public int getExperienceTier() {
        return sanityData.getExperienceTier();
    }

    @Override
    public void setExperienceTier(int tier) {
        sanityData.setExperienceTier(tier);
        entity.setData(AttachmentRegistry.SANITY_ATTACHMENT, sanityData);
    }

    public void setSanityData(SanityData data) {
        this.sanityData = data;
    }

    public void syncToClient(ServerPlayer player) {
        PrimalInstinct.LOGGER.info("[SYNC] Sending sanity to client: {}", player.getName().getString());
        CompoundTag tag = sanityData.serializeNBT(player.registryAccess());
        NetworkManager.sendToPlayerClient(new PacketUpdateSanity(tag), player);
    }
}
