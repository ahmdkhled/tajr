package com.greyeg.tajr.view.over;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.greyeg.tajr.R;
import com.greyeg.tajr.activities.EmptyCallActivity;
import com.greyeg.tajr.models.CurrentOrderData;


import static com.greyeg.tajr.records.CallsReceiver.inOrderActivity;

public class FloatLayout extends Service {

    private WindowManager windowmanager;
    private View floatingview;
    public String Name = "";
    public static boolean run = false;

    public FloatLayout() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        run = true;
        floatingview = LayoutInflater.from(this).inflate(
                R.layout.layout_over, null);

        final WindowManager.LayoutParams params;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    0,
                    PixelFormat.TRANSLUCENT);
        }

        params.gravity = Gravity.TOP | Gravity.LEFT; // Initially view will be
        // added to top-left

        // corner
        params.x = 0;
        params.y = 100;

        windowmanager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowmanager.addView(floatingview, params);


        final View exstra = floatingview
                .findViewById(R.id.extra_view);
        final View logo = floatingview
                .findViewById(R.id.product_image);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exstra.getVisibility() == View.GONE) {
                    exstra.setVisibility(View.VISIBLE);
                } else {
                    exstra.setVisibility(View.GONE);
                }

            }
        });

        final LinearLayout name = floatingview
                .findViewById(R.id.name);


        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RunAnimation(name.getChildAt(0).getId(), name.getChildAt(1).getId());
            }
        });

        final LinearLayout state = floatingview
                .findViewById(R.id.state);

        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RunAnimation(state.getChildAt(0).getId(), state.getChildAt(1).getId());
            }
        });

        logo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                stopSelf();
                return false;
            }
        });

        logo.setOnTouchListener(
                new View.OnTouchListener() {
                    private int initialX;
                    private int initialY;
                    private float initialTouchX;
                    private float initialTouchY;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                initialX = params.x;
                                initialY = params.y;

                                initialTouchX = event.getRawX();
                                initialTouchY = event.getRawY();
                                return false;
                            case MotionEvent.ACTION_UP:
                                int Xdiff = (int) (event.getRawX() - initialTouchX);
                                int Ydiff = (int) (event.getRawY() - initialTouchY);

                                if (Xdiff < 10 && Ydiff < 10) {
//                                    if (isViewCollapsed()) {
//                                        collapsedView.setVisibility(View.GONE);
//                                        expandedView.setVisibility(View.VISIBLE);
//                                    }
                                }
                                return false;
                            case MotionEvent.ACTION_MOVE:
                                params.x = initialX
                                        + (int) (event.getRawX() - initialTouchX);
                                params.y = initialY
                                        + (int) (event.getRawY() - initialTouchY);

                                windowmanager
                                        .updateViewLayout(floatingview, params);
                                return false;
                        }
                        return false;
                    }
                });

        floatingview.findViewById(R.id.root_container).setOnTouchListener(
                new View.OnTouchListener() {
                    private int initialX;
                    private int initialY;
                    private float initialTouchX;
                    private float initialTouchY;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                initialX = params.x;
                                initialY = params.y;

                                initialTouchX = event.getRawX();
                                initialTouchY = event.getRawY();
                                return false;
                            case MotionEvent.ACTION_UP:
                                int Xdiff = (int) (event.getRawX() - initialTouchX);
                                int Ydiff = (int) (event.getRawY() - initialTouchY);

                                if (Xdiff < 10 && Ydiff < 10) {
//                                    if (isViewCollapsed()) {
//                                        collapsedView.setVisibility(View.GONE);
//                                        expandedView.setVisibility(View.VISIBLE);
//                                    }
                                }
                                return false;
                            case MotionEvent.ACTION_MOVE:
                                params.x = initialX
                                        + (int) (event.getRawX() - initialTouchX);
                                params.y = initialY
                                        + (int) (event.getRawY() - initialTouchY);

                                windowmanager
                                        .updateViewLayout(floatingview, params);
                                return false;
                        }
                        return false;
                    }
                });

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        }

        final InputMethodManager[] imm = {null};

        TextView goOrder = floatingview.findViewById(R.id.goOrderActivity);

        goOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inOrderActivity) {
                    Toast.makeText(FloatLayout.this, "والله حضرتك انا ف  صفحةالاوردر دلوقتي ", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), EmptyCallActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                }
            }
        });
        EditText clientName, status;

        clientName = floatingview.findViewById(R.id.client_name);
        clientName.setText(CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getClientName());

        status = floatingview.findViewById(R.id.order_status);
        status.setText(CurrentOrderData.getInstance().getCurrentOrderResponse().getCheckType());

//        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    if(imm[0] ==null){
//                        imm[0] = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    }
//
//                    imm[0].toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
//                }
//            }
//        });

//        editText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imm[0] = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm[0].toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
//            }
//        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        run = false;
        if (floatingview != null)
            windowmanager.removeView(floatingview);

    }

    private void RunAnimation(int id1, int id2) {

        TextView tv = (TextView) floatingview.findViewById(id1);
        FrameLayout bg = (FrameLayout) floatingview.findViewById(id2);

        if (tv.getVisibility() == View.VISIBLE) {
            Animation a = AnimationUtils.loadAnimation(this, R.anim.bottom_sheet_fad_out);
            a.reset();
            tv.clearAnimation();
            tv.startAnimation(a);
            tv.setVisibility(View.GONE);
            bg.setBackgroundResource(R.drawable.ic_background_gray);
        } else {
            Animation a = AnimationUtils.loadAnimation(this, R.anim.bottom_sheet_fad_in);
            a.reset();
            tv.clearAnimation();
            tv.startAnimation(a);
            tv.setVisibility(View.VISIBLE);
            bg.setBackgroundResource(R.drawable.ic_background_gray_down);

        }

    }


}