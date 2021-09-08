package com.kevappsgaming.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kevappsgaming.myapplication.Adapters.ContactAdapter;
import com.kevappsgaming.myapplication.Chat.ChatActivity;
import com.kevappsgaming.myapplication.Database.DatabaseSQL;
import com.kevappsgaming.myapplication.Helper.UserInformation;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactAdapter.ItemClickListener, View.OnClickListener {

    private ContactAdapter adapter;
    public RecyclerView recyclerView;
    private List<UserInformation> contactsList;
    DatabaseReference databaseReference;
    private DatabaseSQL myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_discussion_main);

        myDB = new DatabaseSQL(com.kevappsgaming.myapplication.MainActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.contacts_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactsList = new ArrayList<>();

        adapter = new ContactAdapter(this, contactsList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query query1 = FirebaseDatabase.getInstance().getReference().child("users");
        query1.addListenerForSingleValueEvent(valueEventListener);

        adapter.setClickListener(this);
        adapter.setLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserInformation course = snapshot.getValue(UserInformation.class);
                    contactsList.add(course);
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    @Override
    public void onItemClick(View view, int position){
        //Toast.makeText(MainDiscussionActivity.this, adapter.getItem(position).trim(), Toast.LENGTH_LONG).show();
        myDB.saveReceiverInfo(adapter.getItemUid(position).trim(), adapter.getItemEmail(position).trim(), "0000");
        Intent intent = new Intent(com.kevappsgaming.myapplication.MainActivity.this, ChatActivity.class);
        intent.putExtra("receiverName", adapter.getItemFirstName(position) + " " + adapter.getItemLastName(position));
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position){
        //Toast.makeText(getActivity(), "Long Click", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onResume(){
        super.onResume();
        //getListName();
        //Toast.makeText(ListActivity.this, listname, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view){
    }

}