package com.example.administrator.fastjson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String json;
    private static final String TAG=MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String json="{\n" +
                "    \"name\": \"Matilda\",\n" +
                "    \"friends\": [\n" +
                "        {\n" +
                "            \"name\": \"Rufus\",\n" +
                "            \"age\": \"1\",\n" +
                "            \"sex\": 1\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Marty\",\n" +
                "            \"age\": \"2\",\n" +
                "            \"sex\": 2\n" +
                "        }\n" +
                "    ],\n" +
                "    \"age\": \"2\"\n" +
                "}";

        User user=FastJson.parseObject(json,User.class);
        Log.d(TAG,user.getFriends().get(0).getName());
    }
}
