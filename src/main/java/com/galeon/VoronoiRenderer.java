package com.galeon;

import com.jogamp.opengl.*;
import java.nio.FloatBuffer;

public class VoronoiRenderer implements SceneRenderer {
    private int programId;
    private int vbo;
    private int vao;

    @Override
    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        // Load shaders (fullscreen quad)
        ShaderLoader loader = new ShaderLoader(
            gl,
            "src/main/shaders/voronoi_vertex.glsl",
            "src/main/shaders/voronoi_fragment.glsl"
        );
        programId = loader.getProgramId();

        // Fullscreen quad vertices (NDC)
        float[] quadVertices = {
            -1f, -1f,
             1f, -1f,
            -1f,  1f,
            -1f,  1f,
             1f, -1f,
             1f,  1f
        };

        // Generate VAO
        int[] vaos = new int[1];
        gl.glGenVertexArrays(1, vaos, 0);
        vao = vaos[0];
        gl.glBindVertexArray(vao);

        // Generate VBO
        int[] vbos = new int[1];
        gl.glGenBuffers(1, vbos, 0);
        vbo = vbos[0];
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, quadVertices.length * Float.BYTES,
            FloatBuffer.wrap(quadVertices), GL.GL_STATIC_DRAW);

        int posLoc = gl.glGetAttribLocation(programId, "aPos");
        gl.glEnableVertexAttribArray(posLoc);
        gl.glVertexAttribPointer(posLoc, 2, GL.GL_FLOAT, false, 0, 0);

        gl.glBindVertexArray(0);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glUseProgram(programId);
        gl.glBindVertexArray(vao);

        // Pass time uniform (for animation)
        float time = (System.currentTimeMillis() % 10000) / 1000f;
        int timeLoc = gl.glGetUniformLocation(programId, "uTime");
        gl.glUniform1f(timeLoc, time);

        gl.glDrawArrays(GL.GL_TRIANGLES, 0, 6);

        gl.glBindVertexArray(0);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        drawable.getGL().getGL3().glViewport(0, 0, w, h);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glDeleteProgram(programId);
        gl.glDeleteBuffers(1, new int[]{vbo}, 0);
        gl.glDeleteVertexArrays(1, new int[]{vao}, 0);
    }
}
