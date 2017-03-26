package com.km.democircularstepper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnEarn;
    private Button mBtnReddem;
    private CustomView mCustomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        init();
    }

    private void init() {
        mBtnEarn.setOnClickListener(this);
        mBtnReddem.setOnClickListener(this);
    }

    private void bindViews() {
        mBtnEarn= (Button) findViewById(R.id.btn_earn);
        mBtnReddem= (Button) findViewById(R.id.btn_redeem);
        mCustomView= (CustomView) findViewById(R.id.custom_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_earn:
                mCustomView.incrementStep();
                break;
            case R.id.btn_redeem:
                mCustomView.reset();
                break;
        }

    }
}
