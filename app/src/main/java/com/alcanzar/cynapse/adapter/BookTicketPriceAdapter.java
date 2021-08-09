package com.alcanzar.cynapse.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.model.ConferencePackageModel;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.Util;

import java.util.ArrayList;

public class BookTicketPriceAdapter extends RecyclerView.Adapter<BookTicketPriceAdapter.MyViewHolder> {
    ArrayList<ConferencePackageModel> modalArrayList;
    Context mCons;
    public int selectedPosition=0;
    View itemViewFragment;
    int percent=0;
    ArrayList<String> arrayList;
    float seat=0f;

    String mem,stud;
    public BookTicketPriceAdapter(Context mCons, ArrayList<ConferencePackageModel> modalArrayList, View itemViewFragment,ArrayList<String> arrayList,String mem,String stud,float seat) {

        this.itemViewFragment=itemViewFragment;
        this.mCons=mCons;
        this.modalArrayList=modalArrayList;
        this.arrayList=arrayList;
        this.mem=mem;
        this.stud=stud;
        this.seat=seat;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cust_book_ticket_layout,parent,false);
        return new MyViewHolder(view);
    }


    //    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
//        try{
//            holder.dayText.setText(modalArrayList.get(position).getConference_pack_day());
//            holder.priceText.setText(modalArrayList.get(position).getConference_pack_charge());
//            AppCustomPreferenceClass.writeString(mCons,AppCustomPreferenceClass.checkSelectStateAdapter+position,modalArrayList.get(position).getIsSelect());
//
//            if(selectedPosition==position){
//                Log.e("absjcbjlas",position+"");
//                holder.radioButtonDay.setChecked(true);
//                modalArrayList.get(position).setIsSelect("1");
//                holder.txt_promocode.setText("");
//                holder.lnr_41.setVisibility(View.GONE);
//                holder.adapterTotalPrice.setText(modalArrayList.get(position).getConference_pack_charge());
////            subtractPriceData(holder,position);
//                AppCustomPreferenceClass.writeString(mCons,AppCustomPreferenceClass.checkSelectStateAdapter+position,modalArrayList.get(position).getIsSelect());
//            addPriceData(holder,position);
////                addPriceData
//                checkDateCondition(holder);
//            }else {
////            holder.radioButtonDay.setChecked(false);
//                holder.radioButtonDay.setChecked(false);
//                modalArrayList.get(position).setIsSelect("0");
//                holder.txt_promocode.setText("");
//                holder.lnr_41.setVisibility(View.GONE);
//                AppCustomPreferenceClass.writeString(mCons,AppCustomPreferenceClass.checkSelectStateAdapter+position,modalArrayList.get(position).getIsSelect());
////            subtractPriceData(holder,position);
//                checkDateCondition(holder);
//            }
//            holder.radioButtonDay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    holder.radioAny.setChecked(false);
//                    if(modalArrayList.get(position).getIsSelect().equals("1")){
//                        subtractPriceData(holder,position);
////                  holder.adapterTotalPrice.setText();
//                        dataUpdate(-1);
//
//                    }else if(modalArrayList.get(position).getIsSelect().equals("0")){
////
////                   modalArrayList.get(position).setIsSelect("1");
////                   holder.txt_promocode.setText("");
////                   holder.lnr_41.setVisibility(View.GONE);
////                   AppCustomPreferenceClass.writeString(mCons,AppCustomPreferenceClass.checkSelectStateAdapter+position,modalArrayList.get(position).getIsSelect());
////                   addPriceData(holder,position);
////                   checkDateCondition(holder);
//                        dataUpdate(position);
//
//
//
//                    }
//                }
//            });
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        try {
            holder.dayText.setText(modalArrayList.get(position).getConference_pack_day());
            holder.priceText.setText(modalArrayList.get(position).getConference_pack_charge());
            holder.radioButtonDay.setChecked(false);
            AppCustomPreferenceClass.writeString(mCons, AppCustomPreferenceClass.checkSelectStateAdapter + position, modalArrayList.get(position).getIsSelect());
            Log.e("asfaksbfka",modalArrayList.get(position).getIsSelect());
            holder.radioButtonDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    holder.radioAny.setChecked(false);
                    if(modalArrayList.get(position).getIsSelect().equals("1")){
                        holder.radioButtonDay.setChecked(false);
                        AppConstantClass.change="0";
                        modalArrayList.get(position).setIsSelect("0");
                        holder.txt_promocode.setText("");
                        holder.txt_taxamnt.setText("");
                        holder.lnr_41.setVisibility(View.GONE);
                        AppCustomPreferenceClass.writeString(mCons,AppCustomPreferenceClass.checkSelectStateAdapter+position,modalArrayList.get(position).getIsSelect());

                        subtractPriceData(holder,position);
                        checkConcession(holder);
//                        checkDateCondition(holder);

                        float amu=Float.parseFloat(holder.textTotalPriceFinal_1.getText().toString());
                        holder.txt_total_amount.setText(Util.decimalNumberRound((seat*amu)));
                    }else if(modalArrayList.get(position).getIsSelect().equals("0")){
                        modalArrayList.get(position).setIsSelect("1");
                        holder.radioButtonDay.setChecked(true);
                        holder.txt_promocode.setText("");
                        holder.txt_taxamnt.setText("");
                        holder.lnr_41.setVisibility(View.GONE);
                        AppCustomPreferenceClass.writeString(mCons,AppCustomPreferenceClass.checkSelectStateAdapter+position,modalArrayList.get(position).getIsSelect());

                        addPriceData(holder,position);
                        checkConcession(holder);


                        float amu=Float.parseFloat(holder.textTotalPriceFinal_1.getText().toString());
                        holder.txt_total_amount.setText(Util.decimalNumberRound((seat*amu)));

                    }
                }
            });

