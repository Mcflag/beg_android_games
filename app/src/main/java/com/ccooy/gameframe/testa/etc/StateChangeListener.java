package com.ccooy.gameframe.testa.etc;

/**
 * Description:
 */
public interface StateChangeListener {

    int START=1;
    int STOP=2;
    int PLAYING=3;
    int INIT=4;
    int PAUSE=5;
    int RESUME=6;

    void onStateChanged(int lastState, int nowState);

}
