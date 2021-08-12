package com.alcanzar.cynapse.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.MyConferenceDetails2;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.MyConferenceModel.BookingDetail;
import com.alcanzar.cynapse.model.MyConferenceModel.MyConferenceDetailsModel;

import java.util.ArrayList;

public class MyConferenceDetails2Adapter extends RecyclerView.Adapter<MyConferenceDetails2Adapter.MyConferenceDetails2AdapterVH> {

    private Context context;
    private ArrayList<MyConferenceDetailsModel> arrayList;
    private ArrayList<BookingDetail> bookingDetailList;

    public MyConferenceDetails2Adapter(Context context, ArrayList<MyConferenceDetailsModel> arrayList,ArrayList<BookingDetail> bookingDetailList) {
        this.context = context;
        this.bookingDetailList = bookingDetailList;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyConferenceDetails2AdapterVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_conference_details2_item, viewGroup, false);
        return new MyConferenceDetails2AdapterVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyConferenceDetails2AdapterVH holder, int i) {

        holder.userNametotCountTv.setText(arrayList.get(i).getNoOfSeats());
        holder.userNameTv.setText(arrayList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyConferenceDetails2AdapterVH extends RecyclerView.ViewHolder {
        TextView userNameTv;
        TextView userNametotCountTv;

        public MyConferenceDetails2AdapterVH(@NonNull View itemView) {
            super(itemView);

            userNametotCountTv = itemView.findViewById(R.id.userNametotCountTv);
            userNameTv = itemView.findViewById(R.id.userNameTv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    displayTicketBookedDetails(context,arrayList.get(getAdapterPosition()).getBookingConferenceId());
                }
            });
        }
    }

    private void displayTicketBookedDetails(Context context,String bookingID)
    {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.my_conference_details3_);
        Window window = dialog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        bookingDetailList = ((MyConferenceDetails2)context).databaseHelper.getBookingDetails(DatabaseHelper.TABLE_BOOKING_DETAILS,bookingID);

        MyConferenceDetails3Adapter adapter2  = new MyConferenceDetails3Adapter(context,bookingDetailList);

        RecyclerView recyclerView2 =  dialog.findViewById(R.id.recycleView);
        recyclerView2.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        recyclerView2.setAdapter(adapter2);
        dialog.show();
    }

}
