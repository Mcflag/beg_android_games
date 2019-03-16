package com.ccooy.gameframe.testa.filter;

import android.content.res.Resources;

public class GrayFilter extends AFilter {

    public GrayFilter(Resources mRes) {
        super(mRes);
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile("testa/shader/base_vertex.sh",
            "testa/shader/color/gray_fragment.frag");
    }

    @Override
    protected void onSizeChanged(int width, int height) {

    }
}
