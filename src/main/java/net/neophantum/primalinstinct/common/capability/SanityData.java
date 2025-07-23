package net.neophantum.primalinstinct.common.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class SanityData implements INBTSerializable<CompoundTag> {

    private double sanity;
    private int maxSanity;
    private int insightBonus;
    private int experienceTier;

    public double getSanity() {
        return sanity;
    }

    public void setSanity(double sanity) {
        this.sanity = Math.max(0, Math.min(sanity, maxSanity));
    }

    public int getMaxSanity() {
        return maxSanity;
    }

    public void setMaxSanity(int maxSanity) {
        this.maxSanity = maxSanity;
        if (sanity > maxSanity) {
            sanity = maxSanity;
        }
    }

    public int getInsightBonus() {
        return insightBonus;
    }

    public void setInsightBonus(int insightBonus) {
        this.insightBonus = insightBonus;
    }

    public int getExperienceTier() {
        return experienceTier;
    }

    public void setExperienceTier(int experienceTier) {
        this.experienceTier = experienceTier;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("Sanity", sanity);
        tag.putInt("MaxSanity", maxSanity);
        tag.putInt("InsightBonus", insightBonus);
        tag.putInt("ExperienceTier", experienceTier);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.sanity = tag.getDouble("Sanity");
        this.maxSanity = tag.getInt("MaxSanity");
        this.insightBonus = tag.getInt("InsightBonus");
        this.experienceTier = tag.getInt("ExperienceTier");
    }
}
