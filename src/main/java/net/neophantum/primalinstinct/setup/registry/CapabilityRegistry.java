package net.neophantum.primalinstinct.setup.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neophantum.primalinstinct.PrimalInstinct;
import net.neophantum.primalinstinct.api.sanity.ISanityCap;
import net.neophantum.primalinstinct.common.capability.SanityCap;
import net.neophantum.primalinstinct.common.capability.SanityProvider;

@EventBusSubscriber(modid = PrimalInstinct.MODID)
public class CapabilityRegistry {

    public static final EntityCapability<ISanityCap, Void> SANITY =
            EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath("primalinstinct", "sanity"), ISanityCap.class);

    public static ISanityCap getSanity(Entity entity) {
        return entity.getCapability(SANITY, null);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(CapabilityRegistry.SANITY, EntityType.PLAYER, (player, ctx) -> new SanityProvider());
    }

}
