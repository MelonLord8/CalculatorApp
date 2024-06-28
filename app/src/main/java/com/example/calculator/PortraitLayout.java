package com.example.calculator;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class PortraitLayout extends ConstraintLayout {
    ConstraintSet constraintSet;
    float scale;
    public PortraitLayout(@NonNull Context context) {
        super(context);
        setId(View.generateViewId());

        //Setting the height and the width to match the parent's
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);

        //Making The constraint set and setting its parameters to match the ConstraintLayout
        constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        //Makes the scale used to convert dots to pixels
        scale = getContext().getResources().getDisplayMetrics().density;

        ConstraintLayout buttonSection = new ConstraintLayout(context);
    }

    int convertDpToPx(int dp){
        return ((int) (dp * scale + 0.5f));
    }
}
