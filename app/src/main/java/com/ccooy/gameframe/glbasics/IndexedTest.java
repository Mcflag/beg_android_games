package com.ccooy.gameframe.glbasics;

import android.opengl.GLES20;

import com.ccooy.gameframe.framework.Screen;
import com.ccooy.gameframe.framework.gl.Texture;
import com.ccooy.gameframe.framework.gl.Vertices;
import com.ccooy.gameframe.framework.impl.GLAndroidGame;
import com.ccooy.gameframe.framework.impl.GLGraphics;

public class IndexedTest extends GLAndroidGame {

    @Override
    public Screen getStartScreen() {
        return new SquareScreen(this);
    }

    private class SquareScreen extends Screen {
        GLGraphics glGraphics;
        Texture bob;
        Vertices model;
        float squareCoords[] = {
                -0.5f, 0.5f, 0.0f, // top left
                -0.5f, -0.5f, 0.0f, // bottom left
                0.5f, -0.5f, 0.0f, // bottom right
                0.5f, 0.5f, 0.0f  // top right
        };

        short index[] = {
                0, 1, 2, 0, 2, 3
        };
        float texture[] = {
                0f, 1f,
                0f, 0f,
                1f, 0f,
                1f, 1f,
        };

        int mProgram;
        int mMatrixHandler;

        SquareScreen(GLAndroidGame game) {
            super(game);
            glGraphics = game.getGLGraphics();
            bob = new Texture(game, "basictest/bobrgb888.png");

            model = new Vertices(squareCoords.length, index.length, texture.length, 0);
            model.setVertices(squareCoords, glGraphics.getCoordsPerVertex(), glGraphics.getVertexStride(), "aPosition");
            model.setIndices(index);
            model.setTexture(texture, glGraphics.getCoordsPerTexture(), glGraphics.getTextureStride(), "aTextureCoord");
            mProgram = glGraphics.getGLProgram(getApplicationContext().getResources(), "glbasictest/vshader/index.glsl", "glbasictest/fshader/index.glsl");
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

            bob.bind(GLES20.GL_TEXTURE0);

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
