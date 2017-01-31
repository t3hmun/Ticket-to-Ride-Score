package com.t3hmun.tickettoridescore;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerScoreFragment extends Fragment {

    private static final String ARG_CONFIG = "config";
    private static final String ARG_COLOUR_NUM = "colour";
    private static final String ARG_DATA = "data";
    private ConfigData config;
    private LinearLayout routePane;
    private LinearLayout ticketPane;
    private Activity activity;
    private PlayerData data;
    private int colourNum;
    private LinearLayout remainingStationsPane;
    private LinearLayout remainingTrainsPane;
    private LinearLayout rootPane;

    /**
     * Must have no parameters to allow fragment restore to work.
     * The FragmentManager will always persist the arguments bundle
     */
    public PlayerScoreFragment() {

    }

    /**
     * Returns a new instance of this fragment with the relevant data bundled in.
     */
    public static PlayerScoreFragment newInstance(Colours playerColour, ConfigData config, Integer colourNum) {
        PlayerScoreFragment fragment = new PlayerScoreFragment();
        Bundle args = new Bundle();
        PlayerData data = new PlayerData(playerColour);
        args.putParcelable(ARG_DATA, data);
        args.putParcelable(ARG_CONFIG, config);
        args.putInt(ARG_COLOUR_NUM, colourNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        rootPane = (LinearLayout) rootView.findViewById(R.id.root_pane);
        routePane = (LinearLayout) rootPane.findViewById(R.id.route_pane);
        ticketPane = (LinearLayout) rootPane.findViewById(R.id.ticket_pane);
        remainingStationsPane = (LinearLayout) rootPane.findViewById(R.id.remaining_stations_pane);
        remainingTrainsPane = (LinearLayout) rootPane.findViewById(R.id.remaining_trains_pane);

        loadArgs();

        initRouteViews();
        initTicketViews();


        initRemainingTrains();
        initRemainingStations();

        return rootView;
    }

    private void initTicketViews() {

    }

    private void initRemainingStations() {
        // Remaining stations is EURO edition only.
        if (config.getGameEdition() != GameEdition.EURO) {
            remainingStationsPane.setVisibility(View.GONE);
        } else {
            final String quantity = Integer.toString(data.getRemainingStations());
            final TextView quantityView = (TextView) remainingStationsPane.findViewById(R.id.quantity);
            quantityView.setText(quantity);
            final Button plus = (Button) remainingStationsPane.findViewById(R.id.plus_button);
            final Button minus = (Button) remainingStationsPane.findViewById(R.id.minus_button);

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int remainingStations = data.getRemainingStations() + 1;
                    data.setRemainingStations(remainingStations);
                    String text = Integer.toString(remainingStations);
                    quantityView.setText(text);
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int remainingStations = data.getRemainingStations() - 1;
                    if (remainingStations < 0) return;
                    data.setRemainingStations(remainingStations);
                    String text = Integer.toString(remainingStations);
                    quantityView.setText(text);
                }
            });
        }
    }

    private void initRemainingTrains() {
        final String quantity = Integer.toString(data.getRemainingTrains());
        final TextView quantityView = (TextView) remainingTrainsPane.findViewById(R.id.quantity);
        quantityView.setText(quantity);
        final Button plus = (Button) remainingTrainsPane.findViewById(R.id.plus_button);
        final Button minus = (Button) remainingTrainsPane.findViewById(R.id.minus_button);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int remainingTrains = data.getRemainingTrains() + 1;
                data.setRemainingTrains(remainingTrains);
                String text = Integer.toString(remainingTrains);
                quantityView.setText(text);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int remainingTrains = data.getRemainingTrains() - 1;
                if (remainingTrains < 0) return;
                data.setRemainingTrains(remainingTrains);
                String text = Integer.toString(remainingTrains);
                quantityView.setText(text);
            }
        });
    }

    private void initRouteViews() {
        SparseIntArray routeScores = config.getRouteScores();
        final SparseIntArray routeData = data.getRoutes();
        for (int i = 0; i < routeScores.size(); i++) {
            final int cars = routeScores.keyAt(i);
            final int points = routeScores.get(cars);
            final RouteScoreView rsv = new RouteScoreView(getContext());
            rsv.configure(cars, points, colourNum);
            if (routeData.get(cars, -1) == -1) {
                routeData.put(cars, 0);
            }
            rsv.setQuantity(routeData.get(cars));

            rsv.setOnChangeListener(new RouteScoreView.ScoreChangeListener() {
                @Override
                public void onChange(boolean increment) {
                    int val = routeData.get(cars) + (increment ? 1 : -1);
                    if (val < 0) val = 0;
                    routeData.put(cars, val);
                    rsv.setQuantity(routeData.get(cars));
                }
            });

            routePane.addView(rsv);
        }
    }

    private void loadArgs() {
        Bundle args = getArguments();
        config = args.getParcelable(ARG_CONFIG);
        data = args.getParcelable(ARG_DATA);
        colourNum = args.getInt(ARG_COLOUR_NUM);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof Activity)) return;
        activity = (Activity) context;
    }

    @Override
    public void onPause() {
        // Update data in args.
        // Config does not change so that is not updated.
        getArguments().putParcelable(ARG_DATA, data);
        super.onPause();
    }
}


















