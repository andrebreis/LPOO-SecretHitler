package com.lpoo.gameinterface.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
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

    private Button create_btn;
    private Button join_btn;
    private Button back_btn;

    private Sprite create_sprite;
    private Sprite join_sprite;
    private Sprite back_sprite;

    public LobbyState(GameStateManager gsm, State state) {
        super(gsm, state);
        background = new Texture("gamelobby.png");

        create_sprite = new Sprite(new Texture("createroom.png"));
        join_sprite = new Sprite(new Texture("joinroom.png"));
        back_sprite = new Sprite(new Texture("back.png"));

        create_btn = new Button(new SpriteDrawable(create_sprite));
        create_btn.setSize(Gdx.graphics.getWidth() * 2 / 5, Gdx.graphics.getHeight() * 43 / 500);
        create_btn.setX(Gdx.graphics.getWidth()*11/20);
        create_btn.setY(Gdx.graphics.getHeight() * 1 / 5);

        join_btn = new Button(new SpriteDrawable(join_sprite));
        join_btn.setSize(Gdx.graphics.getWidth() * 2 / 5, Gdx.graphics.getHeight() * 43 / 500);
        join_btn.setX(Gdx.graphics.getWidth()*11/20);
        join_btn.setY(Gdx.graphics.getHeight() * 57 / 500);

        back_btn = new Button(new SpriteDrawable(back_sprite));
        back_btn.setSize(Gdx.graphics.getWidth() * 2 / 5, Gdx.graphics.getHeight() * 43 / 500);
        back_btn.setX(Gdx.graphics.getWidth()*11/20);
        back_btn.setY(Gdx.graphics.getHeight() * 29 / 1000);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(create_btn);
        stage.addActor(join_btn);
        stage.addActor(back_btn);

    }

    @Override
    public void handleInput() {
        if(create_btn.isPressed()){
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
        if(back_btn.isPressed()){
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
        create_btn.draw(sb, 1);
        join_btn.draw(sb, 1);
        back_btn.draw(sb,1);
        sb.end();
    }

    @Override
    public void dispose() {

        background.dispose();
        create_sprite.getTexture().dispose();
        back_sprite.getTexture().dispose();
        join_sprite.getTexture().dispose();

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
