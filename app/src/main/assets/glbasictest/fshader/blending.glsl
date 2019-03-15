precision mediump float;
uniform sampler2D sTexture;
uniform float uScroll;
varying vec2 vTextureCoord;
varying vec4 vColor;

void main() {
    gl_FragColor=vColor + texture2D(sTexture, vec2(vTextureCoord.x, vTextureCoord.y+uScroll))*0.5;
}
