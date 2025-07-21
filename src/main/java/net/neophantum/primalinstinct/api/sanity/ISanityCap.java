package net.neophantum.primalinstinct.api.sanity;

public interface ISanityCap {
    double getCurrentSanity();
    int getMaxSanity();

    void setMaxSanity(int max);
    double setSanity(final double sanity);
    double addSanity(final double sanityToAdd);
    double removeSanity(final double sanityToRemove);

    default int getInsightBonus() { return 0; }
    default int getExperienceTier() { return 0; }

    default void setInsightBonus(int bonus) {}
    default void setExperienceTier(int tier) {}
}
