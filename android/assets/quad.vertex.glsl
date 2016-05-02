attribute vec4 a_position;
attribute vec2 a_texCoord0;

uniform mat4 u_modelMatrix;
uniform mat4 u_viewProjectionMatrix;
uniform float u_uScale;
uniform float u_vScale;

varying vec2 v_texCoords;
varying vec3 v_fragWorldPosition;

void main()
{
    v_texCoords = a_texCoord0 * vec2(u_uScale, u_vScale);

    vec4 worldPosition = u_modelMatrix * a_position;
    gl_Position = u_viewProjectionMatrix * worldPosition;

    v_fragWorldPosition = worldPosition.xyz;
}
