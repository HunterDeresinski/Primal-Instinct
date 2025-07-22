package net.neophantum.primalinstinct.common.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neophantum.primalinstinct.api.sanity.ISanityCap;
import net.neophantum.primalinstinct.common.capability.SanityData;

/**
 * Tracks client-side sanity capability in a stable, updatable wrapper.
 */
public class SanityTracker implements ISanityCap {

    private final SanityData sanity = new SanityData();

    // === Delegate all ISanityCap methods ===
    @Override
    public double getCurrentSanity() {
        return sanity.getCurrentSanity();
    }

    @Override
    public double setSanity(double sanity) {
        return this.sanity.setSanity(sanity);
    }

    @Override
    public double addSanity(double sanityToAdd) {
        return sanity.addSanity(sanityToAdd);
    }

    @Override
    public double removeSanity(double amount) {
        return sanity.removeSanity(amount);
    }

    @Override
    public int getMaxSanity() {
        return sanity.getMaxSanity();
    }

    @Override
    public void setMaxSanity(int max) {
        sanity.setMaxSanity(max);
    }

    @Override
    public int getInsightBonus() {
        return sanity.getInsightBonus();
    }

    @Override
    public void setInsightBonus(int bonus) {
        sanity.setInsightBonus(bonus);
    }

    @Override
    public int getExperienceTier() {
        return sanity.getExperienceTier();
    }

    @Override
    public void setExperienceTier(int tier) {
        sanity.setExperienceTier(tier);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return sanity.serializeNBT(provider);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        sanity.deserializeNBT(provider, tag);
    }
}
