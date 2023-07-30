package com.example.plantrckr;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {
    Context context;

    ArrayList<Transaction> list;

    private String name, provider, sched, fee, date, newsched, adddate;

    public Adapter(Context context, ArrayList<Transaction> list) {
        this.context = context;
        this.list = list;
    }

    @androidx.annotation.NonNull
    @Override
    public myViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull myViewHolder holder, int position) {
        Transaction user = list.get(holder.getAdapterPosition());
        holder.subName.setText(user.getSubName());
        holder.subFee.setText(user.getSubFee());
        holder.subSched.setText(user.getNewSched());

        holder.subEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar= Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.update_item);

                EditText editSub = dialog.findViewById(R.id.edit_subscription);
                EditText editProvider = dialog.findViewById(R.id.edit_provider);
                EditText editDate = dialog.findViewById(R.id.edit_date);
                EditText editFee = dialog.findViewById(R.id.edit_fee);
                EditText editSchedule = dialog.findViewById(R.id.edit_schedule);

                Button editcon = dialog.findViewById(R.id.btn_subscriptionEdit);
                Button editback = dialog.findViewById(R.id.btn_subscriptionback);

                Dialog coneditdialog = new Dialog(context);
                coneditdialog.setContentView(R.layout.confirm);

                TextView text = coneditdialog.findViewById(R.id.confirm_text);
                Button con = coneditdialog.findViewById(R.id.btn_confirmpayment);
                Button can = coneditdialog.findViewById(R.id.btn_cancelpayment);

                editSub.setText((list.get(holder.getAdapterPosition()).getSubName()));
                editProvider.setText((list.get(holder.getAdapterPosition()).getSubProvider()));
                editDate.setText((list.get(holder.getAdapterPosition()).getSubDate()));
                editFee.setText((list.get(holder.getAdapterPosition()).getSubFee()));
                editSchedule.setText((list.get(holder.getAdapterPosition()).getSubSched()));

                editDate.setEnabled(false);
                editSchedule.setEnabled(false);

                text.setText("Confirm Edit");

                editcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Transaction item = list.get(holder.getAdapterPosition());

                        if (!editSub.getText().toString().equals("")) {
                            name = editSub.getText().toString();
                        } else {
                            Toast.makeText(context, "Textbox cannot be blank!", Toast.LENGTH_SHORT).show();
                        }

                        if (!editProvider.getText().toString().equals("")) {
                            provider = editProvider.getText().toString();
                        } else {
                            Toast.makeText(context, "Textbox cannot be blank!", Toast.LENGTH_SHORT).show();
                        }

                        if (!editFee.getText().toString().equals("")) {
                            fee = editFee.getText().toString();
                        } else {
                            Toast.makeText(context, "Textbox cannot be blank!", Toast.LENGTH_SHORT).show();
                        }

                        if (!editSchedule.getText().toString().equals("")) {
                            sched = editSchedule.getText().toString();
                        } else {
                            Toast.makeText(context, "Textbox cannot be blank!", Toast.LENGTH_SHORT).show();
                        }

                        if (!editProvider.getText().toString().equals("") && !editProvider.getText().toString().equals("") && !editDate.getText().toString().equals("")
                                && !editFee.getText().toString().equals("") && !editSchedule.getText().toString().equals("") ) {

                            try {
                                Date dates = dateFormat.parse(date);
                                Date currently = new Date();

                                int day = Integer.parseInt(sched);
                                calendar.setTime(dates);

                                while (!calendar.getTime().after(currently)) {
                                    calendar.add(Calendar.DAY_OF_MONTH, (day));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            coneditdialog.show();

                            con.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("transaction");
                                    databaseRef.child(item.getSubId()).child("subName").setValue(name);
                                    databaseRef.child(item.getSubId()).child("subProvider").setValue(provider);
                                    databaseRef.child(item.getSubId()).child("subFee").setValue(fee);
                                    databaseRef.child(item.getSubId()).child("subSched").setValue(sched);

                                    Toast.makeText(context, "Update Successful!", Toast.LENGTH_SHORT).show();
                                    coneditdialog.dismiss();
                                    dialog.dismiss();

                                    notifyDataSetChanged();
                                    list.clear();

                                }
                            });

                            can.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    coneditdialog.dismiss();
                                }
                            });


                        } else {
                            Toast.makeText(context, "Textbox cannot be blank!", Toast.LENGTH_SHORT).show();
                        }

                    }

                });

                editback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        holder.subDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transaction item = list.get(holder.getAdapterPosition());

                Dialog condeldialog = new Dialog(context);
                condeldialog.setContentView(R.layout.confirm);

                TextView text = condeldialog.findViewById(R.id.confirm_text);
                Button delcon = condeldialog.findViewById(R.id.btn_confirmpayment);
                Button delcan = condeldialog.findViewById(R.id.btn_cancelpayment);

                text.setText("Confirm Delete");

                condeldialog.show();

                delcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("transaction");
                        databaseRef.child(item.getSubId()).removeValue();

                        notifyDataSetChanged();
                        list.clear();
                        condeldialog.dismiss();
                    }
                });

                delcan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        condeldialog.dismiss();
                    }
                });

            }

        });

        holder.subPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transaction item = list.get(holder.getAdapterPosition());

                name = item.getSubName();
                provider = item.getSubProvider();
                date = item.getSubDate();
                fee = item.getSubFee();

                Dialog conpaydialog = new Dialog(context);
                conpaydialog.setContentView(R.layout.confirm);

                TextView text = conpaydialog.findViewById(R.id.confirm_text);
                Button paycon = conpaydialog.findViewById(R.id.btn_confirmpayment);
                Button paycan = conpaydialog.findViewById(R.id.btn_cancelpayment);

                text.setText("Confirm Payment");

                paycon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
                        String userid = users.getUid();

                        History myData = new History();
                        long timestamp = System.currentTimeMillis();
                        myData.setTimestamp(timestamp);

                        ref.child(userid).child("balance").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String value = String.valueOf(dataSnapshot.getValue());

                                    double currentBalance = Double.parseDouble(value);
                                    double amountToGive = Double.parseDouble(fee);
                                    double newBalance = currentBalance - amountToGive;

                                    if (newBalance < 0 ) {
                                        Toast.makeText(context, "Balance is not sufficient!", Toast.LENGTH_SHORT).show();
                                    } else {

                                        Calendar calendar = Calendar.getInstance();

                                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                        String currentDate = dateFormat.format(calendar.getTime());

                                        DatabaseReference data1 = FirebaseDatabase.getInstance().getReference("history").push();

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        String hisId = data1.getKey();
                                        String userId = user.getUid();

                                        data1.child("hisId").setValue(hisId);
                                        data1.child("userId").setValue(userId);
                                        data1.child("transName").setValue(name);
                                        data1.child("transProvider").setValue(provider);
                                        data1.child("transDate").setValue(currentDate);
                                        data1.child("transFee").setValue(fee);
                                        data1.child("timestamp").setValue(timestamp);

                                        DatabaseReference data2 = FirebaseDatabase.getInstance().getReference("transaction");

                                        try {;
                                            newsched = item.getNewSched();
                                            adddate = item.getSubSched();

                                            Date dates = dateFormat.parse(newsched);
                                            int day = Integer.parseInt(adddate);

                                            calendar.setTime(dates);
                                            calendar.add(Calendar.DAY_OF_MONTH, (day));

                                            Date updatedDate = calendar.getTime();
                                            String updatedDateString = dateFormat.format(updatedDate);

                                            String newSched = updatedDateString;

                                            data2.child(item.getSubId()).child("newSched").setValue(newSched);
                                            notifyDataSetChanged();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        DatabaseReference data3 = FirebaseDatabase.getInstance().getReference("users");
                                        data3.child(userid).child("balance").setValue(newBalance);
                                        Toast.makeText(context, "Paid Successfully!", Toast.LENGTH_SHORT).show();

                                        conpaydialog.dismiss();
                                        list.clear();
                                        notifyDataSetChanged();

                                    }

                                } else {
                                    Toast.makeText(context, "No Value", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                            }
                        });


                    }
                });

                paycan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {conpaydialog.dismiss();
                    }
                });

                conpaydialog.show();


            }

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView subName, subFee, subSched;
        public Button subEdit, subDelete, subPay;


        public myViewHolder(@NonNull View ItemView) {
            super(ItemView);

            subName = itemView.findViewById(R.id.tv_subname);
            subFee = itemView.findViewById(R.id.tv_fee);
            subSched = itemView.findViewById(R.id.tv_Schedule);
            subPay = itemView.findViewById(R.id.tv_paybtn);
            subEdit = itemView.findViewById(R.id.tv_editbtn);
            subDelete = itemView.findViewById(R.id.tv_deletebtn);

        }

    }

    public void setData(ArrayList<Transaction> newData) {
        list = newData;
        notifyDataSetChanged();
    }

}


