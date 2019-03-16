package com.ccooy.gameframe.glbasics;

import android.opengl.GLES20;

import com.ccooy.gameframe.framework.Screen;
import com.ccooy.gameframe.framework.gl.Vertices;
import com.ccooy.gameframe.framework.impl.GLAndroidGame;
import com.ccooy.gameframe.framework.impl.GLGraphics;

public class SquareTest extends GLAndroidGame {

    @Override
    public Screen getStartScreen() {
        return new SquareScreen(this);
    }

    private class SquareScreen extends Screen {
        GLGraphics glGraphics;
        Vertices model;
        float squareCoords[] = {
                -0.5f, 0.5f, 0.0f, // top left
                -0.5f, -0.5f, 0.0f, // bottom left
                0.5f, -0.5f, 0.0f, // bottom right
                0.5f, 0.5f, 0.0f  // top right
        };

        float color[] = {1f, 1f, 1f, 1f};
        short index[] = {
                0, 1, 2, 0, 2, 3
        };

        int mProgram;
        int mMatrixHandler;

        SquareScreen(GLAndroidGame game) {
            super(game);
            glGraphics = game.getGLGraphics();
            model = new Vertices(squareCoords.length, index.length, 0, color.length);
            model.setVertices(squareCoords, glGraphics.getCoordsPerVertex(), glGraphics.getVertexStride(), "aPosition");
            model.setIndices(index);
            model.setColor(color, glGraphics.getColorPerVertex(), 0, Vertices.ColorType.UNIFORM, "uColor");
            mProgram = glGraphics.getGLProgram(getApplicationContext().getResources(), "glbasictest/vshader/square.glsl", "glbasictest/fshader/square.glsl");
        }

        @Override
        public void update(float deltaTime) {
            game.getInput().getTouchEvents();
            game.getInput().getKeyEvents();
        }

        @Override
        public void present(float deltaTime) {
            GLES20.glUseProgram(mProgram);
            mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "uMatrix");
            GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0);

            model.draw(mProgram, GLES20.GL_TRIANGLES);
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
