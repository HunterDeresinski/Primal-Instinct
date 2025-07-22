package net.neophantum.primalinstinct.api.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neophantum.primalinstinct.PrimalInstinct;
import net.neophantum.primalinstinct.api.event.MaxSanityCalcEvent;
import net.neophantum.primalinstinct.api.event.SanityRegenCalcEvent;
import net.neophantum.primalinstinct.api.sanity.ISanityCap;
import net.neophantum.primalinstinct.common.capability.SanityData;
import net.neophantum.primalinstinct.common.capability.SanityProvider;
import net.neophantum.primalinstinct.common.network.NetworkManager;
import net.neophantum.primalinstinct.common.network.PacketUpdateSanity;
import net.neophantum.primalinstinct.setup.registry.CapabilityRegistry;
import net.neophantum.primalinstinct.setup.config.ServerConfig;
import net.neophantum.primalinstinct.api.perk.PerkAttributes;

public class SanityUtil {

    // Unique identifiers for attribute modifiers
    private static final ResourceLocation MAX_SANITY_MODIFIER = PrimalInstinct.id("max_sanity_mod");
    private static final ResourceLocation SANITY_REGEN_MODIFIER = PrimalInstinct.id("sanity_regen_mod");

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

        var sanityAttribute = e.getAttribute(PerkAttributes.MAX_SANITY);
        if (sanityAttribute != null) {
            var cache = sanityAttribute.getModifier(MAX_SANITY_MODIFIER);
            if (cache == null || cache.amount() != rawMax) {
                if (cache != null) sanityAttribute.removeModifier(cache);
                sanityAttribute.addTransientModifier(new AttributeModifier(MAX_SANITY_MODIFIER, rawMax, AttributeModifier.Operation.ADD_VALUE));
            }
            rawMax = sanityAttribute.getValue();
        }

        int max = (int) rawMax;

        MaxSanityCalcEvent event = new MaxSanityCalcEvent(e, max);
        NeoForge.EVENT_BUS.post(event);
        return new Sanity(event.getMax(), event.getReserve());
    }

    public static int getMaxSanity(Player e) {
        return calcMaxSanity(e).getRealMax();
    }

    public static void debugSyncSanity(ServerPlayer player) {
        ISanityCap cap = CapabilityRegistry.getSanity(player);

        if (cap == null) {
            System.err.println("[SanityUtil] Cannot sync: capability is null");
            return;
        }


        System.out.println("[SanityUtil] Preparing to sync sanity capability...");
        System.out.println("[SanityUtil]   Instance identity hash: " + System.identityHashCode(cap));
        System.out.println("[SanityUtil]   Current sanity: " + cap.getCurrentSanity());


        if (!(cap instanceof SanityProvider provider)) {
            System.err.println("[SanityUtil] Capability is not a SanityProvider. Cannot serialize.");
            return;
        }

        CompoundTag tag = provider.serializeNBT(player.registryAccess());
        System.out.println("[SanityUtil] Tag being sent: " + tag);

        NetworkManager.sendToPlayerClient(new PacketUpdateSanity(tag), player);
    }

    public static double getSanityRegen(Player e) {
        ISanityCap sanity = CapabilityRegistry.getSanity(e);
        if (sanity == null) return 0;

        double regen = ServerConfig.INIT_SANITY_REGEN.get();
        regen += sanity.getInsightBonus() * ServerConfig.INSIGHT_REGEN_BONUS.get();
        regen += sanity.getExperienceTier() * ServerConfig.TIER_REGEN_BONUS.get();

        var regenAttr = e.getAttribute(PerkAttributes.SANITY_REGEN); //Possibly need to make sanity regen bonus
        if (regenAttr != null) {
            var cache = regenAttr.getModifier(SANITY_REGEN_MODIFIER);
            if (cache == null || cache.amount() != regen) {
                if (cache != null) regenAttr.removeModifier(cache);
                regenAttr.addTransientModifier(new AttributeModifier(SANITY_REGEN_MODIFIER, regen, AttributeModifier.Operation.ADD_VALUE));
            }
            regen = regenAttr.getValue();
        }

        SanityRegenCalcEvent event = new SanityRegenCalcEvent(e, regen);
        NeoForge.EVENT_BUS.post(event);
        return event.getRegen();
    }
}
