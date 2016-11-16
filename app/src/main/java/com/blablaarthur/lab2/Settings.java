package com.blablaarthur.lab2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    Switch sw;
    TextView tw;
    TextView sizeText;
    int textSize;
    int theme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        theme = sharedPref.getInt("Theme", 1);
        textSize = sharedPref.getInt("TextSize", 1);
        if(theme == 0){
            switch (textSize){
                case 0:
                    setTheme(R.style.MyDarkTheme_SmallText);
                    break;
                case 1:
                    setTheme(R.style.MyDarkTheme);
                    break;
                case 2:
                    setTheme(R.style.MyDarkTheme_LargeText);
                    break;
            }
        }
        else{
            switch (textSize){
                case 0:
                    setTheme(R.style.AppTheme_SmallText);
                    break;
                case 1:
                    setTheme(R.style.AppTheme_NormalText);
                    break;
                case 2:
                    setTheme(R.style.AppTheme_LargeText);
                    break;
            }
        }
        setContentView(R.layout.activity_settings);

        Log.d("A_R_T", String.valueOf(theme));

        sw = (Switch) findViewById(R.id.switchNight);
        sizeText = (TextView) findViewById(R.id.sizeText);
        tw = (TextView) findViewById(R.id.textView);
        switch (textSize){
            case 0:
                sizeText.setText("Small");
                break;
            case 1:
                sizeText.setText("Normal");
                break;
            case 2:
                sizeText.setText("Large");
                break;
        }

        if(theme == 1){
            sw.setChecked(false);
            sw.setText(R.string.night_mode_off);
            sw.setTextColor(getResources().getColor(R.color.darkActionBar));
            tw.setTextColor(getResources().getColor(R.color.darkActionBar));
            sizeText.setTextColor(getResources().getColor(R.color.darkActionBar));
        }
        else{
            sw.setChecked(true);
            sw.setText(R.string.night_mode_on);
            sw.setTextColor(getResources().getColor(R.color.white));
            tw.setTextColor(getResources().getColor(R.color.white));
            sizeText.setTextColor(getResources().getColor(R.color.white));
        }
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences sharedPref = getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("Theme", 0);
                    editor.apply();
                    recreate();
                }
                else {
                    SharedPreferences sharedPref = getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("Theme", 1);
                    editor.apply();
                    recreate();
                }
            }
        });
    }

    public void selectSize(View v){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(R.string.importance);
        adb.setSingleChoiceItems(new CharSequence[]{getString(R.string.small_text_size), getString(R.string.normal_text_size) ,getString(R.string.large_text_size)}, textSize, selectSize);
        adb.setPositiveButton(R.string.tofilter, selectSize);
        adb.setNegativeButton(R.string.cancel, selectSize);
        adb.create();
        adb.show();
    }

    DialogInterface.OnClickListener selectSize = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            ListView lv = ((AlertDialog) dialog).getListView();
            if(which == Dialog.BUTTON_POSITIVE){
                textSize = lv.getCheckedItemPosition();
                if(theme == 0){
                    switch (textSize){
                        case 0:
                            setTheme(R.style.MyDarkTheme_SmallText);
                            break;
                        case 1:
                            setTheme(R.style.MyDarkTheme);
                            break;
                        case 2:
                            setTheme(R.style.MyDarkTheme_LargeText);
                            break;
                    }
                }
                else{
                    switch (textSize){
                        case 0:
                            setTheme(R.style.AppTheme_SmallText);
                            break;
                        case 1:
                            setTheme(R.style.AppTheme_NormalText);
                            break;
                        case 2:
                            setTheme(R.style.AppTheme_LargeText);
                            break;
                    }
                }
                switch (textSize){
                    case 0:
                        sizeText.setText("Small");
                        break;
                    case 1:
                        sizeText.setText("Normal");
                        break;
                    case 2:
                        sizeText.setText("Large");
                        break;
                }
                SharedPreferences sharedPref = getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("Theme", theme);
                editor.putInt("TextSize", textSize);
                editor.apply();
                recreate();
            }
        }
    };
}
