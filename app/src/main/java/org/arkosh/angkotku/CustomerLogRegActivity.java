package org.arkosh.angkotku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CustomerLogRegActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener fAuthStateListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_regis);
        Button login = (Button) findViewById(R.id.btnLogin);
        Button regis = (Button) findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        fAuthStateListener = firebaseAuth -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Intent intent = new Intent(CustomerLogRegActivity.this, CustomerHomepageActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerLogRegActivity.this, CustomerLoginActivity.class);
                startActivity(intent);
                return;
            }
        });

        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerLogRegActivity.this, CustomerRegisActivity.class);
                startActivity(intent);
                return;
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