package com.ccooy.gameframe.mrnom;

import android.graphics.Color;
import android.util.Log;

import com.ccooy.gameframe.framework.Game;
import com.ccooy.gameframe.framework.Graphics;
import com.ccooy.gameframe.framework.Input.TouchEvent;
import com.ccooy.gameframe.framework.Screen;

import java.util.List;

public class HelpScreen extends Screen {
    enum HelpState {
        HELP1,
        HELP2,
        HELP3
    }

    HelpState state = HelpState.HELP1;

    public HelpScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        if (state == HelpState.HELP1)
            updateHelp1(touchEvents);
        if (state == HelpState.HELP2)
            updateHelp2(touchEvents);
        if (state == HelpState.HELP3)
            updateHelp3(touchEvents);
    }

    private void updateHelp1(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x > 256 && event.y > 416) {
                    if (Settings.soundEnabled)
                        Assets.click.play(1);
                    state = HelpState.HELP2;
                    touchEvents.clear();
                    return;
                }
            }
        }
    }

    private void updateHelp2(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x > 256 && event.y > 416) {
                    if (Settings.soundEnabled)
                        Assets.click.play(1);
                    state = HelpState.HELP3;
                    touchEvents.clear();
                    return;
                }
            }
        }
    }

    private void updateHelp3(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x > 256 && event.y > 416) {
                    if (Settings.soundEnabled)
                        Assets.click.play(1);
                    game.setScreen(new MainMenuScreen(game));
                    touchEvents.clear();
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.background, 0, 0);

        if (state == HelpState.HELP1)
            g.drawPixmap(Assets.help1, 64, 100);
        if (state == HelpState.HELP2)
            g.drawPixmap(Assets.help2, 64, 100);
        if (state == HelpState.HELP3)
            g.drawPixmap(Assets.help3, 64, 100);
        g.drawPixmap(Assets.buttons, 256, 416, 0, 64, 64, 64);
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

