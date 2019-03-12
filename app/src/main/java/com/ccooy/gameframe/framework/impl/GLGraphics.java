package com.ccooy.gameframe.framework.impl;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.ccooy.gameframe.framework.utils.ShaderUtils;

public class GLGraphics {
    private final int COORDS_PER_VERTEX = 3;
    private final int COORDS_PER_TEXTURE = 2;
    private final int vertexStride = COORDS_PER_VERTEX * 4;
    private final int textureStride = COORDS_PER_TEXTURE * 4;

    private GLSurfaceView glView;
    private String vertexShaderCode = "";
    private String fragmentShaderCode = "";

    public GLGraphics(GLSurfaceView glView) {
        this.glView = glView;
    }

    public int getWidth() {
        return glView.getWidth();
    }

    public int getHeight() {
        return glView.getHeight();
    }

    public int getCoordsPerVertex() {
        return COORDS_PER_VERTEX;
    }

    public int getCoordsPerTexture() {
        return COORDS_PER_TEXTURE;
    }

    public int getVertexStride() {
        return vertexStride;
    }

    public int getTextureStride() {
        return textureStride;
    }

    public void clearScreen(float red, float green, float blue, float alpha) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(red, green, blue, alpha);
    }

    public int getGLProgram() {
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, vertexShaderCode);
        GLES20.glCompileShader(vertexShader);

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, fragmentShaderCode);
        GLES20.glCompileShader(fragmentShader);

        int mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

        return mProgram;
    }

    public int getGLProgram(String vertexCode, String fragmentCode) {
        vertexShaderCode = vertexCode;
        fragmentShaderCode = fragmentCode;

        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, vertexCode);
        GLES20.glCompileShader(vertexShader);

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, fragmentCode);
        GLES20.glCompileShader(fragmentShader);

        int mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

        return mProgram;
    }

    public int getGLProgram(Resources res, String vertexRes, String fragmentRes) {
        return ShaderUtils.createProgram(res, vertexRes, fragmentRes);
    }

    public void setVertexShaderCode(String shaderCode) {
        this.vertexShaderCode = shaderCode;
    }

    public void setFragmentShaderCode(String shaderCode) {
        this.fragmentShaderCode = shaderCode;
    }

}
