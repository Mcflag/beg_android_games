package com.ccooy.gameframe.framework.gl;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Vertices {
    public enum ColorType {
        UNIFORM, ATTRIBUTE
    }

    FloatBuffer vertexBuffer;
    FloatBuffer textureBuffer;
    ShortBuffer indexBuffer;
    FloatBuffer colorBuffer;
    float[] color;

    private int mPositionHandle;
    private int mTextureHandle;
    private int mColorHandle;

    private int vertexCount;
    private int indexCount;

    private int coordsPerVertex;
    private int vertexStride;

    private int coordsPerTexture;
    private int textureStride;

    private int colorPerVertex;
    private int colorStride;

    private String shaderVertexName;
    private String shaderTextureName;
    private String shaderColorName;

    private ColorType colorType = ColorType.UNIFORM;

    private boolean hasIndices = false;
    private boolean hasTexture = false;
    private boolean hasColor = false;

    public Vertices(int maxVertex, int maxIndices, int maxTexture, int maxColor) {
        ByteBuffer bb = ByteBuffer.allocateDirect(maxVertex * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();

        indexCount = maxIndices;
        if (maxIndices > 0) {
            hasIndices = true;
            ByteBuffer bbi = ByteBuffer.allocateDirect(maxIndices * 2);
            bbi.order(ByteOrder.nativeOrder());
            indexBuffer = bbi.asShortBuffer();
        } else {
            indexBuffer = null;
        }

        if (maxTexture > 0) {
            hasTexture = true;
            ByteBuffer bbt = ByteBuffer.allocateDirect(maxTexture * 4);
            bbt.order(ByteOrder.nativeOrder());
            textureBuffer = bbt.asFloatBuffer();
        } else {
            textureBuffer = null;
        }

        if (maxColor > 0) {
            hasColor = true;
            ByteBuffer bbc = ByteBuffer.allocateDirect(maxColor * 4);
            bbc.order(ByteOrder.nativeOrder());
            colorBuffer = bbc.asFloatBuffer();
        } else {
            colorBuffer = null;
        }
    }

    public void setVertices(float[] vertices, int coordsPerVertex, int vertexStride, String shaderVertexName) {
        vertexBuffer.clear();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        this.coordsPerVertex = coordsPerVertex;
        this.vertexStride = vertexStride;
        this.vertexCount = vertices.length / coordsPerVertex;
        this.shaderVertexName = shaderVertexName;
    }

    public void setTexture(float[] textures, int coordsPerTexture, int textureStride, String shaderTextureName) {
        textureBuffer.clear();
        textureBuffer.put(textures);
        textureBuffer.position(0);
        this.coordsPerTexture = coordsPerTexture;
        this.textureStride = textureStride;
        this.shaderTextureName = shaderTextureName;
    }

    public void setIndices(short[] indices) {
        indexBuffer.clear();
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    public void setColor(float[] color, int colorPerVertex, int colorStride, ColorType colorType, String shaderColorName) {
        colorBuffer.clear();
        colorBuffer.put(color);
        colorBuffer.position(0);
        this.colorPerVertex = colorPerVertex;
        this.colorStride = colorStride;
        this.shaderColorName = shaderColorName;
        this.colorType = colorType;
        this.color = color;
    }

    public void draw(int program, int drawMode) {
        mPositionHandle = GLES20.glGetAttribLocation(program, shaderVertexName);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, coordsPerVertex, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        if (hasColor) {
            switch (colorType) {
                case UNIFORM:
                    mColorHandle = GLES20.glGetUniformLocation(program, shaderColorName);
                    GLES20.glUniform4fv(mColorHandle, 1, color, 0);
                    break;
                case ATTRIBUTE:
                    mColorHandle = GLES20.glGetAttribLocation(program, shaderColorName);
                    GLES20.glEnableVertexAttribArray(mColorHandle);
                    GLES20.glVertexAttribPointer(mColorHandle, colorPerVertex, GLES20.GL_FLOAT, false, colorStride, colorBuffer);
                    break;
                default:
                    break;
            }
        }

        if (hasTexture) {
            mTextureHandle = GLES20.glGetAttribLocation(program, shaderTextureName);
            GLES20.glEnableVertexAttribArray(mTextureHandle);
            GLES20.glVertexAttribPointer(mTextureHandle, coordsPerTexture, GLES20.GL_FLOAT, false, textureStride, textureBuffer);
        }

        if (indexCount == 0) {
            GLES20.glDrawArrays(drawMode, 0, vertexCount);
        } else if (indexCount > 0) {
            GLES20.glDrawElements(drawMode, indexCount, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        }

        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
