package com.ccooy.gameframe.glbasics;

import java.util.Random;

import android.opengl.GLES20;

import com.ccooy.gameframe.framework.impl.GLGame;

public class GLGameTest extends GLGame {
    public GLScreen getStartScreen() {
        return new TestScreen(this);
    }

    class TestScreen extends GLScreen {
        GLGraphics glGraphics;
        Random rand = new Random();

        public TestScreen(GLGameInterface game) {
            super(game);
            glGraphics = ((GLGame) game).getGLGraphics();
        }

        @Override
        public void present(float[] matrix) {
            GLES20 gl = glGraphics.getGL();
            glGraphics.clearScreen(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1);
        }

        @Override
        public void update(float deltaTime) {
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

