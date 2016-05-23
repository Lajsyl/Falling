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




varying HIGH vec2 v_texCoord;
varying HIGH float v_linearDepth;

uniform sampler2D u_diffuseTexture;
uniform HIGH vec2 u_uvScale;

void main() {

    vec2 scaledUV = v_texCoord * u_uvScale;

    vec4 diffuseColor = texture2D(u_diffuseTexture, scaledUV);
    gl_FragColor = diffuseColor.rgba;

    //gl_FragDepth = v_linearDepth;
}