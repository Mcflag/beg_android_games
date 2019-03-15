package com.ccooy.gameframe.framework.gl;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.ccooy.gameframe.framework.FileIO;
import com.ccooy.gameframe.framework.impl.GLAndroidGame;
import com.ccooy.gameframe.framework.impl.GLGraphics;

public class Texture {
    GLGraphics glGraphics;
    FileIO fileIO;
    String fileName;
    int textureId;
    int minFilter;
    int magFilter;
    int width;
    int height;

    public Texture(GLAndroidGame glGame, String fileName) {
        this.glGraphics = glGame.getGLGraphics();
        this.fileIO = glGame.getFileIO();
        this.fileName = fileName;
        load();
    }

    private void load() {
        int[] textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);
        textureId = textureIds[0];
        Matrix flip = new Matrix();
        flip.postScale(-1f, -1f);
        InputStream in = null;
        try {
            in = fileIO.readAsset(fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, flip, false);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            setFilters(GLES20.GL_NEAREST, GLES20.GL_NEAREST);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load texture '" + fileName + "'", e);
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                }
        }
    }

    public void reload() {
        load();
        bind();
        setFilters(minFilter, magFilter);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    public void setFilters(int minFilter, int magFilter) {
        this.minFilter = minFilter;
        this.magFilter = magFilter;
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, magFilter);
    }

    public void bind() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
    }

    public void dispose() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        int[] textureIds = {textureId};
        GLES20.glDeleteTextures(1, textureIds, 0);
    }
}

