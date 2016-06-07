package com.lpoo.gameinterface.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;


/**
 * Created by Vasco on 05/06/2016.
 */
public class PlayState extends State {
    private Texture background;
    private Skin skin;
    private Label label;
    private Stage stage;
    private ArrayList<Label> labels;

    private int noFascistLaws = 0, noLiberalLaws = 0;

    protected PlayState(GameStateManager gsm, State state) {
        super(gsm, state);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        background = new Texture("boardgame.jpg");
        labels = new ArrayList<Label>();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        for(int i = 0; i < allPlayers.size(); i++){
            label = new Label(allPlayers.get(i).getName() + allPlayers.get(i).getRole(), skin);
            label.setFontScale(3);
            labels.add(label);
            stage.addActor(label);
        }
        setPositions(labels);
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
        for(int i = 0; i < labels.size(); i++){
            labels.get(i).draw(sb,1);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
    }

    public void setPositions(ArrayList<Label> labels) {
       switch (labels.size()){
           case 5:
               labels.get(0).setPosition(0,Gdx.graphics.getHeight()/2);
               labels.get(1).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels.get(2).setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*14/15);
               labels.get(3).setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()/2);
               labels.get(4).setPosition(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*1/20);
               break;
           case 6:
               labels.get(0).setPosition(0,Gdx.graphics.getHeight()/2);
               labels.get(1).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels.get(2).setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*14/15);
               labels.get(3).setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()/2);
               labels.get(4).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*1/20);
               labels.get(5).setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*1/20);
               break;
           case 7:
               labels.get(0).setPosition(0,Gdx.graphics.getHeight()*2/5);
               labels.get(1).setPosition(0,Gdx.graphics.getHeight()*4/5);
               labels.get(2).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels.get(3).setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*14/15);
               labels.get(4).setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()/2);
               labels.get(5).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*1/20);
               labels.get(6).setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*1/20);
               break;
           case 8:
               labels.get(0).setPosition(0,Gdx.graphics.getHeight()*2/5);
               labels.get(1).setPosition(0,Gdx.graphics.getHeight()*4/5);
               labels.get(2).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels.get(3).setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*14/15);
               labels.get(4).setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()*2/5);
               labels.get(5).setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()*4/5);
               labels.get(6).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*1/20);
               labels.get(7).setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*1/20);
               break;
           case 9:
               labels.get(0).setPosition(0,Gdx.graphics.getHeight()*2/5);
               labels.get(1).setPosition(0,Gdx.graphics.getHeight()*4/5);
               labels.get(2).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels.get(3).setPosition(Gdx.graphics.getWidth()*2/5,Gdx.graphics.getHeight()*14/15);
               labels.get(4).setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*14/15);
               labels.get(5).setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()*2/5);
               labels.get(6).setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()*4/5);
               labels.get(7).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*1/20);
               labels.get(8).setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*1/20);
               break;
           case 10:
               labels.get(0).setPosition(0,Gdx.graphics.getHeight()*3/10);
               labels.get(1).setPosition(0,Gdx.graphics.getHeight()*7/10);
               labels.get(2).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels.get(3).setPosition(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*14/15);
               labels.get(4).setPosition(Gdx.graphics.getWidth()*4/5,Gdx.graphics.getHeight()*14/15);
               labels.get(5).setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()*3/10);
               labels.get(6).setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()*7/10);
               labels.get(7).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*1/20);
               labels.get(8).setPosition(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*1/20);
               labels.get(9).setPosition(Gdx.graphics.getWidth()*4/5,Gdx.graphics.getHeight()*1/20);
               break;
       }
    }
}
