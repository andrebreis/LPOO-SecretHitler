package com.lpoo.gamelogic;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by up201403057 on 06-06-2016.
 */
public class GameBoard {
    private LawTracker fascistTracker, liberalTracker;
   // private ArrayList<Player> players;
    //private Stack<Integer> policyDeck;
    //private ArrayList<Integer> discardPile; TODO: in case anyone wants to see at the end of the game
    private int presidentIndex, chancellorIndex, lastPresident, lastChancellor, noFailedVotes;

    public GameBoard() {
        //this.policyDeck = new Stack<Integer>();
        this.liberalTracker = new LawTracker(SecretHitler.LIBERAL, 5);
        this.fascistTracker = new LawTracker(SecretHitler.FASCIST, 6);
    }

    /*public void createDeck(JSONArray deck){
        for(int i = 0; i < deck.length(); i++){
            try {
                policyDeck.push(deck.getInt(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/

    /*public Player getPresident(){
        return players.get(presidentIndex);
    }*/

    /*public Player getChancellor(){
        if(chancellorIndex == -1)
            return null;
        return players.get(chancellorIndex);
    }*/
}
