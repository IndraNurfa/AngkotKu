package org.arkosh.angkotku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.arkosh.angkotku.model.User;

public class CustomerRegisActivity extends AppCompatActivity {

    private EditText email, name, pass, passConf;
    private FirebaseAuth mAuth;
    private DatabaseReference mDriverDb;
    private FirebaseDatabase db;
    private FirebaseAuth.AuthStateListener fAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_regis);

        mAuth = FirebaseAuth.getInstance();
        fAuthStateListener = firebaseAuth -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Intent intent = new Intent(CustomerRegisActivity.this, CustomerHomepageActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        };

        Button cancel = (Button) findViewById(R.id.btnCancel);
        Button btnRegister = (Button) findViewById(R.id.btnRegister);

        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        name = (EditText) findViewById(R.id.editTextTextPersonName);
        pass = (EditText) findViewById(R.id.editTextTextPassword);
        passConf = (EditText) findViewById(R.id.editTextTextPassword2);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerRegisActivity.this, CustomerHomepageActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }

    private void result() {
        if (TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(name.getText().toString())
                || TextUtils.isEmpty(pass.getText().toString()) || TextUtils.isEmpty(passConf.getText().toString())) {
            Toast.makeText(CustomerRegisActivity.this, "Harus isi semua", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(email.getText().toString())) {
            Toast.makeText(CustomerRegisActivity.this, "Email tidak boleh kosong", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(name.getText().toString())) {
            Toast.makeText(CustomerRegisActivity.this, "Nama tidak boleh kosong", Toast.LENGTH_LONG).show();
            return;
        }
        if (pass.getText().toString().length() < 6 || pass.getText().toString().length() < 6) {
            Toast.makeText(CustomerRegisActivity.this, "Harus 6 karakter atau lebih", Toast.LENGTH_LONG).show();
            return;
        }
        if (!pass.getText().toString().equals(passConf.getText().toString())) {
            Toast.makeText(CustomerRegisActivity.this, "Password Harus Sama", Toast.LENGTH_LONG).show();
            return;
        }
        final String mEmail = email.getText().toString();
        final String mName = name.getText().toString();
        final String mPass = pass.getText().toString();
        User user = new User();
        user.setEmail(mEmail);
        user.setNama(mName);
        user.setPassword(mPass);
        mAuth.createUserWithEmailAndPassword(mEmail, mPass).addOnCompleteListener(CustomerRegisActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(CustomerRegisActivity.this, "Registrasi Error!", Toast.LENGTH_SHORT).show();
                } else {
                    String user_id = mAuth.getCurrentUser().getUid();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user_id);
                    databaseReference.setValue(user);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(fAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(fAuthStateListener);
    }
}