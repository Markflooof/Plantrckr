package com.example.plantrckr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.icu.text.Transliterator;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class myAdapter extends RecyclerView.Adapter<myAdapter.newViewHolder> {
    Context contexts;

    static ArrayList<History> mylist;

    private String name, provider, sched, fee, date, newSched;

    public myAdapter(Context contexts, ArrayList<History> mylist) {
        this.contexts = contexts;
        this.mylist = mylist;
    }

    @androidx.annotation.NonNull
    @Override
    public newViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(contexts).inflate(R.layout.history,parent,false);
        return new newViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull newViewHolder holder, int position) {
        History user = mylist.get(holder.getAdapterPosition());
        holder.hisName.setText(user.getTransName());
        holder.hisFee.setText(user.getTransFee());
        holder.hisDate.setText(user.getTransDate());

        holder.hisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(contexts);
                dialog.setContentView(R.layout.mail);

                TextView mail_mailname = dialog.findViewById(R.id.mail_mailname);
                TextView mail_contents = dialog.findViewById(R.id.mail_contents);
                Button mail_back = dialog.findViewById(R.id.btn_mailback);

                String Name = ((mylist.get(holder.getAdapterPosition()).getTransName()));
                String Provider = ((mylist.get(holder.getAdapterPosition()).getTransProvider()));
                String Fee = ((mylist.get(holder.getAdapterPosition()).getTransFee()));
                String Date = ((mylist.get(holder.getAdapterPosition()).getTransDate()));

                String MailName = Name + " (" + Date + ")";
                String MailContents = "You have successfully paid " + Fee + " on " + Date + " to " + Provider + " in availing " + Name;

                mail_mailname.setText(MailName);
                mail_contents.setText(MailContents);

                mail_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                notifyDataSetChanged();
            }

        });

        holder.hisDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                History item = mylist.get(holder.getAdapterPosition());

                Dialog condeldialog = new Dialog(contexts);
                condeldialog.setContentView(R.layout.confirm);

                TextView text = condeldialog.findViewById(R.id.confirm_text);
                Button delcon = condeldialog.findViewById(R.id.btn_confirmpayment);
                Button delcan = condeldialog.findViewById(R.id.btn_cancelpayment);

                text.setText("Confirm Delete");

                condeldialog.show();

                delcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("history");
                        databaseRef.child(item.getHisId()).removeValue();

                        mylist.clear();
                        notifyDataSetChanged();

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


    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }
    public static class newViewHolder extends RecyclerView.ViewHolder{

        TextView hisName, hisFee, hisDate;
        public Button hisView, hisDelete;


        public newViewHolder(@NonNull View ItemView) {
            super(ItemView);

            hisName = itemView.findViewById(R.id.his_transName);
            hisFee = itemView.findViewById(R.id.his_fee);
            hisDate = itemView.findViewById(R.id.his_Schedule);
            hisView = itemView.findViewById(R.id.his_view);
            hisDelete = itemView.findViewById(R.id.his_deletebtn);


        }

    }

    public static void updateData(ArrayList<History> newData) {
        // Sort the new data in descending order based on the timestamp
        Collections.sort(newData, new Comparator<History>() {
            @Override
            public int compare(History item1, History item2) {
                // Sort in descending order based on the timestamp
                return Long.compare(item2.getTimestamp(), item1.getTimestamp());
            }
        });
        mylist = newData;
    }

}