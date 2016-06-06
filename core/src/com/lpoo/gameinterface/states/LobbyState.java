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
import com.lpoo.gamelogic.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

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

    private TextField ip;
    private Skin skin;

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
        ip = new TextField("",skin);
        ip.setMessageText("Enter your IP here!");
        ip.setSize(Gdx.graphics.getWidth()*5/11,Gdx.graphics.getHeight()*1/8);
        ip.setPosition(Gdx.graphics.getWidth()*3/4-ip.getWidth()/2, Gdx.graphics.getHeight()*10/12);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(joinServerBtn);
        stage.addActor(backBtn);

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
            gsm.set(new WaitingState(gsm, this));
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
        ip.draw(sb,1);
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
        try {
            socket = IO.socket(ip);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void configSocketEvents(){
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
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting New PlayerID");
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    //if gamestarted ... etc
                    //allPlayers.remove
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
        });
    }
}
