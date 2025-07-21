package net.neophantum.primalinstinct.api.event;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

/**
 * An event that fires after preliminary Sanity has been calculated from Insight, Experience Tier, and other modifiers.
 * Also tracks sanity reserve percentage (e.g., reserved by special effects).
 */
public class MaxSanityCalcEvent extends LivingEvent {

    private int max;
    private float reserve;

    public MaxSanityCalcEvent(LivingEntity entity, int max) {
        super(entity);
        this.max = max;
        this.reserve = 0;
    }

    public void setMax(int newMax) {
        this.max = Math.max(newMax, 0);
    }

    public int getMax() {
        return this.max;
    }

    public void setReserve(float newReserve) {
        this.reserve = Mth.clamp(newReserve, 0, 1);
    }

    public float getReserve() {
        return this.reserve;
    }
}
