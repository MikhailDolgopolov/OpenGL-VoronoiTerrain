package com.galeon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.jogamp.opengl.GL3;

public class ShaderLoader {

    private final int programId;

    public ShaderLoader(GL3 gl, String vertPath, String fragPath) {
        programId = gl.glCreateProgram();
        compileAndAttach(gl, vertPath, GL3.GL_VERTEX_SHADER);
        compileAndAttach(gl, fragPath, GL3.GL_FRAGMENT_SHADER);
        linkProgram(gl);
    }

    private void compileAndAttach(GL3 gl, String path, int type) {
        String src = readFile(path);
        int shader = gl.glCreateShader(type);
        gl.glShaderSource(shader, 1, new String[]{src}, null);
        gl.glCompileShader(shader);

        // Compilation check
        int[] stat = new int[1];
        gl.glGetShaderiv(shader, GL3.GL_COMPILE_STATUS, stat, 0);
        if (stat[0] == GL3.GL_FALSE) {
            int[] len = {0};
            gl.glGetShaderiv(shader, GL3.GL_INFO_LOG_LENGTH, len, 0);
            byte[] log = new byte[len[0]];
            gl.glGetShaderInfoLog(shader, log.length, null, 0, log, 0);
            throw new RuntimeException("Shader compile error (" + path + "):\n" + new String(log));
        }

        gl.glAttachShader(programId, shader);
        gl.glDeleteShader(shader);
    }

    private void linkProgram(GL3 gl) {
        gl.glLinkProgram(programId);

        int[] stat = new int[1];
        gl.glGetProgramiv(programId, GL3.GL_LINK_STATUS, stat, 0);
        if (stat[0] == GL3.GL_FALSE) {
            int[] len = {0};
            gl.glGetProgramiv(programId, GL3.GL_INFO_LOG_LENGTH, len, 0);
            byte[] log = new byte[len[0]];
            gl.glGetProgramInfoLog(programId, log.length, null, 0, log, 0);
            throw new RuntimeException("Program link error:\n" + new String(log));
        }

        gl.glValidateProgram(programId);
    }

    private String readFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read shader file: " + path, e);
        }
    }

    public int getProgramId() {
        return programId;
    }
}
