package com.galeon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

public class ShaderLoader {

    private final int programId;
    private final int vertexShaderId;
    private final int fragmentShaderId;

    public ShaderLoader(GL3 gl, String vertPath, String fragPath) {
        programId = gl.glCreateProgram();
        vertexShaderId=compileAndAttach(gl, vertPath, GL3.GL_VERTEX_SHADER);
        fragmentShaderId=compileAndAttach(gl, fragPath, GL3.GL_FRAGMENT_SHADER);
        linkProgram(gl);
    }

    private int compileAndAttach(GL3 gl, String path, int type) {
        String src = readFile(path);
        int shaderId = gl.glCreateShader(type);
        gl.glShaderSource(shaderId, 1, new String[]{src}, null);
        gl.glCompileShader(shaderId);

        int[] status = new int[1];
        gl.glGetShaderiv(shaderId, GL3.GL_COMPILE_STATUS, status, 0);
        if (status[0] == GL.GL_FALSE) {
            int[] len = new int[1];
            gl.glGetShaderiv(shaderId, GL3.GL_INFO_LOG_LENGTH, len, 0);
            byte[] buf = new byte[len[0]];
            gl.glGetShaderInfoLog(shaderId, buf.length, null, 0, buf, 0);
            System.err.println(">>> SHADER INFO LOG:\n" + new String(buf));
        }
        gl.glAttachShader(programId, shaderId);
        return shaderId;
    }

    private void linkProgram(GL3 gl) {
        gl.glLinkProgram(programId);

        int[] status = new int[1];
        gl.glGetProgramiv(programId, GL3.GL_LINK_STATUS, status, 0);
        if (status[0] == GL.GL_FALSE) {
            int[] len = new int[1];
            gl.glGetProgramiv(programId, GL3.GL_INFO_LOG_LENGTH, len, 0);
            byte[] buf = new byte[len[0]];
            gl.glGetProgramInfoLog(programId, buf.length, null, 0, buf, 0);
            System.err.println(">>> PROGRAM INFO LOG:\n" + new String(buf));
        }

        gl.glValidateProgram(programId);
    }

    private String readFile(String path) {
    if (path == null || path.isEmpty()) {
        throw new IllegalArgumentException("Shader path cannot be null or empty");
    }
    try (InputStream in = getClass().getClassLoader().getResourceAsStream(path)) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        // System.out.println(">>> SHADER SOURCE:\n" + sb.toString());
        return sb.toString();
    } 
    catch (IOException e) {
        throw new RuntimeException("Unable to read shader: " + path, e);
        }
    }

    public int getProgramId() {
        return programId;
    }

    public int getVertexShaderId() { return vertexShaderId; }
    public int getFragmentShaderId() { return fragmentShaderId; }
}
