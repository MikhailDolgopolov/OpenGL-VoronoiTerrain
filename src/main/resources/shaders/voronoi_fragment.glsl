#version 330 core

uniform int uPointCount;        // how many points are active
uniform vec2 uPoints[50];       // must match your Java-side size

in vec2 uv;                     // from vertex shader
out vec4 fragColor;

void main() {
    float minDist = 1e6;        // start large

    // Loop only over active points:
    for (int i = 0; i < uPointCount; i++) {
        float d = distance(uv, uPoints[i]);
        if (d < minDist) {
            minDist = d;
        }
    }

    // Map distance to a grayscale color:
    // (you can tweak contrast or scaling here)
    fragColor = vec4(vec3(minDist), 1.0);
}
