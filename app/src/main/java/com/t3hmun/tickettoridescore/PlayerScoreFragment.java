package com.t3hmun.tickettoridescore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerScoreFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_COLOUR_ORDINAL = "section_number";

    /**
     * Must have no parameters to allow fragment restore to work.
     */
    public PlayerScoreFragment() {
    }

    /**
     * Returns a new instance of this fragment with the relevant data bundled in.
     */
    public static PlayerScoreFragment newInstance(Colours playerColour) {
        PlayerScoreFragment fragment = new PlayerScoreFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLOUR_ORDINAL, playerColour.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //noinspection unused
        Colours playerColour = Colours.values()[getArguments().getInt(ARG_COLOUR_ORDINAL)];
        // TODO: Use playerColour to make some things in this fragment coloured.

        TextView tv = (TextView) rootView.findViewById(R.id.test_text);
        //tv.setText(colour.toString() + " bundled: " + playerColour.toString());

        return rootView;
    }
}
