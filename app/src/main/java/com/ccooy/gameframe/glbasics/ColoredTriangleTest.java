package com.ccooy.gameframe.glbasics;

import android.opengl.GLES20;

import com.ccooy.gameframe.framework.Screen;
import com.ccooy.gameframe.framework.gl.Vertices;
import com.ccooy.gameframe.framework.impl.GLAndroidGame;
import com.ccooy.gameframe.framework.impl.GLGraphics;

public class ColoredTriangleTest extends GLAndroidGame {

    @Override
    public Screen getStartScreen() {
        return new ColoredTriangleScreen(this);
    }

    private class ColoredTriangleScreen extends Screen {

        GLGraphics glGraphics;
        Vertices model;
        int mProgram;
        float color[] = {
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f
        };
        float triangleCoords[] = {
                0.5f, 0.5f, 0.0f, // top
                -0.5f, -0.5f, 0.0f, // bottom left
                0.5f, -0.5f, 0.0f  // bottom right
        };
        int mMatrixHandler;

        ColoredTriangleScreen(GLAndroidGame game) {
            super(game);
            glGraphics = game.getGLGraphics();
            model = new Vertices(triangleCoords.length, 0, 0, color.length);
            model.setVertices(triangleCoords, glGraphics.getCoordsPerVertex(), glGraphics.getVertexStride(), "aPosition");
            model.setColor(color, glGraphics.getColorPerVertex(), 0, Vertices.ColorType.ATTRIBUTE, "aColor");
            mProgram = glGraphics.getGLProgram(getApplicationContext().getResources(), "glbasictest/vshader/coloredtriangle.glsl", "glbasictest/fshader/coloredtriangle.glsl");
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
