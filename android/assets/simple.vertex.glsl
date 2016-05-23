attribute vec3 a_position;
attribute vec2 a_texCoord0;

varying vec2 v_texCoord;
varying vec3 v_worldPos;
varying float v_linearDepth;

uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;

void main() {
    vec4 worldPos = u_worldTrans * vec4(a_position, 1.0);
    gl_Position = u_projViewTrans * worldPos;

    v_texCoord = a_texCoord0;
    v_worldPos = worldPos.xyz;

    //HIGH float linearDepth = ((worldPos.z + 0.15) / 30000.0) * 2.0 - 1.0;
    //v_linearDepth = linearDepth;
}