uniform sampler2D DiffuseSampler;
uniform float SanityRatio; // 1.0 = full sanity, 0.0 = no sanity

vec4 applyGrayscale(vec4 color, float intensity) {
    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    return mix(color, vec4(gray, gray, gray, color.a), 1.0 - intensity);
}

void main() {
    vec4 color = texture(DiffuseSampler, gl_TexCoord[0].xy);
    gl_FragColor = applyGrayscale(color, SanityRatio);
}