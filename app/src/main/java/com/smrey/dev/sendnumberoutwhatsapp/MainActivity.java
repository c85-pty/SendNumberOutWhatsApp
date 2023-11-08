package com.smrey.dev.sendnumberoutwhatsapp;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;
import com.smrey.dev.sendnumberoutwhatsapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // in the below line, we are creating variables
    final int REQUEST_CODE = 101;
    String imei;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private Button btnEnviar, btnSalir;
    private EditText editNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });

        btnEnviar = findViewById(R.id.button);
        btnSalir = findViewById(R.id.btnExit);
        editNumber = findViewById(R.id.editTextPhone);
        String defaultLocal = "+507";
        int limiteCodeLocal = defaultLocal.length();
        int limitePhone = 8;
        editNumber.setText(defaultLocal);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = editNumber.getText().toString().trim();

                if (phone.length() == (limiteCodeLocal + limitePhone)) {

                    String numeroPhone = phone.substring(phone.lastIndexOf(defaultLocal));
                    System.out.println("numeroPhone = " + numeroPhone);
                    //create thread on UI Thread (associates with Looper)
                    Handler handler = new Handler();

                    //then use it in a background thread
                    handler.post(new Runnable() {
                        public void run() {
                            String url = "https://api.whatsapp.com/send?phone=" + phone;
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                            finish();
                        }

                    });
                } else {
                    Toast.makeText(getApplicationContext(), "FAVOR INGRESE UN CONTACTO VÁLIDO", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getImeiDevice();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

//    public void setPrivilegeApp(){
//        DevicePolicyManager dpm = (DevicePolicyManager) this.getSystemService(Context.DEVICE_POLICY_SERVICE);
//        dpm.installCaCert(null, cert);
//    }

    public void getImeiDevice() {
        // in the below line, we are initializing our variables.
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        TextView imeiTextView = findViewById(R.id.txtIMEI);

        // in the below line, we are checking for permissions
        if (ActivityCompat.checkSelfPermission(this
                , Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // if permissions are not provided we are requesting for permissions.
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_PHONE_STATE
            }, REQUEST_CODE);
        }

        // in the below line, we are setting our imei to our text view.
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            imei = telephonyManager.getImei();
        } else {
            imei = "-------";
        }*/
        imeiTextView.setText(imei);
    }

    // in the below line, we are calling on request permission result method.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            // in the below line, we are checking if permission is granted.
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // if permissions are granted we are displaying below toast message.
                Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                // in the below line, we are displaying toast message
                // if permissions are not granted.
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}