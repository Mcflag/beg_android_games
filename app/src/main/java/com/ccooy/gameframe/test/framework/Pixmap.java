package com.ccooy.gameframe.test.framework;

import com.ccooy.gameframe.test.framework.Graphics.PixmapFormat;

public interface Pixmap {
    public int getWidth();

    public int getHeight();

    public PixmapFormat getFormat();

    public void dispose();
}
