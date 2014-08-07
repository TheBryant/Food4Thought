package com.unknottedb.food4thought;


import android.app.Activity;
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
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.RelativeLayout;


import java.util.ArrayList;

public class TenderActivity extends Activity{
    private static final String LOG = "TenderActivity";
    private static final String PAGE_INDEX = "page";
    private static final String FOOD_KEY = "food";

    private Context mContext;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private int foodNumber;
    ArrayList<Food> mFoods;
    RelativeLayout parentView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tender);

        //TODO: Save state through destroy
        if (savedInstanceState != null){
            foodNumber = savedInstanceState.getInt(PAGE_INDEX, 0);
            mFoods = (ArrayList)savedInstanceState.getParcelableArrayList(FOOD_KEY);
        }else {
            foodNumber = 0;
            mFoods = new ArrayList<Food>() {};
        }
        mContext = TenderActivity.this;
        parentView = (RelativeLayout)findViewById(R.id.tenderLayout);
        mFragmentManager = getFragmentManager();
        // mFragmentTransaction = mFragmentManager.beginTransaction();

        //Hookup to Picture controller
        fillFood();
        addPictures();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(PAGE_INDEX,foodNumber);
        savedInstanceState.putParcelableArrayList(FOOD_KEY, mFoods);
    }

    public void addPictures(){
        addPicture();
        addPicture();
        addPicture();
    }

    public void addPicture(){
        mFragmentTransaction = mFragmentManager.beginTransaction();
        if (!mFoods.isEmpty())
            mFragmentTransaction.add(R.id.tenderLayout, FoodFragment.newInstance(mFoods.remove(0)));
        else {
            parentView.setBackground(getResources().getDrawable(R.drawable.puppy));
        }
        mFragmentTransaction.commit();
    }


    public void fillFood(){
        mFoods = new ArrayList<Food>() {
            {
                add(new Food(getString(R.string.url1), getString(R.string.link1)));
                add(new Food(getString(R.string.url2), getString(R.string.link2)));
                add(new Food(getString(R.string.url3), getString(R.string.link3)));
                add(new Food(getString(R.string.url4), getString(R.string.link4)));
                add(new Food(getString(R.string.url5), getString(R.string.link5)));
            }};
    }


}
