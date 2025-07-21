package net.neophantum.primalinstinct.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

/**
 * Event fired after the preliminary sanity regeneration rate is calculated.
 * Allows other systems to modify or override sanity regen.
 */
public class SanityRegenCalcEvent extends LivingEvent {

    private double regen;

    public SanityRegenCalcEvent(LivingEntity entity, double regen) {
        super(entity);
        this.regen = regen;
    }

    public void setRegen(double newRegen) {
        this.regen = Math.max(newRegen, 0);
    }

    public double getRegen() {
        return this.regen;
    }
}
