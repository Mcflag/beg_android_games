package com.ccooy.gameframe.glbasics;

import android.opengl.GLES20;

import com.ccooy.gameframe.framework.Screen;
import com.ccooy.gameframe.framework.impl.GLAndroidGame;
import com.ccooy.gameframe.framework.impl.GLGraphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class TransTriangleTest extends GLAndroidGame {

    int mProgram;
    int mPositionHandle;
    int coordsPerVertex;
    int vertexStride;
    int mColorHandle;
    int mMatrixHandler;
    float color[] = {1f, 1f, 1f, 1f};
    FloatBuffer vertexBuffer;
    float triangleCoords[] = {
            0.5f,  0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };

    @Override
    public Screen getStartScreen() {
        return new TransTriangleScreen(this);
    }

    private class TransTriangleScreen extends Screen {
        GLGraphics glGraphics;

        TransTriangleScreen(GLAndroidGame game) {
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

            coordsPerVertex = glGraphics.getCoordsPerVertex();
            vertexStride = glGraphics.getVertexStride();
            mProgram = glGraphics.getGLProgram(getApplicationContext().getResources(), "glbasictest/vshader/transtriangle.glsl", "glbasictest/fshader/transtriangle.glsl");

            final int vertexCount = triangleCoords.length / coordsPerVertex;
            GLES20.glUseProgram(mProgram);
            mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "vMatrix");
            GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0);

            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
            GLES20.glEnableVertexAttribArray(mPositionHandle);
            GLES20.glVertexAttribPointer(mPositionHandle, coordsPerVertex, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
            GLES20.glUniform4fv(mColorHandle, 1, color, 0);
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
