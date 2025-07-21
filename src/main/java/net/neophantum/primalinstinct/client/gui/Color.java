package net.neophantum.primalinstinct.client.gui;

import com.google.common.hash.Hashing;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neophantum.primalinstinct.client.particle.ParticleColor;
import org.joml.Vector3f;

import java.util.function.UnaryOperator;

public class Color {
    public static final Color TRANSPARENT_BLACK = new Color(0, 0, 0, 0).setImmutable();
    public static final Color BLACK = new Color(0, 0, 0).setImmutable();
    public static final Color WHITE = new Color(255, 255, 255).setImmutable();
    public static final Color RED = new Color(255, 0, 0).setImmutable();
    public static final Color GREEN = new Color(0, 255, 0).setImmutable();
    public static final Color BLUE = new Color(0, 0, 255).setImmutable();
    public static final Color YELLOW = new Color(255, 255, 25).setImmutable();
    public static final Color SPRING_GREEN = new Color(0, 255, 187).setImmutable();

    protected boolean mutable = true;
    protected int value;

    public ParticleColor toParticle() {
        return ParticleColor.fromInt(getRGB());
    }

    public Color(int r, int g, int b) {
        this(r, g, b, 0xff);
    }

    public Color(int r, int g, int b, int a) {
        value = ((a & 0xff) << 24) |
                ((r & 0xff) << 16) |
                ((g & 0xff) << 8) |
                (b & 0xff);
    }

    public Color(float r, float g, float b, float a) {
        this(
                (int) (0.5 + 0xff * Mth.clamp(r, 0, 1)),
                (int) (0.5 + 0xff * Mth.clamp(g, 0, 1)),
                (int) (0.5 + 0xff * Mth.clamp(b, 0, 1)),
                (int) (0.5 + 0xff * Mth.clamp(a, 0, 1))
        );
    }

    public Color(int rgba) {
        value = rgba;
    }

    public Color(int rgb, boolean hasAlpha) {
        this.value = hasAlpha ? rgb : (rgb | 0xff_000000);
    }

    public Color copy() {
        return copy(true);
    }

    public Color copy(boolean mutable) {
        return mutable ? new Color(value) : new Color(value).setImmutable();
    }

    public Color setImmutable() {
        this.mutable = false;
        return this;
    }

    public int getRed() {
        return (getRGB() >> 16) & 0xff;
    }

    public int getGreen() {
        return (getRGB() >> 8) & 0xff;
    }

    public int getBlue() {
        return getRGB() & 0xff;
    }

    public int getAlpha() {
        return (getRGB() >> 24) & 0xff;
    }

    public float getRedAsFloat() {
        return getRed() / 255f;
    }

    public float getGreenAsFloat() {
        return getGreen() / 255f;
    }

    public float getBlueAsFloat() {
        return getBlue() / 255f;
    }

    public float getAlphaAsFloat() {
        return getAlpha() / 255f;
    }

    public int getRGB() {
        return value;
    }

    public Vec3 asVector() {
        return new Vec3(getRedAsFloat(), getGreenAsFloat(), getBlueAsFloat());
    }

    public Vector3f asVectorF() {
        return new Vector3f(getRedAsFloat(), getGreenAsFloat(), getBlueAsFloat());
    }

    public Color setRed(int r) {
        return ensureMutable().setRedUnchecked(r);
    }

    public Color setGreen(int g) {
        return ensureMutable().setGreenUnchecked(g);
    }

    public Color setBlue(int b) {
        return ensureMutable().setBlueUnchecked(b);
    }

    public Color setAlpha(int a) {
        return ensureMutable().setAlphaUnchecked(a);
    }

    public Color setRed(float r) {
        return setRed((int) (0xff * Mth.clamp(r, 0, 1)));
    }

    public Color setGreen(float g) {
        return setGreen((int) (0xff * Mth.clamp(g, 0, 1)));
    }

    public Color setBlue(float b) {
        return setBlue((int) (0xff * Mth.clamp(b, 0, 1)));
    }

