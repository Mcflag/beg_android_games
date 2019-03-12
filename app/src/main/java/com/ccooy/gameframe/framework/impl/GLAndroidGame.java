package com.ccooy.gameframe.framework.impl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.ccooy.gameframe.framework.Audio;
import com.ccooy.gameframe.framework.FileIO;
import com.ccooy.gameframe.framework.Game;
import com.ccooy.gameframe.framework.Graphics;
import com.ccooy.gameframe.framework.Input;
import com.ccooy.gameframe.framework.Screen;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class GLAndroidGame extends AppCompatActivity implements Game, GLSurfaceView.Renderer {

    enum GLGameState {
        Initialized,
        Running,
        Paused,
        Finished,
        Idle
    }

    GLSurfaceView glView;
    GLGraphics glGraphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    GLGameState state = GLGameState.Initialized;
    final Object stateChanged = new Object();
    long startTime = System.nanoTime();

    public static float[] mMVPMatrix = new float[16];
    public static float[] mProjectionMatrix = new float[16];
    public static float[] mViewMatrix = new float[16];
    public static float[] mTranslationMatrix = new float[16];

    private static final String TAG = "GLGame";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        glView = new GLSurfaceView(this);
        glView.setEGLContextClientVersion(2);
        glView.setRenderer(this);
        setContentView(glView);

        context = this;
        glGraphics = new GLGraphics(glView);
        fileIO = new AndroidFileIO(this);
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, glView, 1, 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        glView.onResume();
    }

    @Override
    public void onPause() {
        synchronized (stateChanged) {
            if (isFinishing())
                state = GLGameState.Finished;
            else
                state = GLGameState.Paused;
            while (true) {
                try {
                    stateChanged.wait();
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        glView.onPause();
        super.onPause();
    }

    public GLGraphics getGLGraphics() {
        return glGraphics;
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        throw new IllegalStateException("We are using OpenGL!");
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen newScreen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        newScreen.resume();
        newScreen.update(0);
        this.screen = newScreen;
    }

    @Override
    public Screen getCurrentScreen() {
        return screen;
    }

    @Override
    public Screen getStartScreen() {
        return null;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        glGraphics.clearScreen(0f, 0f, 0f, 1f);
        screen = getStartScreen();

        synchronized (stateChanged) {
            if (state == GLGameState.Initialized)
                screen = getStartScreen();
            state = GLGameState.Running;
            screen.resume();
            startTime = System.nanoTime();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        synchronized (stateChanged) {
            state = this.state;
        }

        if (state == GLGameState.Running) {
            float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
            startTime = System.nanoTime();

            screen.update(deltaTime);
            screen.present(deltaTime);
        }

        if (state == GLGameState.Paused) {
            screen.pause();
            synchronized (stateChanged) {
                this.state = GLGameState.Idle;
                stateChanged.notifyAll();
            }
        }

        if (state == GLGameState.Finished) {
            screen.pause();
            screen.dispose();
            synchronized (stateChanged) {
                this.state = GLGameState.Idle;
                stateChanged.notifyAll();
            }
        }

        float[] matrix = new float[16];
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        Matrix.setIdentityM(mTranslationMatrix,0);
        Matrix.multiplyMM(matrix, 0, mMVPMatrix, 0, mTranslationMatrix, 0);
        GLES20.glDisable(GLES20.GL_BLEND);
    }

    public void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}
