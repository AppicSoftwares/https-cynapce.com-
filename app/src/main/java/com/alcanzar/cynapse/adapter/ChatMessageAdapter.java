package com.alcanzar.cynapse.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.model.ChatMessege_Setter_Getter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.SimpleViewHolder> {

    private final Context mContext;
    ArrayList<ChatMessege_Setter_Getter> chatMessege_setter_getters;
    LayoutInflater mLayoutInflater;
    int totalItem = 0;
    //String userIdMyFriend = "";

    public class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Binder {
//        TextView message_userr;
        //   ImageView imageSendbyuser;

        public SimpleViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

//            message_userr = (TextView) view.findViewById(R.id.message_userr);
//            imageSendbyuser = (ImageView) view.findViewById(R.id.imageSendbyuser);
        }

        @Override
        public void onClick(View v) {


        }

        @Override
        public void bindItem(int position) {

        }
    }
        public class TextViewHolder extends SimpleViewHolder {
        TextView message_userr, date,date_right_chat;
        ImageView imageSendbyuser;

        public TextViewHolder(View itemView) {
            super(itemView);
            message_userr = (TextView) itemView.findViewById(R.id.message_userr);
            //date_right_chat = (TextView) itemView.findViewById(R.id.date_right_chat);
            date = (TextView) itemView.findViewById(R.id.date);

        }

        @Override
        public void bindItem(int position) {
            super.bindItem(position);
           //Log.e("message_userr", "=" + chatMessege_setter_getters.get(position).getMessage());
           // Log.e("getDate", "=" + chatMessege_setter_getters.get(position).getDate());
            message_userr.setText(chatMessege_setter_getters.get(position).getMessage());
//            if((position) % 5 == 0)
//            {
//                if(chatMessege_setter_getters.get(position).getDate().equalsIgnoreCase(Util.getCurrentDate()))
//                {
//                    date_right_chat.setVisibility(View.VISIBLE);
//                    date_right_chat.setText(R.string.today_);
//                }
//                else if(chatMessege_setter_getters.get(position).getDate().equalsIgnoreCase(Util.getYesterdayDateString()))
//                {
//                    date_right_chat.setVisibility(View.VISIBLE);
//                    date_right_chat.setText(R.string.yesterday_);
//                }
//                else
//                {
//                    date_right_chat.setVisibility(View.VISIBLE);
//                    date_right_chat.setText(chatMessege_setter_getters.get(position).getDate());
//                }
//
//            }
//            else
//            {
//                date_right_chat.setVisibility(View.GONE);
//                date_right_chat.setText("");
//            }
//
            date.setText(chatMessege_setter_getters.get(position).getAdd_date());

        }
    }

    public String GetTimeWithAMPMFromTime(String dt) {
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("hh:mm:ss");
            Date date = inFormat.parse(dt);
            SimpleDateFormat outFormat = new SimpleDateFormat("hh:mm a");
            String goal = outFormat.format(date);
            return goal;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }



    public interface Binder {
        public void bindItem(int position);
    }


    public ChatMessageAdapter(Context context, ArrayList<ChatMessege_Setter_Getter> chatMessege_setter_getters/*, String userIdMyFriend*/) {
        mContext = context;
        this.chatMessege_setter_getters = chatMessege_setter_getters;
        //  this.userIdMyFriend = userIdMyFriend;
        totalItem = chatMessege_setter_getters.size() - 1;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ChatMessageAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if (viewType == R.layout.msg_txt_left || viewType == R.layout.msg_txt_right) {
            return new TextViewHolder(view);
        }else {
            return new TextViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(ChatMessageAdapter.SimpleViewHolder holder, final int position) {

        holder.bindItem(position);

    }

    @Override
    public int getItemCount() {
        return chatMessege_setter_getters.size();
    }


    @Override
    public int getItemViewType(int position) {
        Log.e("userIdMyFriend", "userIdMyFriendssfdf" + chatMessege_setter_getters.get(position).getView_type());

        if (chatMessege_setter_getters.get(position).getView_type() == 2) {
            return R.layout.msg_txt_left;

        } else
            return R.layout.msg_txt_right;
        }



    }
