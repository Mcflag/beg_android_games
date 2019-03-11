package com.ccooy.gameframe.glframework;

public interface GLGameInterface {
    public Input getInput();

    public FileIO getFileIO();

    public Graphics getGraphics();

    public Audio getAudio();

    public void setScreen(GLScreen screen);

    public GLScreen getCurrentScreen();

    public GLScreen getStartScreen();
}