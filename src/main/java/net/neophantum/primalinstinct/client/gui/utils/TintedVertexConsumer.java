package net.neophantum.primalinstinct.client.gui.utils;

import com.mojang.blaze3d.vertex.VertexConsumer;

public final class TintedVertexConsumer implements VertexConsumer {
    private final VertexConsumer wrapped;
    private final float red;
    private final float green;
    private final float blue;
    private final float alpha;

    public TintedVertexConsumer(VertexConsumer wrapped, float red, float green, float blue, float alpha) {
        this.wrapped = wrapped;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    @Override
    public VertexConsumer addVertex(float x, float y, float z) {
        return wrapped.addVertex(x, y, z);
    }

    @Override
    public VertexConsumer setColor(int r, int g, int b, int a) {
        return wrapped.setColor(
                (int) (r * red),
                (int) (g * green),
                (int) (b * blue),
                (int) (a * alpha)
        );
    }

    @Override
    public VertexConsumer setUv(float u, float v) {
        return wrapped.setUv(u, v);
    }

    @Override
    public VertexConsumer setUv1(int u, int v) {
        return wrapped.setUv1(u, v);
    }

    @Override
    public VertexConsumer setOverlay(int overlay) {
        return wrapped.setOverlay(overlay);
    }

    @Override
    public VertexConsumer setUv2(int u, int v) {
        return wrapped.setUv2(u, v);
    }

    @Override
    public VertexConsumer setNormal(float x, float y, float z) {
        return wrapped.setNormal(x, y, z);
    }
}
