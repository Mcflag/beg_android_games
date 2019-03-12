package com.ccooy.gameframe.glbasics;

import com.ccooy.gameframe.framework.Game;
import com.ccooy.gameframe.framework.Screen;
import com.ccooy.gameframe.framework.impl.GLAndroidGame;
import com.ccooy.gameframe.framework.impl.GLGraphics;

import java.util.Random;

public class GLGameTest extends GLAndroidGame {

    @Override
    public Screen getStartScreen() {
        return new TestScreen(this);
    }

    private class TestScreen extends Screen {
        GLGraphics glGraphics;
        Random rand = new Random();

        TestScreen(Game game) {
            super(game);
            glGraphics = ((GLAndroidGame) game).getGLGraphics();
        }

        @Override
        public void present(float deltaTime) {
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
