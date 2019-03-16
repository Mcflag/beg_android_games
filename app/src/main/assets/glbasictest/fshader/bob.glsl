precision mediump float;
uniform sampler2D sTexture;
uniform float uScroll;
varying vec2 vTextureCoord;
uniform vec4 uColor;

void main() {
    gl_FragColor= texture2D(sTexture, vTextureCoord);
}
