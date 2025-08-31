package com.galeon.Voronoi;

import com.galeon.WindowDrawer;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

import java.nio.FloatBuffer;

public class VoronoiRenderer extends WindowDrawer {
    private static final int NUM_POINTS = 50;
    private int vao, vbo;
    private int uPointCountLoc, uPointsLoc;
    private FloatBuffer pointBuffer;
    private float[] points = new float[NUM_POINTS * 2];

    public VoronoiRenderer() {
        super("Voronoi Texture");
        // initialize random points
        for (int i = 0; i < NUM_POINTS; i++) {
            points[2*i  ] = (float)Math.random();
            points[2*i+1] = (float)Math.random();
        }
        pointBuffer = Buffers.newDirectFloatBuffer(points);
    }

    public static void main(String[] args) {
        new VoronoiRenderer();
    }

    @Override
    protected String getVertexShader() {
        return "shaders/voronoi_vertex.glsl";
    }
    @Override
    protected String getFragmentShader() {
        return "shaders/voronoi_fragment.glsl";
    }

    @Override
    protected void setup(GL4 gl) {
        // Create fullscreen quad VAO/VBO
        float[] quad = {
            -1f,-1f,  1f,-1f,  -1f,1f,
            -1f,1f,   1f,-1f,   1f,1f
        };
        FloatBuffer buf = Buffers.newDirectFloatBuffer(quad);

        int[] tmp = new int[1];
        gl.glGenVertexArrays(1, tmp,0);  vao = tmp[0];
        gl.glBindVertexArray(vao);
        gl.glGenBuffers(1,tmp,0);         vbo = tmp[0];
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, quad.length*Float.BYTES, buf, GL.GL_STATIC_DRAW);
        gl.glVertexAttribPointer(0,2,GL.GL_FLOAT,false,0,0);
        gl.glEnableVertexAttribArray(0);
        gl.glBindVertexArray(0);

        // Look up uniform locations
        uPointCountLoc = gl.glGetUniformLocation(programId, "uPointCount");
        uPointsLoc     = gl.glGetUniformLocation(programId, "uPoints");
    }

    @Override
    protected void render(GL4 gl) {
        // Upload point data every frame (or only when changed)
        gl.glUniform1i(uPointCountLoc, NUM_POINTS);
        gl.glUniform2fv(uPointsLoc, NUM_POINTS, pointBuffer);

        // Draw our quad
        gl.glBindVertexArray(vao);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, 6);
        gl.glBindVertexArray(0);
    }
}
