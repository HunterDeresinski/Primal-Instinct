package net.neophantum.primalinstinct.common.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class ANPlayerData implements INBTSerializable<CompoundTag> {

    private double sanity = 100.0;
    private int maxSanity = 100;
    private int insightBonus = 0;
    private int experienceTier = 0;

    // === Getters & Setters ===

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
        if (sanity > maxSanity) sanity = maxSanity;
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

    // === Serialization ===

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("Sanity", sanity);
        tag.putInt("MaxSanity", maxSanity);
        tag.putInt("InsightBonus", insightBonus);
        tag.putInt("ExperienceTier", experienceTier);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        sanity = tag.getDouble("Sanity");
        maxSanity = tag.getInt("MaxSanity");
        insightBonus = tag.getInt("InsightBonus");
        experienceTier = tag.getInt("ExperienceTier");
    }
}
