package net.neophantum.primalinstinct.common.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neophantum.primalinstinct.api.sanity.ISanityCap;

public class SanityProvider implements ISanityCap, INBTSerializable<CompoundTag> {

    private final SanityData instance = new SanityData();

    // === Capability forwarding ===
    @Override
    public double getCurrentSanity() {
        return instance.getCurrentSanity();
    }

    @Override
    public double setSanity(double sanity) {
        return instance.setSanity(sanity);
    }

    @Override
    public double addSanity(double sanityToAdd) {
        return instance.addSanity(sanityToAdd);
    }

    @Override
    public double removeSanity(double amount) {
        return instance.removeSanity(amount);
    }

    @Override
    public int getMaxSanity() {
        return instance.getMaxSanity();
    }

    @Override
    public void setMaxSanity(int max) {
        instance.setMaxSanity(max);
    }

    @Override
    public int getInsightBonus() {
        return instance.getInsightBonus();
    }

    @Override
    public void setInsightBonus(int bonus) {
        instance.setInsightBonus(bonus);
    }

    @Override
    public int getExperienceTier() {
        return instance.getExperienceTier();
    }

    @Override
    public void setExperienceTier(int tier) {
        instance.setExperienceTier(tier);
    }

    // === NBT Serialization ===
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return instance.serializeNBT(provider); // delegate to SanityData
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        instance.deserializeNBT(provider, tag); // delegate to SanityData
    }
}
