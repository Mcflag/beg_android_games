package com.ccooy.gameframe.testa.camera;

import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.widget.SeekBar;
import com.ccooy.gameframe.R;
import com.ccooy.gameframe.testa.filter.Beauty;
import com.ccooy.gameframe.testa.filter.LookupFilter;

public class Camera3Activity extends Camera2Activity {

    private AppCompatSeekBar mSeek;
    private LookupFilter mLookupFilter;
    private Beauty mBeautyFilter;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_camera3);
        mSeek=(AppCompatSeekBar) findViewById(R.id.mSeek);
        mSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("cccc","process:"+progress);
                mLookupFilter.setIntensity(progress/100f);
                mBeautyFilter.setFlag(progress/20+1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onFilterSet(TextureController controller) {
        mLookupFilter=new LookupFilter(getResources());
        mLookupFilter.setMaskImage("testa/lookup/purity.png");
        mLookupFilter.setIntensity(0.0f);
        controller.addFilter(mLookupFilter);
        mBeautyFilter=new Beauty(getResources());
        controller.addFilter(mBeautyFilter);
    }
}
