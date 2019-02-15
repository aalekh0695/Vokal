package com.example.vokal;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.vokal.adapter.ContactAdapter;
import com.example.vokal.pojo.Card;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static MainActivity ins;
    public static final String INBOX = "content://sms/inbox";
    private RecyclerView recyclerView;
    private List<Card> myCardList;
    private ContactAdapter contactAdapter;
    public Receiver receiver;
    private String contact = "", msgBody = "";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 101 && grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            //permission granted
//            getSMS();
            fetchSMSandUpdate();
        }else{
            Toast.makeText(this, "some premission error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ins = this;
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if(extras == null){
            Log.i("Message", "extra is null");
        }else{
            contact =   extras.getString("title");
            msgBody =   extras.getString("message");
//            Log.i("title", contact);
        }

        myCardList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new ContactAdapter(this, myCardList, contact, msgBody);
        recyclerView.setAdapter(contactAdapter);

        receiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver, intentFilter);
        Log.i("in on create", "receiver registered");
//        prepareData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
            }else{
                fetchSMSandUpdate();
            }
        }


    }
    public static MainActivity  getInstace(){
        return ins;
    }

    @Override
    protected void onPause() {
        Log.i("in on pause", "on pause called");
        super.onPause();
    }

    public void fetchSMSandUpdate() {
        myCardList.clear();
        List<Card> myList = new FetchSMS(this).getSMS();
        if(myList!=null){
            myCardList.addAll(myList);
        }
        contactAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver!=null){
            unregisterReceiver(receiver);
            Log.i("in on destroy", "receiver Unregistered");
        }
        Log.i("in on destroy", "on destroy called");
    }
}
