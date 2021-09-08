package com.kevappsgaming.myapplication.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.kevappsgaming.myapplication.R;

public class ChatActivity extends AppCompatActivity {

    public TextView receiverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        receiverName = (TextView) findViewById(R.id.chatName);
        receiverName.setText(getReceiverName());
        init();
    }

    private void init() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content_chat, new ChatFragment());
        fragmentTransaction.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public String getReceiverName(){
        Intent intent = getIntent();
        String receiverName = intent.getStringExtra("receiverName");
        return receiverName;
    }
}
