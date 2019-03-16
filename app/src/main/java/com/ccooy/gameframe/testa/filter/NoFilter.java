package com.ccooy.gameframe.testa.filter;

import android.content.res.Resources;

public class NoFilter extends AFilter {

    public NoFilter(Resources res) {
        super(res);
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile("testa/shader/base_vertex.sh",
            "testa/shader/base_fragment.sh");
    }

    @Override
    protected void onSizeChanged(int width, int height) {

    }
}
