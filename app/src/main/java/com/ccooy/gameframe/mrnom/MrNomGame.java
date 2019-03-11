package com.ccooy.gameframe.mrnom;

import com.ccooy.gameframe.framework.Screen;
import com.ccooy.gameframe.framework.impl.AndroidGame;

public class MrNomGame extends AndroidGame {

    public Screen getStartScreen() {
        return new LoadingScreen(this);
    }

}
