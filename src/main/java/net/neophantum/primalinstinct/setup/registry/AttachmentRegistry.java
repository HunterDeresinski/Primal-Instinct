package net.neophantum.primalinstinct.setup.registry;

import net.neophantum.primalinstinct.PrimalInstinct;
import net.neophantum.primalinstinct.common.capability.ANPlayerData;
import net.neophantum.primalinstinct.common.capability.SanityData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AttachmentRegistry {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(
                    NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
                    PrimalInstinct.MODID
            );

    public static final Supplier<AttachmentType<SanityData>> SANITY_ATTACHMENT =
            ATTACHMENT_TYPES.register("sanity_cap",
                    () -> AttachmentType.serializable(SanityData::new)
                            .copyOnDeath()
                            .build()
            );

    public static final Supplier<AttachmentType<ANPlayerData>> PLAYER_DATA =
            ATTACHMENT_TYPES.register("player_data",
                    () -> AttachmentType.serializable(ANPlayerData::new)
                            .copyOnDeath()
                            .build()
            );
}
