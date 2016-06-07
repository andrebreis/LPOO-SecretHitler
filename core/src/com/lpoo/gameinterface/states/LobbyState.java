package com.lpoo.gameinterface.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.lpoo.gamelogic.GameBoard;
import com.lpoo.gamelogic.Player;
import com.lpoo.gamelogic.SecretHitler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

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
                Gdx.app.log("SocketIO", "Connected");
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
                    System.out.println(" Adding player" + newPlayer);
                    for(Player p : allPlayers)
                    System.out.println(p);
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
                    System.out.println(position);
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
                    //JSONArray deck = data.getJSONArray("deck");
                    //board = new GameBoard();
                    //board.createDeck(deck);
                    gameStarted = true;
                    JSONArray players = data.getJSONArray("players");
                    for(int i = 0; i < players.length(); i++){
                        JSONObject player = players.getJSONObject(i);
                        //TODO: correct positions if needed
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
                    //TODO: INFORM WHO IS CHANCELLOR ->POPUP IMAGE OR NEW SCREEN
                    gameInfo.setTurnStatus(SecretHitler.VOTING_FOR_CHANCELLOR);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
