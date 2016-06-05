package com.lpoo.gameinterface.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * Created by Vasco on 05/06/2016.
 */
public class LobbyState extends State{
    private Texture lobbybackground;

    public LobbyState(GameStateManager gsm) {
        super(gsm);
        lobbybackground = new Texture("gamelobby.png");
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(lobbybackground, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
