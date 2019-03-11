package com.ccooy.gameframe.mrnom;

import com.ccooy.gameframe.framework.Game;
import com.ccooy.gameframe.framework.Graphics;
import com.ccooy.gameframe.framework.Screen;
import com.ccooy.gameframe.framework.Graphics.PixmapFormat;

public class LoadingScreen extends Screen {
    private final String assetDir = "mrnom/";

    public LoadingScreen(Game game) {
        super(game);
    }

    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        Assets.background = g.newPixmap(assetDir + "background.png", PixmapFormat.RGB565);
        Assets.logo = g.newPixmap(assetDir + "logo.png", PixmapFormat.ARGB4444);
        Assets.mainMenu = g.newPixmap(assetDir + "mainmenu.png", PixmapFormat.ARGB4444);
        Assets.buttons = g.newPixmap(assetDir + "buttons.png", PixmapFormat.ARGB4444);
        Assets.help1 = g.newPixmap(assetDir + "help1.png", PixmapFormat.ARGB4444);
        Assets.help2 = g.newPixmap(assetDir + "help2.png", PixmapFormat.ARGB4444);
        Assets.help3 = g.newPixmap(assetDir + "help3.png", PixmapFormat.ARGB4444);
        Assets.numbers = g.newPixmap(assetDir + "numbers.png", PixmapFormat.ARGB4444);
        Assets.ready = g.newPixmap(assetDir + "ready.png", PixmapFormat.ARGB4444);
        Assets.pause = g.newPixmap(assetDir + "pausemenu.png", PixmapFormat.ARGB4444);
        Assets.gameOver = g.newPixmap(assetDir + "gameover.png", PixmapFormat.ARGB4444);
        Assets.headUp = g.newPixmap(assetDir + "headup.png", PixmapFormat.ARGB4444);
        Assets.headLeft = g.newPixmap(assetDir + "headleft.png", PixmapFormat.ARGB4444);
        Assets.headDown = g.newPixmap(assetDir + "headdown.png", PixmapFormat.ARGB4444);
        Assets.headRight = g.newPixmap(assetDir + "headright.png", PixmapFormat.ARGB4444);
        Assets.tail = g.newPixmap(assetDir + "tail.png", PixmapFormat.ARGB4444);
        Assets.stain1 = g.newPixmap(assetDir + "stain1.png", PixmapFormat.ARGB4444);
        Assets.stain2 = g.newPixmap(assetDir + "stain2.png", PixmapFormat.ARGB4444);
        Assets.stain3 = g.newPixmap(assetDir + "stain3.png", PixmapFormat.ARGB4444);
        Assets.click = game.getAudio().newSound(assetDir + "click.ogg");
        Assets.eat = game.getAudio().newSound(assetDir + "eat.ogg");
        Assets.bitten = game.getAudio().newSound(assetDir + "bitten.ogg");
        Settings.load(game.getFileIO());
        game.setScreen(new MainMenuScreen(game));
    }

    public void present(float deltaTime) {

    }

    public void pause() {

    }

    public void resume() {

    }

    public void dispose() {

    }
}
