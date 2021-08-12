package com.alcanzar.cynapse.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.MessageActivity;
import com.alcanzar.cynapse.api.ChangeMessageStatusApi;
import com.alcanzar.cynapse.model.NotificationDashBoardModel;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatAdapterViewHolder>
{
    private Context context;
    private int rowLayout;
    private ArrayList<NotificationDashBoardModel> arrayList;
    public  ChatAdapter(Context context,int rowLayout,ArrayList<NotificationDashBoardModel> arrayList){
        this.context = context;
        this.rowLayout =rowLayout;
        this.arrayList = arrayList;
    }

    public class ChatAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView userName,msg,time;
        ImageView db_tick;
        public ChatAdapterViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            msg = itemView.findViewById(R.id.msg);
            time = itemView.findViewById(R.id.time);
            db_tick = itemView.findViewById(R.id.db_tick);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(arrayList.get(getAdapterPosition()).getStatus().equalsIgnoreCase("2"))
                    {
                        try {
                            ChangeMessageStatusApi(arrayList.get(getAdapterPosition()).getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                        Intent intent = new Intent(context, MessageActivity.class);
                        intent.putExtra("sender_id",arrayList.get(getAdapterPosition()).getSender_id());
                        intent.putExtra("prod_id",arrayList.get(getAdapterPosition()).getProduct_id());
                        intent.putExtra("reciever_id",arrayList.get(getAdapterPosition()).getReciever_id());
                         intent.putExtra("chat_id",arrayList.get(getAdapterPosition()).getChat_id());
                        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                }
            });
        }
    }
    @Override
    public ChatAdapter.ChatAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout,parent,false);
        return new ChatAdapterViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ChatAdapter.ChatAdapterViewHolder holder, int position) {
        holder.userName.setText(arrayList.get(position).getName());
        holder.msg.setText(arrayList.get(position).getMsg());
        holder.time.setText(arrayList.get(position).getTime());
        if(arrayList.get(position).getStatus().equalsIgnoreCase("1"))
        {
            holder.db_tick.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.db_tick.setVisibility(View.GONE);
        }

    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    private void ChangeMessageStatusApi(String s) throws JSONException {


        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("message_id", s);

        // params.put("device_id", getDeviceID(context));
        header.put("Cynapse", params);
        new ChangeMessageStatusApi(context, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSENOT", response.toString());
                    if (res_code.equals("1")) {

                        // MyToast.toastLong((Activity) context, res_msg);
                    } else {
                        // MyToast.toastLong(context, res_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
            }
        };
    }
}
