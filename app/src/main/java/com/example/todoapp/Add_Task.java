package com.example.todoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Add_Task extends AppCompatActivity {

    Button btn_Color, btn_Save, btn_calendar;
    EditText txt_taskTitle, txt_taskDetail;
    TextView txt_modified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CALENDAR}, 1);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_CALENDAR}, 2);
        }

        Cursor cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI,
                null, null, null, null);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);

        txt_taskTitle = findViewById(R.id.input_text_taskTitle);
        txt_taskDetail = findViewById(R.id.input_text_taskDetails);

        Date src_date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        String modified = df.format(src_date);
        String date = df.format(src_date);

        btn_Color = findViewById(R.id.button_setColor);
        btn_Save = findViewById(R.id.button_save);
        btn_calendar = findViewById(R.id.button_setColor2);

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class_Database add_db = new Class_Database(Add_Task.this);
                add_db.p_add_task(txt_taskTitle.getText().toString().trim(), date.toString().trim(), modified.toString().trim(), txt_taskDetail.getText().toString().trim());
                finish();
            }
        });

        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialognew = new AlertDialog.Builder(Add_Task.this);
                dialognew.setTitle("Import Task");
                dialognew.setMessage("Import Events from Calendar?");
                dialognew.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        while(cursor.moveToNext()){
                            if (cursor!=null){
                                int id_1 = cursor.getColumnIndex(CalendarContract.Events._ID);
                                int title = cursor.getColumnIndex(CalendarContract.Events.TITLE);
                                int desc = cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION);

                                String id_val = cursor.getString(id_1);
                                String event_title = cursor.getString(title);
                                String event_desc = cursor.getString(desc);

                                Class_Database add_db = new Class_Database(Add_Task.this);
                                add_db.p_add_task(event_title.toString().trim(), date.toString().trim(), modified.toString().trim(), event_desc.toString().trim());

                            }else{
                                break;
                            }

                        }
                    }
                });
                dialognew.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                dialognew.create().show();

            }

        });
        
    }

    public boolean onOptionsItemSelected(MenuItem back){
        switch (back.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(back);

    }

}
