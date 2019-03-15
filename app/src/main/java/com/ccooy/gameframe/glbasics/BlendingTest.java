package com.ccooy.gameframe.glbasics;

import android.opengl.GLES20;

import com.ccooy.gameframe.framework.Screen;
import com.ccooy.gameframe.framework.gl.Texture;
import com.ccooy.gameframe.framework.gl.Vertices;
import com.ccooy.gameframe.framework.impl.GLAndroidGame;
import com.ccooy.gameframe.framework.impl.GLGraphics;
import com.ccooy.gameframe.framework.utils.FPSCounter;

public class BlendingTest extends GLAndroidGame {

    @Override
    public Screen getStartScreen() {
        return new SquareScreen(this);
    }

    private class SquareScreen extends Screen {
        FPSCounter fpsCounter;
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
        float color[] = {
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
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

            model = new Vertices(squareCoords.length, index.length, texture.length, color.length);
            model.setVertices(squareCoords, glGraphics.getCoordsPerVertex(), glGraphics.getVertexStride(), "aPosition");
            model.setColor(color, glGraphics.getColorPerVertex(), 0, Vertices.ColorType.ATTRIBUTE, "aColor");
            model.setIndices(index);
            model.setTexture(texture, glGraphics.getCoordsPerTexture(), glGraphics.getTextureStride(), "aTextureCoord");

            mProgram = glGraphics.getGLProgram(getApplicationContext().getResources(), "glbasictest/vshader/blending.glsl", "glbasictest/fshader/blending.glsl");
            fpsCounter = new FPSCounter();
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

            int fTexture = GLES20.glGetUniformLocation(mProgram, "vTextureCoord");
            int fScroll = GLES20.glGetUniformLocation(mProgram, "uScroll");
            GLES20.glUniform1i(fTexture, 0);
            GLES20.glUniform1f(fScroll, 0);

            model.draw(mProgram, GLES20.GL_TRIANGLES);

            fpsCounter.logFrame();
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
