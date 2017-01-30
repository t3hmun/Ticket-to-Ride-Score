package com.t3hmun.tickettoridescore;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RouteScoreView extends LinearLayout {

    private TextView carView;
    private TextView pointsView;
    private TextView quantity;
    private ScoreChangeListener listener;
    private LinearLayout numStepperPane;

    public RouteScoreView(Context context) {
        super(context);
        init(context);
    }

    public RouteScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.route_score_item, this);
        carView = (TextView) view.findViewById(R.id.num_of_cars);
        pointsView = (TextView) view.findViewById(R.id.points);
        quantity = (TextView) view.findViewById(R.id.route_edit);
        Button plus = (Button) view.findViewById(R.id.plus_button);
        Button minus = (Button) view.findViewById(R.id.minus_button);
        int padding = getResources().getDimensionPixelSize(R.dimen.item_padding);


        view.setPadding(0, padding, 0, padding);

        plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onChange(true);
                }
            }
        });

        minus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onChange(false);
                }
            }
        });

        numStepperPane = (LinearLayout) view.findViewById(R.id.num_stepper_pane);
    }

    /**
     * @param carriages The length of the train represented by this route.
     * @param points    The number of points awarded by this route.
     * @param colourNum The number of the colour of this player.
     */
    void configure(int carriages, int points, int colourNum) {
        carView.setText(String.valueOf(carriages));
        pointsView.setText("(" + points + " points)");
        GradientDrawable border = (GradientDrawable) numStepperPane.getBackground();
        border.setStroke(getResources().getDimensionPixelSize(R.dimen.stepper_border), colourNum);
        numStepperPane.invalidate();
    }

    void setQuantity(int num) {
        String text = Integer.toString(num);
        quantity.setText(text);
    }

    void setOnChangeListener(ScoreChangeListener listener) {
        this.listener = listener;
    }

    interface ScoreChangeListener {
        void onChange(boolean increment);
    }
}
