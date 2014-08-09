package com.unknottedb.food4thought;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.Window;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TenderActivity extends Activity{
    private static final String TAG = "TenderActivity";
    private static final String PAGE_INDEX = "page";
    private static final String FOOD_KEY = "food";

    private Context mContext;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private int foodNumber;
    ArrayList<Food> mFoods;
    Button mStartButton;
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
        new CallApiTask().execute("asd");
        mStartButton = (Button)findViewById(R.id.startButton);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupStart();
                mStartButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(PAGE_INDEX,foodNumber);
        savedInstanceState.putParcelableArrayList(FOOD_KEY, mFoods);
    }

    public void setupStart(){
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
        Log.d(TAG, "Foods left: "+mFoods.size());
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




    class CallApiTask extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog = new ProgressDialog(TenderActivity.this);
        InputStream inputStream = null;
        Boolean done = false;

        protected void onPreExecute() {
            progressDialog.setMessage("Downloading your data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    CallApiTask.this.cancel(true);
                }
            });
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String url_select = "http://food2fork.com/api/search?key=07ee419780de3a265d22549ca1601317&sort=t&page=1";
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
            JSONObject jObj = null;
            try {
                // Set up HTTP post
                // HttpClient is more then less deprecated. Need to change to URLConnection
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(url_select);
                httpPost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                // Read content & Log
                inputStream = httpEntity.getContent();
            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsupportedEncodingException", e1.toString());
                e1.printStackTrace();
            } catch (ClientProtocolException e2) {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }
            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }
                inputStream.close();
                String json = sBuilder.toString();
                jObj = new JSONObject(json);
            } catch (Exception e) {
                Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
            }
            return jObj;
        }

        protected void onPostExecute(JSONObject jObj) {
            //parse JSON data
            try {
                JSONArray jArray = jObj.getJSONArray("recipes");
                for(int i=0; i < jArray.length(); i++) {
                    JSONObject mJObj = jArray.getJSONObject(i);
                    String storeURL = mJObj.getString("source_url");
                    String photoURL = mJObj.getString("image_url");

                    Food newFood = new Food(photoURL, storeURL);
                    mFoods.add(newFood);
                    Log.d(TAG,photoURL);
                }
                this.progressDialog.dismiss();
                done = true;
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            }
        }
    }


}
