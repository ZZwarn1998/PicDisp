package com.zzwarn.picdisposal;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    Button btn_0,btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_10;
    Intent int_0,int_1,int_2,int_3,int_4,int_5,int_6,int_7,int_8,int_9,int_10;
    String proc_adr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle msg = getIntent().getBundleExtra("msg");
        proc_adr = msg.getString("proc_adr");

        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_0.setOnClickListener(new Buttonlistener());

        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(new Buttonlistener());

        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(new Buttonlistener());

        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_3.setOnClickListener(new Buttonlistener());

        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_4.setOnClickListener(new Buttonlistener());

        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_5.setOnClickListener(new Buttonlistener());

        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_6.setOnClickListener(new Buttonlistener());

        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_7.setOnClickListener(new Buttonlistener());

        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_8.setOnClickListener(new Buttonlistener());

        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_9.setOnClickListener(new Buttonlistener());

        btn_10 = (Button) findViewById(R.id.btn_10);
        btn_10.setOnClickListener(new Buttonlistener());
    }

    private class Buttonlistener implements View.OnClickListener {
        public void onClick(View v) {
            Bundle msg = new Bundle();
            msg.putString("proc_adr", proc_adr);
            switch (v.getId()) {
                case R.id.btn_0:
                    int_0 = new Intent(MainActivity.this, Binary.class);
                    int_0.putExtra("msg", msg);
                    startActivity(int_0);
                    break;
                case R.id.btn_1:
                    int_1 = new Intent(MainActivity.this, Cartoon.class);
                    int_1.putExtra("msg", msg);
                    startActivity(int_1);
                    break;
                case R.id.btn_2:
                    int_2 = new Intent(MainActivity.this, Concave.class);
                    int_2.putExtra("msg", msg);
                    startActivity(int_2);
                    break;
                case R.id.btn_3:
                    int_3 = new Intent(MainActivity.this, Convex.class);
                    int_3.putExtra("msg", msg);
                    startActivity(int_3);
                    break;
                case R.id.btn_4:
                    int_4 = new Intent(MainActivity.this, Emboss.class);
                    int_4.putExtra("msg", msg);
                    startActivity(int_4);
                    break;
                case R.id.btn_5:
                    int_5 = new Intent(MainActivity.this, FrostedGlass.class);
                    int_5.putExtra("msg", msg);
                    startActivity(int_5);
                    break;
                case R.id.btn_6:
                    int_6 = new Intent(MainActivity.this, LUT.class);
                    int_6.putExtra("msg", msg);
                    startActivity(int_6);
                    break;
                case R.id.btn_7:
                    int_7 = new Intent(MainActivity.this, Nostalgia.class);
                    int_7.putExtra("msg", msg);
                    startActivity(int_7);
                    break;
                case R.id.btn_8:
                    int_8 = new Intent(MainActivity.this, Pixelate.class);
                    int_8.putExtra("msg", msg);
                    startActivity(int_8);
                    break;
                case R.id.btn_9:
                    int_9 = new Intent(MainActivity.this, Segment.class);
                    int_9.putExtra("msg", msg);
                    startActivity(int_9);
                    break;
                case R.id.btn_10:
                    int_10 = new Intent(MainActivity.this, Sharpen.class);
                    int_10.putExtra("msg", msg);
                    startActivity(int_10);
                    break;
                default:
                    break;
            }
        }
    }

}