package com.example.jadynai.globalwidget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_global_widget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalWidget.makeText("通知栏测试").show();
            }
        });
    }
}
