package net.neophantum.primalinstinct.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neophantum.primalinstinct.PrimalInstinct;
import net.neophantum.primalinstinct.api.sanity.ISanityCap;
import net.neophantum.primalinstinct.api.util.SanityUtil;
import net.neophantum.primalinstinct.setup.registry.CapabilityRegistry;

@EventBusSubscriber(modid = PrimalInstinct.MODID)
public class SanityEvents {

    private static final int LIGHT_THRESHOLD = 7;
    private static final int DRAIN_INTERVAL = 100; // ticks (5 seconds)
    private static final double SANITY_LOSS_LIGHT = 1.0;
    private static final double SANITY_LOSS_DAMAGE = 5.0;

    // We track per-player tick counters if needed for multiplayer or multiple players
    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide()) return;

        ISanityCap sanity = CapabilityRegistry.getSanity(player);
        if (sanity != null) {
            int lightLevel = level.getBrightness(LightLayer.BLOCK, player.blockPosition());

            if (lightLevel < LIGHT_THRESHOLD) {
                tickCounter++;
                if (tickCounter >= DRAIN_INTERVAL) {
                    sanity.removeSanity(SANITY_LOSS_LIGHT);
                    tickCounter = 0;

                    if (player instanceof ServerPlayer serverPlayer) {
                        SanityUtil.syncSanity(serverPlayer); // Sync after light-based change
                    }
                }
            } else {
                tickCounter = 0;
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDamaged(LivingDamageEvent.Post event) {
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide()) return;

        ISanityCap sanity = CapabilityRegistry.getSanity(player);
        if (sanity != null) {
            sanity.removeSanity(SANITY_LOSS_DAMAGE);
            if (player instanceof ServerPlayer serverPlayer) {
                SanityUtil.syncSanity(serverPlayer); // Sync after damage
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SanityUtil.syncSanity(player); // Sync sanity on login
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SanityUtil.syncSanity(player); // Sync sanity on respawn
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof ServerPlayer target && event.getEntity() instanceof ServerPlayer viewer) {
            SanityUtil.syncSanity(target); // Sync target’s sanity to tracker
        }
    }

    @SubscribeEvent
    public static void onDimChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SanityUtil.syncSanity(player); // Sync sanity when switching dimensions
        }
    }
}
