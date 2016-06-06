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

/**
 * Created by Vasco on 06/06/2016.
 */
public class WaitingState extends State {

    private Texture background;
    private Label label;
    private Button startBtn;
    private Skin skin;
    private Sprite startSprite;
    private Stage stage;

    public WaitingState(GameStateManager gsm, State state) {
        super(gsm, state);

        background = new Texture("waitingstate.jpg");

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        label = new Label("WAITING FOR PLAYERS...  " + allPlayers.size() + "/10", skin);
        label.setFontScale(6);
        label.setColor(Color.BLACK);
        label.setPosition(Gdx.graphics.getWidth()/8, Gdx.graphics.getHeight() * 350 / 480);

        startSprite = new Sprite(new Texture("start.png"));
        startBtn = new Button(new SpriteDrawable(startSprite));
        startBtn.setSize(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*1/8);
        startBtn.setX(Gdx.graphics.getWidth() * 1 / 2 - startBtn.getWidth() / 2);
        startBtn.setY(Gdx.graphics.getHeight() * 1 / 2 - startBtn.getHeight() / 2);


        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(startBtn);
        stage.addActor(label);
        System.out.println(me.toString());

       // System.out.println(me.toString());
    }

    @Override
    public void handleInput() {
        if(me.getPosition() == 0 && allPlayers.size() > 4) {
            if (startBtn.isPressed()) {
                gsm.set(new PlayState(gsm));
                dispose();
            }
        }
     //   System.out.println("players: ");
      //  for(Player p : allPlayers)
       //     System.out.println(p.toString());
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        label.setText("WAITING FOR PLAYERS...  " + allPlayers.size() + "/10");
        sb.begin();
        sb.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if(me.getPosition() == 0 && allPlayers.size() > 4)
            startBtn.draw(sb, 1);
        label.draw(sb, 1);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        startSprite.getTexture().dispose();
    }
}
