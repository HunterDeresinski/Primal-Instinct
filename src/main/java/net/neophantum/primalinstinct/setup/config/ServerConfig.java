package net.neophantum.primalinstinct.setup.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {

    public static ModConfigSpec SERVER_CONFIG;

    public static ModConfigSpec.IntValue INIT_MAX_SANITY;
    public static ModConfigSpec.DoubleValue INIT_SANITY_REGEN;
    public static ModConfigSpec.IntValue INSIGHT_SANITY_BONUS;
    public static ModConfigSpec.DoubleValue INSIGHT_REGEN_BONUS;
    public static ModConfigSpec.IntValue TIER_SANITY_BONUS;
    public static ModConfigSpec.DoubleValue TIER_REGEN_BONUS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Sanity Settings").push("sanity");

        INIT_MAX_SANITY = builder
                .comment("Initial maximum sanity")
                .defineInRange("initMaxSanity", 100, 0, Integer.MAX_VALUE);

        INIT_SANITY_REGEN = builder
                .comment("Initial sanity regeneration per tick")
                .defineInRange("initSanityRegen", 0.01, 0.0, 10.0);

        INSIGHT_SANITY_BONUS = builder
                .comment("Sanity bonus gained from gaining insight")
                .defineInRange("insightSanityBonus", 10, 0, Integer.MAX_VALUE);

        INSIGHT_REGEN_BONUS = builder
                .comment("Sanity regeneration bonus from insight")
                .defineInRange("insightRegenBonus", 0.02, 0.0, 10.0);

        TIER_SANITY_BONUS = builder
                .comment("Sanity bonus per tier level")
                .defineInRange("tierSanityBonus", 5, 0, Integer.MAX_VALUE);

        TIER_REGEN_BONUS = builder
                .comment("Sanity regeneration bonus per tier level")
                .defineInRange("tierRegenBonus", 0.01, 0.0, 10.0);

        builder.pop();

        SERVER_CONFIG = builder.build();
    }
}
