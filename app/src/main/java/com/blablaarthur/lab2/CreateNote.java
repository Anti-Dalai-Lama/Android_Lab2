package com.blablaarthur.lab2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class CreateNote extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    EditText title;
    EditText desc;
    ImageView imp;
    ImageView image;
    TextView time;
    TextView date;

    Note note;

    private static final int SELECT_PICTURE = 100;
    private static final int REQUEST_PERMISSION = 10;
    String action = "";
    String image_path = "";
    int importance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int theme = sharedPref.getInt("Theme", 1);
        int textSize = sharedPref.getInt("TextSize", 1);
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
        setContentView(R.layout.activity_create_note);
        title = (EditText) findViewById(R.id.editTitle);
        desc = (EditText) findViewById(R.id.editDesc);
        imp = (ImageView) findViewById(R.id.imp);
        image = (ImageView) findViewById(R.id.image);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        time = (TextView) findViewById(R.id.textTime);
        date = (TextView) findViewById(R.id.textDate);

        if(theme == 0){
            title.setTextColor(getResources().getColor(R.color.white));
            time.setTextColor(getResources().getColor(R.color.white));
            date.setTextColor(getResources().getColor(R.color.white));
        }

        ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayUseLogoEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);

        Intent currentIntent = getIntent();
        action = currentIntent.getAction();
        if(action.equals("android.intent.myaction.WATCH")){
            title.setFocusable(false);
            desc.setFocusable(false);
            bar.setTitle(R.string.note);
        }

        if(savedInstanceState == null) {
            importance = 0;
            if (action.equals("android.intent.myaction.CREATE")) {
                bar.setTitle(R.string.create);
                SetImportanceImage(importance);
                image.setImageBitmap(BitmapLoader.decodeBitmapFromResource(getResources(), R.drawable.image, 60, 60));
                Calendar now = Calendar.getInstance();
                time.setText(CalendarConverter.getTime(now));
                date.setText(CalendarConverter.getDate(now));
                note = new Note();
            } else {
                if (action.equals("android.intent.myaction.EDIT"))
                    bar.setTitle(R.string.edit);
                DBAdapter db = new DBAdapter(this);
                db.openDB();
                Cursor c = db.getNoteById(currentIntent.getIntExtra("Id", 0));
                //Log.d("A_R_T", String.valueOf(c.getInt(1)));
                if( c != null && c.moveToFirst() ) {
                    note = new Note(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getString(4), c.getString(5));
                    c.close();
                }
                db.closeDB();

                title.setText(note.Title);
                importance = note.Importance;
                SetImportanceImage(importance);
                image_path = note.Image;
                SetNoteImage(image_path);
                time.setText(note.DateTime.split(" ")[0]);
                date.setText(note.DateTime.split(" ")[1]);
                desc.setText(note.Description);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!action.equals("android.intent.myaction.WATCH")) {
            MenuInflater menuInflator = getMenuInflater();
            menuInflator.inflate(R.menu.create_note_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_note:
                if(title.getText().length() > 0 && desc.getText().length() > 0){
                    DBAdapter db = new DBAdapter(this);
                    db.openDB();
                    if(action.equals("android.intent.myaction.CREATE")){
                        db.insert(title.getText().toString(), desc.getText().toString(), importance, time.getText().toString() + " " + date.getText().toString(), image_path);
                    }
                    else {
                        db.update(note.Id, title.getText().toString(), desc.getText().toString(), importance, time.getText().toString() + " " + date.getText().toString(), image_path);
                    }
                    db.closeDB();
                    setResult(RESULT_OK);
                    finish();
                }
                else{
                    Toast.makeText(CreateNote.this, R.string.fillemptyfields, Toast.LENGTH_LONG).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void ChooseImage(View view){
        if(!action.equals("android.intent.myaction.WATCH")) {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, SELECT_PICTURE);
        }
        else{
            if(!image_path.equals("")){
                Intent myIntent = new Intent(CreateNote.this,
                        ImageWatcher.class);
                myIntent.putExtra("Image", image_path);
                startActivity(myIntent);
            }
            else{
                Toast.makeText(this, R.string.nopic, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    image_path = getPathFromURI(selectedImageUri);
                    image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    image.setImageBitmap(BitmapLoader.decodeBitmapFromPath(image_path,60,60));
                }
            }
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        res = cursor.getString(column_index);
        cursor.close();
        return res;
    }

    public void ChangeImp(View view){
        if(!action.equals("android.intent.myaction.WATCH")){
            ImageView current = (ImageView) view;
            switch (importance){
                case 0:
                    importance = 1;
                    break;
                case 1:
                    importance = 2;
                    break;
                case 2:
                    importance = 0;
                    break;
            }
            SetImportanceImage(importance);
        }
    }

    public void setDate(View view){
        if(!action.equals("android.intent.myaction.WATCH")) {
            DatePickerFragment fragment = new DatePickerFragment();
            Bundle args = new Bundle();
            args.putString("Date", date.getText().toString());
            fragment.setArguments(args);
            fragment.show(getFragmentManager(), "date");
        }
    }

    public void setTime(View view){
        if(!action.equals("android.intent.myaction.WATCH")) {
            TimePickerFragment fragment = new TimePickerFragment();
            Bundle args = new Bundle();
            args.putString("Time", time.getText().toString());
            fragment.setArguments(args);
            fragment.show(getFragmentManager(), "time");
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d("A_R_T", (dayOfMonth + "." + month + "." + year));
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        date.setText(CalendarConverter.getDate(c));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(2000,2,2, hourOfDay,minute);
        time.setText(CalendarConverter.getTime(c));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        note.Title = title.getText().toString();
        note.Description = desc.getText().toString();
        note.Importance = importance;
        note.Image = image_path;
        note.DateTime = time.getText().toString() + " " + date.getText().toString();
        outState.putParcelable("Note", note);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        note = savedInstanceState.getParcelable("Note");

        title.setText(note.Title);
        desc.setText(note.Description);
        importance = note.Importance;
        SetImportanceImage(importance);
        image_path = note.Image;
        SetNoteImage(image_path);
        time.setText(note.DateTime.split(" ")[0]);
        date.setText(note.DateTime.split(" ")[1]);
    }

    public void SetImportanceImage(int importance){
        int imp_style = 0;
        switch (importance) {
            case 0:
                imp_style = R.drawable.red_dot;
                break;
            case 1:
                imp_style = R.drawable.orange_dot;
                break;
            case 2:
                imp_style = R.drawable.green_dot;
                break;
        }
        imp.setImageResource(imp_style);
    }

    public void SetNoteImage(String path){
        if (path.equals("")) {
            image.setImageBitmap(BitmapLoader.decodeBitmapFromResource(getResources(), R.drawable.image, 60, 60));
        } else {
            image.setImageBitmap(BitmapLoader.decodeBitmapFromPath(path, 60, 60));
        }
    }
}
