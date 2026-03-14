# Shader & Resource Pack References

## Vanilla Shader References
- **Vanilla shader source (per-version)**: https://github.com/misode/mcmeta - use branch `<version>-assets` (e.g. `1.21.1-assets`) to get exact vanilla shaders at `assets/minecraft/shaders/`
- **Core shader list & documentation**: https://github.com/McTsts/Minecraft-Shaders-Wiki/blob/main/Core%20Shader%20List.md
- **Common shader patterns**: https://github.com/HalbFettKaese/common-shaders

## Map/Minimap Shader Implementations
- **JNNGL/VanillaMinimaps** (best reference for map-based minimap): https://github.com/JNNGL/VanillaMinimaps
  - Modifies `rendertype_text.vsh` and `rendertype_text.fsh`
  - Maps render via `rendertype_text`, NOT `rendertype_entity_translucent_cull`
  - Detects maps by checking `textureSize(Sampler0, 0) == ivec2(128, 128)`
  - Encodes data into map pixels, decoded in shader via `texelFetch`
  - Repositions map quad using `gl_VertexID % 4` to set NDC screen coordinates
  - Uses `ScreenSize` uniform for aspect ratio correction
  - Extracts player yaw from `ModelViewMat` for rotation: `transpose(mat3(ModelViewMat)) * vec3(1,0,0)`
- **bradleyq/shader-toolkit** (holographic minimap, different approach): https://github.com/bradleyq/shader-toolkit
  - Uses `GameTime` to alternate frames between normal and minimap render
  - Scales entire 3D scene down and offsets it - NOT map-based
  - Modifies multiple rendertype shaders (solid, cutout, translucent)

## Key Technical Notes

### Map rendering pipeline
- Map pixel data renders via `rendertype_text` shader
- Map background frame renders via entity shaders
- Map texture is always 128x128

### Shader detection for maps
```glsl
ivec2 texSize = textureSize(Sampler0, 0);
if (texSize == ivec2(128, 128)) {
    // This is a map face being rendered
}
```

### Marker pixel approach (server-assisted detection)
Encode a known color at pixel (0,0) of the map data, check in shader:
```glsl
vec4 marker = texelFetch(Sampler0, ivec2(0, 0), 0);
if (marker.rgb == someKnownColor) {
    // This is OUR minimap, not a regular map
}
```

### Vertex repositioning (HUD placement)
```glsl
int vid = gl_VertexID % 4;
// Quad vertices: 0=top-left, 1=bottom-left, 2=bottom-right, 3=top-right
// Set gl_Position in NDC (-1 to 1) to place on screen
```

### fog.glsl compatibility
- Vanilla 1.21.1 fog.glsl provides `fog_distance()` and `linear_fog()` functions
- Later versions (1.21.9+) restructured fog.glsl - may only declare uniforms
- Safest approach: implement fog inline to avoid version-specific fog.glsl conflicts
- Inline fog_distance: `(FogShape == 0) ? length(Position) : max(length(Position.xz), abs(Position.y))`
- Inline linear_fog: guard with `if (vertexDistance <= FogStart) return color;` then `smoothstep`

### Pack format versions
| MC Version | Resource Pack Format |
|------------|---------------------|
| 1.21.0-1.21.1 | 34 |
| 1.21.2 | 42 |
| 1.21.4 | 46 |
| 1.21.5 | 55 |
| 1.21.6 | 63 |
| 1.21.7-1.21.8 | 64 |
| 1.21.9-1.21.10 | 69 |
| 1.21.11 | 75 |

### pack.mcmeta schema change (important)
- Vanilla changed `pack.mcmeta` for newer versions: `min_format`/`max_format` are used instead of `pack_format`.
- In practice, `1.21.8` still uses `pack_format` + `supported_formats`, while `1.21.9+` uses `min_format` + `max_format`.
- If your generated pack uses a modern format (e.g. 69/75) but only writes `pack_format`, clients may reject the pack metadata.

## Pack format wiki
- https://minecraft.wiki/w/Pack_format
