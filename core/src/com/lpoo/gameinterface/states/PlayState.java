package com.lpoo.gameinterface.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.lpoo.gamelogic.Player;
import com.sun.javafx.scene.paint.GradientUtils;
import com.sun.xml.internal.ws.dump.LoggingDumpTube;

import java.awt.Point;


/**
 * Created by Vasco on 05/06/2016.
 */
public class PlayState extends State {
    private Texture background;
    private Texture president;
    private Texture chancellor;
    private Skin skin;
    private Label label;
    private Stage stage;
    private String[] players = {"Joao", "Manuel", "Joaquim", "Broas", "Rui", "Paulo", "Vasco", "Tomas", "Jose", "Nuno"};
    private Label[] labels;
    private Point presidentPoint;
    private Point chancellorPoint;

    protected PlayState(GameStateManager gsm) {
        super(gsm);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        background = new Texture("boardgame.jpg");
        president = new Texture ("president.png");
        chancellor = new Texture ("chancellor.png");

        labels = new Label[players.length];
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        for(int i = 0; i < players.length; i++){
            label = new Label(players[i], skin);
            label.setFontScale(3);
            labels[i] = label;
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
        for(int i = 0; i < labels.length; i++){
            labels[i].draw(sb,1);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        president.dispose();
        chancellor.dispose();
    }

    public void  setPlatePosition(Player p, Point point){
        if (labels [p.getPosition()].getX()  == 0){
            point.setLocation(Gdx.graphics.getWidth()*1/5,labels [p.getPosition()].getY());
        }
        else if(labels [p.getPosition()].getX()  == Gdx.graphics.getWidth()*8/9){
            point.setLocation(Gdx.graphics.getWidth()*4/5,labels [p.getPosition()].getY());
        } else if (labels[p.getPosition()].getY() == Gdx.graphics.getHeight() * 1 / 20) {
            point.setLocation(labels [p.getPosition()].getX(),Gdx.graphics.getHeight() * 3 / 20);
        }
        else if(labels[p.getPosition()].getY() == Gdx.graphics.getHeight() * 14 / 15){
            point.setLocation(labels [p.getPosition()].getX(),Gdx.graphics.getHeight() * 12 / 15);
        }
    }

    public void setPositions(Label[] labels) {
       switch (labels.length){
           case 5:
               labels[0].setPosition(0,Gdx.graphics.getHeight()/2);
               labels[1].setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels[2].setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*14/15);
               labels[3].setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()/2);
               labels[4].setPosition(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*1/20);
               break;
           case 6:
               labels[0].setPosition(0,Gdx.graphics.getHeight()/2);
               labels[1].setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels[2].setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*14/15);
               labels[3].setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()/2);
               labels[4].setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*1/20);
               labels[5].setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*1/20);
               break;
           case 7:
               labels[0].setPosition(0,Gdx.graphics.getHeight()*2/5);
               labels[1].setPosition(0,Gdx.graphics.getHeight()*4/5);
               labels[2].setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels[3].setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*14/15);
               labels[4].setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()/2);
               labels[5].setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*1/20);
               labels[6].setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*1/20);
               break;
           case 8:
               labels[0].setPosition(0,Gdx.graphics.getHeight()*2/5);
               labels[1].setPosition(0,Gdx.graphics.getHeight()*4/5);
               labels[2].setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels[3].setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*14/15);
               labels[4].setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()*2/5);
               labels[5].setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()*4/5);
               labels[6].setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*1/20);
               labels[7].setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*1/20);
               break;
           case 9:
               labels[0].setPosition(0,Gdx.graphics.getHeight()*2/5);
               labels[1].setPosition(0,Gdx.graphics.getHeight()*4/5);
               labels[2].setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels[3].setPosition(Gdx.graphics.getWidth()*2/5,Gdx.graphics.getHeight()*14/15);
               labels[4].setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*14/15);
               labels[5].setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()*2/5);
               labels[6].setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()*4/5);
               labels[7].setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*1/20);
               labels[8].setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*1/20);
               break;
           case 10:
               labels[0].setPosition(0,Gdx.graphics.getHeight()*3/10);
               labels[1].setPosition(0,Gdx.graphics.getHeight()*7/10);
               labels[2].setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels[3].setPosition(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*14/15);
               labels[4].setPosition(Gdx.graphics.getWidth()*4/5,Gdx.graphics.getHeight()*14/15);
               labels[5].setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()*3/10);
               labels[6].setPosition(Gdx.graphics.getWidth()*8/9,Gdx.graphics.getHeight()*7/10);
               labels[7].setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*1/20);
               labels[8].setPosition(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*1/20);
               labels[9].setPosition(Gdx.graphics.getWidth()*4/5,Gdx.graphics.getHeight()*1/20);
               break;
       }
    }
}
