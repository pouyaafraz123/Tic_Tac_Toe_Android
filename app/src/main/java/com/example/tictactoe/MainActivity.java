package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button multiplayerBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        multiplayerBTN=findViewById(R.id.multi_player);
        multiplayerBTN.setOnClickListener(e->{
            newMultiplayerGame();
        });
    }

    private void newMultiplayerGame() {
        Intent intent = new Intent(MainActivity.this,GameBoard.class);
        intent.putExtra("mode",GameMode.MULTIPLAYER);
        startActivity(intent);
    }

}