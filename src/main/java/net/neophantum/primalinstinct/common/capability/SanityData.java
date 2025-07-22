package net.neophantum.primalinstinct.common.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neophantum.primalinstinct.api.sanity.ISanityCap;

public class SanityData implements ISanityCap, INBTSerializable<CompoundTag> {

    private double currentSanity = 100;
    private int maxSanity = 100;
    private int insightBonus = 0;
    private int experienceTier = 0;

    @Override
    public double getCurrentSanity() {
        return currentSanity;
    }

    @Override
    public int getMaxSanity() {
        return maxSanity;
    }

    @Override
    public void setMaxSanity(int max) {
        this.maxSanity = max;
    }

    @Override
    public double setSanity(double sanity) {
        this.currentSanity = Math.max(0, Math.min(sanity, maxSanity));
        return this.currentSanity;
    }

    @Override
    public double addSanity(double sanityToAdd) {
        return setSanity(this.currentSanity + sanityToAdd);
    }

    @Override
    public double removeSanity(double amount) {
        double before = this.currentSanity;
        this.currentSanity = Math.max(0, this.currentSanity - amount);
        System.out.println("[Sanity] Removed " + amount + " sanity. " +
                "Before: " + before + ", After: " + this.currentSanity);
        return this.currentSanity;
    }

    @Override
    public int getInsightBonus() {
        return insightBonus;
    }

    @Override
    public int getExperienceTier() {
        return experienceTier;
    }

    @Override
    public void setInsightBonus(int bonus) {
        this.insightBonus = bonus;
    }

    @Override
    public void setExperienceTier(int tier) {
        this.experienceTier = tier;
    }

    // === NEW METHODS REQUIRED BY INBTSerializable<CompoundTag> ===
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("Sanity", currentSanity);
        tag.putInt("MaxSanity", maxSanity);
        tag.putInt("InsightBonus", insightBonus);
        tag.putInt("ExperienceTier", experienceTier);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.currentSanity = tag.getDouble("Sanity");
        this.maxSanity = tag.getInt("MaxSanity");
        this.insightBonus = tag.getInt("InsightBonus");
        this.experienceTier = tag.getInt("ExperienceTier");
    }
}
