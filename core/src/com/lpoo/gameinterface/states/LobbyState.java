package com.lpoo.gameinterface.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.lpoo.gamelogic.Player;
import com.lpoo.gamelogic.SecretHitler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by Vasco on 05/06/2016.
 */
public class LobbyState extends State{

    private Stage stage;

    private Texture background;

    private Button joinServerBtn;
    private Button backBtn;

    private Sprite joinServerSprite;
    private Sprite backSprite;

    private TextField ipField;
    private Skin skin;

    private State thisState;
    private State waitingState;

    public LobbyState(GameStateManager gsm, State state) {
        super(gsm, state);
        gameInfo = new SecretHitler();

        background = new Texture("gamelobby.png");

        joinServerSprite = new Sprite(new Texture("joinserver.png"));
        backSprite = new Sprite(new Texture("back.png"));

        joinServerBtn = new Button(new SpriteDrawable(joinServerSprite));
        joinServerBtn.setSize(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*2/7);
        joinServerBtn.setX(Gdx.graphics.getWidth()*3/4-joinServerBtn.getWidth()/2);
        joinServerBtn.setY(Gdx.graphics.getHeight()/2);


        backBtn = new Button(new SpriteDrawable(backSprite));
        backBtn.setSize(Gdx.graphics.getWidth()*1/2,Gdx.graphics.getHeight()*2/7);
        backBtn.setX(Gdx.graphics.getWidth()*3/4-backBtn.getWidth()/2);
        backBtn.setY(Gdx.graphics.getHeight()*1/12);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        ipField = new TextField("",skin);
        ipField.setMessageText("Enter your IP here!");
        ipField.setSize(Gdx.graphics.getWidth()*5/11,Gdx.graphics.getHeight()*1/8);
        ipField.setPosition(Gdx.graphics.getWidth()*3/4- ipField.getWidth()/2, Gdx.graphics.getHeight()*10/12);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(joinServerBtn);
        stage.addActor(backBtn);
        stage.addActor(ipField);

        thisState = this;
    }

    @Override
    public void handleInput() {
        if(joinServerBtn.isPressed()){
            connectSocket();
            configSocketEvents();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            waitingState = new WaitingState(gsm, this);
            gsm.set(waitingState);
            dispose();
        }
        if(backBtn.isPressed()){
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
        joinServerBtn.draw(sb, 1);
        backBtn.draw(sb,1);
        ipField.draw(sb,1);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        joinServerSprite.getTexture().dispose();
        backSprite.getTexture().dispose();

    }

    public void connectSocket(){
        String ip = "http://localhost:8080"; // ipField.getText();
        if(!ipField.getText().equals(""))
            ip = ipField.getText();
        try {
                socket = IO.socket(ip);
                socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void configSocketEvents(){
        //if(!socket.hasListeners(Socket.EVENT_CONNECT))
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            }
        }).on("idAndPosition", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    int position = data.getInt("position");
                    me.setId(id);
                    me.setPosition(position);
                    allPlayers.add(me);
                    socket.emit("playerName", me.getName());
                   // Gdx.app.log("SocketIO", "My ID: " + id + " my name: " + me.getName() + " my position: " + me.getPosition());
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting ID");
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                   // Gdx.app.log("SocketIO", "New Player Connect: " + id);
                    Player newPlayer = new Player(data.getString("id"), data.getString("name"), data.getInt("position"));
                    allPlayers.add(newPlayer);
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting New PlayerID");
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    int position = data.getInt("position");
                    if(!gameStarted){
                        allPlayers.remove(position);
                        for(int i = 0; i < allPlayers.size(); i++)
                            allPlayers.get(i).setPosition(i);
                    }
                    else{
                        allPlayers.get(position).setPlaying(false);
                        //for(Player p : allPlayers)
                        //   System.out.println(p.toString());
                    }
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray objects = (JSONArray) args[0];
                for (int i = 0; i < objects.length(); i++) {
                    try {
                        Player newPlayer = new Player(objects.getJSONObject(i).getString("id"), objects.getJSONObject(i).getString("name"), objects.getJSONObject(i).getInt("position"));
                        allPlayers.add(newPlayer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //players.put(objects.getJSONObject(i).getString("id"), coopPlayer);
                }
            }
        }).on("setPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    gameStarted = true;
                    JSONArray players = data.getJSONArray("players");
                    for(int i = 0; i < players.length(); i++){
                        JSONObject player = players.getJSONObject(i);
                        allPlayers.get(i).setRole(player.getInt("role"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                waitingState.setAdvanceState(true);
                //gsm.set(new PlayState(gsm, thisState));
            }
        }).on("setPresident", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    gameInfo.setPresidentIndex(data.getInt("index"));
                    System.out.println(data.getInt("index"));
                    gameInfo.setChancellorIndex(-1);
                    int approvedLaw = data.getInt("law");
                    if(approvedLaw == SecretHitler.FASCIST)
                        gameInfo.setNoFascistLaws(gameInfo.getNoFascistLaws()+1);
                    else
                        gameInfo.setNoLiberalLaws(gameInfo.getNoLiberalLaws()+1);
                    gameInfo.setTurnStatus(SecretHitler.PICKING_CHANCELLOR);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("initiateChancellorVote", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try{
                    int chancellorToBe = data.getInt("position");
                    gameInfo.setChancellorCandidateIndex(chancellorToBe);
                    //TODO: INFORM WHO IS CHANCELLOR ->POPUP IMAGE OR NEW SCREEN
                    gameInfo.setTurnStatus(SecretHitler.VOTING_FOR_CHANCELLOR);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("chancellorVoteResult", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try{
                    boolean voteSuccessful = data.getBoolean("verdict");

                    JSONObject votes = data.getJSONObject("votes");
                    System.out.println("vote: " + voteSuccessful);
                    if(voteSuccessful){
                        gameInfo.setChancellorIndex(gameInfo.getChancellorCandidateIndex());
                        //gameInfo.setTurnStatus(SecretHitler.PRESIDENT_PICKING_LAW);
                    }
                    else {
                        gameInfo.setNoFailedVotes(gameInfo.getNoFailedVotes() + 1);
                        gameInfo.setChancellorCandidateIndex(-1);
                        gameInfo.setTurnStatus(SecretHitler.PICKING_CHANCELLOR);
                        //if(gameInfo.getNoFailedVotes() == 3) gameInfo.setTurnStatus(SecretHitler.CHAOS); TODO: IMPLEMENT
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("getPresidentOptions", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray data = (JSONArray) args[0];
                try{
                    gameInfo.setLawOptions(data);
                    gameInfo.setTurnStatus(SecretHitler.PRESIDENT_PICKING_LAW);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("getChancellorOptions", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray data = (JSONArray) args[0];
                try{
                    gameInfo.setLawOptions(data);
                    gameInfo.setTurnStatus(SecretHitler.CHANCELLOR_PICKING_LAW);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("endGame", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try{
                    FileHandle resultFile = Gdx.files.local("results.txt");
                    if(!resultFile.exists()) {
                        try {
                            resultFile.file().createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    String gameResult = "";
                    Date date = new Date(TimeUtils.millis());
                    if(data.getInt("victor") == SecretHitler.FASCIST_WIN || data.getInt("victor") == SecretHitler.HITLER_WIN)
                        gameResult = "The fascists won and took over the Europe! Remember this date: " + date + "\n";
                    else
                        gameResult = "The liberals won and saved the Europe once again! Remember this date: " + date + "\n";
                    resultFile.writeString(gameResult,true);
                    gameInfo.setTurnStatus(data.getInt("victor"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
