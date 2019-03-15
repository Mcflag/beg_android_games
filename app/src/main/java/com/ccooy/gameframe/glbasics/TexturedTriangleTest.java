package com.ccooy.gameframe.glbasics;

import android.opengl.GLES20;

import com.ccooy.gameframe.framework.Screen;
import com.ccooy.gameframe.framework.gl.Texture;
import com.ccooy.gameframe.framework.gl.Vertices;
import com.ccooy.gameframe.framework.impl.GLAndroidGame;
import com.ccooy.gameframe.framework.impl.GLGraphics;

public class TexturedTriangleTest extends GLAndroidGame {

    @Override
    public Screen getStartScreen() {
        return new TexturedTriangleScreen(this);
    }

    private class TexturedTriangleScreen extends Screen {

        GLGraphics glGraphics;
        Vertices model;
        Texture bob;

        float triangleCoords[] = {
                0.5f, 0.5f, 0.0f, // top
                -0.5f, -0.5f, 0.0f, // bottom left
                0.5f, -0.5f, 0.0f  // bottom right
        };

        final short index[] = {0, 1, 2};
        float texture[] = {
                1f, 1f,
                0f, 0f,
                1f, 0f,
        };
        int mProgram;
        int mMatrixHandler;
        float timer = 0f;
        float scroll = 0f;

        TexturedTriangleScreen(GLAndroidGame game) {
            super(game);
            glGraphics = game.getGLGraphics();
            bob = new Texture(game, "basictest/bobrgb888.png");

            model = new Vertices(triangleCoords.length, index.length, texture.length, 0);
            model.setVertices(triangleCoords, glGraphics.getCoordsPerVertex(), glGraphics.getVertexStride(), "aPosition");
            model.setIndices(index);
            model.setTexture(texture, glGraphics.getCoordsPerTexture(), glGraphics.getTextureStride(), "aTextureCoord");

            mProgram = glGraphics.getGLProgram(getApplicationContext().getResources(), "glbasictest/vshader/texturetriangle.glsl", "glbasictest/fshader/texturetriangle.glsl");

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
            int fTexture = GLES20.glGetUniformLocation(mProgram, "vTextureCoord");
            int fScroll = GLES20.glGetUniformLocation(mProgram, "uScroll");
            GLES20.glUniform1i(fTexture, 0);

            timer = timer + deltaTime;
            if (timer > 0.001) {
                scroll += 0.01;
                if (scroll >= 1) {
                    scroll = 0;
                }
                timer = 0;
            }
            GLES20.glUniform1f(fScroll, scroll);

            model.draw(mProgram, GLES20.GL_TRIANGLES);
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
