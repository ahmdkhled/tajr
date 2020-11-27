package com.tajr.tajr.helper;

import android.util.Log;
import android.view.View;

import androidx.cardview.widget.CardView;

public class FabUtil {

    private boolean rotate;
    private boolean rotateshipper;
    private CardView[] orderActions;
    private CardView[] ShipperActions;
    private View back_drop;

    public FabUtil(CardView[] orderActions, View back_drop) {
        this.orderActions = orderActions;
        this.back_drop = back_drop;
    }

    public FabUtil(CardView[] orderActions, CardView[] shipperActions, View back_drop) {
        this.orderActions = orderActions;
        ShipperActions = shipperActions;
        this.back_drop = back_drop;
    }

    public  void toggle(View view){
        rotate =ViewAnimation.rotateFab(view,!rotate);
        Log.d("FABBTOGGLEE", "toggle: "+rotate);
        if (rotate) {
            for (CardView cardView :orderActions){
                ViewAnimation.showIn(cardView);
            }
            back_drop.setVisibility(View.VISIBLE);
        } else {
            for (CardView cardView :orderActions){
                ViewAnimation.showOut(cardView);
            }
            back_drop.setVisibility(View.GONE);
        }
    }

    public void toggleFabModeShipper(View v) {
        rotateshipper = ViewAnimation.rotateFab(v, !rotateshipper);
        if (rotateshipper) {
            for (CardView cardView :ShipperActions){
                ViewAnimation.showIn(cardView);
            }
            back_drop.setVisibility(View.VISIBLE);
        } else {
            for (CardView cardView :ShipperActions){
                ViewAnimation.showOut(cardView);
            }
            back_drop.setVisibility(View.GONE);
        }
    }
}
