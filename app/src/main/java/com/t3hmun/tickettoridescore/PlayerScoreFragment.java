package com.t3hmun.tickettoridescore;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerScoreFragment extends Fragment {

    private static final String ARG_CONFIG = "config";
    private static final String ARG_COLOUR_NUM = "colour";
    private static final String ARG_DATA = "data";
    private ConfigData config;
    private PlayerData data;
    private int colourNum;
    private LayoutInflater inflater;
    private PlayerScoreChangeListener scoreChangeListener;
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
        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        rootPane = (LinearLayout) rootView.findViewById(R.id.root_pane);

        loadArgs();

        // The init methods generate the UI parts according to game edition and restore state.

        initRouteViews();
        initTicketViews();


        initRemainingTrains();
        initRemainingStations();

        initLongestRoute();

        return rootView;
    }

    private void initLongestRoute() {
        CheckBox lrCheck = (CheckBox) rootPane.findViewById(R.id.longest_route_check);
        lrCheck.setChecked(data.isLongestRoute());
        lrCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setLongestRoute(isChecked);
                scoreChanged();
            }
        });
    }

    private void initTicketViews() {
        final LinearLayout ticketPane = (LinearLayout) rootPane.findViewById(R.id.ticket_pane);

        int[] possibleTickets = config.getPossibleTickets();
        final SparseIntArray tickets = data.getTickets();
        final Locale locale = Locale.getDefault();

        for (final int points : possibleTickets) {
            View ticketView = inflater.inflate(R.layout.ticket_score_item, ticketPane, false);
            TextView numText = (TextView) ticketView.findViewById(R.id.points_number);
            final TextView quantityText = (TextView) ticketView.findViewById(R.id.quantity);
            int id = R.id.plus_button;
            Button plusButton = (Button) ticketView.findViewById(id);
            Button minusButton = (Button) ticketView.findViewById(R.id.minus_button);

            ticketPane.addView(ticketView);

            numText.setText(String.format(locale, "%d", points));
            quantityText.setText(String.format(locale, "%d", tickets.get(points)));

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = tickets.get(points) + 1;
                    tickets.put(points, quantity);
                    quantityText.setText(String.format(locale, "%d", quantity));
                    scoreChanged();
                }
            });

            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Negative values are allowed for tickets (failed tickets).
                    int quantity = tickets.get(points) - 1;
                    tickets.put(points, quantity);
                    quantityText.setText(String.format(locale, "%d", quantity));
                    scoreChanged();
                }
            });
        }
    }

    private void initRemainingStations() {
        LinearLayout remainingStationsPane = (LinearLayout) rootPane.findViewById(R.id.remaining_stations_pane);
        // Remaining stations is EURO edition only.
        if (config.getGameEdition() != GameEdition.EURO) {
            remainingStationsPane.setVisibility(View.GONE);
        } else {
            // Set the border colour.
            final LinearLayout numStepperPane = (LinearLayout) remainingStationsPane.findViewById(R.id.num_stepper_pane);
            GradientDrawable border = (GradientDrawable) numStepperPane.getBackground();
            border.setStroke(getResources().getDimensionPixelSize(R.dimen.stepper_border), colourNum);
            numStepperPane.invalidate();

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
                    scoreChanged();
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
                    scoreChanged();
                }
            });
        }
    }

    private void initRemainingTrains() {
        LinearLayout remainingTrainsPane = (LinearLayout) rootPane.findViewById(R.id.remaining_trains_pane);

        // Set the border colour.
        final LinearLayout numStepperPane = (LinearLayout) remainingTrainsPane.findViewById(R.id.num_stepper_pane);
        GradientDrawable border = (GradientDrawable) numStepperPane.getBackground();
        border.setStroke(getResources().getDimensionPixelSize(R.dimen.stepper_border), colourNum);
        numStepperPane.invalidate();

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
                scoreChanged();
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
                scoreChanged();
            }
        });
    }

    private void initRouteViews() {
        LinearLayout routePane = (LinearLayout) rootPane.findViewById(R.id.route_pane);
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
                    scoreChanged();
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
        if (context instanceof PlayerScoreChangeListener) {
            scoreChangeListener = (PlayerScoreChangeListener) context;
        }
    }

    @Override
    public void onPause() {
        // Update data in args.
        // Config does not change so that is not updated.
        getArguments().putParcelable(ARG_DATA, data);
        super.onPause();
    }


    /**
     * A mildly inefficient but simple way of allowing the activity to update scores.
     */
    private void scoreChanged() {
        if (scoreChangeListener != null) {
            scoreChangeListener.ScoreChanged(data);
        }
    }

    public interface PlayerScoreChangeListener {
        void ScoreChanged(PlayerData data);
    }
}


















