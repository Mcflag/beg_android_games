package com.ccooy.gameframe.glbasics;

import android.opengl.GLES20;

import com.ccooy.gameframe.framework.Screen;
import com.ccooy.gameframe.framework.impl.GLAndroidGame;
import com.ccooy.gameframe.framework.impl.GLGraphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class ColoredTriangleTest extends GLAndroidGame {

    float triangleCoords[] = {
            0.5f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };
    float color[] = {
            0.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f
    };

    int mProgram;
    FloatBuffer vertexBuffer;
    FloatBuffer colorBuffer;

    int mPositionHandle;
    int mColorHandle;
    int mMatrixHandler;

    int coordsPerVertex;
    int colorPerVertex;
    int vertexStride;
    int vertexCount;

    @Override
    public Screen getStartScreen() {
        return new ColoredTriangleScreen(this);
    }

    private class ColoredTriangleScreen extends Screen {

        GLGraphics glGraphics;

        ColoredTriangleScreen(GLAndroidGame game) {
            super(game);
            glGraphics = game.getGLGraphics();
        }

        @Override
        public void update(float deltaTime) {
            game.getInput().getTouchEvents();
            game.getInput().getKeyEvents();
        }

        @Override
        public void present(float deltaTime) {
            ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
            bb.order(ByteOrder.nativeOrder());
            vertexBuffer = bb.asFloatBuffer();
            vertexBuffer.put(triangleCoords);
            vertexBuffer.position(0);

            ByteBuffer bbc = ByteBuffer.allocateDirect(color.length * 4);
            bbc.order(ByteOrder.nativeOrder());
            colorBuffer = bbc.asFloatBuffer();
            colorBuffer.put(color);
            colorBuffer.position(0);

            coordsPerVertex = glGraphics.getCoordsPerVertex();
            colorPerVertex = glGraphics.getColorPerVertex();
            vertexStride = glGraphics.getVertexStride();
            vertexCount = triangleCoords.length / coordsPerVertex;
            mProgram = glGraphics.getGLProgram(getApplicationContext().getResources(), "glbasictest/vshader/coloredtriangle.glsl", "glbasictest/fshader/coloredtriangle.glsl");

            GLES20.glUseProgram(mProgram);
            mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "uMatrix");
            GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0);

            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
            GLES20.glEnableVertexAttribArray(mPositionHandle);
            GLES20.glVertexAttribPointer(mPositionHandle, coordsPerVertex, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

            mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
            GLES20.glEnableVertexAttribArray(mColorHandle);
            GLES20.glVertexAttribPointer(mColorHandle, colorPerVertex, GLES20.GL_FLOAT, false, 0, colorBuffer);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
            GLES20.glDisableVertexAttribArray(mPositionHandle);
        }

        @Override
        public void pause() {

        }

        @Override
        public void resume() {

        }

        @Override
        public void dispose() {

        }
    }
}
