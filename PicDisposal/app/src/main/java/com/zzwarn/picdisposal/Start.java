package com.zzwarn.picdisposal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.HttpGet;

import java.io.IOException;
import java.io.InputStream;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class Start extends AppCompatActivity {
    Button btn_test;
    EditText edt_ip, edt_port;
    Intent int_0;
    int statusCode = 404;

    private static int REQUEST_PERMISSION_CODE = 10;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }

        btn_test = (Button) findViewById(R.id.btn_test);
        btn_test.setOnClickListener(new Buttonlistener());

        edt_ip = (EditText) findViewById(R.id.edt_ip);
        edt_port = (EditText) findViewById(R.id.edt_port);

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "authority:" + permissions[i] + ", result:" + grantResults[i]);
            }
        }
    }

    private class Buttonlistener implements View.OnClickListener{
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_test:
                    String ip = edt_ip.getText().toString();
                    String port = edt_port.getText().toString();
                    String proc_adr = ip + ":" + port;
                    String test_uri = "http://"+ proc_adr +"/imageDisposal/TestConnect?test=OK";
                    getStatusCode(test_uri);
                    try {
                        Thread.sleep(200);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    if (statusCode == 200){
                        Toast.makeText(Start.this, "Available Connection", Toast.LENGTH_SHORT).show();
                        int_0 = new Intent(Start.this, MainActivity.class);
                        Bundle msg = new Bundle();
                        msg.putString("proc_adr", proc_adr);
                        int_0.putExtra("msg", msg);
                        Log.d("proc_adr", proc_adr);
                        startActivity(int_0);
                    }else{
                        Toast.makeText(Start.this, "StatusCode " + Integer.valueOf(statusCode).toString(), Toast.LENGTH_SHORT).show();
                    }
                default:
                    break;
            }
        }
    }

    protected void getStatusCode(String uri){
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    HttpGet req = new HttpGet(uri);
                    HttpClient client = new DefaultHttpClient();
                    req.setHeader("Content-Type", "application/json");
                    Log.d("Uri", uri);
                    HttpResponse res = client.execute(req);
                    statusCode = res.getStatusLine().getStatusCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}