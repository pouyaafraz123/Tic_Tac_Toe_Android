package com.example.tictactoe;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Arrays;

public class Game implements View.OnClickListener {
    private boolean isRunning=false;
    private static final int NO_WINNER = 4;
    private static final String TAG = "GAME";
    ImageView[] btns = new ImageView[9];
    int[] positions = new int[9];
    private static final int[][] WIN_POSITION = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6},
            {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    private int State;
    private static final int FIRST_PLAYER = 0;
    private static final int SECOND_PLAYER = 1;
    private static final int NOT_PLAYED = -1;

    private String turn;
    private int firstPlayerWinCount;
    private int secondPlayerWinCount;
    private final String TURN_KEY = "turn";
    private final String FIRST_PLAYER_WIN_COUNT_KEY = "f";
    private final String SECOND_PLAYER_WIN_COUNT_KEY = "s";

    Activity activity;
    TextView title;
    TableLayout layout;
    ConstraintLayout dialog;
    TextView dialogText;
    Button playAgain;

    private SharedPreferences preferences;

    public Game(GameMode mode, Activity activity, TextView title) {
        init(activity, title);
        playAgain.setOnClickListener(e-> reset());
    }

    private SharedPreferences loadData(Activity activity) {
        final SharedPreferences preferences;
        String PREFERENCES = "p";
        preferences = activity.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        turn = preferences.getString(TURN_KEY,
                activity.getResources().getString(R.string.first_player_turn));

        firstPlayerWinCount = preferences.getInt(FIRST_PLAYER_WIN_COUNT_KEY,0);
        secondPlayerWinCount = preferences.getInt(SECOND_PLAYER_WIN_COUNT_KEY,0);
        return preferences;
    }

    private void init(Activity l, TextView title) {
        this.title = title;
        activity = l;
        layout= l.findViewById(R.id.main);
        dialog= l.findViewById(R.id.dialog);
        dialogText=dialog.findViewById(R.id.win_text);
        playAgain=dialog.findViewById(R.id.play_again_btn);
        preferences = loadData(activity);
        isRunning = true;
        makeGame(title);
    }

    private void makeGame(TextView title) {
        title.setText(turn);
        int k = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                btns[k] = (ImageView) ((LinearLayout) layout.getChildAt(i)).getChildAt(j);
                btns[k++].setOnClickListener(this);
            }
        reset();
        Log.d(TAG, "makeGame: "+turn);
        State = (turn.equals(activity.getResources().getString(R.string.first_player_turn)) ? FIRST_PLAYER : SECOND_PLAYER);
    }

    private void reset() {
        preferences = loadData(activity);
        dialog.setVisibility(View.GONE);
        for (int i = 0; i < 9; i++) {
            positions[i] = NOT_PLAYED;
            btns[i].setImageResource(0);
            State = FIRST_PLAYER;
        }
        isRunning=true;
    }

    @Override
    public void onClick(View view) {
        if (!isRunning) return;
        ImageView img = (ImageView) view;
        img.setVisibility(View.VISIBLE);
        if (!(positions[Integer.parseInt(img.getTag().toString())] == NOT_PLAYED))
            return;
        positions[Integer.parseInt(img.getTag().toString())] = State;
        img.setScaleX(0);
        img.setScaleY(0);
        img.setAlpha(0f);
        img.animate().alpha(1).scaleX(1).scaleY(1).setDuration(1000);
        /*int cx = img.getRight();
        int cy = img.getBottom();
        float finalRadius = (float) Math.hypot(cx, cy);
        Log.d(TAG, "onClick: cx:"+cx + "  cy:"+cy+  "finalRadius:"+finalRadius);
        ViewAnimationUtils.createCircularReveal(img,cx,cy,0,finalRadius).setDuration(1000).start();*/
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
            isRunning=false;
            dialog.setVisibility(View.VISIBLE);
            dialogText.setText(R.string.first_player_win);
        } else if (c == SECOND_PLAYER) {
            isRunning=false;
            dialog.setVisibility(View.VISIBLE);
            dialogText.setText(R.string.second_player_win);
        } else if (filled()) {
            isRunning=false;
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
            preferences.edit().putString(TURN_KEY,(turn.equals(activity.getResources()
                    .getString(R.string.first_player_turn)) ? activity.getResources()
                    .getString(R.string.second_player_turn) : activity.getResources()
                    .getString(R.string.first_player_turn))).apply();
            return NO_WINNER;
        }
        if (p[0] == p[1] && p[1] == p[2] && p[0] == FIRST_PLAYER) {
            preferences.edit().putInt(FIRST_PLAYER_WIN_COUNT_KEY,firstPlayerWinCount+1)
                    .putString(TURN_KEY,(turn.equals(activity.getResources()
                    .getString(R.string.first_player_turn)) ? activity.getResources()
                    .getString(R.string.second_player_turn) : activity.getResources()
                    .getString(R.string.first_player_turn))).apply();
            return FIRST_PLAYER;
        } else if (p[0] == p[1] && p[1] == p[2] && p[0] == SECOND_PLAYER) {
            preferences.edit().putInt(SECOND_PLAYER_WIN_COUNT_KEY,secondPlayerWinCount+1)
                    .putString(TURN_KEY,(turn.equals(activity.getResources()
                    .getString(R.string.first_player_turn)) ? activity.getResources()
                    .getString(R.string.second_player_turn) : activity.getResources()
                    .getString(R.string.first_player_turn))).apply();
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
