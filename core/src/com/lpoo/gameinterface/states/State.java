package com.lpoo.gameinterface.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Vasco on 05/06/2016.
 */


public abstract class State {
    protected GameStateManager gsm;
    protected State (GameStateManager gsm)
    {
        this.gsm = gsm;
    }

    protected abstract void handleInput();

    public abstract void update(float dt);  // dt is the time between frames rendered

    public abstract void render(SpriteBatch sb);

    public abstract void dispose();

}