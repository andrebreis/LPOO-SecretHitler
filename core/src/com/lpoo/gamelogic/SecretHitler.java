package com.lpoo.gamelogic;

/**
 * Created by up201403057 on 06-06-2016.
 */
public class SecretHitler {

    public static final int LIBERAL = 0, FASCIST = 1, HITLER = 2;
    public static final int PICKING_CHANCELLOR = 0, VOTING_FOR_CHANCELLOR = 1, PRESIDENT_PICKING_LAW = 2, CHANCELLOR_PICKING_LAW = 3, LAW_ACTIONS = 4;
    public static final int NO_STATUS = 5;

    private int noFascistLaws = 0, noLiberalLaws = 0, presidentIndex = 0, chancellorIndex = -1;

    private int turnStatus = 0;

    public SecretHitler(){};

    public int getNoFascistLaws() {
        return noFascistLaws;
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
