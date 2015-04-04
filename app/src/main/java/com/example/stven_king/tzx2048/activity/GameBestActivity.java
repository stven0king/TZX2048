package com.example.stven_king.tzx2048.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.stven_king.tzx2048.R;

/**
 * Created by STVEN_KING on 2015/1/22.
 */
public class GameBestActivity extends Activity {
    private ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_best);
        imageButton = (ImageButton) findViewById(R.id.im);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButton.setClickable(false);
                startActivity(new Intent(GameBestActivity.this,MainActivity.class));
                finish();
                onDestroy();
            }
        });
    }
}
