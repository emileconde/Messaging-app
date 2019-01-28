package com.example.android.conde.com.firebasemessaging;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    //data that is returned by firebase
    private ArrayList<DataSnapshot> mSnapshotList;

    //Listens for when data is added to the firebase databse
    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public ChatListAdapter(Activity activity, DatabaseReference databaseReference, String displayName) {
        mActivity = activity;
        //setting it to the location in db where the data comes from
        mDatabaseReference = databaseReference.child("messages");
        mDatabaseReference.addChildEventListener(mListener);
        mDisplayName = displayName;
        mSnapshotList = new ArrayList<>();
    }

    static class ViewHolder{
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return mSnapshotList.size();
    }

    //get and return ann InstantMessage object
    @Override
    public InstantMessage getItem(int position) {
        DataSnapshot snapshot = mSnapshotList.get(position);
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int i) {
        return mSnapshotList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater =
                    (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chat_msg_row, viewGroup, false);
            final ViewHolder holder = new ViewHolder();
            holder.authorName = view.findViewById(R.id.author);
            holder.body = view.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();
            view.setTag(holder);
        }

        final InstantMessage message = getItem(position);
        final ViewHolder holder = (ViewHolder) view.getTag();

        //If the message name is the same as the person who logged in
        boolean isMe = message.getAuthor().equals(mDisplayName);
        setChatRowAppearance(isMe, holder);

        String author = message.getAuthor();
        holder.authorName.setText(author);

        String msg = message.getMessage();
        holder.body.setText(msg);

        return view;
    }


    //Takes care of the styling of a message
    private void setChatRowAppearance(boolean isItMe, ViewHolder holder){
        if(isItMe){
            //Make the layout align to the right
            holder.params.gravity = Gravity.END;
            holder.authorName.setTextColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble2);
        }else {
            //The the layout align the left
            holder.params.gravity = Gravity.START;
            holder.authorName.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }

        //Actually setting the values
        holder.authorName.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);
    }



    public void cleanup(){
        mDatabaseReference.removeEventListener(mListener);
    }


}
