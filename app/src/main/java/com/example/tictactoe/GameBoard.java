package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameBoard extends AppCompatActivity {
    LinearLayout layout;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        layout=findViewById(R.id.main_layout);
        title=findViewById(R.id.turn_view);
        new Game(GameMode.MULTIPLAYER,layout,title);
    }
}