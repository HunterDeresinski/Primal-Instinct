package net.neophantum.primalinstinct.setup.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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
import net.neophantum.primalinstinct.common.capability.SanityData;

@EventBusSubscriber(modid = PrimalInstinct.MODID)
public class CapabilityRegistry {

    public static final EntityCapability<SanityCap, Void> SANITY =
            EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath(PrimalInstinct.MODID, "sanity"), SanityCap.class);

    public static SanityCap getSanityCap(LivingEntity entity) {
        if (entity == null) return null;
        return entity.getCapability(SANITY);
    }

    /**
     * Get the {@link ISanityCap} from the specified entity.
     *
     * @param entity The entity
     * @return The ISanityCap instance, or null if not present
     */
    public static ISanityCap getSanity(final LivingEntity entity) {
        if (entity == null) return null;
        return entity.getCapability(SANITY);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(SANITY, EntityType.PLAYER, (player, ctx) -> new SanityCap(player));
    }

    @EventBusSubscriber(modid = PrimalInstinct.MODID)
    public static class Events {

        @SubscribeEvent
        public static void onPlayerLoggedIn(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event) {
            syncSanityCap(event.getEntity());
        }

        @SubscribeEvent
        public static void onPlayerRespawn(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent event) {
            syncSanityCap(event.getEntity());
        }

        @SubscribeEvent
        public static void onStartTracking(net.neoforged.neoforge.event.entity.player.PlayerEvent.StartTracking event) {
            if (event.getTarget() instanceof Player && event.getEntity() instanceof ServerPlayer) {
                syncSanityCap(event.getTarget());
            }
        }

        @SubscribeEvent
        public static void onPlayerChangedDimension(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent event) {
            syncSanityCap(event.getEntity());
        }

        public static void syncSanityCap(Entity entity) {
            if (entity instanceof ServerPlayer serverPlayer) {
                SanityCap cap = getSanityCap(serverPlayer);
                if (cap != null) {
                    cap.syncToClient(serverPlayer);
                }
            }
        }
    }
}
