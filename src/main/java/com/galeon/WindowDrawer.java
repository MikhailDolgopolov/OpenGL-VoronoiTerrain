package com.galeon;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.EventQueue;

public class WindowDrawer implements GLEventListener {
    private final SceneRenderer renderer;

    public WindowDrawer(SceneRenderer renderer) {
        this.renderer = renderer;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GLProfile.initSingleton();
            GLProfile profile = GLProfile.get(GLProfile.GL3);
            GLCapabilities caps = new GLCapabilities(profile);
            caps.setDoubleBuffered(true);

            GLWindow window = GLWindow.create(caps);

            FPSAnimator animator = new FPSAnimator(window, 1, true);
            animator.start();

            // replace JFrame with the window
            window.setTitle("Red Quad Test");
            window.setSize(800, 600);
            window.setUndecorated(false);
            window.setResizable(true); 
            window.setVisible(true);
            window.addGLEventListener(new WindowDrawer(new SimpleQuadRenderer()));
            window.addWindowListener(new WindowAdapter() {
                @Override
                public void windowDestroyed(WindowEvent e) {
                    animator.stop();
                    System.exit(1);
                }
            });
        });
        
    }

    @Override public void init(GLAutoDrawable drawable) {
        renderer.init(drawable);
    }
    @Override public void display(GLAutoDrawable drawable) {
        renderer.display(drawable);
    }
    @Override public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        renderer.reshape(drawable, x, y, w, h);
    }
    @Override public void dispose(GLAutoDrawable drawable) {
        renderer.dispose(drawable);
    }
}
