package com.t3hmun.tickettoridescore;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;

class PlayerData implements Parcelable {

    private Colours playerColour;
    private SparseIntArray routes = new SparseIntArray(10);
    private SparseIntArray tickets = new SparseIntArray(10);
    private int remainingTrains = 0;
    private int remainingStations = 0;
    private boolean longestRoute = false;

    PlayerData(Colours playerColour) {
        this.playerColour = playerColour;
    }

    private PlayerData(Parcel in) {
        readSparseIntArray(in, routes);
        readSparseIntArray(in, tickets);
        remainingTrains = in.readInt();
        remainingStations = in.readInt();
        longestRoute = in.readInt() == 1;
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
        dest.writeInt(remainingTrains);
        dest.writeInt(remainingStations);
        dest.writeInt(longestRoute ? 1 : 0);
        dest.writeInt(playerColour.ordinal());
    }

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
}