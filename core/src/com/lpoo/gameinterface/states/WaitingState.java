package com.lpoo.gameinterface.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.lpoo.gamelogic.Player;

import java.util.ArrayList;

/**
 * Created by Vasco on 06/06/2016.
 */
public class WaitingState extends State {

    private Texture background;

    private Label label;

    private Button startBtn;
    private Button backBtn;

    private Skin skin;

    private Sprite startSprite;
    private Sprite backSprite;

    private Stage stage;

    private boolean gameStarted = false;


    public WaitingState(GameStateManager gsm, State state) {
        super(gsm, state);

        background = new Texture("waitingstate.jpg");

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        label = new Label("WAITING FOR PLAYERS...  " + allPlayers.size() + "/10", skin);
        label.setFontScale(Gdx.graphics.getWidth()/800);
        label.setColor(Color.BLACK);
        label.setPosition(Gdx.graphics.getWidth()*3/8, Gdx.graphics.getHeight() * 350 / 480);

        startSprite = new Sprite(new Texture("start.png"));
        startBtn = new Button(new SpriteDrawable(startSprite));
        startBtn.setSize(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight() /8);
        startBtn.setX(Gdx.graphics.getWidth() / 2 - startBtn.getWidth() / 2);
        startBtn.setY(Gdx.graphics.getHeight() / 2 - startBtn.getHeight() / 2);

        backSprite = new Sprite(new Texture("back.png"));
        backBtn = new Button(new SpriteDrawable(backSprite));
        backBtn.setSize(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/8);
        backBtn.setX(Gdx.graphics.getWidth()  / 2 - startBtn.getWidth() / 2);
        backBtn.setY(Gdx.graphics.getHeight()/4);


        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(startBtn);
        stage.addActor(backBtn);
        stage.addActor(label);

       // System.out.println(me.toString());
    }

    @Override
    public void handleInput() {
        if(me.getPosition() == 0 && allPlayers.size() > 4) {
            if (startBtn.isPressed() && !gameStarted) {
                socket.emit("gameStarted");
                gameStarted = true;
                //dispose();
            }
        }
        if(backBtn.isPressed()){
            allPlayers = new ArrayList<Player>();
            socket = socket.disconnect();
            gsm.set(new LobbyState(gsm,this));
            dispose();

        }
     //   System.out.println("players: ");
      //  for(Player p : allPlayers)
       //     System.out.println(p.toString());
    }

    @Override
    public void update(float dt) {
        if(advanceState){
            gsm.set(new PlayState(gsm, this));
            dispose();
        }
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        label.setText("WAITING FOR PLAYERS...  " + allPlayers.size() + "/10");
        sb.begin();
        sb.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if(me.getPosition() == 0 && allPlayers.size() > 4)
            startBtn.draw(sb, 1);
        backBtn.draw(sb,1);
        label.draw(sb, 1);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        startSprite.getTexture().dispose();
    }
}
