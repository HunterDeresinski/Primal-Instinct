#version 150

in vec2 Position;
out vec2 texCoord;

uniform mat4 ProjMat;

void main() {
    texCoord = Position * 0.5 + 0.5;
    gl_Position = ProjMat * vec4(Position, 0.0, 1.0);
}
