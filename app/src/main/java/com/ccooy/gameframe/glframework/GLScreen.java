package com.ccooy.gameframe.glframework;

public abstract class GLScreen {
    protected final GLGameInterface game;

    public GLScreen(GLGameInterface game) {
        this.game = game;
    }

    public abstract void update(float deltaTime);

    public abstract void present(float[] matrix);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();
}
