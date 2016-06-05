package com.lpoo.gameinterface.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Vasco on 05/06/2016.
 */
public class PlayState extends State {

    private Texture background;

    protected PlayState(GameStateManager gsm) {
        super(gsm);
        background = new Texture("boardgame.jpg");
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
    }
}
