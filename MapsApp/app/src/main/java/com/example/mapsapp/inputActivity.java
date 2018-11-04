package com.example.mapsapp;

// https://github.com/elvinjgalarza/Class-Blog/tree/master/MapsApp

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class inputActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.inputActivity.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
    }

    // when the button is clicked, this method is called
    public void sendInput(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

}
