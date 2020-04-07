package com.example.madsdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editText = findViewById(R.id.editText);
        final TextView textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(R.string.bottom_text);
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                textView.setText("Don't press me all the time!!!（＞人＜；）");
                return true;
            }
        });

        textView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textView.setText("I was touched!(＃°Д°)");
                return false;
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean hasFocus){
                if(hasFocus == true) {
                    Toast.makeText(getApplicationContext(), "文本框获得焦点", Toast.LENGTH_LONG).show();
                }
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    if(editText.getText().toString().trim().length() == 0){
                        textView.setText("Dude, you didn't type anything in.(╯▔皿▔)╯");
                    }
                    else{
                        textView.setText("Your input: " + editText.getText());
                    }

                    editText.clearFocus();
                }
                return false;
            }
        });
    }
}
