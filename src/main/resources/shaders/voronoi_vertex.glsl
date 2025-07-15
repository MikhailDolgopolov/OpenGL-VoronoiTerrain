#version 330 core

layout(location = 0) in vec2 position;  // from your VAO

out vec2 uv;                           // to the fragment shader

void main() {
    // Send NDC → UV [0,1]
    uv = position * 0.5 + 0.5;
    gl_Position = vec4(position, 0.0, 1.0);
}
