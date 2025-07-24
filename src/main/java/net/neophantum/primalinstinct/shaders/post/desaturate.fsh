uniform sampler2D DiffuseSampler;
uniform float SanityRatio;

vec4 applyGrayscale(vec4 color, float intensity) {
    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    return mix(vec4(gray, gray, gray, color.a), color, intensity); // fade to gray as SanityRatio → 0
}

void main() {
    vec4 color = texture(DiffuseSampler, gl_TexCoord[0].xy);
    gl_FragColor = applyGrayscale(color, SanityRatio);
}