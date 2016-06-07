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
import com.lpoo.gamelogic.Player;
import com.lpoo.gamelogic.SecretHitler;

import java.awt.Point;

import java.util.ArrayList;


/**
 * Created by Vasco on 05/06/2016.
 */
public class PlayState extends State {
    private Texture background;
    private Texture presidentPlate;
    private Texture chancellorPlate;
    private Texture liberalswin;
    private Texture fascistswin;

    private Skin skin;
    private Label label;
    private Label information;
    private Stage stage;

    private ArrayList<Button> buttons;
    private Button yesButton, noButton, tickButton;
    private Sprite yesSprite, noSprite, tickSprite;
    private ArrayList<Label> labels;

    private Point presidentPlatePosition, chancellorPlatePosition;
    private boolean rotatePresident = false , rotateChancellor = false;

    private boolean hasVoted = false;

    protected PlayState(GameStateManager gsm, State state) {
        super(gsm, state);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        background = new Texture("boardgame.jpg");

        labels = new ArrayList<Label>();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        for(int i = 0; i < allPlayers.size(); i++){
            label = new Label(allPlayers.get(i).getName(), skin);
            label.setFontScale(3);
            labels.add(label);
            stage.addActor(label);
        }
        setPositions(labels);

        //Information label missing size and position, create variable to show or not label
        information = new Label("",skin);
        information.setFontScale(Gdx.graphics.getWidth()/800);
        stage.addActor(information);

        //TODO: missing set size and position
        yesSprite = new Sprite(new Texture("yes.png"));
        yesButton = new Button(new SpriteDrawable(yesSprite));
        yesButton.setSize(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*1/8);
        yesButton.setX(Gdx.graphics.getWidth() * 1 / 2 -  yesButton.getWidth() / 2);
        yesButton.setY(Gdx.graphics.getHeight() * 1 / 2 -  yesButton.getHeight() / 2);
        stage.addActor(yesButton);

        noSprite = new Sprite(new Texture("back.png"));
        noButton = new Button(new SpriteDrawable(noSprite));
        noButton.setSize(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*1/8);
        noButton.setX(Gdx.graphics.getWidth() * 1 / 2 -  noButton.getWidth() / 2);
        noButton.setY(Gdx.graphics.getHeight()*1/4);
        stage.addActor(noButton);

        buttons = new ArrayList<Button>();
        //TODO: maybe change size and little bit positions
        for(int i = 0; i < allPlayers.size(); i++){
            tickSprite = new Sprite(new Texture("tick.png"));
            tickButton = new Button(new SpriteDrawable(tickSprite));
            yesButton.setSize(Gdx.graphics.getHeight()*1/16,Gdx.graphics.getHeight()*1/16);
            buttons.add(tickButton);
            stage.addActor(tickButton);
        }
        setTicksPositions(buttons);

        //TODO: Fascists and Liberals win
        liberalswin = new Texture ("liberalvictory.png");
        fascistswin = new Texture ("fascistvictory.png");

        //President and chancellor
       // presidentPlate = new Texture ("president.png");
        chancellorPlate = new Texture ("chancellor.png");
        presidentPlatePosition = new Point(-1, -1);
        chancellorPlatePosition = new Point(-1, -1);
    }

