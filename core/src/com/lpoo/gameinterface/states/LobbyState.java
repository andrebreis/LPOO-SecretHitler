package com.lpoo.gameinterface.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;


/**
 * Created by Vasco on 05/06/2016.
 */
public class LobbyState extends State{

    private Stage stage;

    private Texture background;

    private Button create_btn;
    private Button join_btn;
    private Button back_btn;

    private Sprite create_sprite;
    private Sprite join_sprite;
    private Sprite back_sprite;

    public LobbyState(GameStateManager gsm) {
        super(gsm);
        background = new Texture("gamelobby.png");

        create_sprite = new Sprite(new Texture("createroom.png"));
        join_sprite = new Sprite(new Texture("joinroom.png"));
        back_sprite = new Sprite(new Texture("back.png"));

        create_btn = new Button(new SpriteDrawable(create_sprite));
        create_btn.setSize(400,150);
        create_btn.setX(600);
        create_btn.setY(350);

        join_btn = new Button(new SpriteDrawable(join_sprite));
        join_btn.setSize(400,150);
        join_btn.setX(600);
        join_btn.setY(200);

        back_btn = new Button(new SpriteDrawable(back_sprite));
        back_btn.setSize(400,150);
        back_btn.setX(600);
        back_btn.setY(50);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(create_btn);
        stage.addActor(join_btn);
        stage.addActor(back_btn);

    }

    @Override
    public void handleInput() {
        if(create_btn.isPressed()){
            gsm.set(new PlayState(gsm));
            dispose();
        }
        if(back_btn.isPressed()){
            gsm.set(new MenuState(gsm));
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
        sb.draw(background, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        create_btn.draw(sb, 1);
        join_btn.draw(sb, 1);
        back_btn.draw(sb,1);
        sb.end();
    }

    @Override
    public void dispose() {

        background.dispose();
        create_sprite.getTexture().dispose();
        back_sprite.getTexture().dispose();
        join_sprite.getTexture().dispose();

    }
}
