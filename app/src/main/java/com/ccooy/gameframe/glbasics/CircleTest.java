package com.ccooy.gameframe.glbasics;

import android.opengl.GLES20;

import com.ccooy.gameframe.framework.Screen;
import com.ccooy.gameframe.framework.gl.Texture;
import com.ccooy.gameframe.framework.gl.Vertices;
import com.ccooy.gameframe.framework.impl.GLAndroidGame;
import com.ccooy.gameframe.framework.impl.GLGraphics;

import java.util.ArrayList;

public class CircleTest extends GLAndroidGame {

    @Override
    public Screen getStartScreen() {
        return new CircleScreen(this);
    }

    private class CircleScreen extends Screen {
        GLGraphics glGraphics;
        Vertices model;
        Texture bob;
        private float[] circlePos;
        private float height = 0.0f;
        private float radius = 1.0f;
        private float textureRadius = 0.5f;
        private int n = 360;  //切割份数

        private float[] texture;
        int mProgram;
        int mMatrixHandler;

        CircleScreen(GLAndroidGame game) {
            super(game);
            glGraphics = game.getGLGraphics();
            bob = new Texture(game, "basictest/bobrgb888.png");
            circlePos = createPositions();
            texture = createTexture();

            model = new Vertices(circlePos.length, 0, texture.length, 0);
            model.setVertices(circlePos, glGraphics.getCoordsPerVertex(), glGraphics.getVertexStride(), "aPosition");
            model.setTexture(texture, glGraphics.getCoordsPerTexture(), glGraphics.getTextureStride(), "aTextureCoord");

            mProgram = glGraphics.getGLProgram(getApplicationContext().getResources(), "glbasictest/vshader/oval.glsl", "glbasictest/fshader/oval.glsl");
        }

        @Override
        public void update(float deltaTime) {
            game.getInput().getTouchEvents();
            game.getInput().getKeyEvents();
        }

        @Override
        public void present(float deltaTime) {
            GLES20.glUseProgram(mProgram);
            mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "uMatrix");
            GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0);

            bob.bind(GLES20.GL_TEXTURE0);
            model.draw(mProgram, GLES20.GL_TRIANGLE_FAN);
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

        private float[] createPositions() {
            ArrayList<Float> data = new ArrayList<>();
            data.add(0.0f);
            data.add(0.0f);
            data.add(height);
            float angleSpan = 360.0f / n;
            for (int i = 0; i < 360 + angleSpan; i += angleSpan) {
                data.add((float) (radius * Math.sin(i * Math.PI / 180f)));
                data.add((float) (radius * Math.cos(i * Math.PI / 180f)));
                data.add(height);
            }
            float[] f = new float[data.size()];
            for (int i = 0; i < f.length; i++) {
                f[i] = data.get(i);
            }
            return f;
        }

        private float[] createTexture() {
            ArrayList<Float> data = new ArrayList<>();
            data.add(0.5f);
            data.add(0.5f);
            float angleSpan = 360.0f / n;
            for (int i = 0; i < 360 + angleSpan; i += angleSpan) {
                data.add(0.5f + (float) (textureRadius * Math.sin(i * Math.PI / 180f)));
                data.add(0.5f + (float) (textureRadius * Math.cos(i * Math.PI / 180f)));
            }
            float[] f = new float[data.size()];
            for (int i = 0; i < f.length; i++) {
                f[i] = data.get(i);
            }
            return f;
        }
    }
}
