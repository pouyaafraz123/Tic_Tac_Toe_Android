package com.example.tictactoe;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Arrays;

public class Game implements View.OnClickListener {
    private static final int NO_WINNER = 4;
    ImageView[] btns = new ImageView[9];
    int[] positions = new int[9];
    private static final int[][] WIN_POSITION = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    private int State;
    private static final int FIRST_PLAYER = 0;
    private static final int SECOND_PLAYER = 1;
    private static final int NOT_PLAYED = -1;
    TextView title;
    LinearLayout layout;
    ConstraintLayout dialog;
    TextView dialogText;
    Button playAgain;

    public Game(GameMode mode, LinearLayout layout, TextView title) {
        this.title = title;
        this.layout = layout;
        ConstraintLayout l = (ConstraintLayout) layout.getParent();
        dialog=l.findViewById(R.id.dialog);
        dialogText=dialog.findViewById(R.id.win_text);
        playAgain=dialog.findViewById(R.id.play_again_btn);
        playAgain.setOnClickListener(e-> reset());
        title.setText(R.string.first_player_turn);
        int k = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                btns[k++] = (ImageView) ((LinearLayout) layout.getChildAt(i)).getChildAt(j);

            }

        reset();
        State = FIRST_PLAYER;
        for (ImageView i : btns
        ) {
            i.setOnClickListener(this);
        }

    }

    private void reset() {
        dialog.setVisibility(View.GONE);
        for (int i = 0; i < 9; i++) {
            positions[i] = NOT_PLAYED;
            btns[i].setImageResource(0);
            State = FIRST_PLAYER;
        }
    }

    @Override
    public void onClick(View view) {
        ImageView img = (ImageView) view;
        if (!(positions[Integer.parseInt(img.getTag().toString())] == NOT_PLAYED))
            return;
        positions[Integer.parseInt(img.getTag().toString())] = State;
        img.setScaleX(0);
        img.setScaleY(0);
        img.setAlpha(0f);
        img.animate().alpha(1).scaleX(1).scaleY(1).setDuration(1000);
        if (State == FIRST_PLAYER) {
            img.setImageResource(R.drawable.red);
            title.setText(R.string.second_player_turn);
            State = SECOND_PLAYER;
        } else if (State == SECOND_PLAYER) {
            img.setImageResource(R.drawable.cross);
            title.setText(R.string.first_player_turn);
            State = FIRST_PLAYER;
        }
        int c = check();
        if (c == FIRST_PLAYER) {
            dialog.setVisibility(View.VISIBLE);
            dialogText.setText(R.string.first_player_win);
        } else if (c == SECOND_PLAYER) {
            dialog.setVisibility(View.VISIBLE);
            dialogText.setText(R.string.second_player_win);
        } else if (filled()) {
            dialog.setVisibility(View.VISIBLE);
            dialogText.setText(R.string.equal);
        }

    }

    private int check() {
        for (int[] p : WIN_POSITION) {
            int c = checkWinner(new int[]{positions[p[0]], positions[p[1]], positions[p[2]]});
            if (c == FIRST_PLAYER || c == SECOND_PLAYER)
                return c;
        }
        return NO_WINNER;
    }

    private int checkWinner(int[] p) {
        if (Arrays.binarySearch(p, NOT_PLAYED) >= 0) {
            return NO_WINNER;
        }
        if (p[0] == p[1] && p[1] == p[2] && p[0] == FIRST_PLAYER) {
            return FIRST_PLAYER;
        } else if (p[0] == p[1] && p[1] == p[2] && p[0] == SECOND_PLAYER) {
            return SECOND_PLAYER;
        }
        return -998;
    }

    public boolean filled() {
        for (int position : positions) {
            if (position == NOT_PLAYED) {
                return false;
            }
        }
        return true;
    }

}
