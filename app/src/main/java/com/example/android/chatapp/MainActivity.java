package com.example.android.chatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.chatapp.adapter.MessageListAdapter;
import com.example.android.chatapp.model.Message;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private LinearLayoutManager mLinearLayoutManager;
    private MessageListAdapter mMessageAdapter;
    private DatabaseReference mMessageDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private ChildEventListener mChildEventListener;
    String mUsername;
    List<Message> messageList;
    private Button mSendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessageDatabaseReference = mFirebaseDatabase.getReference().child("message");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if(mFirebaseUser == null){
            startActivity(new Intent(this,LoginActivity.class));
        }else{
            mUsername = mFirebaseUser.getDisplayName();
        }
        messageList = new ArrayList<>();
        mMessageRecycler =(RecyclerView) findViewById(R.id.rv_message_list);
        mMessageAdapter = new MessageListAdapter(MainActivity.this,messageList);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mMessageRecycler.setLayoutManager(mLinearLayoutManager);
        mMessageRecycler.setAdapter(mMessageAdapter);

        mSendButton = (Button) findViewById(R.id.button_chatbox_send);
        final EditText mMessageEdit = (EditText) findViewById(R.id.edit_text_chatbox);
        Calendar cal = new GregorianCalendar(TimeZone.getDefault());
       final String date = DateUtils.formatDateTime(this, cal.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME);


        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMessageEdit.getText().length()==0){
                   Toast mToast = Toast.makeText(MainActivity.this, "You have not entered any text", Toast.LENGTH_SHORT);
                   mToast.show();
                }
                else{
                Message message = new Message(mMessageEdit.getText().toString(),mUsername,date);
                mMessageDatabaseReference.push().setValue(message);
                mMessageEdit.setText("");
                }
            }
        });

        mChildEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message currentMessage = dataSnapshot.getValue(Message.class);
                 messageList.add(currentMessage);
                mMessageAdapter = new MessageListAdapter(MainActivity.this,messageList);
                mMessageRecycler.swapAdapter(mMessageAdapter,false);
                mMessageRecycler.hasPendingAdapterUpdates();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        mMessageDatabaseReference.addChildEventListener(mChildEventListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                finish();
                startActivity(new Intent(this,LoginActivity.class));

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
