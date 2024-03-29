package com.lpoo.gameinterface.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lpoo.gamelogic.Player;
import com.lpoo.gamelogic.SecretHitler;

import java.util.ArrayList;

import io.socket.client.Socket;

/**
 * Created by Vasco on 05/06/2016.
 */


public abstract class State {
    protected GameStateManager gsm;

    protected Socket socket;
    protected Player me;
    protected ArrayList<Player> allPlayers;
    protected SecretHitler gameInfo;
    protected boolean gameStarted = false;
    protected boolean advanceState;

    protected State (GameStateManager gsm)
    {
        this.gsm = gsm;
        this.allPlayers = new ArrayList<Player>();
       // this.board = null;
        this.gameInfo = new SecretHitler();
        this.socket = null;
        this.advanceState = false;
    }

    protected State(GameStateManager gsm, State copyState){
        this.gsm = gsm;
        this.socket = copyState.socket;
        this.me = copyState.me;
        this.allPlayers = copyState.allPlayers;
        this.gameStarted = copyState.gameStarted;
        this.gameInfo = copyState.gameInfo;
    }

    protected abstract void handleInput();

    public abstract void update(float dt);  // dt is the time between frames rendered

    public abstract void render(SpriteBatch sb);

    public abstract void dispose();

    public void setAdvanceState(boolean advanceState) {
        this.advanceState = advanceState;
    }
}