    public Color setAlpha(float a) {
        return setAlpha((int) (0xff * Mth.clamp(a, 0, 1)));
    }

    public Color scaleAlpha(float factor) {
        return setAlpha((int) (getAlpha() * Mth.clamp(factor, 0, 1)));
    }

    public Color mixWith(Color other, float weight) {
        return ensureMutable()
                .setRedUnchecked((int) (getRed() + (other.getRed() - getRed()) * weight))
                .setGreenUnchecked((int) (getGreen() + (other.getGreen() - getGreen()) * weight))
                .setBlueUnchecked((int) (getBlue() + (other.getBlue() - getBlue()) * weight))
                .setAlphaUnchecked((int) (getAlpha() + (other.getAlpha() - getAlpha()) * weight));
    }

    public Color darker() {
        int a = getAlpha();
        return ensureMutable().mixWith(BLACK, 0.25f).setAlphaUnchecked(a);
    }

    public Color brighter() {
        int a = getAlpha();
        return ensureMutable().mixWith(WHITE, 0.25f).setAlphaUnchecked(a);
    }

    public Color setValue(int value) {
        return ensureMutable().setValueUnchecked(value);
    }

    public Color modifyValue(UnaryOperator<Integer> fn) {
        int newValue = fn.apply(value);
        return newValue == value ? this : ensureMutable().setValueUnchecked(newValue);
    }

    protected Color ensureMutable() {
        return this.mutable ? this : new Color(this.value);
    }

    protected Color setRedUnchecked(int r) {
        this.value = (this.value & 0xff00ffff) | ((r & 0xff) << 16);
        return this;
    }

    protected Color setGreenUnchecked(int g) {
        this.value = (this.value & 0xffff00ff) | ((g & 0xff) << 8);
        return this;
    }

    protected Color setBlueUnchecked(int b) {
        this.value = (this.value & 0xffffff00) | (b & 0xff);
        return this;
    }

    protected Color setAlphaUnchecked(int a) {
        this.value = (this.value & 0x00ffffff) | ((a & 0xff) << 24);
        return this;
    }

    protected Color setValueUnchecked(int value) {
        this.value = value;
        return this;
    }

    public static int mixColors(int color1, int color2, float w) {
        int a1 = (color1 >> 24) & 0xff;
        int r1 = (color1 >> 16) & 0xff;
        int g1 = (color1 >> 8) & 0xff;
        int b1 = color1 & 0xff;

        int a2 = (color2 >> 24) & 0xff;
        int r2 = (color2 >> 16) & 0xff;
        int g2 = (color2 >> 8) & 0xff;
        int b2 = color2 & 0xff;

        return ((int) (a1 + (a2 - a1) * w) << 24) |
                ((int) (r1 + (r2 - r1) * w) << 16) |
                ((int) (g1 + (g2 - g1) * w) << 8) |
                ((int) (b1 + (b2 - b1) * w));
    }

    public static Color rainbowColor(int timeStep) {
        int t = Math.abs(timeStep) % 1536;
        int phase = t / 256;
        int step = t % 256;
        int r = colorInPhase(phase + 4, step);
        int g = colorInPhase(phase + 2, step);
        int b = colorInPhase(phase, step);
        return new Color(r, g, b);
    }

    private static int colorInPhase(int phase, int progress) {
        phase %= 6;
        if (phase <= 1) return 0;
        if (phase == 2) return progress;
        if (phase <= 4) return 255;
        return 255 - progress;
    }

    public static Color generateFromLong(long seed) {
        return rainbowColor(Hashing.crc32().hashLong(seed).asInt()).mixWith(WHITE, 0.5f);
    }

    public static Color interpolate(Color a, Color b, double progress) {
        return new Color(
                (int) (a.getRed() + (b.getRed() - a.getRed()) * progress),
                (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * progress),
                (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * progress),
                (int) (a.getAlpha() + (b.getAlpha() - a.getAlpha()) * progress)
        );
    }
}
