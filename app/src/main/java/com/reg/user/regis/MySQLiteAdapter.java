package com.reg.user.regis;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteAdapter {
    Context context;
    MySQLiteOpenHelper mySQLiteOpenHelper;

    public MySQLiteAdapter(Context context) {
        this.context=context;
        mySQLiteOpenHelper=new MySQLiteOpenHelper(context);
    }

    public long InsertRecord(String date,String duration,int steps,double distance,double calories) {
        SQLiteDatabase db=mySQLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(mySQLiteOpenHelper.DATE,date);
        contentValues.put(mySQLiteOpenHelper.DURATION,duration);
        contentValues.put(mySQLiteOpenHelper.STEPS,steps);
        contentValues.put(mySQLiteOpenHelper.DISTANCE,distance);
        contentValues.put(mySQLiteOpenHelper.CALORIES,calories);
        long id=db.insert(MySQLiteOpenHelper.TABLE_NAME,null,contentValues);
        return id;
    }

   public String DisplayAllRecords() {
        SQLiteDatabase db=mySQLiteOpenHelper.getWritableDatabase();
        String[] columns={MySQLiteOpenHelper.UID,MySQLiteOpenHelper.DATE,MySQLiteOpenHelper.DURATION,MySQLiteOpenHelper.STEPS,MySQLiteOpenHelper.DISTANCE,MySQLiteOpenHelper.CALORIES};
        Cursor cursor=db.query(MySQLiteOpenHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer=new StringBuffer();
        while(cursor.moveToNext()) {

            int uid=cursor.getInt(0);
            String date=cursor.getString(1);
            String duration=cursor.getString(2);
            int steps=cursor.getInt(3);
            double distance=cursor.getDouble(4);
            double calories=cursor.getDouble(5);

            buffer.append(date+" | "+duration+" | "+steps+"steps| "+distance+"meters | "+calories+"cals \n");
        }

        return buffer.toString();
    }









    public class MySQLiteOpenHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "PEDOMETER.db";
        private static final String TABLE_NAME = "stat_table";
        private static final int DATABASE_VERSION = 3;
        private static final String UID = "_id";
        private static final String DATE= "DATE";
        private static final String DURATION = "DURATION";
        private static final String STEPS= "STEPS";
        private static final String DISTANCE = "DISTANCE";
        private static final String CALORIES = "CALORIES";
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATE+ " VARCHAR(255), " + DURATION+ " VARCHAR(255), "+STEPS+" INTEGER, "+DISTANCE+" REAL, "+CALORIES+" REAL);";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        private Context context;

        public MySQLiteOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
            MyToastMessage.myMessage(context, "Constructor called...");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                MyToastMessage.myMessage(context, "onCreate called...");
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                MyToastMessage.myMessage(context, "" + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                MyToastMessage.myMessage(context, "onUpgrade called...");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (Exception e) {
                MyToastMessage.myMessage(context, "" + e);
            }
        }
    }
}
