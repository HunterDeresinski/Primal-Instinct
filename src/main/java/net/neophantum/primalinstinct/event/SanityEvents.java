//package net.neophantum.primalinstinct.event;
//
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.LightLayer;
//import net.minecraft.world.entity.Mob;
//import net.minecraft.world.phys.AABB;
//
//import net.neophantum.primalinstinct.PrimalInstinct;
//import net.neophantum.primalinstinct.capability.SanityCapability;
//import net.neophantum.primalinstinct.capability.SanityData;
//
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.neoforge.event.tick.PlayerTickEvent;
//import net.neoforged.neoforge.capabilities.Capabilities;
//import net.neoforged.neoforge.event.RegisterCapabilitiesEvent;
//import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
//
//@net.neoforged.bus.api.EventBusSubscriber(modid = PrimalInstinct.MODID, bus = net.neoforged.bus.api.EventBusSubscriber.Bus.MOD)
//public class SanityEvents {
//
//    // ✅ Register the capability for all players
//    @SubscribeEvent
//    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
//        event.registerEntity(
//                SanityCapability.INSTANCE, // your static EntityCapability object
//                Player.class,
//                (player, ctx) -> new SanityData() // create your capability data instance here
//        );
//    }
//
//    // ✅ Server tick hook
//    @SubscribeEvent
//    public static void onPlayerTick(PlayerTickEvent event) {
//        if (event.phase() != PlayerTickEvent.Phase.END) return;
//
//        Player player = event.player();
//        Level level = player.level();
//
//        if (level.isClientSide()) return;
//
//        SanityCapability.INSTANCE.get(player, null).ifPresent(sanity -> {
//            boolean nearHostiles = !level.getEntitiesOfClass(Mob.class,
//                    new AABB(player.blockPosition()).inflate(8),
//                    mob -> mob.isAlive() && mob.isAggressive()).isEmpty();
//
//            int lightLevel = level.getBrightness(LightLayer.BLOCK, player.blockPosition());
//
//            if (nearHostiles || lightLe
