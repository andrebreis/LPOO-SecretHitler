package com.lpoo.gameinterface.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by Vasco on 06/06/2016.
 */
public class WaitingState extends State {

    private Texture background;
    private Label label;
    private Button start_btn;
    private Skin skin;
    private Sprite start_sprite;
    private Stage stage;
    private Integer players = 4;

    public WaitingState(GameStateManager gsm) {
        super(gsm);

        background = new Texture("waitingstate.jpg");

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        label = new Label(Integer.toString(players), skin);
        label.setFontScale(7);
        label.setPosition(Gdx.graphics.getWidth()*553/800,Gdx.graphics.getHeight()*345/480);

        start_sprite = new Sprite(new Texture("start.png"));
        start_btn = new Button(new SpriteDrawable(start_sprite));
        start_btn.setSize(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*1/8);
        start_btn.setX(Gdx.graphics.getWidth()*1/2-start_btn.getWidth()/2);
        start_btn.setY(Gdx.graphics.getHeight()*1/2-start_btn.getHeight()/2);


        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(start_btn);
        stage.addActor(label);
    }

    @Override
    public void handleInput() {
        if(start_btn.isPressed()){
            gsm.set(new PlayState(gsm));
            dispose();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        start_btn.draw(sb, 1);
        label.draw(sb,1);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        start_sprite.getTexture().dispose();
    }
}
