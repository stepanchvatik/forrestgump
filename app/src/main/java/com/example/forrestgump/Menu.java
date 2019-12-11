package com.example.forrestgump;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//import android.hardware.biometrics.BiometricPrompt;

public class Menu extends AppCompatActivity {
    Executor executor = Executors.newSingleThreadExecutor();
    boolean isKrust = false;
    final BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                Toast.makeText(Menu.super.getApplicationContext(),"RIP",Toast.LENGTH_SHORT).show();
            } else {
              //  Toast.makeText(c,"RIP RIP",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            isKrust=true;
            Intent intent = new Intent(getApplicationContext(),Game.class);
            startActivity(intent);
            //TODO: Called when a biometric is recognized.
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            Toast.makeText(Menu.super.getApplicationContext(),"Ty nejsi Krust",Toast.LENGTH_SHORT).show();
        }
    });



    DatabaseHelper db = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        FragmentActivity activity = this;



        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Nová hra")
                .setSubtitle("Pouze Krust může hrát tuto hru")
                .setDescription("Přilož prst pro ověření.")
                .setNegativeButtonText("Nejsem Krust")
                .build();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        Button newgame = (Button) findViewById(R.id.newgame);

        newgame.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                biometricPrompt.authenticate(promptInfo);

                /*

  */          }
        });

        Button store = (Button) findViewById(R.id.store);
        store.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Store.class);
                startActivity(intent);

            }
        });


    }
}
