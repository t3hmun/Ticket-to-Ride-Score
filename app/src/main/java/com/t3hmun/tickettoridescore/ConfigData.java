package com.t3hmun.tickettoridescore;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;

import java.util.HashMap;


/**
 * Data for ConfigActivity state and passing result to main activity.
 */
class ConfigData implements Parcelable {

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

    private final HashMap<Colours, Boolean> players = new HashMap<>(10);
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

    GameEdition getGameEdition() {
        return gameEdition;
    }

    void setGameEdition(GameEdition gameEdition) {
        this.gameEdition = gameEdition;
    }


    /**
     * Gets the trains to points mapping for the selected game edition.
     *
     * @return Length to points mapping.
     */
    SparseIntArray getRouteScores() {
        int[] lengths;
        int[] points;
        switch (gameEdition) {
            default:
            case USA:
                lengths = new int[]{1, 2, 3, 4, 5, 6};
                points = new int[]{1, 2, 4, 7, 10, 15};
                break;
            case EURO:
                lengths = new int[]{1, 2, 3, 4, 6, 8};
                points = new int[]{1, 2, 4, 7, 15, 21};
        }
        SparseIntArray map = new SparseIntArray(lengths.length);
        for (int i = 0; i < lengths.length; i++) {
            map.append(lengths[i], points[i]);
        }

        return map;
    }


    /**
     * Gets the possible ticket point scores for the edition of the game.
     *
     * @return An array of possible point scores.
     */
    int[] getPossibleTickets() {
        int[] tickets;
        switch (gameEdition) {
            default:
            case USA:
                tickets = new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 16, 17, 20, 21};
                break;
            case EURO:
                tickets = new int[]{5, 6, 7, 8, 9, 10, 11, 12, 13, 20, 21};
        }

        return tickets;
    }
}
