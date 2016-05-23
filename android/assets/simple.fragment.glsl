#ifdef GL_ES
  #define LOWP lowp
  #define MED mediump
  #define HIGH highp
  precision mediump float;
#else
  #define MED
  #define LOWP
  #define HIGH
#endif

varying vec2 v_texCoord;
varying vec3 v_worldPos;
varying float v_linearDepth;

uniform sampler2D u_diffuseTexture;
uniform vec2 u_uvScale;
uniform vec3 u_cameraPos;

uniform float u_maxDrawDistance;
uniform float u_maxOpacityDistance;

void main() {
    vec2 scaledUV = v_texCoord * u_uvScale;

    vec4 diffuseColor = texture2D(u_diffuseTexture, scaledUV);
    gl_FragColor = diffuseColor.rgba;

    float fragmentToCameraDistance = length(u_cameraPos.xyz - v_worldPos);
    float fadeOutOpacity = 1.0 - smoothstep(u_maxOpacityDistance, u_maxDrawDistance, fragmentToCameraDistance);
    gl_FragColor.a *= fadeOutOpacity;

    //gl_FragDepth = v_linearDepth;
}
