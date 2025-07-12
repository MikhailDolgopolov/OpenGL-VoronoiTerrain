package com.galeon;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import java.nio.FloatBuffer;

public class SimpleQuadRenderer implements SceneRenderer {
    private int programId;
    private int vao, vbo;

    @Override
    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        // Compile & link minimal shaders
        ShaderLoader loader = new ShaderLoader(
            gl,
            "src/main/shaders/vertex_simple.glsl",
            "src/main/shaders/fragment_simple.glsl"
        );
        programId = loader.getProgramId();

        System.out.println("Loaded program ID: " + programId);
        System.out.flush();

        // Prepare a full‑screen quad (80% size for clarity)
        float s = 0.8f;
        float[] quad = {
            -s, -s,
             s, -s,
            -s,  s,
            -s,  s,
             s, -s,
             s,  s
        };
        FloatBuffer buf = Buffers.newDirectFloatBuffer(quad);

        // Create VAO
        int[] tmp = new int[1];
        gl.glGenVertexArrays(1, tmp, 0);
        vao = tmp[0];
        gl.glBindVertexArray(vao);

        // Create VBO
        gl.glGenBuffers(1, tmp, 0);
        vbo = tmp[0];
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, quad.length * Float.BYTES, buf, GL.GL_STATIC_DRAW);

        // Attribute pointer (must match layout(location=0))
        gl.glVertexAttribPointer(0, 2, GL.GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        // Unbind
        gl.glBindVertexArray(0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        // Clear to green
        gl.glClearColor(0.0f, 0.5f, 0.0f, 1f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        // Draw red quad
        gl.glUseProgram(programId);
        gl.glBindVertexArray(vao);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, 6);
        gl.glBindVertexArray(0);
    }

    @Override public void reshape(GLAutoDrawable d, int x, int y, int w, int h) {
        d.getGL().getGL3().glViewport(0, 0, w, h);
    }
    @Override public void dispose(GLAutoDrawable d) {
        GL3 gl = d.getGL().getGL3();
        gl.glDeleteProgram(programId);
        gl.glDeleteBuffers(1, new int[]{vbo}, 0);
        gl.glDeleteVertexArrays(1, new int[]{vao}, 0);
    }
}

