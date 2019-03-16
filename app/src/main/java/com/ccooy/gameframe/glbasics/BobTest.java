package com.ccooy.gameframe.glbasics;

import com.ccooy.gameframe.framework.Screen;
import com.ccooy.gameframe.framework.impl.GLAndroidGame;
import com.ccooy.gameframe.framework.impl.GLGraphics;
import com.ccooy.gameframe.framework.utils.FPSCounter;

public class BobTest extends GLAndroidGame {

    public Screen getStartScreen() {
        return new BobScreen(this);
    }

    class BobScreen extends Screen {
        static final int NUM_BOBS = 100;
        GLGraphics glGraphics;
        FPSCounter fpsCounter;
        Bob[] bobs;

        public BobScreen(GLAndroidGame game) {
            super(game);
            glGraphics = (game).getGLGraphics();

            bobs = new Bob[NUM_BOBS];
            for (int i = 0; i < NUM_BOBS; i++) {
                bobs[i] = new Bob(game, getApplicationContext());
            }
            fpsCounter=new FPSCounter();
        }

        @Override
        public void update(float deltaTime) {
            game.getInput().getTouchEvents();
            game.getInput().getKeyEvents();
            for (int i = 0; i < NUM_BOBS; i++) {
                bobs[i].update(deltaTime);
            }
        }

        @Override
        public void present(float deltaTime) {
            for (int i = 0; i < NUM_BOBS; i++) {
                bobs[i].bobDraw(mMVPMatrix);
            }
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
