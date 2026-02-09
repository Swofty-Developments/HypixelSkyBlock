#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>

uniform sampler2D Sampler0;

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
flat in int isMinimap;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    if (color.a < 0.1) {
        discard;
    }

    if (isMinimap == 1) {
        // Preserve raw map pixels for minimap readability and overlay fidelity.
        vec4 mapColor = texture(Sampler0, texCoord0);
        if (mapColor.a < 0.01) {
            discard;
        }

        // Hide the tiny top-left metadata marker block used for signature/anchor decoding.
        if (texCoord0.x < (5.0 / 128.0) && texCoord0.y < (2.0 / 128.0)) {
            discard;
        }

        fragColor = mapColor;
        return;
    }

    fragColor = apply_fog(
            color,
            sphericalVertexDistance,
            cylindricalVertexDistance,
            FogEnvironmentalStart,
            FogEnvironmentalEnd,
            FogRenderDistanceStart,
            FogRenderDistanceEnd,
            FogColor
    );
}
