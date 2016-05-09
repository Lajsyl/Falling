#ifdef GL_ES
    precision mediump float;
#endif

varying vec2 v_texCoords;
varying vec3 v_fragWorldPosition;

uniform sampler2D u_texture;
uniform float u_maxDrawDistance;
uniform float u_maxOpacityDistance;
uniform vec3 u_cameraPosition;

void main()
{
    vec4 textureColor = texture2D(u_texture, fract(v_texCoords));

    float fragmentToCameraDistance = length(u_cameraPosition - v_fragWorldPosition);
    float alphaScale = smoothstep(u_maxOpacityDistance, u_maxDrawDistance, fragmentToCameraDistance);
    alphaScale = 1.0 - alphaScale;
    textureColor.a = textureColor.a * alphaScale;

    gl_FragColor = textureColor;
}
