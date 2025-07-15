package com.galeon.Voronoi;

import com.jogamp.common.nio.Buffers;
import java.nio.FloatBuffer;

public class VoronoiTextureGenerator {
    private static final int NUM_POINTS = 50;
    private float[] points = new float[NUM_POINTS * 2];
    private FloatBuffer pointBuffer;

    public VoronoiTextureGenerator() {
        for (int i = 0; i < NUM_POINTS; i++) {
            points[i * 2] = (float) Math.random(); // x-coordinate
            points[i * 2 + 1] = (float) Math.random(); // y-coordinate
        }
        pointBuffer = Buffers.newDirectFloatBuffer(points);
    }

    public FloatBuffer getPointBuffer() {
        return pointBuffer;
    }

    public void setPoint(int index, float x, float y) {
        points[index * 2] = x;
        points[index * 2 + 1] = y;
        pointBuffer.rewind();
        pointBuffer.put(points);
    }
}
