package com.lpoo.gameinterface.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.lpoo.gamelogic.Player;
import com.lpoo.gamelogic.SecretHitler;

import java.util.ArrayList;


/**
 * Created by Vasco on 05/06/2016.
 */
public class PlayState extends State {
    private Texture liberalArticle, fascistArticle;

    private Texture background;
    private Texture presidentPlate, presidentPlateRotated;
    private Texture chancellorPlate, chancellorPlateRotated;
    private Texture liberalsWin;
    private Texture fascistsWin;
    private Texture fascistLawTracker, liberalLawTracker;

    private Skin skin;
    private Label label;
    private Label information, fascistInformation, generalInformation;
    private Label fascistLaws, liberalLaws;
    private Stage stage;

    private ArrayList<Button> buttons;
    private ArrayList<Button> cardPickButtons;
    private Button yesButton, noButton, tickButton;
    private Sprite yesSprite, noSprite, tickSprite;
    private SpriteDrawable fascistLawSprite, liberalLawSprite;
    private ArrayList<Label> labels;

    private Vector2 presidentPlatePosition, chancellorPlatePosition;
    private boolean rotatePresident = false , rotateChancellor = false;

    private int exitCounter = 300;

    private boolean hasVoted = false;
    private int lastTurnStatus = -1;

    protected PlayState(GameStateManager gsm, State state) {
        super(gsm, state);
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        background = new Texture("boardgame.jpg");

        fascistLawTracker = new Texture("fascistboard.png");
        liberalLawTracker = new Texture("liberalboard.png");

        liberalArticle = new Texture("liberal_article.png");
        fascistArticle = new Texture("fascist_article.png");

        labels = new ArrayList<Label>();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        for(int i = 0; i < allPlayers.size(); i++){
            label = new Label(allPlayers.get(i).getPosition() + "-" + allPlayers.get(i).getName(), skin);
            label.setFontScale(Gdx.graphics.getWidth()/600);
            labels.add(label);
            stage.addActor(label);
        }
        setPositions(labels);

        fascistLaws = new Label("", skin);
        fascistLaws.setFontScale(Gdx.graphics.getWidth()/800);
        liberalLaws = new Label("", skin);
        liberalLaws.setFontScale(Gdx.graphics.getWidth()/800);

        String role;
        if(me.getRole() == SecretHitler.LIBERAL)
            role = "Liberal";
        else if(me.getRole() == SecretHitler.FASCIST)
            role = "Fascist";
        else role = "Hitler";
        generalInformation = new Label("Your role: " + role, skin);
        generalInformation.setFontScale(Gdx.graphics.getWidth()/800);
        generalInformation.setPosition(Gdx.graphics.getWidth()-generalInformation.getPrefWidth()-20, Gdx.graphics.getHeight() *17/20);
        stage.addActor(generalInformation);

        fascistInformation = new Label("", skin);
        fascistInformation.setFontScale(Gdx.graphics.getWidth()/800);
        fascistInformation.setPosition(Gdx.graphics.getWidth()/19+20, Gdx.graphics.getHeight() *17/20);
        String info = "Fascists are: ";
        for(Player p : allPlayers){
            if(p.getRole() == SecretHitler.FASCIST)
                info = info + p.getPosition() + "-" + p.getName() + ", ";
        }
        info = info + "Hitler is: ";
        for(Player p : allPlayers){
            if(p.getRole() == SecretHitler.HITLER)
                info = info + p.getPosition() + "-" + p.getName();
        }
        fascistInformation.setText(info);
        stage.addActor(fascistInformation);

        //Information label missing size and position, create variable to show or not label
        information = new Label("",skin);
        information.setFontScale(Gdx.graphics.getWidth()/800);
        information.setPosition(50, 50);
        stage.addActor(information);

        //TODO: missing set size and position
        yesSprite = new Sprite(new Texture("yes.png"));
        yesButton = new Button(new SpriteDrawable(yesSprite));
        yesButton.setSize(Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/8);
        yesButton.setX(Gdx.graphics.getWidth()/4 -  yesButton.getWidth());
        yesButton.setY(Gdx.graphics.getHeight() / 2 -  yesButton.getHeight() / 2);
        stage.addActor(yesButton);

        noSprite = new Sprite(new Texture("no.png"));
        noButton = new Button(new SpriteDrawable(noSprite));
        noButton.setSize(Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/8);
        noButton.setX(Gdx.graphics.getWidth() *3/ 4);
        noButton.setY(Gdx.graphics.getHeight() / 2 -  noButton.getHeight() / 2);
        stage.addActor(noButton);

        buttons = new ArrayList<Button>();
        //TODO: maybe change size and little bit positions
        for(int i = 0; i < allPlayers.size(); i++){
            tickSprite = new Sprite(new Texture("tick.png"));
            tickButton = new Button(new SpriteDrawable(tickSprite));
            buttons.add(tickButton);
            stage.addActor(tickButton);
        }
        setTicksPositions(buttons);

        liberalsWin = new Texture ("liberalvictory.png");
        fascistsWin = new Texture ("fascistvictory.png");

        presidentPlatePosition = new Vector2(0, 0);
        chancellorPlatePosition = new Vector2(0, 0);

        presidentPlate = new Texture("president.png");
        presidentPlateRotated = new Texture("presidentrotate.png");
        chancellorPlate = new Texture("chancellor.png");
        chancellorPlateRotated = new Texture("chancellorrotate.png");

        cardPickButtons = new ArrayList<Button>();

        liberalLawSprite = new SpriteDrawable(new Sprite(liberalArticle));
        fascistLawSprite = new SpriteDrawable(new Sprite(fascistArticle));

    }

