package com.galeon;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public interface SceneRenderer extends GLEventListener  {
    void init(GLAutoDrawable drawable);
    void display(GLAutoDrawable drawable);
    void reshape(GLAutoDrawable drawable, int x, int y, int w, int h);
    void dispose(GLAutoDrawable drawable);
}

