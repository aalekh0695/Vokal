package com.example.vokal;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import com.example.vokal.adapter.ContactAdapter;
import com.example.vokal.pojo.Card;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FetchSMS {
    public static final String INBOX = "content://sms/inbox";
    Context context;
    public List<Card> myCardList;
    public ContactAdapter contactAdapter;
    public List<String> sms_date;
    public List<String> sms_contact;
    public List<String> sms_body;
    public List<String> hoursAgo;


    public FetchSMS(Context context) {
        this.context = context;
        myCardList = new ArrayList<>();
        sms_date = new ArrayList<>();
        sms_contact = new ArrayList<>();
        sms_body = new ArrayList<>();
        hoursAgo = new ArrayList<>();
    }

    public List<Card> getSMS() {
        long timeinMillis = new Date(System.currentTimeMillis() - (long)24 * 3600 * 1000).getTime();
        Cursor cursor = context.getContentResolver().query(Uri.parse(INBOX), null, "date" + ">?",
                new String[]{""+timeinMillis}, "date DESC");

        int indexBody = cursor.getColumnIndex("body");
        int indexAddress = cursor.getColumnIndex("address");
        if(indexBody<0 || !cursor.moveToFirst())
            return null;

        do{
            String date =  cursor.getString(cursor.getColumnIndex("date"));
            Long timestamp = Long.parseLong(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            Date finaldate = calendar.getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd:HH:mm:ss");
            String formattedDate=dateFormat.format(finaldate);
            sms_date.add(formattedDate);
            sms_contact.add(cursor.getString(indexAddress));
            sms_body.add(cursor.getString(indexBody));

        }while (cursor.moveToNext());
        cursor.close();
        compareTime(sms_date);
        Log.i("prepared data", sms_body.toString());
        return prepareData(hoursAgo, sms_contact, sms_body);
    }

    private List<Card> prepareData(List<String> sms_date, List<String> sms_contact, List<String> sms_body) {
        for(int i=0;i<sms_contact.size();i++){
            Card card = new Card(sms_date.get(i), sms_contact.get(i), sms_body.get(i));
            myCardList.add(card);
        }

//        contactAdapter.notifyDataSetChanged();
        return myCardList;
    }

    private void compareTime(List<String> sms_date) {
        for(int i=0; i<sms_date.size();i++){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:HH:mm:ss");
            String time = simpleDateFormat.format(new Date());
            float diff;
            String[] lastOffTimeArray = sms_date.get(i).split(":");
            String[] timeArray = time.split(":");
            //GET THE DIFF
            if(getIntVal(timeArray[0]) != getIntVal(lastOffTimeArray[0])){
                diff = Math.abs(( (getIntVal(timeArray[1])+24)*3600 + getIntVal(timeArray[2])*60 + getIntVal(timeArray[3]) )-
                        ( getIntVal(lastOffTimeArray[1])*3600 + getIntVal(lastOffTimeArray[2])*60 + getIntVal(lastOffTimeArray[3])));

//            return diff;
            }else{
                diff = Math.abs(( (getIntVal(timeArray[1]))*3600 + getIntVal(timeArray[2])*60 + getIntVal(timeArray[3]) )-
                        ( getIntVal(lastOffTimeArray[1])*3600 + getIntVal(lastOffTimeArray[2])*60 + getIntVal(lastOffTimeArray[3])));
            }

            int timeDiff = (int) (diff/3600);
            switch (timeDiff){
                case 0:
                    hoursAgo.add("0 Hours Ago");
                    break;
                case 1:
                    hoursAgo.add("1 Hour Ago");
                    break;
                case 2:
                    hoursAgo.add("2 Hours Ago");
                    break;
                case 3:
                    hoursAgo.add("3 Hours Ago");
                    break;
                case 6:
                    hoursAgo.add("6 Hours Ago");
                    break;
                case 12:
                    hoursAgo.add("12 Hours Ago");
                    break;
                default:
                    hoursAgo.add("Beyond 12 Hours");
                    break;
            }

//            hoursAgo.add(String.valueOf(diff/3600));
        }
    }

    private int getIntVal(String s){
        return Integer.parseInt(s);
    }

}
