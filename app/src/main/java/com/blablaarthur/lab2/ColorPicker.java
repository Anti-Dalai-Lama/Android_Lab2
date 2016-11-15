package com.blablaarthur.lab2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

/**
 * Created by Артур on 04.10.2016.
 */

public class ColorPicker  extends AppCompatActivity {

    RelativeLayout colorArea;
    SeekBar r;
    SeekBar g;
    SeekBar b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_picker);

        colorArea = (RelativeLayout) findViewById(R.id.color_area);
        r = (SeekBar) findViewById(R.id.seekBarR);
        g = (SeekBar) findViewById(R.id.seekBarG);
        b = (SeekBar) findViewById(R.id.seekBarB);

        r.setOnSeekBarChangeListener(seekBarChangeListener);
        g.setOnSeekBarChangeListener(seekBarChangeListener);
        b.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    updateBackground();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }
            };

    private void updateBackground() {
        int rnum = r.getProgress();
        int gnum = g.getProgress();
        int bnum = b.getProgress();

        colorArea.setBackgroundColor(0xff000000 + rnum * 0x10000 + gnum * 0x100
                + bnum);
    }
}