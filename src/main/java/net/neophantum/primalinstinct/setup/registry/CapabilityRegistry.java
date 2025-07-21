package net.neophantum.primalinstinct.setup.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neophantum.primalinstinct.api.sanity.ISanityCap;

public class CapabilityRegistry {
    public static final EntityCapability<ISanityCap, Void> SANITY =
            EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath("primalinstinct", "sanity"), ISanityCap.class);

    public static ISanityCap getSanity(Entity entity) {
        return entity.getCapability(SANITY, null);
    }
}