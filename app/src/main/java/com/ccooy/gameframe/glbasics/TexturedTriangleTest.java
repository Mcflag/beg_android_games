package com.ccooy.gameframe.glbasics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.ccooy.gameframe.framework.Screen;
import com.ccooy.gameframe.framework.impl.GLAndroidGame;
import com.ccooy.gameframe.framework.impl.GLGraphics;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class TexturedTriangleTest extends GLAndroidGame {

    float triangleCoords[] = {
            0.5f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };

    final short drawOrder[] = {0, 1, 2};
    float texture[] = {
            1f, 1f,
            0f, 0f,
            1f, 0f,
    };

    int mProgram;
    FloatBuffer vertexBuffer;
    FloatBuffer textureBuffer;
    ShortBuffer drawListBuffer;

    int mPositionHandle;
    int mTextureHandle;
    int mMatrixHandler;

    int coordsPerVertex;
    int coordsPerTexture;

    int vertexStride;
    int textureStride;
    int vertexCount;

    int textureId;

    @Override
    public Screen getStartScreen() {
        return new TexturedTriangleScreen(this);
    }

    private class TexturedTriangleScreen extends Screen {

        GLGraphics glGraphics;
        float timer = 0f;
        float scroll = 0f;

        TexturedTriangleScreen(GLAndroidGame game) {
            super(game);
            glGraphics = game.getGLGraphics();
            textureId = this.loadTexture("basictest/bobrgb888.png");

            ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
            bb.order(ByteOrder.nativeOrder());
            vertexBuffer = bb.asFloatBuffer();
            vertexBuffer.put(triangleCoords);
            vertexBuffer.position(0);

            ByteBuffer bbc = ByteBuffer.allocateDirect(texture.length * 4);
            bbc.order(ByteOrder.nativeOrder());
            textureBuffer = bbc.asFloatBuffer();
            textureBuffer.put(texture);
            textureBuffer.position(0);

            ByteBuffer bbo = ByteBuffer.allocateDirect(drawOrder.length * 4);
            bbo.order(ByteOrder.nativeOrder());
            drawListBuffer = bbo.asShortBuffer();
            drawListBuffer.put(drawOrder);
            drawListBuffer.position(0);

            coordsPerVertex = glGraphics.getCoordsPerVertex();
            vertexStride = glGraphics.getVertexStride();
            coordsPerTexture = glGraphics.getCoordsPerTexture();
            textureStride = glGraphics.getTextureStride();
            vertexCount = triangleCoords.length / coordsPerVertex;
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

            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
            GLES20.glEnableVertexAttribArray(mPositionHandle);
            GLES20.glVertexAttribPointer(mPositionHandle, coordsPerVertex, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

            mTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
            GLES20.glEnableVertexAttribArray(mTextureHandle);
            GLES20.glVertexAttribPointer(mTextureHandle, coordsPerTexture, GLES20.GL_FLOAT, false, textureStride, textureBuffer);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
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

            GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
            GLES20.glDisableVertexAttribArray(mPositionHandle);
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

        protected int loadTexture(String fileName) {
            int[] textures = new int[1];
            InputStream imagestream = null;
            Bitmap bitmap = null;

            Matrix flip = new Matrix();
            flip.postScale(-1f, -1f);

            try {
                imagestream = game.getFileIO().readAsset(fileName);
                bitmap = BitmapFactory.decodeStream(imagestream);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), flip, false);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (imagestream != null) {
                        imagestream.close();
                        imagestream = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            GLES20.glGenTextures(1, textures, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);

            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            if (bitmap != null) {
                bitmap.recycle();
            }

            return textures[0];
        }
    }
}