    @Override
    protected void handleInput() {
        switch(gameInfo.getTurnStatus()){
            case SecretHitler.PICKING_CHANCELLOR:
                if(me.getPosition() == gameInfo.getPresidentIndex()) {
                        for (int i = 0; i < buttons.size(); i++) {
                        if (buttons.get(i).isPressed() && !hasVoted && !buttons.get(i).isDisabled()) {
                            socket.emit("pickedChancellor", i);
                            hasVoted = true;
                        }
                    }
                }
                break;
            case SecretHitler.VOTING_FOR_CHANCELLOR:
                if(!hasVoted){
                    if(yesButton.isPressed() && !yesButton.isDisabled()){
                        socket.emit("voteForChancellor", true);
                        hasVoted = true;
                    }
                    else if(noButton.isPressed() && !noButton.isDisabled()){
                        socket.emit("voteForChancellor", false);
                        hasVoted = true;
                    }
                }
                break;
            case SecretHitler.PRESIDENT_PICKING_LAW:
                if(me.getPosition() == gameInfo.getPresidentIndex()) {
                    for (int i = 0; i < cardPickButtons.size(); i++) {
                        if(cardPickButtons.get(i).isPressed() && !cardPickButtons.get(i).isDisabled() && !hasVoted){
                            socket.emit("removeLaw", i);
                            hasVoted = true;
                        }
                    }
                }
                break;
            case SecretHitler.CHANCELLOR_PICKING_LAW:
                if(me.getPosition() == gameInfo.getChancellorIndex()){
                    for(int i = 0; i < cardPickButtons.size(); i++) {
                        if(cardPickButtons.get(i).isPressed() && !cardPickButtons.get(i).isDisabled() && !hasVoted){
                            socket.emit("pickLaw", i);
                            hasVoted = true;
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void update(float dt) {
        if(lastTurnStatus != gameInfo.getTurnStatus()){
            hasVoted = false;
            lastTurnStatus = gameInfo.getTurnStatus();
            if(gameInfo.getTurnStatus() == SecretHitler.PRESIDENT_PICKING_LAW && me.getPosition() == gameInfo.getPresidentIndex()){
                cardPickButtons.clear();
                for(int i =0; i < gameInfo.getLawOptions().size(); i++){
                    if(gameInfo.getLawOptions().get(i) == SecretHitler.LIBERAL)
                        cardPickButtons.add(new Button(liberalLawSprite));
                    else
                        cardPickButtons.add(new Button(fascistLawSprite));
                    cardPickButtons.get(i).setSize(Gdx.graphics.getWidth() / 8, Gdx.graphics.getHeight() / 8);
                    cardPickButtons.get(i).setX(Gdx.graphics.getWidth() / 6 * (i+1)  -  yesButton.getWidth() / 2);
                    cardPickButtons.get(i).setY(Gdx.graphics.getHeight() / 6  -  yesButton.getHeight() / 2);
                    stage.addActor(cardPickButtons.get(i));
                }
            }
            else if(gameInfo.getTurnStatus() == SecretHitler.CHANCELLOR_PICKING_LAW && me.getPosition() == gameInfo.getChancellorIndex()){
                cardPickButtons.clear();
                for(int i =0; i < gameInfo.getLawOptions().size(); i++){
                    if(gameInfo.getLawOptions().get(i) == SecretHitler.LIBERAL)
                        cardPickButtons.add(new Button(liberalLawSprite));
                    else
                        cardPickButtons.add(new Button(fascistLawSprite));
                    cardPickButtons.get(i).setSize(Gdx.graphics.getWidth() / 8, Gdx.graphics.getHeight() / 8);
                    cardPickButtons.get(i).setX(Gdx.graphics.getWidth() / 6 * (i+1)  -  yesButton.getWidth() / 2);
                    cardPickButtons.get(i).setY(Gdx.graphics.getHeight() / 6  -  yesButton.getHeight() / 2);
                    stage.addActor(cardPickButtons.get(i));
                }
            }
        }
        if(gameInfo.getTurnStatus() == -1){
            gameInfo.setPresidentIndex(0);
            gameInfo.setTurnStatus(SecretHitler.PICKING_CHANCELLOR);
            lastTurnStatus = SecretHitler.PICKING_CHANCELLOR;
        }
        setPlatePosition(allPlayers.get(gameInfo.getPresidentIndex()), presidentPlatePosition, true);
        if(gameInfo.getChancellorIndex() != -1)
            setPlatePosition(allPlayers.get(gameInfo.getChancellorIndex()), chancellorPlatePosition, false);
        handleInput();
        if(gameInfo.getTurnStatus() > SecretHitler.NO_GAME_STATUS){
            exitCounter -= dt;
            if(exitCounter <= 0)
                Gdx.app.exit();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        if(gameInfo.getTurnStatus() == SecretHitler.HITLER_WIN){
            sb.draw(fascistsWin, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            information.setText("Hitler was elected chancellor!");
            information.setPosition(Gdx.graphics.getWidth()/2 - information.getPrefWidth()/2, Gdx.graphics.getHeight()/8);
            information.setColor(0, 0, 0, 1);
            information.draw(sb, 1);
            sb.end();
            return;
        }
        else if(gameInfo.getTurnStatus() == SecretHitler.FASCIST_WIN){
            sb.draw(fascistsWin, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            information.setText("6 Fascist Laws were approved, they control the government!");
            information.setPosition(Gdx.graphics.getWidth()/2 - information.getPrefWidth()/2, Gdx.graphics.getHeight()/8);
            information.setColor(0, 0, 0, 1);
            information.draw(sb, 1);
            sb.end();
            return;
        }
        else if(gameInfo.getTurnStatus() == SecretHitler.LIBERAL_WIN){
            sb.draw(liberalsWin, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            information.setText("5 Liberal Laws were approved, they control the government!");
            information.setPosition(Gdx.graphics.getWidth()/2 - information.getPrefWidth()/2, Gdx.graphics.getHeight()/8);
            information.setColor(0, 0, 0, 1);
            information.draw(sb, 1);
            sb.end();
            return;
        }
        sb.draw(background, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.draw(liberalLawTracker, Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()*5/9, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/4);
        //liberalLaws.setText(gameInfo.getNoLiberalLaws() + "/5");
        //liberalLaws.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()*5/6);
        //liberalLaws.draw(sb, 1);
        for(int i = 0; i < gameInfo.getNoLiberalLaws(); i++){
            //sb.draw(liberalArticle, 2*Gdx.graphics.getWidth()/9 + i * Gdx.graphics.getWidth()/12, Gdx.graphics.getHeight()*5/9, Gdx.graphics.getWidth()/12, Gdx.graphics.getWidth()/12);
        }
        sb.draw(fascistLawTracker, Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()*2/9, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/4);
        liberalLaws.setText(gameInfo.getNoFascistLaws() + "/6");
        liberalLaws.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        liberalLaws.draw(sb, 1);
        for(int i = 0; i < labels.size(); i++){
            labels.get(i).draw(sb,1);
        }
        if(rotatePresident){
            sb.draw(presidentPlateRotated, presidentPlatePosition.x, presidentPlatePosition.y - (Gdx.graphics.getWidth() / 25 /3), Gdx.graphics.getHeight() / (5 * 3/4), Gdx.graphics.getWidth() / 25);
        }
        else {
            sb.draw(presidentPlate, presidentPlatePosition.x, presidentPlatePosition.y - (Gdx.graphics.getHeight() / (5 * 3/4)/3), Gdx.graphics.getWidth() / 25, Gdx.graphics.getHeight() / (5 *3/4));
        }
        if(gameInfo.getChancellorIndex() != -1){
            if(rotateChancellor){
                sb.draw(chancellorPlateRotated, chancellorPlatePosition.x, chancellorPlatePosition.y - (Gdx.graphics.getWidth() / 25 /3), Gdx.graphics.getHeight() / (5 * 3/4), Gdx.graphics.getWidth() / 25);
            }
            else {
                sb.draw(chancellorPlate, chancellorPlatePosition.x, chancellorPlatePosition.y - (Gdx.graphics.getHeight() / (5 * 3/4)/3), Gdx.graphics.getWidth() / 25, Gdx.graphics.getHeight() / (5 *3/4));
            }
        }

        switch(gameInfo.getTurnStatus()){
            case SecretHitler.PICKING_CHANCELLOR:
                if(me.getPosition() == gameInfo.getPresidentIndex() && !hasVoted){
                    for(int i = 0; i < buttons.size(); i++){
                        if(i != me.getPosition()) {
                            buttons.get(i).setDisabled(false);
                            buttons.get(i).draw(sb, 1);
                        }
                    }
                }
                break;
            case SecretHitler.VOTING_FOR_CHANCELLOR:
                if(!hasVoted){
                    information.setText("Voting for chancellor: " + allPlayers.get(gameInfo.getChancellorCandidateIndex()).getPosition() + "-" + allPlayers.get(gameInfo.getChancellorCandidateIndex()).getName());
                    noButton.setDisabled(false);
                    yesButton.setDisabled(false);
                    yesButton.draw(sb, 1);
                    noButton.draw(sb, 1);
                }
                else
                    information.setText("Waiting for other players to vote...");
                information.draw(sb, 1);
                break;
            case SecretHitler.PRESIDENT_PICKING_LAW:
                if(me.getPosition() == gameInfo.getPresidentIndex() && !hasVoted){
                    for(int i = 0; i < cardPickButtons.size(); i++) {
                        cardPickButtons.get(i).setDisabled(false);
                        cardPickButtons.get(i).draw(sb, 1);
                    }
                }
                break;
            case SecretHitler.CHANCELLOR_PICKING_LAW:
                if(me.getPosition() == gameInfo.getChancellorIndex() && !hasVoted){
                    for(int i = 0; i < cardPickButtons.size(); i++) {
                        cardPickButtons.get(i).setDisabled(false);
                        cardPickButtons.get(i).draw(sb, 1);
                    }
                }
                break;
        }
        if(me.getRole() == SecretHitler.FASCIST || (me.getRole() == SecretHitler.HITLER && allPlayers.size() <= 6 ))
            fascistInformation.draw(sb, 1);
        generalInformation.draw(sb, 1);

        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        presidentPlate.dispose();
        chancellorPlate.dispose();
    }

    public void  setPlatePosition(Player p, Vector2 point, boolean isPresident){
        if (labels.get(p.getPosition()).getX()  == 0){
            point.set(labels.get(p.getPosition()).getPrefWidth() + 10, labels.get(p.getPosition()).getY());
            if(isPresident) rotatePresident = false;
            else rotateChancellor = false;
        }
        else if(labels.get(p.getPosition()).getX()  == Gdx.graphics.getWidth() - labels.get(p.getPosition()).getPrefWidth()){
            point.set(Gdx.graphics.getWidth() - labels.get(p.getPosition()).getPrefWidth()*4,labels.get(p.getPosition()).getY());
            if(isPresident) rotatePresident = false;
            else rotateChancellor = false;
        } else if (labels.get(p.getPosition()).getY() == Gdx.graphics.getHeight()/40) {
            point.set(labels.get(p.getPosition()).getX(),Gdx.graphics.getHeight()/10 + 10);
            if(isPresident) rotatePresident = true;
            else rotateChancellor = true;
        }
        else if(labels.get(p.getPosition()).getY() == Gdx.graphics.getHeight() * 14 / 15){
            point.set(labels.get(p.getPosition()).getX(),Gdx.graphics.getHeight() * 13 / 15);
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
        for(int i = 0; i < buttons.size(); i++){
            if (labels.get(i).getX()  == 0)
                buttons.get(i).setPosition(labels.get(i).getX() + labels.get(i).getPrefWidth()/2,labels.get(i).getY() - labels.get(i).getHeight() - 20);
             else if(labels.get(i).getX()  == Gdx.graphics.getWidth() - labels.get(i).getPrefWidth())
                buttons.get(i).setPosition(labels.get(i).getX() + labels.get(i).getPrefWidth()/6,labels.get(i).getY() - labels.get(i).getHeight() - 20);
            else
                buttons.get(i).setPosition(labels.get(i).getX()+labels.get(i).getPrefWidth()+20, labels.get(i).getY());
            buttons.get(i).setHeight(labels.get(i).getHeight());
            buttons.get(i).setWidth(buttons.get(i).getHeight());
        }
    }
}
