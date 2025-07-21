package net.neophantum.primalinstinct;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neophantum.primalinstinct.api.perk.PerkAttributes;
import net.neophantum.primalinstinct.block.ModBlocks;
import net.neophantum.primalinstinct.client.ClientModEvents;
import net.neophantum.primalinstinct.item.ModCreativeModeTabs;
import net.neophantum.primalinstinct.item.ModItems;
import org.slf4j.Logger;

@Mod(PrimalInstinct.MODID)
public class PrimalInstinct {
    public static final String MODID = "primalinstinct";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PrimalInstinct(IEventBus modEventBus, ModContainer modContainer) {
        // Register configuration
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // Register items, blocks, creative tabs
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        PerkAttributes.ATTRIBUTES.register(modEventBus);

        // Register setup listeners
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        // Register server-side global event bus
        NeoForge.EVENT_BUS.register(this);
        modEventBus.register(ClientModEvents.class);

        // Client-only setup is now handled in ClientModEvents.java
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Common setup code here
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.KHAORITE);
            event.accept(ModItems.RAW_KHAORITE);
        }
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.KHAORITE_BLOCK);
        }
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModBlocks.KHAORITE_ORE);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Server startup logic here
    }
}
