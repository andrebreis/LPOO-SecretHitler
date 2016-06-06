package com.lpoo.gamelogic;

/**
 * Created by up201403057 on 06-06-2016.
 */

public class Player {

    private String name, id;
    private int position;
    private int party, role;
    private boolean playing;

    public Player(String name){
        this.id = "";
        this.name = name;
        this.position = -1;
        this.playing = true;
        this.party = this.role = -1;
    }

    public Player(String id, String name, int position){
        this(name);
        this.id = id;
        this.position = position;
    }

    public void setRole(int role) {
        this.role = this.party = role;
        if(this.role == SecretHitler.HITLER)
            this.party = SecretHitler.FASCIST;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public int getPosition() {
        return position;
    }

    public int getRole() {
        return role;

    }

    public int getParty() {
        return party;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", position=" + position +
                '}';
    }
}