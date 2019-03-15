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

    public Texture(GLAndroidGame glGame, String fileName) {
        this.fileIO = glGame.getFileIO();
        this.fileName = fileName;
        load();
    }

    private void load() {
        int[] textures = new int[1];
        InputStream imagestream = null;
        Bitmap bitmap = null;

        Matrix flip = new Matrix();
        flip.postScale(-1f, -1f);

        try {
            imagestream = fileIO.readAsset(fileName);
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

        setFilters(GLES20.GL_NEAREST, GLES20.GL_NEAREST);
        setWrap(GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        if (bitmap != null) {
            bitmap.recycle();
        }

        textureId = textures[0];
    }

    public void reload(int textureChannel) {
        load();
        bind(textureChannel);
        setFilters(GLES20.GL_NEAREST, GLES20.GL_NEAREST);
        setWrap(GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    public void setFilters(int minFilter, int magFilter) {
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, magFilter);
    }

    public void setWrap(int wrapS, int wrapT) {
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, wrapS);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, wrapT);
    }

    public void bind(int textureChannel) {
        GLES20.glActiveTexture(textureChannel);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
    }

    public void dispose() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        int[] textureIds = {textureId};
        GLES20.glDeleteTextures(1, textureIds, 0);
    }
}

