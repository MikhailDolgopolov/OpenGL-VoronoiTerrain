package com.galeon;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.EventQueue;

public abstract class WindowDrawer implements GLEventListener {
    protected int programId;

    protected WindowDrawer(String title) {
        EventQueue.invokeLater(() -> {
            GLProfile.initSingleton();
            GLProfile profile = GLProfile.get(GLProfile.GL3);
            GLCapabilities caps = new GLCapabilities(profile);
            caps.setDoubleBuffered(true);

            GLWindow window = GLWindow.create(caps);
            window.setTitle(title);
            window.setSize(800, 600);
            window.addGLEventListener(this);
            window.addWindowListener(new WindowAdapter() {
                @Override
                public void windowDestroyed(WindowEvent e) {
                    System.exit(0);
                }
            });
            FPSAnimator animator = new FPSAnimator(window, 60, true);
            animator.start();
            window.setVisible(true);
        });
    }

    // Called once, on GL thread
    @Override
    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        // compile + link your shaders
        programId = loadProgram(gl, getVertexShader(), getFragmentShader());
        // and any other one‑time GL setup
        setup(gl);
    }

    // Called every frame
    @Override
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glUseProgram(programId);
        render(gl);
    }

    @Override public void reshape(GLAutoDrawable d, int x, int y, int w, int h) {
        d.getGL().getGL3().glViewport(x, y, w, h);
    }
    @Override public void dispose(GLAutoDrawable d) {
        d.getGL().getGL3().glDeleteProgram(programId);
    }

    private int loadProgram(GL3 gl, String vert, String frag) {
        ShaderLoader loader = new ShaderLoader(gl, vert, frag);
        return loader.getProgramId();
    }

    /** Called once after program is linked. Override to create VAO/VBO, textures, etc. */
    protected void setup(GL3 gl) {}

    /** Called every frame (inside display). Override to set uniforms, bind VAO, draw. */
    protected abstract void render(GL3 gl);

    /** Provide your GLSL paths here */
    protected abstract String getVertexShader();
    protected abstract String getFragmentShader();
}
