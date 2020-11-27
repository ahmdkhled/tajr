package com.tajr.tajr.records;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.tajr.tajr.records.DatabaseHandler.SERIAL_NUMBER;

/**
 * Created by VS00481543 on 07-11-2017.
 */
// Will be performing all actions on database.
public class DatabaseManager {

    SQLiteDatabase sqLiteDatabase;

    public DatabaseManager(Context activity) {
        sqLiteDatabase = DatabaseSingleton.getInstance(activity);
    }

    public void addCallDetails(CallDetails callDetails) {

        ContentValues values = new ContentValues();
        values.put(SERIAL_NUMBER, callDetails.getSerial());
        values.put(DatabaseHandler.PHONE_NUMBER, callDetails.getNum());
        values.put(DatabaseHandler.UPLOADED,callDetails.getUploaded());
        values.put(DatabaseHandler.TIME, callDetails.getTime1());
        values.put(DatabaseHandler.DATE, callDetails.getDate1());
        values.put(DatabaseHandler.DURATION, callDetails.getDuration());


        sqLiteDatabase.insert(DatabaseHandler.TABLE_RECORD, null, values);
    }

    public void updateCallDetails(CallDetails callDetails ){
        ContentValues values = new ContentValues();
        values.put(SERIAL_NUMBER, callDetails.getSerial());
        values.put(DatabaseHandler.PHONE_NUMBER, callDetails.getNum());
        values.put(DatabaseHandler.UPLOADED,"uploaded");
        values.put(DatabaseHandler.TIME, callDetails.getTime1());
        values.put(DatabaseHandler.DATE, callDetails.getDate1());
        values.put(DatabaseHandler.DURATION, callDetails.getDuration());
        sqLiteDatabase.update(DatabaseHandler.TABLE_RECORD, values, SERIAL_NUMBER+"="+callDetails.getSerial(), null);
        Log.d("eslamfaisal", "updateCallDetails: uploaded");
    }


    public List<CallDetails> getAllDetails() {
        List<CallDetails> recordList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DatabaseHandler.TABLE_RECORD;

        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CallDetails callDetails = new CallDetails();
                callDetails.setSerial(cursor.getInt(0));
                callDetails.setNum(cursor.getString(1));
                callDetails.setTime1(cursor.getString(2));
                callDetails.setUploaded(cursor.getString(3));
                callDetails.setDate1(cursor.getString(4));
                callDetails.setDuration(cursor.getString(5));

                if (!recordList.contains(callDetails)){
                    recordList.add(callDetails);
                }
            } while (cursor.moveToNext());
        }

        return recordList;
    }

}
