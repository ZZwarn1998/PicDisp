package com.zzwarn.picdisposal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.RequestParams;
import com.zzwarn.utility.AbsolutePathFromUri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class Segment extends AppCompatActivity implements View.OnClickListener{
    private EditText edtTxt, edtTxt2;
    private ProgressDialog prgDialog;
    public static final int REQUEST_PICK_IMAGE = 11101;
    public String imgPath = null;
    public String sdCardDir = Environment.getExternalStorageDirectory() + File.separator +
            Environment.DIRECTORY_DCIM + File.separator + "PicDisposal" + File.separator;

    private String proc_adr;
    private RequestParams params = new RequestParams();
    private Bitmap bitmap;
    private String bs64str;
    private String img_name;
    private String dis_type = "9";
    private String disposal = "segment";
    private String K = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segment);

        Bundle msg = getIntent().getBundleExtra("msg");
        proc_adr = msg.getString("proc_adr");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        prgDialog= new ProgressDialog(this);
        prgDialog.setCancelable(false);

        edtTxt = (EditText) findViewById(R.id.editText);
        edtTxt2 = (EditText) findViewById(R.id.editText2);

        findViewById(R.id.choose_image).setOnClickListener(this);
        findViewById(R.id.choose_image).setOnClickListener(this);
        findViewById(R.id.upload_image).setOnClickListener(this);
        findViewById(R.id.show_image).setOnClickListener(this);
        findViewById(R.id.save_image).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.choose_image:
                choose_img();
                break;
            case R.id.upload_image:
                img_name = edtTxt.getText().toString();
                String str_K= edtTxt2.getText().toString();
                if(checkStrIsNum(str_K)){
                    int int_K = Integer.valueOf(str_K).intValue();
                    if (int_K >=1 && int_K <=10){
                        K = str_K;
                        upload_img();
                    }else{
                        Toast.makeText(Segment.this,"Clusters K should be in the range between 1 and 10.",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(Segment.this, "The value of clusters should be integer.", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.show_image:
                String uri = "http://" + proc_adr +"/imageDisposal/DownloadServlet?filename=" + disposal + ".jpg";
                Log.d("dispose", uri);
                Bitmap btm = getImg(uri);
                if(btm==null){
                    Toast.makeText(this, "Empty image", Toast.LENGTH_LONG).show();
                }else {
                    ImageView imgView = (ImageView) findViewById(R.id.rimageView);
                    imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imgView.setImageBitmap(btm);
                    Toast.makeText(this, "Successfully display!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.save_image:
                String pic_url_save="http://" + proc_adr +"/imageDisposal/DownloadServlet?filename=" + disposal + ".jpg";
                Bitmap btm_ = getImg(pic_url_save);
                if(btm_ ==null){
                    Toast.makeText(this, "Empty image", Toast.LENGTH_LONG).show();
                }else {
                    saveImg(btm_);
                }
                break;
            default:
                break;
        }
    }

    public static boolean checkStrIsNum(String str) {
        Pattern NUMBER_PATTERN = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        String bigStr;
        try {
            // Firstly, convert String into BIgDecimal. Then, convert BigDecimal into String.
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            // Failed Conversion
            return false;
        }
        Matcher isNum = NUMBER_PATTERN.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_PICK_IMAGE:
                    if (data != null){
                        imgPath = AbsolutePathFromUri.getRealPathFromUri(this, data.getData());
                        ImageView imgView = (ImageView) findViewById(R.id.imageView);
                        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                        imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        imgView.setImageBitmap(bitmap);
                    } else{
                        Toast.makeText(Segment.this,"Destroyed image! Please select the other one.", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }

    }

    public void choose_img(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    REQUEST_PICK_IMAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
        }
    }

    public void upload_img(){
        if (imgPath != null && !imgPath.isEmpty()) {
            prgDialog.setMessage("Converting Image to Binary Data");
            prgDialog.show();
            img2str();
        } else {
            Toast.makeText(getApplicationContext(), "You must select image from gallery before you try to upload",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void img2str(){
        new AsyncTask<Void, Void, String>() {
            protected void onPreExecute() {
            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath, options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // compress image
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byte_arr = stream.toByteArray();
                // convert Image to String
                bs64str = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                prgDialog.setMessage("Calling Upload");
                // add parameters
                params.put("image", bs64str);
                params.put("cmd", img_name + "@" + dis_type + "@" + K);
                // upload image
                trans_params();
            }
        }.execute(null, null, null);
    }

    public void trans_params(){
        prgDialog.setMessage("Uploading...");
        String uri = "http://" + proc_adr + "/imageDisposal/dispose.jsp";
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(uri, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                prgDialog.hide();
                Toast.makeText(getApplicationContext(), "Successfully upload!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                prgDialog.hide();
                // if statusCode is equal to 404
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(),
                            "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // if statusCode is equal to 500
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // others
                else {
                    Toast.makeText(
                            getApplicationContext(), "Error Occured n Most Common Error: n1. Device " +
                                    "not connected to Internetn2. Web App is not deployed in App servern3." +
                                    " App server is not runningn HTTP Status code : "
                                    + statusCode, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (prgDialog != null) {
            prgDialog.dismiss();
        }
    }

    public static Bitmap getImg(String uri){
        HttpGet get = new HttpGet(uri);
        HttpClient client = new DefaultHttpClient();
        Bitmap pic = null;
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            pic = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pic;
    }


    private void saveImg(Bitmap bitmap) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Calendar cal = Calendar.getInstance();
        String save_name = disposal + "_" + K + "_" + img_name + "_" + dateFormat.format(cal.getTime());
        try {
            File dirFile = new File(sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File file = new File(sdCardDir, save_name + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Uri uri = Uri.fromFile(file);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            Toast.makeText(this, save_name + ".jpg has been saved", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap.recycle();
    }
}