package com.galeon;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import java.nio.FloatBuffer;

public class SimpleQuadRenderer implements SceneRenderer {
    private int programId, vao, vbo;

    @Override
    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();


        // compile & link
        ShaderLoader loader = new ShaderLoader(
            gl,
            "shaders/vertex_simple.glsl",
            "shaders/fragment_simple.glsl"
        );
        programId = loader.getProgramId();

        // quad coords
        float s = 0.5f;
        float[] quad = {
            -s,-s,  s,-s,  -s, s,
            -s, s,  s,-s,   s, s
        };
        FloatBuffer fb = Buffers.newDirectFloatBuffer(quad);

        // VAO
        int[] tmp = new int[1];
        gl.glGenVertexArrays(1, tmp, 0);
        vao = tmp[0];
        gl.glBindVertexArray(vao);

        // VBO
        gl.glGenBuffers(1, tmp, 0);
        vbo = tmp[0];
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, quad.length * Float.BYTES, fb, GL.GL_STATIC_DRAW);

        // attribute 0 ⇒ vec2 position
        gl.glVertexAttribPointer(0, 2, GL.GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        // unbind
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
        gl.glBindVertexArray(0);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        gl.glClearColor(0f, 0.5f, 0f, 1f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glUseProgram(programId);
        gl.glBindVertexArray(vao);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, 6);
        gl.glBindVertexArray(0);

        int e = gl.glGetError();
        if (e != GL.GL_NO_ERROR) {
            System.err.println("GL Error: 0x" + Integer.toHexString(e));
        }
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
