package com.lpoo.gamelogic;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by up201403057 on 06-06-2016.
 */
public class SecretHitler {

    public static final int LIBERAL = 0, FASCIST = 1, HITLER = 2;
    public static final int PICKING_CHANCELLOR = 0, VOTING_FOR_CHANCELLOR = 1, PRESIDENT_PICKING_LAW = 2, CHANCELLOR_PICKING_LAW = 3, LAW_ACTIONS = 4, CHAOS = 9;
    public static final int NO_GAME_STATUS = 5, FASCIST_WIN = 6, HITLER_WIN = 7, LIBERAL_WIN = 8;

    private int noFascistLaws = 0, noLiberalLaws = 0, presidentIndex = 0, chancellorIndex = -1, chancellorCandidateIndex = -1, noFailedVotes = 0;

    private int turnStatus = -1;

    private ArrayList<Integer> lawOptions;

    public SecretHitler(){
        this.lawOptions = new ArrayList<Integer>();
    };

    public void setLawOptions(JSONArray options) throws JSONException {
        lawOptions = new ArrayList<Integer>();
        for(int i = 0; i < options.length(); i++){
            lawOptions.add(options.getInt(i));
        }
    }

    public ArrayList<Integer> getLawOptions() {
        return lawOptions;
    }

    public int getNoFailedVotes() {
        return noFailedVotes;
    }

    public void setNoFailedVotes(int noFailedVotes) {
        this.noFailedVotes = noFailedVotes;
    }

    public void setChancellorCandidateIndex(int chancellorCandidateIndex) {
        this.chancellorCandidateIndex = chancellorCandidateIndex;
    }

    public int getNoFascistLaws() {
        return noFascistLaws;
    }

    public int getChancellorCandidateIndex() {
        return chancellorCandidateIndex;
    }

    public void setNoFascistLaws(int noFascistLaws) {
        this.noFascistLaws = noFascistLaws;
    }

    public int getNoLiberalLaws() {
        return noLiberalLaws;
    }

    public void setNoLiberalLaws(int noLiberalLaws) {
        this.noLiberalLaws = noLiberalLaws;
    }

    public int getPresidentIndex() {
        return presidentIndex;
    }

    public void setPresidentIndex(int presidentIndex) {
        this.presidentIndex = presidentIndex;
    }

    public int getChancellorIndex() {
        return chancellorIndex;
    }

    public void setChancellorIndex(int chancellorIndex) {
        this.chancellorIndex = chancellorIndex;
    }

    public int getTurnStatus() {
        return turnStatus;
    }

    public void setTurnStatus(int turnStatus) {
        this.turnStatus = turnStatus;
    }
}
