package com.t3hmun.tickettoridescore;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class RouteScoreView extends LinearLayout {
    public RouteScoreView(Context context) {
        super(context);
        init(context);
    }

    public RouteScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(Context context) {
        inflate(context, R.layout.route_score_item, this);
    }
}
