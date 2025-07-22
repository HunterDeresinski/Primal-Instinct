package net.neophantum.primalinstinct.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neophantum.primalinstinct.PrimalInstinct;
import net.neophantum.primalinstinct.common.capability.SanityData;
import net.neophantum.primalinstinct.common.capability.SanityProvider;
import net.neophantum.primalinstinct.setup.registry.CapabilityRegistry;
import net.neophantum.primalinstinct.api.util.SanityUtil;

@EventBusSubscriber(modid = PrimalInstinct.MODID)
public class SanityEvents {

    private static final int LIGHT_THRESHOLD = 7;
    private static final int DRAIN_INTERVAL = 100; // ticks (20 ticks = 1 sec)
    private static final double SANITY_LOSS_LIGHT = 1.0;
    private static final double SANITY_LOSS_DAMAGE = 5.0;
    private static int tickCounter = 0;

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(
                CapabilityRegistry.SANITY,
                EntityType.PLAYER,
                (player, ctx) -> new SanityProvider()
        );
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide()) return;

        var sanity = CapabilityRegistry.getSanity(player);
        if (sanity != null) {
            int lightLevel = level.getBrightness(LightLayer.BLOCK, player.blockPosition());

            if (lightLevel < LIGHT_THRESHOLD) {
                tickCounter++;
                if (tickCounter >= DRAIN_INTERVAL) {
                    sanity.removeSanity(SANITY_LOSS_LIGHT);
                    tickCounter = 0;

                    if (player instanceof ServerPlayer serverPlayer) {
                        SanityUtil.debugSyncSanity(serverPlayer); // only sync after change
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

        var sanity = CapabilityRegistry.getSanity(player);
        if (sanity != null) {
            sanity.removeSanity(SANITY_LOSS_DAMAGE);
            System.out.println("[SanityEvents] After removal, sanity = " + sanity.getCurrentSanity());
            System.out.println("[SanityEvents] Instance identity hash: " + System.identityHashCode(sanity));
            if (player instanceof ServerPlayer serverPlayer) {
                SanityUtil.debugSyncSanity(serverPlayer); // only sync after damage
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SanityUtil.debugSyncSanity(player);
        }
    }
}
