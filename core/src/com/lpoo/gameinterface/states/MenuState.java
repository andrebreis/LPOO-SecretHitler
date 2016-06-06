package com.lpoo.gameinterface.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.lpoo.gamelogic.Player;


/**
 * Created by Vasco on 05/06/2016.
 */
public class MenuState extends State{
    private Stage stage;

    private Texture background;

    private Button createJoinBtn;
    private Button exitBtn;

    private Sprite createJoinSprite;
    private Sprite exitSprite;

    private TextField name;
    private Skin skin;

    public MenuState(GameStateManager gsm) {

        super(gsm);
        background = new Texture("startmenu.png");

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        name = new TextField("",skin);
        name.setMessageText("Enter your name here!");
        name.setSize(Gdx.graphics.getWidth()*5/11,Gdx.graphics.getHeight()*1/8);
        name.setPosition(Gdx.graphics.getWidth()*3/4-name.getWidth()/2, Gdx.graphics.getHeight()*10/12);


        createJoinSprite = new Sprite(new Texture("createjoinroom.png"));
        exitSprite = new Sprite(new Texture("exit.png"));
        createJoinBtn = new Button(new SpriteDrawable(createJoinSprite));
        createJoinBtn.setSize(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*2/7);
        createJoinBtn.setX(Gdx.graphics.getWidth()*3/4-createJoinBtn.getWidth()/2);
        createJoinBtn.setY(Gdx.graphics.getHeight()/2);

        exitBtn = new Button(new SpriteDrawable(exitSprite));
        exitBtn.setSize(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*2/7);
        exitBtn.setX(Gdx.graphics.getWidth()*3/4-exitBtn.getWidth()/2);
        exitBtn.setY(Gdx.graphics.getHeight()*1/12);


        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(createJoinBtn);
        stage.addActor(exitBtn);
        stage.addActor(name);

    }

    @Override
    public void handleInput() {
        if(createJoinBtn.isPressed()){
            this.me = new Player(name.getText());
            gsm.set(new LobbyState(gsm, this));
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
        exitBtn.draw(sb, 1);
        createJoinBtn.draw(sb, 1);
        name.draw(sb,1);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        createJoinSprite.getTexture().dispose();
        exitSprite.getTexture().dispose();
    }
}
