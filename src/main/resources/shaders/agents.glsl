#version 430 core
struct Agent {
    vec2 pos;
    vec2 vel;
    float angle;
};
layout(std430, binding = 0) buffer AgentsBuffer {
    Agent agents[];
};