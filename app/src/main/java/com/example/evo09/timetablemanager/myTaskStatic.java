package com.example.evo09.timetablemanager;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class myTaskStatic extends Fragment implements View.OnClickListener{

    Fragment fragment=null;
    Fragment frag;
    FragmentManager fm1;
    FragmentTransaction ft1;

    Random random;

    private ProgressDialog csprogress;
    SQLiteDatabase SQLITEDATABASE;
    SQLiteHelper SQLITEHELPER;
    Cursor cursor;
    Date STimedate,ETimedate,BSTimedate;

    TextView StartTime,EndTime,BreakStartTime;
    static Button dynamic,statically;
    EditText PeriodDuration,BreakDuration,Alarmbefore;
    Button buttonSubmit;
    int TimeFlag=0,shour,smint,ehour,emint,intStartTime,intEndTime,intBreakStartTime;
    String getStartTime,getEndTime,getBreakStartTime,getPeriodDuration,getBreakDuration,getAlarmbefore,StrStartTime,StrEndTime;
    Snackbar snackbar1;
    //boolean CheckEmptyValidate;
    String getTime;
    private String Strday[] = new String[] { "Monday", "Tuesday", "Wednesday", "Thursday","Friday","Saturday","Sunday" };
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.Create_Time_Grid);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview= inflater.inflate(R.layout.fragment_my_static_schedules, container, false);
        SQLITEHELPER = new SQLiteHelper(getActivity());
        csprogress = new ProgressDialog(getContext());

        // random = new Random();
        //add view
        dynamic=(Button) rootview.findViewById(R.id.dynamic);
        //dynamic.setBackgroundColor(Color.argb(255, random.nextInt(256), random.nextInt(258), random.nextInt(260)));
        statically=(Button) rootview.findViewById(R.id.statically);
        //statically.setBackgroundColor(Color.argb(255, random.nextInt(256), random.nextInt(258), random.nextInt(260)));
        StartTime=(TextView)rootview.findViewById(R.id.StartTime);
        EndTime=(TextView)rootview.findViewById(R.id.EndTime);
        BreakStartTime=(TextView)rootview.findViewById(R.id.BreakStartTime);
        Alarmbefore=(EditText) rootview.findViewById(R.id.Alarmbefore);
        BreakDuration=(EditText)rootview.findViewById(R.id.BreakDuration);
        PeriodDuration=(EditText)rootview.findViewById(R.id.PeriodDuration);
        buttonSubmit=(Button)rootview.findViewById(R.id.buttonSubmit);

        //add listener
        dynamic.setOnClickListener(this);
        statically.setOnClickListener(this);
        StartTime.setOnClickListener(this);
        EndTime.setOnClickListener(this);
        BreakStartTime.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

        return rootview;
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }
    public void onPrepareOptionsMenu(Menu main ) {
        //menu.clear();
        //menu.clear();
        main.clear();
    }

    @Override
    public void onClick(View view) {
        LinearLayout Staticshowdata=(LinearLayout)getActivity().findViewById(R.id.Staticshowdata);
        LinearLayout dynamicShowdata=(LinearLayout)getActivity().findViewById(R.id.dynamicShowdata);
        switch (view.getId()){
            case R.id.StartTime:{
                TimeFlag=789;
                SetTime();
                break;
            }
            case R.id.EndTime:{
                TimeFlag=456;
                SetTime();
                break;
            }
            case R.id.BreakStartTime:{
                TimeFlag=123;
                SetTime();
                break;
            }
            case R.id.buttonSubmit:{
                ButtonSubmit();
                break;
            }
            case R.id.dynamic:
            {
                Staticshowdata.setVisibility(View.GONE);
                Date d = new Date();
                String stime = String.format("%02d:%02d %s", d.getHours() == 0 ? 12 : d.getHours(), d.getMinutes(), d.getHours() < 12 ? "AM" : "PM");
                String etime = String.format("%02d:%02d %s", d.getHours()+1 == 0 ? 12 : d.getHours()+1, d.getMinutes(), d.getHours()+1 < 12 ? "AM" : "PM");
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                final String dayOfTheWeek = sdf.format(d);

                SQLITEDATABASE = getActivity().openOrCreateDatabase(SQLITEHELPER.DATABASE_NAME, MODE_PRIVATE, null);
                String CREATE_WEEKTABLE = "CREATE TABLE IF NOT EXISTS " + SQLITEHELPER.TABLE_NAME + " (" + SQLITEHELPER.KEY_ID + " INTEGER PRIMARY KEY NOT NULL, "+ SQLITEHELPER.KEY_DOWeek + " VARCHAR NOT NULL, " + SQLITEHELPER.KEY_STime + " VARCHAR NOT NULL, " + SQLITEHELPER.KEY_ETime + " VARCHAR NOT NULL, " + SQLITEHELPER.KEY_Subject + " VARCHAR NOT NULL, " + SQLITEHELPER.KEY_Venue + " VARCHAR NOT NULL , " + SQLITEHELPER.KEY_AlermBefor + " VARCHAR)";
                SQLITEDATABASE.execSQL(CREATE_WEEKTABLE);
                SQLITEDATABASE.execSQL("INSERT or replace INTO " + SQLITEHELPER.TABLE_NAME + " " + "(" + SQLITEHELPER.KEY_DOWeek + "," + SQLITEHELPER.KEY_STime + "," + SQLITEHELPER.KEY_ETime + "," + SQLITEHELPER.KEY_Subject + "," + SQLITEHELPER.KEY_Venue + "," + SQLITEHELPER.KEY_AlermBefor + ")" + " VALUES('" + dayOfTheWeek + "', '" + stime + "', '" + etime + "', '" + "Math" + "', '" + "Room 101" + "' , '" + "5" + "');");
                fm1 = getActivity().getSupportFragmentManager();
                ft1 = fm1.beginTransaction();
                frag = new myTask();
                ft1.replace(R.id.content_frame, frag);
                ft1.commit();
                break;
            }
            case R.id.statically:
            {

                dynamicShowdata.setVisibility(View.GONE);
                Staticshowdata.setVisibility(View.VISIBLE);
                break;
            }
        }
    }
    public void SetTime(){
        final Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker= new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                int inthour = selectedHour;
                int intminute=selectedMinute;
                getTime=String.format("%02d:%02d %s", inthour == 0 ? 12 : inthour, intminute, inthour < 12 ? "AM" : "PM");
                if(TimeFlag==789) {
                    StartTime.setText(getTime);
                    intStartTime = inthour * 60 + intminute;
                }
                else if(TimeFlag==456) {
                    EndTime.setText(getTime);
                    intEndTime=inthour * 60 + intminute;
                }
                else if( TimeFlag==123){
                    BreakStartTime.setText(getTime);
                    intBreakStartTime=inthour * 60 + intminute;
                }

            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
    public void ButtonSubmit(){
        getStartTime=StartTime.getText().toString();
        getEndTime=EndTime.getText().toString();
        getBreakStartTime=BreakStartTime.getText().toString();
        getPeriodDuration=PeriodDuration.getText().toString();
        getBreakDuration=BreakDuration.getText().toString();
        getAlarmbefore=Alarmbefore.getText().toString();
        CheckEmpty(getStartTime,getEndTime,getBreakStartTime,getPeriodDuration,getBreakDuration,getAlarmbefore,intStartTime,intEndTime,intBreakStartTime);
    }
    public void  CheckEmpty(String getStartTime,String getEndTime,String getBreakStartTime,String getPeriodDuration,String getBreakDuration,String getAlarmbefore,int intStartTime,int intEndTime,int intBreakStartTime){
        if(TextUtils.isEmpty(getStartTime) || getStartTime.length()==0 || TextUtils.isEmpty(getEndTime)  || getEndTime.length()==0 || TextUtils.isEmpty(getBreakStartTime) || getBreakStartTime.length()==0 || TextUtils.isEmpty(getPeriodDuration) || getPeriodDuration.length()==0  || TextUtils.isEmpty(getBreakDuration) || getBreakDuration.length()==0 ){
            snackbar1 = Snackbar.make(getView(), "Error: all fields are required", Snackbar.LENGTH_SHORT);snackbar1.show();
        }
        else if(getPeriodDuration.length()==1){
            snackbar1 = Snackbar.make(getView(), "Error! Check Period Duration!", Snackbar.LENGTH_SHORT);snackbar1.show();
        }
        else if(intStartTime>=intBreakStartTime ) {
            snackbar1 = Snackbar.make(getView(), "Error! Break-Start-time must be greater then Start-time!", Snackbar.LENGTH_SHORT);snackbar1.show();
        }
        else if(intBreakStartTime<intStartTime+Integer.parseInt(getPeriodDuration)){
            snackbar1 = Snackbar.make(getView(), "Error! Break-Start-time overlapping Period Duration !", Snackbar.LENGTH_SHORT);snackbar1.show();
        }
        else if(getBreakDuration.length()==1){
            snackbar1 = Snackbar.make(getView(), "Error! Check Break Duration!", Snackbar.LENGTH_SHORT);snackbar1.show();
        }
        else if( intEndTime<=intBreakStartTime){
            snackbar1 = Snackbar.make(getView(), "Error! End-time must be greater then Break-Start-time!", Snackbar.LENGTH_SHORT);snackbar1.show();
        }
        else if(intEndTime<intBreakStartTime+Integer.parseInt(getBreakDuration)){
            snackbar1 = Snackbar.make(getView(), "Error! End-time overlapping Break Duration !", Snackbar.LENGTH_SHORT);snackbar1.show();
        }
        else {
            if((intBreakStartTime-intStartTime)%Integer.parseInt(getPeriodDuration)==0){
                if((intEndTime-(intBreakStartTime+Integer.parseInt(getBreakDuration)))%Integer.parseInt(getPeriodDuration)==0){
                    StaticDBCreate(getStartTime,getEndTime,getBreakStartTime,getPeriodDuration,getBreakDuration,getAlarmbefore);
                }
                else {
                    snackbar1 = Snackbar.make(getView(), "Error! Provided data is not valid!", Snackbar.LENGTH_SHORT);snackbar1.show();
                }
            }
        }
    }
    public void StaticDBCreate(String getStartTime,String getEndTime,String getBreakStartTime,String getPeriodDuration,String getBreakDuration,String getAlarmbefore){
        SQLITEDATABASE = getActivity().openOrCreateDatabase(SQLITEHELPER.DATABASE_NAME, MODE_PRIVATE, null);
        String CREATE_WEEKTABLE = "CREATE TABLE IF NOT EXISTS " + SQLITEHELPER.TABLE_NAME + " (" + SQLITEHELPER.KEY_ID + " INTEGER PRIMARY KEY NOT NULL, "+ SQLITEHELPER.KEY_DOWeek + " VARCHAR NOT NULL, " + SQLITEHELPER.KEY_STime + " VARCHAR NOT NULL, " + SQLITEHELPER.KEY_ETime + " VARCHAR NOT NULL, " + SQLITEHELPER.KEY_Subject + " VARCHAR NOT NULL, " + SQLITEHELPER.KEY_Venue + " VARCHAR NOT NULL , " + SQLITEHELPER.KEY_AlermBefor + " VARCHAR)";
        SQLITEDATABASE.execSQL(CREATE_WEEKTABLE);
        if(SQLITEDATABASE.isOpen()) {
            //Log.d("SQ", "open");
            insertCreateddata(getStartTime,getEndTime,getBreakStartTime,getPeriodDuration,getBreakDuration,getAlarmbefore);
        }
        else {
            snackbar1 = Snackbar.make(getView(), "Error! Something went wrong!", Snackbar.LENGTH_SHORT);snackbar1.show();
        }
    }
    public void insertCreateddata(String getStartTime,String getEndTime,String getBreakStartTime,String getPeriodDuration,String getBreakDuration,String getAlarmbefore){

        String[] SplitStime = getStartTime.split(" ");
        String StineSplitStime = SplitStime[0];
        STimedate = new Date();
        STimedate.setTime((((Integer.parseInt(StineSplitStime.split(":")[0])) * 60 + (Integer.parseInt(StineSplitStime.split(":")[1]))) + STimedate.getTimezoneOffset()) * 60000);
        int SST = STimedate.getHours() * 60 + STimedate.getMinutes();

        String[] SplitEtime = getEndTime.split(" ");
        String EtineSplitEtime = SplitEtime[0];
        ETimedate = new Date();
        ETimedate.setTime((((Integer.parseInt(EtineSplitEtime.split(":")[0])) * 60 + (Integer.parseInt(EtineSplitEtime.split(":")[1]))) + ETimedate.getTimezoneOffset()) * 60000);
        int SET = ETimedate.getHours() * 60 + ETimedate.getMinutes();

        String[] SplitBreakTime=getBreakStartTime.split(" ");
        String BreakTimeSplitBreakTime = SplitBreakTime[0];
        BSTimedate = new Date();
        BSTimedate.setTime((((Integer.parseInt(BreakTimeSplitBreakTime.split(":")[0])) * 60 + (Integer.parseInt(BreakTimeSplitBreakTime.split(":")[1]))) + BSTimedate.getTimezoneOffset()) * 60000);
        int BST = (BSTimedate.getHours() * 60 + BSTimedate.getMinutes());

        if(getAlarmbefore.length()==0){
            getAlarmbefore="00";
        }

        while (SST <SET) {
            if (SST==BST) {
                shour = STimedate.getHours();
                smint = STimedate.getMinutes();
                StrStartTime=String.format("%02d:%02d %s", shour == 0 ? 12 : shour, smint, shour < 12 ? "AM" : "PM");

                STimedate.setTime(STimedate.getTime() + Integer.parseInt(getBreakDuration) * 60000);
                ehour = STimedate.getHours();
                emint = STimedate.getMinutes();
                StrEndTime=String.format("%02d:%02d %s", ehour == 0 ? 12 : ehour, emint, ehour < 12 ? "AM" : "PM");
                for(int i=0;i<7;i++) {
                    SQLITEDATABASE.execSQL("INSERT or replace INTO " + SQLITEHELPER.TABLE_NAME + " " + "(" + SQLITEHELPER.KEY_DOWeek + "," + SQLITEHELPER.KEY_STime + "," + SQLITEHELPER.KEY_ETime + "," + SQLITEHELPER.KEY_Subject + "," + SQLITEHELPER.KEY_Venue + "," + SQLITEHELPER.KEY_AlermBefor + ")" + " VALUES('" + Strday[i] + "', '" + StrStartTime + "', '" + StrEndTime + "', '" + "Break" + "', '" + "Break" + "' , '" + getAlarmbefore + "');");
                  // Log.d("StrEndTime",StrStartTime+"-"+StrEndTime+"-"+Strday[i]);
                }
            }
            else {
                shour = STimedate.getHours();
                smint = STimedate.getMinutes();
                StrStartTime=String.format("%02d:%02d %s", shour == 0 ? 12 : shour, smint, shour < 12 ? "AM" : "PM");

                STimedate.setTime(STimedate.getTime() + Integer.parseInt(getPeriodDuration) * 60000);
                ehour = STimedate.getHours();
                emint = STimedate.getMinutes();
                StrEndTime=String.format("%02d:%02d %s", ehour == 0 ? 12 : ehour, emint, ehour < 12 ? "AM" : "PM");
                for(int i=0;i<7;i++) {
                    SQLITEDATABASE.execSQL("INSERT or replace INTO " + SQLITEHELPER.TABLE_NAME + " " + "(" + SQLITEHELPER.KEY_DOWeek + "," + SQLITEHELPER.KEY_STime + "," + SQLITEHELPER.KEY_ETime + "," + SQLITEHELPER.KEY_Subject + "," + SQLITEHELPER.KEY_Venue + "," + SQLITEHELPER.KEY_AlermBefor + ")" + " VALUES('" + Strday[i] + "', '" + StrStartTime + "', '" + StrEndTime + "', '" + "Subject" + "', '" + "Venue" + "' , '" + getAlarmbefore + "');");
                     //Log.d("StrEndTime",StrStartTime+"-"+StrEndTime+"-"+Strday[i]);
                }
            }

            SST = STimedate.getHours() * 60 + STimedate.getMinutes();

            //Log.d("StrEndTime","\n\n");
        }
        csprogress.setMessage("Loading...");
        csprogress.show();
        csprogress.setCancelable(false);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                chechFragmentStatus();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        csprogress.dismiss();
                    }
                }, 500);
            }
        }, 2000);//just mention the time when you want to launch your action
    }
    public void chechFragmentStatus(){
        cursor = SQLITEDATABASE.rawQuery("SELECT * FROM " + SQLITEHELPER.TABLE_NAME+" ORDER BY " + SQLITEHELPER.KEY_STime + " ASC", null);
        if(cursor.getCount()>0){
            fm1 = getActivity().getSupportFragmentManager();
            ft1 = fm1.beginTransaction();
            frag = new myTask();
            ft1.replace(R.id.content_frame, frag);
            ft1.commit();
        }
    }
}