package com.ccooy.gameframe.testa.image;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.ccooy.gameframe.testa.image.filter.AFilter;

import java.io.IOException;

public class SGLView extends GLSurfaceView {

    private SGLRender render;

    public SGLView(Context context) {
        this(context,null);
    }

    public SGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setEGLContextClientVersion(2);
        render=new SGLRender(this);
        setRenderer(render);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        try {
            render.setImage(BitmapFactory.decodeStream(getResources().getAssets().open("testa/texture/fengj.png")));
            requestRender();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SGLRender getRender(){
        return render;
    }

    public void setFilter(AFilter filter){
        render.setFilter(filter);
    }

}
