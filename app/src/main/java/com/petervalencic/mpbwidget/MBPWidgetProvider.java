package com.petervalencic.mpbwidget;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by peterv on 1.2.2016.
 */
public class MBPWidgetProvider extends AppWidgetProvider
{
    private static final String LOG = "MBPWidget";
    private static final String ACTION_CLICK = "ACTION_CLICK";
    private static JSONObject jsonObject;
    private static String json;
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy  hh:mm:ss a");
    static String strWidgetText = "";
    static Context context;
    static AppWidgetManager appWidgetManager;
    static int[] appWidgetIds;




    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);
        Log.w(LOG, "onReceive method called");

        //

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {

        Log.w(LOG, "updateAppWidget");
        this.context=context;
        this.appWidgetIds=appWidgetIds;
        this.appWidgetManager=appWidgetManager;
        MyTask task = new MyTask();
        task.execute("http://www.nib.si/mbp/apps/compass.rose/webapi/CompassRose/getData");


        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                MBPWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {


            // create some random data
            int number = (new Random().nextInt(100));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.mbp_layout);
            Log.w("WidgetExample", String.valueOf(number));
            // Set the text
            remoteViews.setTextViewText(R.id.update, String.valueOf(number));

            // Register an onClickListener
            Intent intent = new Intent(context, MBPWidgetProvider.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.layout, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }



    }

    public static void osveziPodatke(String jsonPodatki)
    {
        String currentTime;
        double currentWindSpeed = 0;
        double currentWindDirection = 0;
        double meanWindSpeed = 0;
        double meanWindBeaufort = 0;
        double meanWindDirection = 0;
        double temperatureSeaSurface = 0;
        double temperatureAir = 0;
        double wavesHeight = 0;
        double wavesDirection = 0;
        double wavesPeriod = 0;

        Log.w(LOG, jsonPodatki);
        try {
            JSONObject jsonObject = new JSONObject(jsonPodatki);
            currentTime = jsonObject.getString("currentTime");
            currentWindSpeed = jsonObject.getDouble("currentWindSpeed");
            currentWindDirection = jsonObject.getDouble("currentWindDirection");
            meanWindSpeed = jsonObject.getDouble("meanWindSpeed");
            meanWindBeaufort = jsonObject.getDouble("meanWindBeaufort");
            meanWindDirection = jsonObject.getDouble("meanWindDirection");
            temperatureSeaSurface = jsonObject.getDouble("temperatureSeaSurface");
            temperatureAir = jsonObject.getDouble("temperatureAir");
            wavesHeight = jsonObject.getDouble("wavesHeight");
            wavesDirection = jsonObject.getDouble("wavesDirection");
            wavesPeriod = jsonObject.getDouble("wavesPeriod");

            Log.d(LOG,"##>" + jsonObject.getString("currentTime"));
            Log.d(LOG,"##>" + currentTime);
            Log.d(LOG,"##>" + currentWindSpeed);
            Log.d(LOG,"##>" + currentWindDirection);
            Log.d(LOG,"##>" + meanWindSpeed);
            Log.d(LOG,"##>" + meanWindBeaufort);
            Log.d(LOG,"##>" + meanWindDirection);
            Log.d(LOG,"##>" + temperatureSeaSurface);
            Log.d(LOG,"##>" + temperatureAir);
            Log.d(LOG,"##>" + wavesHeight);
            Log.d(LOG,"##>" + wavesDirection);
            Log.d(LOG, "##>" + wavesPeriod);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.mbp_layout);
            remoteViews.setTextViewText(R.id.textView, String.valueOf(currentWindSpeed));

        }catch (JSONException ex)
        {
            Log.e(LOG,"Napaka pri parsanju JSON");
        }

    }


    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            return content;
        }

        protected void onPostExecute(String result) {
           if (result != null)
           {
              osveziPodatke(result);
           }
        }

    }
}