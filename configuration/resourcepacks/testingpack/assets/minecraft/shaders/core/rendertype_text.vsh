#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>
#moj_import <minecraft:globals.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;

uniform sampler2D Sampler0;
uniform sampler2D Sampler2;

out float sphericalVertexDistance;
out float cylindricalVertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;
flat out int isMinimap;

int decodeBit0(vec3 marker) {
    // 0 = red-like, 1 = blue-like
    return (marker.b > marker.r && marker.b > marker.g) ? 1 : 0;
}

int decodeBit1(vec3 marker) {
    // 0 = yellow-like, 1 = cyan-like
    return (marker.g > marker.r && marker.b > marker.r) ? 1 : 0;
}

int decodeBit2(vec3 marker) {
    // 0 = green-like, 1 = magenta-like
    return (marker.r > marker.g && marker.b > marker.g) ? 1 : 0;
}

int decodeAnchor(vec3 marker0, vec3 marker1, vec3 marker2) {
    // Anchor id encoded as 3 bits across map pixels (2,0), (3,0), (4,0).
    int anchor = decodeBit0(marker0) | (decodeBit1(marker1) << 1) | (decodeBit2(marker2) << 2);
    if (anchor < 0 || anchor > 6) {
        return 0;
    }
    return anchor;
}

void main() {
    vec4 projected = ProjMat * ModelViewMat * vec4(Position, 1.0);
    isMinimap = 0;

    // Map pixel textures are 128x128 and render via rendertype_text.
    // Gate by a 2x2 color signature so vanilla map/background textures are ignored.
    ivec2 texSize = textureSize(Sampler0, 0);
    if (texSize == ivec2(128, 128)) {
        vec4 marker00 = texelFetch(Sampler0, ivec2(0, 0), 0);
        vec4 marker10 = texelFetch(Sampler0, ivec2(1, 0), 0);
        vec4 marker01 = texelFetch(Sampler0, ivec2(0, 1), 0);
        vec4 marker11 = texelFetch(Sampler0, ivec2(1, 1), 0);

        bool looksLikeSignature =
            // orange-like
            (marker00.r > marker00.g && marker00.g > marker00.b) &&
            // magenta-like
            (marker10.r > marker10.g && marker10.b > marker10.g) &&
            // cyan-like
            (marker01.g > marker01.r && marker01.b > marker01.r) &&
            // yellow-like
            (marker11.r > marker11.b && marker11.g > marker11.b);

        if (!looksLikeSignature) {
            gl_Position = projected;
            sphericalVertexDistance = fog_spherical_distance(Position);
            cylindricalVertexDistance = fog_cylindrical_distance(Position);
            vertexColor = Color * texelFetch(Sampler2, UV2 / 16, 0);
            texCoord0 = UV0;
            return;
        }

        isMinimap = 1;

        float aspect = max(ScreenSize.x / ScreenSize.y, 0.001);

        // Keep minimap square in physical pixels across all aspect ratios.
        float sideY = 0.56;
        float sideX = sideY / aspect;
        float marginY = 0.05;
        float marginX = marginY / aspect;
        float left;
        float right;
        float top;
        float bottom;

        int anchor = decodeAnchor(
            texelFetch(Sampler0, ivec2(2, 0), 0).rgb,
            texelFetch(Sampler0, ivec2(3, 0), 0).rgb,
            texelFetch(Sampler0, ivec2(4, 0), 0).rgb
        );
        if (anchor == 0) {
            right = 1.0 - marginX;
            left = right - sideX;
            top = 1.0 - marginY;
            bottom = top - sideY;
        } else if (anchor == 1) {
            right = 1.0 - marginX;
            left = right - sideX;
            top = sideY * 0.5;
            bottom = -sideY * 0.5;
        } else if (anchor == 2) {
            // Server info widget: slightly larger while preserving vanilla-style text pixels.
            float bottomRightSideY = 0.58;
            float bottomRightSideX = bottomRightSideY / aspect;
            float edgeMarginY = 0.015;
            float edgeMarginX = edgeMarginY / aspect;
            right = 1.0 - edgeMarginX;
            left = right - bottomRightSideX;
            bottom = -1.0 + edgeMarginY;
            top = bottom + bottomRightSideY;
        } else if (anchor == 3) {
            left = -1.0 + marginX;
            right = left + sideX;
            top = sideY * 0.5;
            bottom = -sideY * 0.5;
        } else if (anchor == 4) {
            float edgeMarginY = 0.004;
            top = 1.0 - edgeMarginY;
            bottom = top - sideY;
            left = -sideX * 0.5;
            right = sideX * 0.5;
        } else if (anchor == 6) {
            // Align stats strip directly under the top-right minimap with the same center.
            float gapY = 0.012;
            right = 1.0 - marginX;
            left = right - sideX;
            top = (1.0 - marginY) - sideY - gapY;
            bottom = top - sideY;
        } else {
            bottom = -1.0 + marginY;
            top = bottom + sideY;
            left = -sideX * 0.5;
            right = sideX * 0.5;
        }

        // Use UV-space mapping instead of gl_VertexID ordering so placement
        // remains stable regardless of driver/index ordering.
        float u = clamp(UV0.x, 0.0, 1.0);
        float v = clamp(UV0.y, 0.0, 1.0);
        vec2 hudPos = vec2(
            mix(left, right, u),
            mix(top, bottom, v)
        );

        // Detach from hand/item transforms and pin to HUD space.
        projected = vec4(hudPos, 0.0, 1.0);
    }

    gl_Position = projected;
    sphericalVertexDistance = fog_spherical_distance(Position);
    cylindricalVertexDistance = fog_cylindrical_distance(Position);
    vertexColor = Color * texelFetch(Sampler2, UV2 / 16, 0);
    texCoord0 = UV0;
}
