package com.t3hmun.tickettoridescore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RouteScoreView extends LinearLayout {

    private TextView carView;
    private TextView pointsView;

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
    }

    void initNumbers(int carriages, int points) {
        carView.setText(String.valueOf(carriages));
        pointsView.setText("(" + points + " points)");
    }
}
