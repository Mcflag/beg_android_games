precision mediump float;
uniform sampler2D sTexture;
uniform float uScroll;
varying vec2 vTextureCoord;

void main() {
    gl_FragColor=texture2D(sTexture, vec2(vTextureCoord.x, vTextureCoord.y+uScroll));
}
