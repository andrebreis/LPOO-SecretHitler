package com.lpoo.gameinterface.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;


/**
 * Created by Vasco on 05/06/2016.
 */
public class MenuState extends State{
    private Stage stage;

    private Texture background;

    private Button create_join_btn;
    private Button exit_btn;

    private Sprite create_join_sprite;
    private Sprite exit_sprite;

    private TextField name;
    private Skin skin;

    public MenuState(GameStateManager gsm) {

        super(gsm);
        background = new Texture("startmenu.png");

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        name = new TextField("",skin);
        name.setText("Enter your name here!");
        name.setSize(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*1/16);
        name.setPosition(Gdx.graphics.getWidth()*1/12, Gdx.graphics.getHeight()*5/12);


        create_join_sprite = new Sprite(new Texture("createjoinroom.png"));
        exit_sprite = new Sprite(new Texture("exit.png"));
        create_join_btn = new Button(new SpriteDrawable(create_join_sprite));
        create_join_btn.setSize(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*1/8);
        create_join_btn.setX(Gdx.graphics.getWidth()*1/12);
        create_join_btn.setY(Gdx.graphics.getHeight()*3/12);

        exit_btn = new Button(new SpriteDrawable(exit_sprite));
        exit_btn.setSize(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*1/8);
        exit_btn.setX(Gdx.graphics.getWidth()*1/12);
        exit_btn.setY(Gdx.graphics.getHeight()*1/12);


        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(create_join_btn);
        stage.addActor(exit_btn);
        stage.addActor(name);

    }

    @Override
    public void handleInput() {
        if(create_join_btn.isPressed()){
            gsm.set(new LobbyState(gsm));
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
        exit_btn.draw(sb, 1);
        create_join_btn.draw(sb, 1);
        name.draw(sb,1);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        create_join_sprite.getTexture().dispose();
        exit_sprite.getTexture().dispose();
    }
}
