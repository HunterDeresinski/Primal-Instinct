package net.neophantum.primalinstinct.api.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.CapabilityRegistry;
import net.neoforged.neoforge.common.NeoForge;
import net.neophantum.primalinstinct.api.sanity.ISanityCap;

public class SanityUtil {
    public static double getCurrentSanity(LivingEntity e) {
        ISanityCap sanity = CapabilityRegistry.getSanity(e);
        return sanity != null ? sanity.getCurrentSanity() : 0;
    }

    public record Sanity(int Max, float Reserve) {
        public int getRealMax() {
            return (int) (Max * (1.0 - Reserve));
        }
    }

    public static Sanity calcMaxSanity(Player e) {
        ISanityCap sanity = CapabilityRegistry.getSanity(e);
        if (sanity == null) return new Sanity(0, 0f);

        double rawMax = ServerConfig.INIT_MAX_SANITY.get();
        rawMax += sanity.getInsightBonus() * ServerConfig.INSIGHT_SANITY_BONUS.get();
        rawMax += sanity.getExperienceTier() * ServerConfig.TIER_SANITY_BONUS.get();

        // Optional: use attributes or events
        MaxSanityCalcEvent event = new MaxSanityCalcEvent(e, (int) rawMax);
        NeoForge.EVENT_BUS.post(event);
        int max = event.getMax();
        float reserve = event.getReserve();

        return new Sanity(max, reserve);
    }

    public static int getMaxSanity(Player e) {
        return calcMaxSanity(e).getRealMax();
    }

    public static double getSanityRegen(Player e) {
        ISanityCap sanity = CapabilityRegistry.getSanity(e);
        if (sanity == null) return 0;

        double regen = ServerConfig.INIT_SANITY_REGEN.get();
        regen += sanity.getInsightBonus() * ServerConfig.INSIGHT_REGEN_BONUS.get();
        regen += sanity.getExperienceTier() * ServerConfig.TIER_REGEN_BONUS.get();

        SanityRegenCalcEvent event = new SanityRegenCalcEvent(e, regen);
        NeoForge.EVENT_BUS.post(event);
        return event.getRegen();
    }
}
