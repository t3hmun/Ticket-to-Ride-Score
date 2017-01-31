package com.t3hmun.tickettoridescore;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;

class PlayerData implements Parcelable {

    public static final Creator<PlayerData> CREATOR = new Creator<PlayerData>() {
        @Override
        public PlayerData createFromParcel(Parcel in) {
            return new PlayerData(in);
        }

        @Override
        public PlayerData[] newArray(int size) {
            return new PlayerData[size];
        }
    };

    // These must be final because listeners capture these vars.
    private final Colours playerColour;
    private final SparseIntArray routes = new SparseIntArray(10);
    private final SparseIntArray tickets = new SparseIntArray(10);
    private int remainingTrains;
    private int remainingStations;
    private boolean longestRoute;

    PlayerData(Colours playerColour) {
        this.playerColour = playerColour;
    }

    private PlayerData(Parcel in) {
        readSparseIntArray(in, routes);
        readSparseIntArray(in, tickets);
        setRemainingTrains(in.readInt());
        setRemainingStations(in.readInt());
        setLongestRoute(in.readInt() == 1);
        playerColour = Colours.values()[in.readInt()];

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        writeSparseIntArray(dest, routes);
        writeSparseIntArray(dest, tickets);
        dest.writeInt(getRemainingTrains());
        dest.writeInt(getRemainingStations());
        dest.writeInt(isLongestRoute() ? 1 : 0);
        dest.writeInt(playerColour.ordinal());
    }

    private void writeSparseIntArray(@NonNull Parcel dest, SparseIntArray sip) {
        int len = sip.size();
        dest.writeInt(len);
        for (int i = 0; i < len; i++) {
            dest.writeInt(sip.keyAt(i));
            dest.writeInt(sip.valueAt(i));
        }
    }

    private void readSparseIntArray(@NonNull Parcel in, SparseIntArray sip) {
        int len = in.readInt();
        for (int i = 0; i < len; i++) {
            int pos = in.readInt();
            int val = in.readInt();
            sip.append(pos, val);
        }
    }

    int calcuateScore(SparseIntArray routeScores) {
        int total = 0;
        for (int i = 0; i < routes.size(); i++) {
            int routeLen = routes.keyAt(i);
            int quantity = routes.get(routeLen);
            int points = routeScores.get(routeLen);
            int score = quantity * points;
            total += score;
        }

        for (int i = 0; i < tickets.size(); i++) {
            int points = tickets.keyAt(i);
            int quantity = tickets.get(points);
            int score = points * quantity;
            total += score;
        }

        total += remainingTrains;
        total += remainingStations * 4;
        return total;
    }

    SparseIntArray getRoutes() {
        return routes;
    }

    SparseIntArray getTickets() {
        return tickets;
    }

    int getRemainingStations() {
        return remainingStations;
    }

    int getRemainingTrains() {
        return remainingTrains;
    }

    void setRemainingTrains(int remainingTrains) {
        this.remainingTrains = remainingTrains;
    }

    void setRemainingStations(int remainingStations) {
        this.remainingStations = remainingStations;
    }

    boolean isLongestRoute() {
        return longestRoute;
    }

    void setLongestRoute(boolean longestRoute) {
        this.longestRoute = longestRoute;
    }

    Colours getPlayerColour() {
        return playerColour;
    }
}