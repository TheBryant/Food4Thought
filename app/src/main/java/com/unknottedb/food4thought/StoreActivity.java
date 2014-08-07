package com.unknottedb.food4thought;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class StoreActivity extends Activity{
    public static final String EXTRA_STORE = "com.hameat.hameats.store";

    private TextView mDescriptionText;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        mDescriptionText = (TextView)findViewById(R.id.storeDescriptionText);
        mDescriptionText.setText((String)getIntent().getSerializableExtra(EXTRA_STORE));

    }
}
