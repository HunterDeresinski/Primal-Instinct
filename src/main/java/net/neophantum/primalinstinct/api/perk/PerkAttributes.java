package net.neophantum.primalinstinct.api.perk;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neophantum.primalinstinct.PrimalInstinct;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

@EventBusSubscriber(modid = PrimalInstinct.MODID)
public class PerkAttributes {
    public static final HashMap<DeferredHolder<Attribute, Attribute>, UUID> UUIDS = new HashMap<>();
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, PrimalInstinct.MODID);

    public static final DeferredHolder<Attribute, Attribute> MAX_SANITY = registerAttribute("max_sanity", id -> new RangedAttribute(id, 0.0D, 0.0D, 10000.0D).setSyncable(true), "39a6c845-7cb2-4c5c-9b9c-4b22c2b00d7b");
    public static final DeferredHolder<Attribute, Attribute> SANITY_REGEN = registerAttribute("sanity_regen", id -> new RangedAttribute(id, 0.0D, 0.0D, 1000.0D).setSyncable(true), "6f1b64f9-3ec9-4f32-9641-6a7a83de3452");

    public static DeferredHolder<Attribute, Attribute> registerAttribute(String name, Function<String, Attribute> attribute, String uuid) {
        return registerAttribute(name, attribute, UUID.fromString(uuid));
    }

    public static DeferredHolder<Attribute, Attribute> registerAttribute(String name, Function<String, Attribute> attribute, UUID uuid) {
        DeferredHolder<Attribute, Attribute> registryObject = ATTRIBUTES.register(name, () -> attribute.apply(name));
        UUIDS.put(registryObject, uuid);
        return registryObject;
    }

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().stream().filter(type -> type == EntityType.PLAYER).forEach(type -> {
            ATTRIBUTES.getEntries().forEach(attr -> {
                event.add(type, attr);
            });
        });
    }
}