    @Override
    protected void handleInput() {
        if(yesButton.isPressed()){
            socket.emit("voteForChancellor", 1);
            hasVoted = true;
        }
        else if(noButton.isPressed()){
            socket.emit("voteForChancellor", 0);
            hasVoted = true;
        }
        for(int i = 0; i < buttons.size(); i++){
            if(buttons.get(i).isPressed()){
                if(gameInfo.getTurnStatus() == SecretHitler.PICKING_CHANCELLOR && me.getId().equals(allPlayers.get(gameInfo.getPresidentIndex()).getId())){
                    socket.emit("pickedChancellor", i);
                }
                //else if() TODO: configure clicks
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        setPlatePosition(allPlayers.get(gameInfo.getPresidentIndex()), presidentPlatePosition, true);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        for(int i = 0; i < labels.size(); i++){
            labels.get(i).draw(sb,1);
        }
        if(rotatePresident){
            presidentPlate = new Texture("presidentrotate.png");
            sb.draw(presidentPlate, presidentPlatePosition.x, presidentPlatePosition.y - (Gdx.graphics.getWidth() / 25 /3), Gdx.graphics.getHeight() / (5 * 3/4), Gdx.graphics.getWidth() / 25);
        }
        else {
            presidentPlate = new Texture("president.png");
            sb.draw(presidentPlate, presidentPlatePosition.x, presidentPlatePosition.y - (Gdx.graphics.getHeight() / (5 * 3/4)/3), Gdx.graphics.getWidth() / 25, Gdx.graphics.getHeight() / (5 *3/4));
        }
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        presidentPlate.dispose();
        chancellorPlate.dispose();
    }

    public void  setPlatePosition(Player p, Point point, boolean isPresident){
        if (labels.get(p.getPosition()).getX()  == 0){
            point.setLocation(labels.get(p.getPosition()).getPrefWidth() + 10, labels.get(p.getPosition()).getY()); //TODO dat 10
            if(isPresident) rotatePresident = false;
            else rotateChancellor = false;
        }
        else if(labels.get(p.getPosition()).getX()  == Gdx.graphics.getWidth() - labels.get(p.getPosition()).getPrefWidth()){
            point.setLocation(Gdx.graphics.getWidth() - labels.get(p.getPosition()).getPrefWidth() - 10,labels.get(p.getPosition()).getY());
            if(isPresident) rotatePresident = false;
            else rotateChancellor = false;
        } else if (labels.get(p.getPosition()).getY() == Gdx.graphics.getHeight()/40) {
            point.setLocation(labels.get(p.getPosition()).getX(),Gdx.graphics.getHeight()/40-20);
            if(isPresident) rotatePresident = true;
            else rotateChancellor = true;
        }
        else if(labels.get(p.getPosition()).getY() == Gdx.graphics.getHeight() * 14 / 15){
            point.setLocation(labels.get(p.getPosition()).getX(),Gdx.graphics.getHeight() * 12 / 15);
            if(isPresident) rotatePresident = true;
            else rotateChancellor = true;
        }
    }

    public void setPositions(ArrayList<Label> labels) {
       switch (labels.size()){
           case 5:
               labels.get(0).setPosition(0,Gdx.graphics.getHeight()/2);
               labels.get(1).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels.get(2).setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*14/15);
               labels.get(3).setPosition(Gdx.graphics.getWidth() - labels.get(3).getPrefWidth(),Gdx.graphics.getHeight()/2);
               labels.get(4).setPosition(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()/40);
               break;
           case 6:
               labels.get(0).setPosition(0,Gdx.graphics.getHeight()/2);
               labels.get(1).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels.get(2).setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*14/15);
               labels.get(3).setPosition(Gdx.graphics.getWidth() - labels.get(3).getPrefWidth(),Gdx.graphics.getHeight()/2);
               labels.get(4).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()/40);
               labels.get(5).setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()/40);
               break;
           case 7:
               labels.get(0).setPosition(0,Gdx.graphics.getHeight()*2/5);
               labels.get(1).setPosition(0,Gdx.graphics.getHeight()*4/5);
               labels.get(2).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels.get(3).setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*14/15);
               labels.get(4).setPosition(Gdx.graphics.getWidth() - labels.get(4).getPrefWidth(),Gdx.graphics.getHeight()/2);
               labels.get(5).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()/40);
               labels.get(6).setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()/40);
               break;
           case 8:
               labels.get(0).setPosition(0,Gdx.graphics.getHeight()*2/5);
               labels.get(1).setPosition(0,Gdx.graphics.getHeight()*4/5);
               labels.get(2).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels.get(3).setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*14/15);
               labels.get(4).setPosition(Gdx.graphics.getWidth() - labels.get(4).getPrefWidth(),Gdx.graphics.getHeight()*2/5);
               labels.get(5).setPosition(Gdx.graphics.getWidth() - labels.get(5).getPrefWidth(),Gdx.graphics.getHeight()*4/5);
               labels.get(6).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()/40);
               labels.get(7).setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()/40);
               break;
           case 9:
               labels.get(0).setPosition(0,Gdx.graphics.getHeight()*2/5);
               labels.get(1).setPosition(0,Gdx.graphics.getHeight()*4/5);
               labels.get(2).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels.get(3).setPosition(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*14/15);
               labels.get(4).setPosition(Gdx.graphics.getWidth()*4/5,Gdx.graphics.getHeight()*14/15);
               labels.get(5).setPosition(Gdx.graphics.getWidth() - labels.get(5).getPrefWidth(),Gdx.graphics.getHeight()*2/5);
               labels.get(6).setPosition(Gdx.graphics.getWidth() - labels.get(6).getPrefWidth(),Gdx.graphics.getHeight()*4/5);
               labels.get(7).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()/40);
               labels.get(8).setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()/40);
               break;
           case 10:
               labels.get(0).setPosition(0,Gdx.graphics.getHeight()*3/10);
               labels.get(1).setPosition(0,Gdx.graphics.getHeight()*7/10);
               labels.get(2).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()*14/15);
               labels.get(3).setPosition(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*14/15);
               labels.get(4).setPosition(Gdx.graphics.getWidth()*4/5,Gdx.graphics.getHeight()*14/15);
               labels.get(5).setPosition(Gdx.graphics.getWidth() - labels.get(5).getPrefWidth(),Gdx.graphics.getHeight()*3/10);
               labels.get(6).setPosition(Gdx.graphics.getWidth() - labels.get(6).getPrefWidth(),Gdx.graphics.getHeight()*7/10);
               labels.get(7).setPosition(Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()/40);
               labels.get(8).setPosition(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()/40);
               labels.get(9).setPosition(Gdx.graphics.getWidth()*4/5,Gdx.graphics.getHeight()/40);
               break;
       }
    }

    public void setTicksPositions(ArrayList<Button> buttons) {
        switch (buttons.size()){
            case 5:
                buttons.get(0).setPosition(labels.get(0).getWidth(),Gdx.graphics.getHeight()/2);
                buttons.get(1).setPosition(Gdx.graphics.getWidth()*1/5+labels.get(1).getWidth()/2,Gdx.graphics.getHeight()*27/30);
                buttons.get(2).setPosition(Gdx.graphics.getWidth()*3/5+labels.get(2).getWidth()/2,Gdx.graphics.getHeight()*27/30);
                buttons.get(3).setPosition(Gdx.graphics.getWidth()*8/9-labels.get(3).getWidth(),Gdx.graphics.getHeight()/2);
                buttons.get(4).setPosition(Gdx.graphics.getWidth()*1/2+labels.get(4).getWidth()/2,Gdx.graphics.getHeight()*3/40);
                break;
            case 6:
                buttons.get(0).setPosition(labels.get(0).getWidth(),Gdx.graphics.getHeight()/2);
                buttons.get(1).setPosition(Gdx.graphics.getWidth()*1/5+labels.get(1).getWidth()/2,Gdx.graphics.getHeight()*27/30);
                buttons.get(2).setPosition(Gdx.graphics.getWidth()*3/5+labels.get(2).getWidth()/2,Gdx.graphics.getHeight()*27/30);
                buttons.get(3).setPosition(Gdx.graphics.getWidth()*8/9-labels.get(3).getWidth(),Gdx.graphics.getHeight()/2);
                buttons.get(4).setPosition(Gdx.graphics.getWidth()*1/5+labels.get(4).getWidth()/2,Gdx.graphics.getHeight()*3/40);
                buttons.get(5).setPosition(Gdx.graphics.getWidth()*3/5+labels.get(5).getWidth()/2,Gdx.graphics.getHeight()*3/40);
                break;
            case 7:
                buttons.get(0).setPosition(labels.get(0).getWidth(),Gdx.graphics.getHeight()*2/5);
                buttons.get(1).setPosition(labels.get(1).getWidth(),Gdx.graphics.getHeight()*4/5);
                buttons.get(2).setPosition(Gdx.graphics.getWidth()*1/5+labels.get(2).getWidth()/2,Gdx.graphics.getHeight()*27/30);
                buttons.get(3).setPosition(Gdx.graphics.getWidth()*3/5+labels.get(3).getWidth()/2,Gdx.graphics.getHeight()*27/30);
                buttons.get(4).setPosition(Gdx.graphics.getWidth()*8/9-labels.get(4).getWidth(),Gdx.graphics.getHeight()/2);
                buttons.get(5).setPosition(Gdx.graphics.getWidth()*1/5+labels.get(5).getWidth()/2,Gdx.graphics.getHeight()*3/40);
                buttons.get(6).setPosition(Gdx.graphics.getWidth()*3/5+labels.get(6).getWidth()/2,Gdx.graphics.getHeight()*3/40);
                break;
            case 8:
                //TODO: FIX THIS ONE
                buttons.get(0).setPosition(labels.get(0).getWidth(),Gdx.graphics.getHeight()*2/5);
                buttons.get(1).setPosition(labels.get(1).getWidth(),Gdx.graphics.getHeight()*4/5);
                buttons.get(2).setPosition(Gdx.graphics.getWidth()*1/5+labels.get(2).getWidth()/2,Gdx.graphics.getHeight()*27/30);
                buttons.get(3).setPosition(Gdx.graphics.getWidth()*3/5+labels.get(3).getWidth()/2,Gdx.graphics.getHeight()*27/30);
                buttons.get(4).setPosition(Gdx.graphics.getWidth()*8/9-labels.get(4).getWidth(),Gdx.graphics.getHeight()*2/5);
                buttons.get(4).setPosition(Gdx.graphics.getWidth()*8/9-labels.get(5).getWidth(),Gdx.graphics.getHeight()*4/5);
                buttons.get(5).setPosition(Gdx.graphics.getWidth()*1/5+labels.get(6).getWidth()/2,Gdx.graphics.getHeight()*3/40);
                buttons.get(6).setPosition(Gdx.graphics.getWidth()*3/5+labels.get(7).getWidth()/2,Gdx.graphics.getHeight()*3/40);
                break;
            case 9:
                buttons.get(0).setPosition(labels.get(0).getWidth(),Gdx.graphics.getHeight()*2/5);
                buttons.get(1).setPosition(labels.get(1).getWidth(),Gdx.graphics.getHeight()*4/5);
                buttons.get(2).setPosition(Gdx.graphics.getWidth()*1/5+labels.get(2).getWidth()/2,Gdx.graphics.getHeight()*27/30);
                buttons.get(3).setPosition(Gdx.graphics.getWidth()*1/2+labels.get(3).getWidth()/2,Gdx.graphics.getHeight()*27/30);
                buttons.get(4).setPosition(Gdx.graphics.getWidth()*4/5+labels.get(4).getWidth()/2,Gdx.graphics.getHeight()*27/30);
                buttons.get(5).setPosition(Gdx.graphics.getWidth()*8/9-labels.get(5).getWidth(),Gdx.graphics.getHeight()*2/5);
                buttons.get(6).setPosition(Gdx.graphics.getWidth()*8/9-labels.get(6).getWidth(),Gdx.graphics.getHeight()*4/5);
                buttons.get(7).setPosition(Gdx.graphics.getWidth()*1/5+labels.get(7).getWidth()/2,Gdx.graphics.getHeight()*3/40);
                buttons.get(8).setPosition(Gdx.graphics.getWidth()*3/5+labels.get(8).getWidth()/2,Gdx.graphics.getHeight()*3/40);
                break;
            case 10:
                buttons.get(0).setPosition(labels.get(0).getWidth(),Gdx.graphics.getHeight()*2/5);
                buttons.get(1).setPosition(labels.get(1).getWidth(),Gdx.graphics.getHeight()*4/5);
                buttons.get(2).setPosition(Gdx.graphics.getWidth()*1/5+labels.get(2).getWidth()/2,Gdx.graphics.getHeight()*27/30);
                buttons.get(3).setPosition(Gdx.graphics.getWidth()*1/2+labels.get(3).getWidth()/2,Gdx.graphics.getHeight()*27/30);
                buttons.get(4).setPosition(Gdx.graphics.getWidth()*4/5+labels.get(4).getWidth()/2,Gdx.graphics.getHeight()*27/30);
                buttons.get(5).setPosition(Gdx.graphics.getWidth()*8/9-labels.get(5).getWidth(),Gdx.graphics.getHeight()*2/5);
                buttons.get(6).setPosition(Gdx.graphics.getWidth()*8/9-labels.get(6).getWidth(),Gdx.graphics.getHeight()*4/5);
                buttons.get(7).setPosition(Gdx.graphics.getWidth()*1/5+labels.get(7).getWidth()/2,Gdx.graphics.getHeight()*3/40);
                buttons.get(8).setPosition(Gdx.graphics.getWidth()*1/2+labels.get(8).getWidth()/2,Gdx.graphics.getHeight()*3/40);
                buttons.get(9).setPosition(Gdx.graphics.getWidth()*4/5+labels.get(9).getWidth()/2,Gdx.graphics.getHeight()*3/40);
                break;
        }
    }
}
