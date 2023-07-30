package com.example.plantrckr;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity{

    private TextView txt_name, profile_email, profile_firstname, profile_lastname, profile_contact, profile_address, profile_birthday;
    private TextView edit_email, edit_password, edit_password2, edit_firstname, edit_lastname, edit_contact, edit_address, edit_birthday;
    private TextView txt_cash, cash_Amount, cash_Contact;
    private TextView cashout_senContact, cashout_recContact, cashout_Amount;
    public String cash;
    private TextView add_subscription, add_provider, add_date, add_schedule, add_fee;
    private CheckBox showPasswordCheckBox, showPasswordCheckBox2;
    private RecyclerView recyclerView;
    private RecyclerView userhistory;
    private String newSched;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btn_home = findViewById(R.id.btn_home);
        ImageButton btn_add = findViewById(R.id.btn_addsub);
        ImageButton btn_notification = findViewById(R.id.btn_notification);
        ImageButton btn_showprofile = findViewById(R.id.btn_showprofile);
        ImageButton btn_logout = findViewById(R.id.btn_logout);
        Button btn_cashin = findViewById(R.id.btn_cashin);
        Button btn_addcash = findViewById(R.id.btn_addcash);
        Button btn_cashout = findViewById(R.id.btn_cashout);
        Button btn_givecash = findViewById(R.id.btn_givecash);
        Button btn_editAcc = findViewById(R.id.btn_editacc);
        Button btn_conEdit = findViewById(R.id.btn_conEdit);
        Button btn_editBack = findViewById(R.id.btn_editBack);

        Button btn_subscriptionAdd = findViewById(R.id.btn_subscriptionAdd);
        TextView page = findViewById(R.id.page);

        txt_name = findViewById(R.id.txt_name);
        profile_email = findViewById(R.id.profile_email);
        profile_firstname = findViewById(R.id.profile_firstname);
        profile_lastname = findViewById(R.id.profile_lastname);
        profile_contact = findViewById(R.id.profile_contact);
        profile_address = findViewById(R.id.profile_address);
        profile_birthday = findViewById(R.id.profile_birthday);
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        edit_password2 = findViewById(R.id.edit_password2);
        edit_firstname = findViewById(R.id.edit_firstname);
        edit_lastname = findViewById(R.id.edit_lastname);
        edit_contact = findViewById(R.id.edit_contact);
        edit_address = findViewById(R.id.edit_address);
        edit_birthday = findViewById(R.id.edit_birthday);
        txt_cash = findViewById(R.id.txt_cash);
        cash_Contact = findViewById(R.id.cash_Contact);
        cash_Amount = findViewById(R.id.cash_Amount);
        cashout_senContact = findViewById(R.id.cashout_senContact);
        cashout_recContact = findViewById(R.id.cashout_recContact);
        cashout_Amount = findViewById(R.id.cashout_Amount);
        add_subscription = findViewById(R.id.add_subscription);
        add_provider = findViewById(R.id.add_provider);
        add_date = findViewById(R.id.add_date);
        add_fee = findViewById(R.id.add_fee);
        add_schedule = findViewById(R.id.add_schedule);
        recyclerView = findViewById(R.id.userlist);
        userhistory = findViewById(R.id.userhistory);
        showPasswordCheckBox = findViewById(R.id.password_checkbox);
        showPasswordCheckBox2 = findViewById(R.id.password_checkbox2);

        LinearLayout body_home = findViewById(R.id.body_home);
        LinearLayout body_add = findViewById(R.id.body_add);
        LinearLayout body_notification = findViewById(R.id.body_notification);
        LinearLayout body_profile = findViewById(R.id.body_profile);
        LinearLayout body_editAcc = findViewById(R.id.body_editAcc);
        LinearLayout body_cash = findViewById(R.id.body_cash);
        LinearLayout body_cashout = findViewById(R.id.body_cashout);

        btn_logout.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class )));

        edit_email.setEnabled(false);
        edit_contact.setEnabled(false);

        DisplayText();
        lister();

        showPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Toggle the password visibility based on the checkbox state
                if (isChecked) {
                    edit_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    edit_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        showPasswordCheckBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Toggle the password visibility based on the checkbox state
                if (isChecked) {
                    edit_password2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    edit_password2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        btn_cashin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page.setText("CASH IN");

                if (body_cash.getVisibility() == View.GONE) {
                    body_home.setVisibility(View.GONE);
                    body_add.setVisibility(View.GONE);
                    body_notification.setVisibility(View.GONE);
                    body_profile.setVisibility(View.GONE);
                    body_editAcc.setVisibility(View.GONE);
                    body_cash.setVisibility(View.VISIBLE);
                    body_cashout.setVisibility(View.GONE);

                    btn_home.setBackgroundResource(R.drawable.home);
                    btn_add.setBackgroundResource(R.drawable.add);
                    btn_notification.setBackgroundResource(R.drawable.notification);
                    btn_showprofile.setBackgroundResource(R.drawable.profile);

                }

            }

        });

        btn_cashout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page.setText("CASH OUT");

                if (body_cashout.getVisibility() == View.GONE) {
                    body_home.setVisibility(View.GONE);
                    body_add.setVisibility(View.GONE);;
                    body_notification.setVisibility(View.GONE);
                    body_profile.setVisibility(View.GONE);
                    body_editAcc.setVisibility(View.GONE);
                    body_cash.setVisibility(View.GONE);
                    body_cashout.setVisibility(View.VISIBLE);

                    btn_home.setBackgroundResource(R.drawable.home);
                    btn_add.setBackgroundResource(R.drawable.add);
                    btn_notification.setBackgroundResource(R.drawable.notification);
                    btn_showprofile.setBackgroundResource(R.drawable.profile);

                }

            }

        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page.setText("HOME");

                if (body_home.getVisibility() == View.GONE) {
                    body_home.setVisibility(View.VISIBLE);
                    body_add.setVisibility(View.GONE);
                    body_notification.setVisibility(View.GONE);
                    body_profile.setVisibility(View.GONE);
                    body_editAcc.setVisibility(View.GONE);
                    body_cash.setVisibility(View.GONE);
                    body_cashout.setVisibility(View.GONE);

                    btn_home.setBackgroundResource(R.drawable.home_clicked);
                    btn_add.setBackgroundResource(R.drawable.add);
                    btn_notification.setBackgroundResource(R.drawable.notification);
                    btn_showprofile.setBackgroundResource(R.drawable.profile);

                    lister();
                }

            }

        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page.setText("ADD SUBSCRIPTION");

                if (body_add.getVisibility() == View.GONE) {
                    body_home.setVisibility(View.GONE);
                    body_add.setVisibility(View.VISIBLE);
                    body_notification.setVisibility(View.GONE);
                    body_profile.setVisibility(View.GONE);
                    body_editAcc.setVisibility(View.GONE);
                    body_cash.setVisibility(View.GONE);
                    body_cashout.setVisibility(View.GONE);

                    btn_home.setBackgroundResource(R.drawable.home);
                    btn_add.setBackgroundResource(R.drawable.add_clicked);
                    btn_notification.setBackgroundResource(R.drawable.notification);
                    btn_showprofile.setBackgroundResource(R.drawable.profile);
                }

            }
        });

        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page.setText("HISTORY");

                if (body_notification.getVisibility() == View.GONE) {
                    body_home.setVisibility(View.GONE);
                    body_add.setVisibility(View.GONE);
                    body_notification.setVisibility(View.VISIBLE);
                    body_profile.setVisibility(View.GONE);
                    body_editAcc.setVisibility(View.GONE);
                    body_cash.setVisibility(View.GONE);
                    body_cashout.setVisibility(View.GONE);

                    btn_home.setBackgroundResource(R.drawable.home);
                    btn_add.setBackgroundResource(R.drawable.add);
                    btn_notification.setBackgroundResource(R.drawable.notification_clicked);
                    btn_showprofile.setBackgroundResource(R.drawable.profile);

                    lister();
                }

            }
        });

        btn_showprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page.setText("PROFILE");

                if (body_profile.getVisibility() == View.GONE) {
                    body_home.setVisibility(View.GONE);
                    body_add.setVisibility(View.GONE);
                    body_notification.setVisibility(View.GONE);
                    body_profile.setVisibility(View.VISIBLE);
                    body_editAcc.setVisibility(View.GONE);
                    body_cash.setVisibility(View.GONE);
                    body_cashout.setVisibility(View.GONE);

                    btn_home.setBackgroundResource(R.drawable.home);
                    btn_add.setBackgroundResource(R.drawable.add);
                    btn_notification.setBackgroundResource(R.drawable.notification);
                    btn_showprofile.setBackgroundResource(R.drawable.profile_clicked);
                }

            }
        });

        btn_editAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page.setText("EDIT ACCOUNT");

                if (body_editAcc.getVisibility() == View.GONE) {
                    body_home.setVisibility(View.GONE);
                    body_add.setVisibility(View.GONE);
                    body_notification.setVisibility(View.GONE);
                    body_profile.setVisibility(View.GONE);
                    body_editAcc.setVisibility(View.VISIBLE);
                    body_cash.setVisibility(View.GONE);
                    body_cashout.setVisibility(View.GONE);

                    btn_home.setBackgroundResource(R.drawable.home);
                    btn_add.setBackgroundResource(R.drawable.add);
                    btn_notification.setBackgroundResource(R.drawable.notification);
                    btn_showprofile.setBackgroundResource(R.drawable.profile_clicked);
                }

            }
        });

        btn_editBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page.setText("EDIT PROFILE");

                if (body_profile.getVisibility() == View.GONE) {
                    body_home.setVisibility(View.GONE);
                    body_add.setVisibility(View.GONE);
                    body_notification.setVisibility(View.GONE);
                    body_profile.setVisibility(View.VISIBLE);
                    body_editAcc.setVisibility(View.GONE);
                    body_cash.setVisibility(View.GONE);
                    body_cashout.setVisibility(View.GONE);

                    btn_home.setBackgroundResource(R.drawable.home);
                    btn_add.setBackgroundResource(R.drawable.add);
                    btn_notification.setBackgroundResource(R.drawable.notification);
                    btn_showprofile.setBackgroundResource(R.drawable.profile_clicked);
                }

            }
        });

        btn_addcash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBalance();
            }
        });

        btn_givecash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reduceBalance();
            }
        });

        btn_conEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edit_password.getText().toString().trim();
                String confirm = edit_password2.getText().toString().trim();
                if (TextUtils.equals(password,confirm)) {
                    updateProfile();

                    page.setText("PROFILE");

                    if (body_profile.getVisibility() == View.GONE) {
                        body_home.setVisibility(View.GONE);
                        body_add.setVisibility(View.GONE);
                        body_notification.setVisibility(View.GONE);
                        body_profile.setVisibility(View.VISIBLE);
                        body_editAcc.setVisibility(View.GONE);
                        body_cash.setVisibility(View.GONE);

                        btn_home.setBackgroundResource(R.drawable.home);
                        btn_add.setBackgroundResource(R.drawable.add);
                        btn_notification.setBackgroundResource(R.drawable.notification);
                        btn_showprofile.setBackgroundResource(R.drawable.profile_clicked);
                    }
                } else {
                edit_password2.setError("Password do not match");
            }

            }
        });

        btn_subscriptionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubscription();
            }
        });

    }

    private void DisplayText() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        ref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue().toString();
                String password = dataSnapshot.child("password").getValue().toString();
                String firstname = dataSnapshot.child("firstname").getValue().toString();
                String lastname = dataSnapshot.child("lastname").getValue().toString();
                String contact = dataSnapshot.child("contact").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                String birthday = dataSnapshot.child("birthday").getValue().toString();
                String balance = dataSnapshot.child("balance").getValue().toString();

                txt_name.setText(firstname + " " + lastname);
                cash = balance;

                profile_email.setText(email);
                profile_firstname.setText(firstname);
                profile_lastname.setText(lastname);
                profile_contact.setText(contact);
                profile_address.setText(address);
                profile_birthday.setText(birthday);

                double value = Double.parseDouble(balance);
                DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                String formattedAmount = decimalFormat.format(value);
                txt_cash.setText(formattedAmount);

                edit_email.setText(email);
                edit_password.setText(password);
                edit_firstname.setText(firstname);
                edit_lastname.setText(lastname);
                edit_contact.setText(contact);
                edit_address.setText(address);
                edit_birthday.setText(birthday);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String currentDate = dateFormat.format(calendar.getTime());
        add_date.setText(currentDate);

        final DatePickerDialog editbirthday = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the calendar object with the selected date
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Format the selected date and set it to the EditText
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        String selectedDate = dateFormat.format(calendar.getTime());
                        edit_birthday.setText(selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        final DatePickerDialog addsubdate = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the calendar object with the selected date
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Format the selected date and set it to the EditText
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        String selectedDate = dateFormat.format(calendar.getTime());
                        add_date.setText(selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        edit_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editbirthday.show();
            }
        });

        add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addsubdate.show();
            }
        });
    }

    private void updateBalance() {
        String contact = (String) profile_contact.getText();

        String cashcon = cash_Contact.getText().toString().trim();
        String amount = cash_Amount.getText().toString().trim();

        if (TextUtils.isEmpty(cashcon)) {
            cash_Contact.setError("TextBox cannot be blank!");
            return;
        }

        if (TextUtils.isEmpty(amount)) {
            cash_Amount.setError("TextBox cannot be blank!");
            return;
        }

        if ( contact.equals(cash_Contact.getText().toString())) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("users");
            String userid = user.getUid();

            DatabaseReference userRef = databaseRef.child(userid);;

            double currentBalance = Double.parseDouble(cash);
            double amountToAdd = Double.parseDouble((cash_Amount.getText().toString()));

            double newBalance = currentBalance + amountToAdd;

            if (newBalance > 25000) {
                Toast.makeText(getApplicationContext(), "Cash in wallet cannot exceed 25,000", Toast.LENGTH_SHORT).show();
            } else {
                userRef.child("balance").setValue(newBalance);

                cash_Contact.setText("");
                cash_Amount.setText("");
                cash = String.valueOf(newBalance);

                DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                String formattedAmount = decimalFormat.format(newBalance);
                txt_cash.setText(formattedAmount);

                Toast.makeText(getApplicationContext(), "Cash in Successful", Toast.LENGTH_SHORT).show();
            }

        } else {

            Toast.makeText(getApplicationContext(), "Wrong Contact Number", Toast.LENGTH_SHORT).show();
        }

    }

    private void reduceBalance() {

        String contact = (String) profile_contact.getText();
        String senContact = cashout_senContact.getText().toString().trim();
        String recContact = cashout_recContact.getText().toString().trim();
        String Amount = cashout_Amount.getText().toString().trim();

        if (TextUtils.isEmpty(senContact)) {
            cashout_senContact.setError("TextBox cannot be blank!");
            return;
        }

        if (senContact.length() !=11) {
            cashout_senContact.setError("Invalid contact number!");
            return;
        }

        if (TextUtils.isEmpty(recContact)) {
            cashout_recContact.setError("TextBox cannot be blank!");
            return;
        }

        if (recContact.length() !=11) {
            cashout_recContact.setError("Invalid contact number!");
            return;
        }

        if (TextUtils.isEmpty(Amount)) {
            cashout_Amount.setError("TextBox cannot be blank!");
            return;
        }


        if ( contact.equals(cashout_senContact.getText().toString())) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("users");

            String userid = user.getUid();
            DatabaseReference userRef = databaseRef.child(userid);;

            double currentBalance = Double.parseDouble(cash);
            double amountToGive = Double.parseDouble((cashout_Amount.getText().toString()));

            double newBalance = currentBalance - amountToGive;

            if (newBalance < 0) {
                Toast.makeText(getApplicationContext(), "Balance is not sufficient", Toast.LENGTH_SHORT).show();
            } else {

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

                Query query = rootRef.child("users").orderByChild("contact").equalTo(recContact);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Loop through the dataSnapshot to get the UID of the user
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String uid = snapshot.getKey();
                                int curbal = snapshot.child("balance").getValue(Integer.class);

                                userRef.child("balance").setValue(newBalance);
                                cashout_senContact.setText("");
                                cashout_recContact.setText("");
                                cashout_Amount.setText("");
                                cash = String.valueOf(newBalance);

                                DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                                String formattedAmount = decimalFormat.format(newBalance);
                                txt_cash.setText(formattedAmount);

                                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("users");
                                DatabaseReference userRef = databaseRef.child(uid);;

                                double newbal = curbal + amountToGive;
                                userRef.child("balance").setValue(newbal);

                                Toast.makeText(getApplicationContext(), "Cash out Successful", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "No contact number registered", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Error occurred while performing the query. Handle the error.
                    }
                });

            }

        } else {

            Toast.makeText(getApplicationContext(), "Wrong Contact Number", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateProfile() {
        String email = edit_email.getText().toString().trim();
        String password = edit_password.getText().toString().trim();
        String firstname = edit_firstname.getText().toString().trim();
        String lastname = edit_lastname.getText().toString().trim();
        String contact = edit_contact.getText().toString().trim();
        String address = edit_address.getText().toString().trim();
        String birthday = edit_birthday.getText().toString().trim();
        String confirm = edit_password2.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            edit_email.setError("Please enter your email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edit_password.setError("Please enter your password!");
            return;
        }

        if (TextUtils.isEmpty(firstname)) {
            edit_firstname.setError("Please enter your first name!");
            return;
        }


        if (TextUtils.isEmpty(lastname)) {
            edit_lastname.setError("Please enter your last name!");
            return;
        }

        if (TextUtils.isEmpty(contact)) {
            edit_contact.setError("Please enter your contact number!");
            return;
        }

        if (contact.length() !=11) {
            edit_contact.setError("Invalid contact number!");
            return;
        }


        if (TextUtils.isEmpty(address)) {
            edit_address.setError("Please enter your last name!");
            return;
        }

        if (TextUtils.isEmpty(birthday)) {
            edit_birthday.setError("Please enter your date of birth!");
            return;
        }

        if ( confirm.equals(edit_password.getText().toString())) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("users");
            String userid = user.getUid();

            DatabaseReference userRef = databaseRef.child(userid);;

            userRef.child("email").setValue(email);
            userRef.child("password").setValue(password);
            userRef.child("firstname").setValue(firstname);
            userRef.child("lastname").setValue(lastname);
            userRef.child("contact").setValue(contact);
            userRef.child("address").setValue(address);
            userRef.child("birthday").setValue(birthday);

            txt_name.setText(firstname + " " + lastname);

            profile_email.setText(email);
            profile_firstname.setText(firstname);
            profile_lastname.setText(lastname);
            profile_contact.setText(contact);
            profile_address.setText(address);
            profile_birthday.setText(birthday);
            edit_password2.setText("");

            Toast.makeText(getApplicationContext(), "Edit Successful", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(getApplicationContext(), "Edit Account Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void addSubscription() {
        Calendar calendar= Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String currentDate = dateFormat.format(calendar.getTime());

        String subName = add_subscription.getText().toString().trim();
        String subProvider = add_provider.getText().toString().trim();
        String subDate = add_date.getText().toString().trim();
        String subFee = add_fee.getText().toString().trim();
        String subSched = add_schedule.getText().toString().trim();

        try {

            Date date = dateFormat.parse(subDate);
            Date currently = new Date();

            int day = Integer.parseInt(subSched);
            calendar.setTime(date);

            while (!calendar.getTime().after(currently)) {
                calendar.add(Calendar.DAY_OF_MONTH, (day));
            }

            Date updatedDate = calendar.getTime();
            String updatedDateString = dateFormat.format(updatedDate);
            newSched = updatedDateString;


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(subName)) {
            add_subscription.setError("Please enter the subscription name.");
            return;
        }

        if (TextUtils.isEmpty(subProvider)) {
            add_provider.setError("Please enter the subscription provider.");
            return;
        }

        if (TextUtils.isEmpty(subDate)) {
            add_date.setError("Please enter the date availed of the subscription.");
            return;
        }


        if (TextUtils.isEmpty(subFee)) {
            add_fee.setError("Please enter the fee per transaction.");
            return;
        }

        if (TextUtils.isEmpty(subSched)) {
            add_schedule.setError("Please enter the schedule of payment.");
            return;
        }
        
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("transaction").push();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String subId = databaseRef.getKey();
        String userId = user.getUid();

        Transaction transaction = new Transaction(subId, userId, subName, subProvider, subDate, subFee, subSched, newSched);

        databaseRef.setValue(transaction)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Add subscription successful", Toast.LENGTH_SHORT).show();

                            add_subscription.setText("");
                            add_provider.setText("");
                            add_date.setText(currentDate);
                            add_fee.setText("");
                            add_schedule.setText("");

                            lister();

                        } else {
                            Toast.makeText(getApplicationContext(), "Add subscription failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        Toast.makeText(getApplicationContext(), "Add Subscription Successful!", Toast.LENGTH_SHORT).show();
    }

    private void lister(){
        DatabaseReference databaseReference;
        Adapter adapter;
        ArrayList<Transaction> list;

        DatabaseReference databaseRef;
        myAdapter myadapter ;
        ArrayList<History> mylist;

        FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();

        String userId = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("transaction");
        Query query1 = databaseReference.orderByChild("userId").equalTo(userId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseRef = FirebaseDatabase.getInstance().getReference("history");
        Query query2 = databaseRef.orderByChild("userId").equalTo(userId);
        userhistory.setHasFixedSize(true);
        userhistory.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new Adapter(this,list);
        recyclerView.setAdapter(adapter);

        mylist = new ArrayList<>();
        myadapter = new myAdapter(this,mylist);
        userhistory.setAdapter(myadapter);

        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Transaction transaction = dataSnapshot.getValue(Transaction.class);
                    list.add(transaction);
                    DisplayText();
                    adapter.notifyDataSetChanged();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    History history = dataSnapshot.getValue(History.class);
                    myAdapter.updateData(mylist);
                    mylist.add(history);
                }
                myAdapter.updateData(mylist);
                myadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}




