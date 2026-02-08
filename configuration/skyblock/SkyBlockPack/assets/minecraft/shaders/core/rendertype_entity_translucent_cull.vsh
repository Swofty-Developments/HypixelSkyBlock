#version 150

#moj_import <light.glsl>
#moj_import <fog.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV1;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler0;
uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec3 Light0_Direction;
uniform vec3 Light1_Direction;

out float vertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;
out vec2 texCoord1;
out vec4 normal;
flat out int isMinimap;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexDistance = fog_distance(Position, 0);
    vertexColor = Color * minecraft_mix_light(Light0_Direction, Light1_Direction, Normal, vec4(1.0));
    texCoord0 = UV0;
    texCoord1 = UV2;
    normal = ProjMat * ModelViewMat * vec4(Normal, 0.0);

    isMinimap = 0;

    ivec2 texSize = textureSize(Sampler0, 0);

    if (texSize.x == 128 && texSize.y == 128) {
        vec4 marker = texelFetch(Sampler0, ivec2(0, 0), 0);

        if (marker.r > 0.9 && marker.g < 0.1 && marker.b > 0.9) {
            isMinimap = 1;

            gl_Position.xy *= 0.25;
            gl_Position.x += 0.70;
            gl_Position.y += 0.70;
        }
    }
}
