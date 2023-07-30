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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SignActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth fireauth;
    private EditText sign_email;
    private EditText sign_password;
    private EditText sign_confirm;
    private EditText sign_firstname;
    private EditText sign_lastname;
    private EditText sign_contact;
    private EditText sign_address;
    private EditText sign_birthday;
    private CheckBox showPasswordCheckBox;
    private CheckBox showConpassCheckBox;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        fireauth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        Button btn_back = findViewById(R.id.btn_back);
        Button btn_sign = findViewById(R.id.btn_sign);

        sign_email = findViewById(R.id.sign_email);
        sign_password = findViewById(R.id.sign_password);
        sign_confirm = findViewById(R.id.sign_password2);
        sign_firstname = findViewById(R.id.sign_firstname);
        sign_lastname = findViewById(R.id.sign_lastname);
        sign_contact = findViewById(R.id.sign_contact);
        sign_address = findViewById(R.id.sign_address);
        sign_birthday = findViewById(R.id.sign_birthday);
        showPasswordCheckBox = findViewById(R.id.password_checkbox);
        showConpassCheckBox = findViewById(R.id.conpass_checkbox);

        showPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Toggle the password visibility based on the checkbox state
                if (isChecked) {
                    sign_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    sign_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                // Move the cursor to the end of the text
                sign_password.setSelection(sign_password.getText().length());
            }
        });

        showConpassCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Toggle the password visibility based on the checkbox state
                if (isChecked) {
                    sign_confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    sign_confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                // Move the cursor to the end of the text
                sign_confirm.setSelection(sign_confirm.getText().length());
            }
        });

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        btn_back.setOnClickListener(v -> startActivity(new Intent(SignActivity.this, LoginActivity.class)));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        String currentDate = dateFormat.format(calendar.getTime());
        sign_birthday.setText(currentDate);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(SignActivity.this,
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
                        sign_birthday.setText(selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set the EditText click listener to show the DatePickerDialog
        sign_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

    }

    private void register() {
        String email = sign_email.getText().toString().trim();
        String password = sign_password.getText().toString().trim();
        String firstname = sign_firstname.getText().toString().trim();
        String lastname = sign_lastname.getText().toString().trim();
        String contact = sign_contact.getText().toString().trim();
        String address = sign_address.getText().toString().trim();
        String birthday = sign_birthday.getText().toString().trim();
        String confirm = sign_confirm.getText().toString().trim();


        if (TextUtils.isEmpty(email)) {
            sign_email.setError("Please enter your email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            sign_password.setError("Please enter your password!");
            return;
        }

        if (TextUtils.equals(password, confirm)) {

        } else {
            sign_confirm.setError("Password do not match");
            return;
        }

        if (TextUtils.isEmpty(firstname)) {
            sign_firstname.setError("Please enter your first name!");
            return;
        }


        if (TextUtils.isEmpty(lastname)) {
            sign_lastname.setError("Please enter your last name!");
            return;
        }

        if (TextUtils.isEmpty(contact)) {
            sign_contact.setError("Please enter your first name!");
            return;
        }

        if (contact.length() !=11) {
            sign_contact.setError("Invalid contact number!");
            return;
        }


        if (TextUtils.isEmpty(address)) {
            sign_address.setError("Please enter your last name!");
            return;
        }

        if (TextUtils.isEmpty(birthday)) {
            sign_birthday.setError("Please enter your date of birth!");
            return;
        }

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query duplicateCheckQuery = rootRef.child("users").orderByChild("contact").equalTo(contact);

        duplicateCheckQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Toast.makeText(getApplicationContext(), "Contact number already exists, please use a different one.", Toast.LENGTH_SHORT).show();

                } else {

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Registration success
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        String userId = user.getUid();

                                        User newuser = new User(userId, email, password, firstname, lastname, contact, address, birthday, 0.0);

                                        databaseReference.child(userId).setValue(newuser)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                                            Calendar calendar = Calendar.getInstance();
                                                            String currentDate = dateFormat.format(calendar.getTime());

                                                            Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();

                                                            sign_email.setText("");
                                                            sign_password.setText("");
                                                            sign_confirm.setText("");
                                                            sign_firstname.setText("");

                                                            sign_lastname.setText("");
                                                            sign_contact.setText("");
                                                            sign_address.setText("");
                                                            sign_birthday.setText(currentDate);


                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        // Registration failed
                                        Toast.makeText(getApplicationContext(), "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error occurred while checking the database. Handle the error.

            }
        });

        /*
        databaseRef.child(newRecordId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    sign_contact.setError("Contact number already in use!");

                } else {
                    // The ID is not in use, create the new record

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });

         */

    }

}







