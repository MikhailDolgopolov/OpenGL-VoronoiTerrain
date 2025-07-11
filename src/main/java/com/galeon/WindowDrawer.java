package com.galeon;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.JFrame;
import java.awt.EventQueue;

public class WindowDrawer implements GLEventListener {
    private final SceneRenderer renderer;

    public WindowDrawer(SceneRenderer renderer) {
        this.renderer = renderer;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GLProfile.initSingleton();
            System.setProperty("jogl.disable.openglcore", "true");
            GLProfile profile = GLProfile.get(GLProfile.GL3);
            GLCapabilities caps = new GLCapabilities(profile);

            GLJPanel canvas = new GLJPanel(caps);
            canvas.addGLEventListener(new WindowDrawer(new VoronoiRenderer()));

            FPSAnimator animator = new FPSAnimator(canvas, 60, true);
            animator.start();

            JFrame frame = new JFrame("Voronoi Texture");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(canvas);
            frame.setSize(800, 600);
            frame.setVisible(true);
        });
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        renderer.init(drawable);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        renderer.display(drawable);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        renderer.reshape(drawable, x, y, w, h);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        renderer.dispose(drawable);
    }
}
