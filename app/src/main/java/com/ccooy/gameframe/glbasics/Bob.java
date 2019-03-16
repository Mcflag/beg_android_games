package com.ccooy.gameframe.glbasics;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ccooy.gameframe.framework.gl.Texture;
import com.ccooy.gameframe.framework.gl.Vertices;
import com.ccooy.gameframe.framework.impl.GLAndroidGame;
import com.ccooy.gameframe.framework.impl.GLGraphics;

import java.util.Random;

public class Bob {
    final Random rand = new Random();
    private float x, y;
    private float dirX, dirY;

    GLGraphics glGraphics;
    Texture bob;
    Vertices model;
    float squareCoords[] = {
            -0.1f, 0.1f, 0.0f, // top left
            -0.1f, -0.1f, 0.0f, // bottom left
            0.1f, -0.1f, 0.0f, // bottom right
            0.1f, 0.1f, 0.0f  // top right
    };

    short index[] = {
            0, 1, 2, 0, 2, 3
    };
    float color[] = {
            0.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
    };
    float texture[] = {
            0f, 1f,
            0f, 0f,
            1f, 0f,
            1f, 1f,
    };

    int mProgram;
    int mMatrixHandler;

    public Bob(GLAndroidGame game, Context context) {
        x = rand.nextFloat()*2-1;
        y = rand.nextFloat()*2-1;
        dirX = rand.nextFloat()*0.02f-0.01f;
        dirY = rand.nextFloat()*0.02f-0.01f;
        glGraphics = game.getGLGraphics();
        bob = new Texture(game, "basictest/bobrgb888.png");

        model = new Vertices(squareCoords.length, index.length, texture.length, color.length);
        model.setVertices(squareCoords, glGraphics.getCoordsPerVertex(), glGraphics.getVertexStride(), "aPosition");
        model.setColor(color, glGraphics.getColorPerVertex(), 0, Vertices.ColorType.ATTRIBUTE, "aColor");
        model.setIndices(index);
        model.setTexture(texture, glGraphics.getCoordsPerTexture(), glGraphics.getTextureStride(), "aTextureCoord");

        mProgram = glGraphics.getGLProgram(context.getResources(), "glbasictest/vshader/bob.glsl", "glbasictest/fshader/bob.glsl");
    }

    public void bobDraw(float[] mvpMatrix) {
        float[] matrix = new float[16];
        float[] mTranslationMatrix = new float[16];

        Matrix.setIdentityM(mTranslationMatrix, 0);
        Matrix.translateM(mTranslationMatrix, 0, x, y, 0);
        Matrix.multiplyMM(matrix, 0, mvpMatrix, 0, mTranslationMatrix, 0);

        GLES20.glUseProgram(mProgram);
        mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "uMatrix");
        GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, matrix, 0);

        bob.bind(GLES20.GL_TEXTURE0);

        model.draw(mProgram, GLES20.GL_TRIANGLES);
    }

    public void update(float deltaTime) {
        if (x < -1) {
            dirX = -dirX;
            x = -1f;
        }

        if (x > 1) {
            dirX = -dirX;
            x = 1f;
        }

        if (y < -2) {
            dirY = -dirY;
            y = -2f;
        }

        if (y > 2) {
            dirY = -dirY;
            y = 2f;
        }

        x = x + dirX;
        y = y + dirY;
    }
}
