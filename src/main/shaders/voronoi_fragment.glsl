#version 330 core
out vec4 FragColor;
in vec2 fragCoord;

uniform float uTime;

float random(vec2 p) {
    return fract(sin(dot(p ,vec2(127.1,311.7))) * 43758.5453123);
}

vec2 random2(vec2 p) {
    return fract(sin(vec2(dot(p, vec2(127.1, 311.7)),
                          dot(p, vec2(269.5, 183.3)))) * 43758.5453);
}

float voronoi(vec2 uv) {
    vec2 gv = fract(uv) - 0.5;
    vec2 id = floor(uv);

    float minDist = 1.0;
    for(int y = -1; y <= 1; y++) {
        for(int x = -1; x <= 1; x++) {
            vec2 offset = vec2(x, y);
            vec2 neighbor = id + offset;
            vec2 point = random2(neighbor);
            float dist = length(gv - offset - point);
            minDist = min(minDist, dist);
        }
    }
    return minDist;
}

void main() {
    vec2 uv = fragCoord * 10.0 + uTime * 0.5; // scale + animate
    float d = voronoi(uv);
    FragColor = vec4(vec3(1.0 - d), 1.0);
}
