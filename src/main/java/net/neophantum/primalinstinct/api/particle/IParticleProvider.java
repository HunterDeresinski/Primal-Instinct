package net.neophantum.primalinstinct.api.particle;

import net.minecraft.nbt.CompoundTag;
import net.neophantum.primalinstinct.client.particle.ParticleColor;

public interface IParticleProvider {
    ParticleColor create(CompoundTag tag);
    ParticleColor create(int r, int g, int b);
}