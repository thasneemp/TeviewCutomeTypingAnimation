package com.example.thasneem.textviewanimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;

public class MainActivity extends AppCompatActivity {
    AppCompatEditText appCompatEditText;
    AnimateTextView animateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appCompatEditText = findViewById(R.id.editext);
        animateTextView = findViewById(R.id.ter);


        appCompatEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    animateTextView.setText("");
                    return;
                }
                animateTextView.setText(s + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}
