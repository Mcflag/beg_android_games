attribute vec4 aPosition;
uniform mat4 uMatrix;
attribute vec2 aTextureCoord;
varying vec2 vTextureCoord;

void main() {
    gl_Position=uMatrix*aPosition;
    vTextureCoord=aTextureCoord;
}
