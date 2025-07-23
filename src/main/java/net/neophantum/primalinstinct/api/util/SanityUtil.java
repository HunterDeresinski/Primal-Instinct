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
import net.neophantum.primalinstinct.common.capability.SanityCap;
import net.neophantum.primalinstinct.common.network.NetworkManager;
import net.neophantum.primalinstinct.common.network.PacketUpdateSanity;
import net.neophantum.primalinstinct.setup.registry.CapabilityRegistry;
import net.neophantum.primalinstinct.setup.config.ServerConfig;
import net.neophantum.primalinstinct.api.perk.PerkAttributes;

public class SanityUtil {

    // Unique identifiers for attribute modifiers
    private static final ResourceLocation MAX_SANITY_MODIFIER = PrimalInstinct.id("max_sanity_mod");
    private static final ResourceLocation SANITY_REGEN_MODIFIER = PrimalInstinct.id("sanity_regen_mod");

    public static double getCurrentSanity(LivingEntity entity) {
        ISanityCap cap = CapabilityRegistry.getSanity(entity);
        return cap != null ? cap.getCurrentSanity() : 0;
    }

    public record Sanity(int Max, float Reserve) {
        public int getRealMax() {
            return (int) (Max * (1.0f - Reserve));
        }
    }

    public static Sanity calcMaxSanity(Player player) {
        ISanityCap cap = CapabilityRegistry.getSanity(player);
        if (cap == null) return new Sanity(0, 0f);

        double base = 100;
        base += cap.getInsightBonus() * 5;  // example scaling factor
        base += cap.getExperienceTier() * 10; // example scaling factor

        var attr = player.getAttribute(PerkAttributes.MAX_SANITY);
        if (attr != null) {
            var mod = attr.getModifier(MAX_SANITY_MODIFIER);
            if (mod == null || mod.amount() != base) {
                if (mod != null) attr.removeModifier(mod);
                attr.addTransientModifier(new AttributeModifier(MAX_SANITY_MODIFIER, base, AttributeModifier.Operation.ADD_VALUE));
            }
            base = attr.getValue();
        }

        MaxSanityCalcEvent event = new MaxSanityCalcEvent(player, (int) base);
        NeoForge.EVENT_BUS.post(event);
        return new Sanity(event.getMax(), event.getReserve());
    }

    public static int getMaxSanity(Player player) {
        return calcMaxSanity(player).getRealMax();
    }

    public static void syncSanity(ServerPlayer player) {
        var cap = CapabilityRegistry.getSanity(player);
        if (cap instanceof SanityCap sanityCap) {
            sanityCap.syncToClient(player);
        }
    }

    public static double getSanityRegen(Player player) {
        ISanityCap cap = CapabilityRegistry.getSanity(player);
        if (cap == null) return 0;

        double regen = 1.0; // base regen
        regen += cap.getInsightBonus() * 0.25;  // example scaling
        regen += cap.getExperienceTier() * 0.5; // example scaling

        var attr = player.getAttribute(PerkAttributes.SANITY_REGEN);
        if (attr != null) {
            var mod = attr.getModifier(SANITY_REGEN_MODIFIER);
            if (mod == null || mod.amount() != regen) {
                if (mod != null) attr.removeModifier(mod);
                attr.addTransientModifier(new AttributeModifier(SANITY_REGEN_MODIFIER, regen, AttributeModifier.Operation.ADD_VALUE));
            }
            regen = attr.getValue();
        }


        SanityRegenCalcEvent event = new SanityRegenCalcEvent(player, regen);
        NeoForge.EVENT_BUS.post(event);
        return event.getRegen();
    }
}
