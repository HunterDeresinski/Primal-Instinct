package net.neophantum.primalinstinct.api.particle;

import net.minecraft.resources.ResourceLocation;
import net.neophantum.primalinstinct.api.nbt.ITagSerializable;
import net.neophantum.primalinstinct.client.particle.ParticleColor;

public interface IParticleColor extends ITagSerializable, Cloneable {

    float getRed();
    float getGreen();
    float getBlue();
    int getColor();

    /**
     * Generates a new color within the max range of the given color.
     */
    default ParticleColor nextColor(int ticks) {
        return (ParticleColor) this;
    }

    default ParticleColor transition(int ticks) {
        return (ParticleColor) this;
    }

    default int getRedInt() {
        return (int) (getRed() * 255.0F);
    }

    default int getGreenInt() {
        return (int) (getGreen() * 255.0F);
    }

    default int getBlueInt() {
        return (int) (getBlue() * 255.0F);
    }

    ResourceLocation getRegistryName();
}
