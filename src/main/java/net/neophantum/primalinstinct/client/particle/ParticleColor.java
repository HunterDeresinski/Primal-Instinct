package net.neophantum.primalinstinct.client.particle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.neophantum.primalinstinct.PrimalInstinct;
import net.neophantum.primalinstinct.api.registry.ParticleColorRegistry;
import net.neophantum.primalinstinct.api.particle.IParticleColor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ParticleColor implements IParticleColor, Cloneable {

    public static final MapCodec<ParticleColor> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(ParticleColor::getRegistryName),
            Codec.INT.fieldOf("r").forGetter(ParticleColor::getRedInt),
            Codec.INT.fieldOf("g").forGetter(ParticleColor::getGreenInt),
            Codec.INT.fieldOf("b").forGetter(ParticleColor::getBlueInt)
    ).apply(instance, ParticleColorRegistry::from));

    public static final StreamCodec<RegistryFriendlyByteBuf, ParticleColor> STREAM = StreamCodec.of(
            (buf, val) -> {
                buf.writeResourceLocation(val.getRegistryName());
                buf.writeInt(val.getRedInt());
                buf.writeInt(val.getGreenInt());
                buf.writeInt(val.getBlueInt());
            },
            buf -> {
                ResourceLocation id = buf.readResourceLocation();
                int r = buf.readInt();
                int g = buf.readInt();
                int b = buf.readInt();
                return ParticleColorRegistry.from(id, r, g, b);
            }
    );

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(PrimalInstinct.MODID, "constant");

    public static final ParticleColor DEFAULT = new ParticleColor(255, 25, 180);
    public static final ParticleColor WHITE = new ParticleColor(255, 255, 255);
    public static final ParticleColor RED = new ParticleColor(255, 50, 50);
    public static final ParticleColor GREEN = new ParticleColor(50, 255, 50);
    public static final ParticleColor BLUE = new ParticleColor(50, 50, 255);
    public static final ParticleColor YELLOW = new ParticleColor(255, 255, 0);
    public static final ParticleColor PURPLE = new ParticleColor(255, 50, 255);
    public static final ParticleColor CYAN = new ParticleColor(50, 255, 255);
    public static final ParticleColor ORANGE = new ParticleColor(255, 128, 0);
    public static final ParticleColor MAGENTA = new ParticleColor(255, 0, 255);
    public static final ParticleColor LIGHT_BLUE = new ParticleColor(173, 216, 230);
    public static final ParticleColor LIME = new ParticleColor(0, 255, 0);
    public static final ParticleColor PINK = new ParticleColor(255, 192, 203);
    public static final ParticleColor GRAY = new ParticleColor(128, 128, 128);
    public static final ParticleColor LIGHT_GRAY = new ParticleColor(211, 211, 211);
    public static final ParticleColor BROWN = new ParticleColor(125, 42, 42);
    public static final ParticleColor BLACK = new ParticleColor(0, 0, 0);

    public static final List<ParticleColor> PRESET_COLORS = List.of(
            BROWN, RED, ORANGE, YELLOW, LIME, GREEN, CYAN, LIGHT_BLUE, BLUE, PURPLE, MAGENTA, PINK, WHITE, LIGHT_GRAY, GRAY, BLACK
    );

    public static final ParticleColor TO_HIGHLIGHT = RED;
    public static final ParticleColor FROM_HIGHLIGHT = CYAN;
    public static final Random random = new Random();

    private final float r;
    private final float g;
    private final float b;
    private final int color;

    public ParticleColor(int r, int g, int b) {
        this.r = Math.max(r, 1) / 255F;
        this.g = Math.max(g, 1) / 255F;
        this.b = Math.max(b, 1) / 255F;
        this.color = (r << 16) | (g << 8) | b;
    }

    public ParticleColor(float r, float g, float b) {
        this((int) r, (int) g, (int) b);
    }

    public ParticleColor(CompoundTag tag) {
        this(tag.getInt("r"), tag.getInt("g"), tag.getInt("b"));
    }

    public static ParticleColor fromInt(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        return new ParticleColor(r, g, b);
    }

    public static ParticleColor defaultParticleColor() {
        return DEFAULT;
    }

    public static ParticleColor makeRandomColor(int r, int g, int b, RandomSource random) {
        return new ParticleColor(random.nextInt(r), random.nextInt(g), random.nextInt(b));
    }

    public float getRed() { return r; }
    public float getGreen() { return g; }
    public float getBlue() { return b; }

    public int getRedInt() { return (int) (r * 255); }
    public int getGreenInt() { return (int) (g * 255); }
    public int getBlueInt() { return (int) (b * 255); }

    public int getColor() { return color; }

    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        IntWrapper wrapper = toWrapper();
        tag.putInt("r", wrapper.r);
        tag.putInt("g", wrapper.g);
        tag.putInt("b", wrapper.b);
        tag.putString("type", getRegistryName().toString());
        return tag;
    }

    public String toHex() {
        return String.format("#%02x%02x%02x", getRedInt(), getGreenInt(), getBlueInt());
    }

    public static ParticleColor fromHex(String hex) {
        return new ParticleColor(
                Integer.valueOf(hex.substring(1, 3), 16),
                Integer.valueOf(hex.substring(3, 5), 16),
                Integer.valueOf(hex.substring(5, 7), 16)
        );
    }

    public ParticleColor getOppositeColor() {
        double y = (299 * getRedInt() + 587 * getGreenInt() + 114 * getBlueInt()) / 1000.0;
        return y >= 128 ? BLACK : WHITE;
    }

    public ParticleColor nextColor(int ticks) {
        IntWrapper w = toWrapper();
        return new ParticleColor(random.nextInt(w.r), random.nextInt(w.g), random.nextInt(w.b));
    }

    public double euclideanDistance(ParticleColor other) {
        return Math.sqrt(Math.pow(this.r - other.r, 2) + Math.pow(this.g - other.g, 2) + Math.pow(this.b - other.b, 2));
    }

    public IntWrapper toWrapper() {
        return new IntWrapper(this);
    }

    @Override
    public ParticleColor clone() {
        try {
            return (ParticleColor) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParticleColor other)) return false;
        return Objects.equals(getRegistryName(), other.getRegistryName()) &&
                Float.compare(r, other.r) == 0 &&
                Float.compare(g, other.g) == 0 &&
                Float.compare(b, other.b) == 0 &&
                color == other.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistryName(), r, g, b, color);
    }

    @Deprecated(forRemoval = true)
    public static class IntWrapper implements Cloneable {
        public int r, g, b;

        public IntWrapper(int r, int g, int b) {
            this.r = Math.max(1, r);
            this.g = Math.max(1, g);
            this.b = Math.max(1, b);
        }

        public IntWrapper(ParticleColor color) {
            this.r = color.getRedInt();
            this.g = color.getGreenInt();
            this.b = color.getBlueInt();
        }

        public ParticleColor toParticleColor() {
            return new ParticleColor(r, g, b);
        }

        @Override
        public IntWrapper clone() {
            try {
                return (IntWrapper) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }
}
