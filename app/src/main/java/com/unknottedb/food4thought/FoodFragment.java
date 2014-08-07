package com.unknottedb.food4thought;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Random;


public class FoodFragment extends Fragment {
    private static final String TAG = "FoodFragment";
    public static final String EXTRA_PHOTO = "com.hameat.hameats.photo";
    public static final String EXTRA_URL = "com.hameat.hameats.url";
    private ImageView mImageView;
    private LinearLayout mLayout;
    private int mImageResource;
    int x_cord, y_cord;
    int screenWidth;
    int x_center;
    private Context mContext;
    RelativeLayout mParentView;

    public static FoodFragment newInstance(Food food) {
        Bundle args = new Bundle();
        args.putString(EXTRA_PHOTO, food.getPhotoUrl());
        args.putString(EXTRA_URL, food.getLinkUrl());
        FoodFragment fragment = new FoodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public FoodFragment() {//
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWidth();
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_food, container, false);
        mParentView = (RelativeLayout) container;
        //moveToBack(v);
        v.setLayoutParams(new LinearLayout.LayoutParams((screenWidth - 80), 450));
        v.setX(40);
        v.setY(40);
        v.setRotation(((new Random().nextFloat())*14)-7); //Starts pictures off random tilt
        mImageView = (ImageView)v.findViewById(R.id.foodImageView);
        mLayout = (LinearLayout)v.findViewById(R.id.imageLayout);
         new DownloadImageTask(mImageView).execute(getArguments().getString(EXTRA_PHOTO));

        mLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x_cord = (int) event.getRawX();
                y_cord = (int) event.getRawY();
                v.setX(x_cord - x_center + 0); //Tune this up
                v.setY(y_cord - 400);
                v.setRotation((float) ((x_cord - x_center) / 10));

                if (x_cord < x_center / 4) {
                    showSad(container);
                } else if (x_cord > screenWidth - (x_center / 4)) {
                    showHappy(container);
                } else {
                    clearFaces(container);
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (x_cord < x_center / 4) {
                        mParentView.removeView(v);
                        ((TenderActivity) getActivity()).addPicture();
                        clearFaces(container);
                    } else if (x_cord > screenWidth - (x_center / 4)) {
                        mParentView.removeView(v);
                        ((TenderActivity) getActivity()).addPicture();
                        clearFaces(container);
                        //Intent i = new Intent(mContext, StoreActivity.class);
                        //i.putExtra(StoreActivity.EXTRA_STORE, "StoreID/InfoHere");
                        //startActivity(i);
                        openWebURL(getArguments().getString(EXTRA_URL));
                    } else {
                        v.setX(40);
                        v.setY(40);
                    }
                }
                return true;
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        moveToBack(getView());
    }

    public void showHappy(View parentView){
        parentView.findViewById(R.id.imageHappy).setVisibility(View.VISIBLE);
    }
    public void showSad(View parentView){
        parentView.findViewById(R.id.imageSad).setVisibility(View.VISIBLE);
    }
    public void clearFaces(View parentView){
        parentView.findViewById(R.id.imageSad).setVisibility(View.INVISIBLE);
        parentView.findViewById(R.id.imageHappy).setVisibility(View.INVISIBLE);
    }

    public void getWidth() {
        if (getActivity() != null) {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            x_center = screenWidth / 2;
        }
    }

    private void moveToBack(View currentView){
        ViewGroup vg = ((ViewGroup) currentView.getParent());
        Log.d(TAG, "Index is"+vg.indexOfChild(currentView));
        int index = vg.indexOfChild(currentView);
        for(int i = 0; i<index; i++){
            vg.bringChildToFront(vg.getChildAt(0));
        }
    }

    public void openWebURL( String inURL ) {
        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(inURL) );

        startActivity( browse );
    }

}