//            holder.radioButtonDay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    holder.radioAny.setChecked(false);
//                    if (modalArrayList.get(position).getIsSelect().equals("1")) {
//
//                        holder.radioButtonDay.setChecked(false);
//                        modalArrayList.get(position).setIsSelect("0");
//
//                        AppCustomPreferenceClass.writeString(mCons, AppCustomPreferenceClass.checkSelectStateAdapter + position, modalArrayList.get(position).getIsSelect());
//                        try {
//                            subtractPriceData(holder, position);
//                            checkDateCondition(holder);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    } else if (modalArrayList.get(position).getIsSelect().equals("0")) {
//                        modalArrayList.get(position).setIsSelect("1");
//                        AppCustomPreferenceClass.writeString(mCons, AppCustomPreferenceClass.checkSelectStateAdapter + position, modalArrayList.get(position).getIsSelect());
//                        try {
//                            addPriceData(holder, position);
//                            checkDateCondition(holder);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }
//            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void checkDateCondition(MyViewHolder holder) {
        try {
            String[] c_date1 = AppConstantClass.currentDate1.split("-"),
                    c_date2 = AppConstantClass.dataTest.split("-");

            int x = Integer.parseInt(c_date1[0]) - Integer.parseInt(c_date2[0]);
            int y = Integer.parseInt(c_date1[1]) - Integer.parseInt(c_date2[1]);
            int z = Integer.parseInt(c_date1[2]) - Integer.parseInt(c_date2[2]);
            Log.e("jabdsjc", x + "," + y + "," + z);
            if (x >= 0 && y >= 0 || z > 0) {
                holder.linearLayout1.setVisibility(View.VISIBLE);
                holder.linearLayout2.setVisibility(View.VISIBLE);
                setPerDataInAmount(holder);

            }else {
                holder.linearLayout1.setVisibility(View.GONE);
                holder.linearLayout2.setVisibility(View.GONE);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void checkConcession(MyViewHolder holder){
        holder.lnr_afer_promoprice_1.setVisibility(View.GONE);
        float amu1 = Float.parseFloat(holder.adapterTotalPrice.getText() + "");
        if (arrayList.contains("4")) {
            Log.d("kdkdkdkdskdsk111", "iiii");
            if(stud.equals("")){
                stud="0";
            }
            amu1 = amu1 - (Float.parseFloat(stud) * amu1) / 100;
            holder.lnr_consession.setVisibility(View.VISIBLE);
            holder. txt_amnt_rupe.setVisibility(View.VISIBLE);
            holder.txt_amnt_conces.setText("Amount after the student concession" + " " + stud + "%" + " " + "is");
            holder.txt_aft_concess.setText(amu1 + "");

        } else {
            Log.d("kdkdkdkdskdsk222", "iiii");
            if(mem.equals("")){
                mem="0";
            }
            amu1 = amu1 - (Float.parseFloat(mem) * amu1) / 100;
            holder.lnr_consession.setVisibility(View.VISIBLE);
            holder.txt_amnt_rupe.setVisibility(View.VISIBLE);
            holder.txt_amnt_conces.setText("Amount after the member concession" + " " + mem + "%" + " " + "is");
            holder.txt_aft_concess.setText(amu1 + "");
        }
        holder.textTotalPriceFinal.setText(amu1 + "");
        holder.textTotalPriceFinal_1.setText(amu1 + "");
        checkDateCondition(holder);
        if (R.id.rButton1 == holder.radioGroupData.getCheckedRadioButtonId()) {
            amu1 = Float.parseFloat(holder.textTotalPriceFinal.getText()+"" )+ Float.parseFloat(holder.accommodationPrice.getText() + "");
            Log.e("Aefqefasdgwerg","asfasfa"+amu1);
            holder.textTotalPriceFinal.setText(amu1 + "");
            holder.textTotalPriceFinal_1.setText(amu1 + "");
//                holder.charg_extra.setText(amu+"");
        }




    }

    public void setPerDataInAmount(MyViewHolder holder) {

        percent=Integer.parseInt(holder.textPerPrice.getText()+"");

//        if (!holder.radioAny.isChecked()){

//            float amu= Float.parseFloat(holder.adapterTotalPrice.getText()+"");
//            float b=(amu*percent)/100;
//            amu=amu+b;

//            if(R.id.rButton1== holder.radioGroupData.getCheckedRadioButtonId()){
//                amu=amu+Float.parseFloat(holder.accommodationPrice.getText()+"");
//            }
//           holder. textTotalPriceFinal.setText(amu+"");
        float amu = Float.parseFloat(holder.textTotalPriceFinal.getText() + "");

        Log.e("Aefqef1000000", ((Float.parseFloat(mem) * amu) / 100)+"");

//            if(arrayList.contains("4"))
//            {
//                amu = amu -((Float.parseFloat(stud) * amu) / 100);
//                holder.lnr_consession.setVisibility(View.VISIBLE);
//                holder.txt_amnt_rupe.setVisibility(View.VISIBLE);
//                holder.txt_amnt_conces.setText("Amount after the student concession"+" "+stud+"%" +" "+"is");
//                holder.txt_aft_concess.setText(Util.decimalNumberRound(amu));
//
//            }else {
//                amu = amu - ((Float.parseFloat(mem) * amu) / 100);
//                holder.lnr_consession.setVisibility(View.VISIBLE);
//                holder.txt_amnt_rupe.setVisibility(View.VISIBLE);
//                holder.txt_amnt_conces.setText("Amount after the member concession"+" "+mem+"%"+" "+"is");
//                holder.txt_aft_concess.setText(Util.decimalNumberRound(amu));
//            }
//            Log.e("Aefqef44444", amu + "");

//            float b = (amu * percent) / 100;
//            amu = amu + b;
//            Log.e("Aefqef446666", amu + "");
        Log.e("Aefqef", amu + "");

        float b = (amu * percent) / 100;
        amu = amu + b;
        Log.e("Aefqef446666", amu + "");
        holder.charg_extra.setText(Util.decimalNumberRound(amu));
        holder.textTotalPriceFinal.setText(Util.decimalNumberRound(amu));
        holder.textTotalPriceFinal_1.setText(Util.decimalNumberRound(amu));
//            if(!AppConstantClass.prom_price_1.equals("")) {
//                amu=amu-Float.parseFloat(AppConstantClass.prom_price_1);
//            }
//        }
    }

    private void subtractPriceData(MyViewHolder holder, int position) {

//        holder.textTotalPriceFinal.setText(Util.decimalNumberRound(t_price));
//        holder.textTotalPriceFinal_1.setText(Util.decimalNumberRound(t_price));

        try{
            float priceA= Float.parseFloat(modalArrayList.get(position).getConference_pack_charge());
            float priceB= Float.parseFloat(holder.adapterTotalPrice.getText()+"");
            float t_price=Math.abs(priceA-priceB);

            holder.adapterTotalPrice.setText(Util.decimalNumberRound(t_price));


        }catch (NumberFormatException ne)
        {
            ne.printStackTrace();
        }

    }

    private void addPriceData(MyViewHolder holder, int position) {
        try{
            float priceA= Float.parseFloat(modalArrayList.get(position).getConference_pack_charge());
            float priceB= Float.parseFloat(holder.adapterTotalPrice.getText()+"");
            float t_price=priceA+priceB;

            holder.adapterTotalPrice.setText(Util.decimalNumberRound(t_price));


        }catch (NumberFormatException ne)
        {
            ne.printStackTrace();
        }

//        if(R.id.rButton1==holder.radioGroupData.getCheckedRadioButtonId()){
//            t_price=t_price+Integer.parseInt(holder.accommodationPrice.getText()+"");
//        }
//
//        holder.textTotalPriceFinal.setText(Util.decimalNumberRound(t_price));
//
////        if(!AppConstantClass.prom_price_1.equals("")) {
////            t_price=t_price-Float.parseFloat(AppConstantClass.prom_price_1);
////        }
//        holder.textTotalPriceFinal_1.setText(Util.decimalNumberRound(t_price));
    }
    @Override
    public int getItemCount() {
        return modalArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButtonDay;
        TextView dayText,priceText,adapterTotalPrice,textTotalPriceFinal,accommodationPrice,textPerPrice,txt_promocode,textTotalPriceFinal_1;
        RadioGroup radioGroupData;
        RadioButton radioAny;
        LinearLayout linearLayout1,linearLayout2,lnr_41,lnr_consession,lnr_afer_promoprice_1;
        TextView txt_amnt_conces,txt_aft_concess,txt_amnt_rupe,txt_total_amount,txt_taxamnt,charg_extra;
        public MyViewHolder(View itemView) {
            super(itemView);
            radioButtonDay=itemView.findViewById(R.id.radioButtonDay);
            dayText=itemView.findViewById(R.id.dayText);
            lnr_afer_promoprice_1=itemViewFragment.findViewById(R.id.lnr_afer_promoprice_1);
            priceText=itemView.findViewById(R.id.priceText);
            adapterTotalPrice=itemViewFragment.findViewById(R.id.adapterTotalPrice);
            textTotalPriceFinal=itemViewFragment.findViewById(R.id.textTotalPriceFinal);
            textTotalPriceFinal_1=itemViewFragment.findViewById(R.id.textTotalPriceFinal_1);
            radioGroupData=itemViewFragment.findViewById(R.id.radioGroupData);
            accommodationPrice=itemViewFragment.findViewById(R.id.accommodationPrice);
            radioAny=itemViewFragment.findViewById(R.id.radioAny);
            textPerPrice=itemViewFragment.findViewById(R.id.textPerPrice);
            linearLayout1=itemViewFragment.findViewById(R.id.dateLayout);
            linearLayout2=itemViewFragment.findViewById(R.id.chargeper);
            lnr_41=itemViewFragment.findViewById(R.id.lnr_41);
            txt_promocode=itemViewFragment.findViewById(R.id.txt_promocode);
            lnr_consession=itemViewFragment.findViewById(R.id.lnr_consession);
            txt_amnt_conces=itemViewFragment.findViewById(R.id.txt_amnt_conces);
            txt_aft_concess=itemViewFragment.findViewById(R.id.txt_aft_concess);
            txt_amnt_rupe=itemViewFragment.findViewById(R.id.txt_amnt_rupe);
            txt_total_amount=itemViewFragment.findViewById(R.id.txt_total_amount);
            txt_taxamnt = itemViewFragment.findViewById(R.id.txt_taxamnt);
            charg_extra=itemViewFragment.findViewById(R.id.charg_extra);
        }
    }
    public void dataUpdate() {
//        modalArrayList.removeAll(AppConstantClass.notyListModal);
        for(int i=0;i<modalArrayList.size();i++){
            modalArrayList.get(i).setIsSelect("0");
        }
//        modalArrayList.addAll( AppConstantClass.notyListModal);
        // selectedPosition=position;
        Log.e("absjcbjlas","notyfiyChange");
        notifyDataSetChanged();
//        notify();
    }
}
