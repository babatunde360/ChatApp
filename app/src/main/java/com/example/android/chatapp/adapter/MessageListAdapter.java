package com.example.android.chatapp.adapter;

import android.content.Context;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.chatapp.R;
import com.example.android.chatapp.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Created by tunde on 4/2/2018.
 */

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private List<Message> mMessageList;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;

    public MessageListAdapter(Context context, List<Message> messageList){
       this.mContext = context;
       this.mMessageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        Message message = (Message) mMessageList.get(position);
        if(message.getUsername().equals(mFirebaseUser.getDisplayName())){
            return VIEW_TYPE_MESSAGE_SENT;
        }else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == VIEW_TYPE_MESSAGE_SENT){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent,parent,false);
            return new SentMessageHolder(view);
        }else if(viewType == VIEW_TYPE_MESSAGE_RECEIVED){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received,parent,false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) mMessageList.get(position);
        switch(holder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }



    private class ReceivedMessageHolder extends RecyclerView.ViewHolder{
        TextView messageText,timeText;



        public ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);

        }
        void bind(Message message){
            messageText.setText(message.getMessage());
            timeText.setText( String.valueOf(message.getTimeCreated()));
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder{
        TextView sentMessageText,sentMessageTimeText;
        public SentMessageHolder(View itemView) {
            super(itemView);
            sentMessageText = (TextView) itemView.findViewById(R.id.text_message_sent_body);
            sentMessageTimeText = (TextView) itemView.findViewById(R.id.text_message_sent_time);
        }

        void bind(Message message){
            sentMessageText.setText(message.getMessage());
            sentMessageTimeText.setText(String.valueOf(message.getTimeCreated()));
        }
    }
}
