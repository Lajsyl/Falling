attribute vec3 a_position;
attribute vec2 a_texCoord0;

varying vec2 v_texCoord;
varying float v_fadeOutOpacity;
//varying vec3 v_worldPos;
//varying float v_linearDepth;

uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;

uniform vec3 u_cameraPos;
uniform vec3 u_objectPos;
uniform float u_maxDrawDistance;
uniform float u_maxOpacityDistance;

void main() {
    vec4 worldPos = u_worldTrans * vec4(a_position, 1.0);
    gl_Position = u_projViewTrans * worldPos;

    v_texCoord = a_texCoord0;
    //v_worldPos = worldPos.xyz;

    float vertexToCameraDistance = length(u_cameraPos - u_objectPos);
    v_fadeOutOpacity = 1.0 - smoothstep(u_maxOpacityDistance, u_maxDrawDistance, vertexToCameraDistance);

    //HIGH float linearDepth = ((worldPos.z + 0.15) / 30000.0) * 2.0 - 1.0;
    //v_linearDepth = linearDepth;
}