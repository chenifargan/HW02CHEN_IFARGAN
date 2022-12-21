package com.example.hw02chen_ifargan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

public class Activity_Menu extends AppCompatActivity {
    private MaterialButton menu_BTN_start;

    /* access modifiers changed from: private */
    public TextInputEditText menu_EDT_id;



    /* access modifiers changed from: protected */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_menu);
        findViews();
        initViews();
    }

    private void initViews() {
        this.menu_BTN_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Activity_Menu.this.makeServerCall();
            }
        });
    }

    private void findViews() {
        this.menu_BTN_start = (MaterialButton) findViewById(R.id.menu_BTN_start);
        this.menu_EDT_id = (TextInputEditText) findViewById(R.id.menu_EDT_id);
    }



    public boolean tocontinio(String id){

        if(id.length()<9){
            Toast.makeText(this, "Not ID enter again " , Toast.LENGTH_LONG).show();
            return false;
        }
      return true;




    }


    /* access modifiers changed from: private */
    public void makeServerCall() {
        new Thread() {
            public void run() {


                String data = Activity_Menu.getJSON(Activity_Menu.this.getString(R.string.url));
                Log.d("pttt", data);
                //Activity_Menu activity_Menu = Activity_Menu.this;

                if (data != null && tocontinio(menu_EDT_id.getText().toString())) {
                   Activity_Menu activity_Menu = Activity_Menu.this;
                    activity_Menu.startGame(activity_Menu.menu_EDT_id.getText().toString(), data);
                }
            }
        }.start();
    }

    /* access modifiers changed from: private */
    public void startGame(String id, String data) {
        Log.d("pttttt"," "+id);
        String state = data.split(",")[Integer.valueOf(String.valueOf(id.charAt(7))).intValue()];
        Intent intent = new Intent(getBaseContext(), Activity_Game.class);
        intent.putExtra(Activity_Game.EXTRA_ID, id);

        intent.putExtra(Activity_Game.EXTRA_STATE, state);
        startActivity(intent);
    }

    public static String getJSON(String url) {
        String data = "";
        HttpsURLConnection con = null;
        try {
            HttpsURLConnection con2 = (HttpsURLConnection) new URL(url).openConnection();
            con2.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(con2.getInputStream()));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = br.readLine();
                String line = readLine;
                if (readLine == null) {
                    break;
                }
                sb.append(line + "\n");
            }
            br.close();
            data = sb.toString();
            if (con2 != null) {
                try {
                    con2.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (MalformedURLException ex2) {
            ex2.printStackTrace();
            if (con != null) {
                con.disconnect();
            }
        } catch (IOException ex3) {
            ex3.printStackTrace();
            if (con != null) {
                con.disconnect();
            }
        } catch (Throwable th) {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex4) {
                    ex4.printStackTrace();
                }
            }
            throw th;
        }
        return data;
    }

}