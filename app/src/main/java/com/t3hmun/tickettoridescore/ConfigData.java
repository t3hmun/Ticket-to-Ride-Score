package com.t3hmun.tickettoridescore;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;


/**
 * Data for ConfigActivity state and passing result to main activity.
 */
class ConfigData implements Parcelable {

    private HashMap<Colours, Boolean> players = new HashMap<>(10);
    private GameEdition gameEdition;

    private ConfigData(Parcel in) {
        setGameEdition(GameEdition.values()[in.readInt()]);
        int size = in.readInt();
        Colours[] ord = Colours.values();
        for (int i = 0; i < size; i++) {
            Colours player = ord[in.readInt()];
            Boolean isSet = in.readInt() == 1;
            getPlayers().put(player, isSet);
        }
    }

    public static final Creator<ConfigData> CREATOR = new Creator<ConfigData>() {
        @Override
        public ConfigData createFromParcel(Parcel in) {
            return new ConfigData(in);
        }

        @Override
        public ConfigData[] newArray(int size) {
            return new ConfigData[size];
        }
    };


    /**
     * Initialise with default values.
     */
    ConfigData() {
        setGameEdition(GameEdition.USA);
        Colours[] colours = Colours.values();
        for (Colours player : colours) {
            getPlayers().put(player, false);
        }
    }

    @Override
    public int describeContents() {
        // I don't believe this has any relevance to normal use of Parcelable.
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getGameEdition().ordinal());
        dest.writeInt(getPlayers().size());
        for (HashMap.Entry<Colours, Boolean> entry : getPlayers().entrySet()) {
            dest.writeInt(entry.getKey().ordinal());
            dest.writeInt(entry.getValue() ? 1 : 0);
        }
    }

    HashMap<Colours, Boolean> getPlayers() {
        return players;
    }

    void setPlayers(HashMap<Colours, Boolean> players) {
        this.players = players;
    }

    GameEdition getGameEdition() {
        return gameEdition;
    }

    void setGameEdition(GameEdition gameEdition) {
        this.gameEdition = gameEdition;
    }
}
