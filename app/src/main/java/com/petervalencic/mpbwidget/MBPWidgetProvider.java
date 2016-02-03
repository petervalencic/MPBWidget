package com.petervalencic.mpbwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by peterv on 1.2.2016.
 */
public class MBPWidgetProvider extends AppWidgetProvider {
    private static final String LOG = "MBPWidget";
    private static final String ACTION_CLICK = "ACTION_CLICK";
    private static JSONObject jsonObject;
    private static String json;
    static Context context;
    static AppWidgetManager appWidgetManager;
    static int[] appWidgetIds;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);
        Log.w(LOG, "onReceive method called");
        MyTask task = new MyTask();
        task.execute("http://www.nib.si/mbp/apps/compass.rose/webapi/CompassRose/getData");

        //

    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.w(LOG, "updateAppWidget");
        this.context = context;
        this.appWidgetIds = appWidgetIds;
        this.appWidgetManager = appWidgetManager;

        // Get all ids
        ComponentName thisWidget = new ComponentName(context, MBPWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {


            // create some random data
            int number = (new Random().nextInt(100));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.mbp_layout);

            Log.w("WidgetExample", String.valueOf(number));

            // Register an onClickListener
            Intent intent = new Intent(context, MBPWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);


            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setOnClickPendingIntent(R.id.layout, pendingIntent);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
            // showWebPage(context, appWidgetManager, widgetId);


        }
        //MyTask task = new MyTask();
        //task.execute("http://www.nib.si/mbp/apps/compass.rose/webapi/CompassRose/getData");


    }

    public static void showWebPage(Context ctx, AppWidgetManager appWidgetManager, int widgetID) {
        RemoteViews widgetView = new RemoteViews(ctx.getPackageName(), R.layout.mbp_layout);
        Uri uri = Uri.parse("http://google.com/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pIntent = PendingIntent.getActivity(ctx, widgetID, intent, 0);
        // viewID - our clickable view ID
        widgetView.setOnClickPendingIntent(R.id.imageView, pIntent);

        appWidgetManager.updateAppWidget(widgetID, widgetView);


    }

    public static void osveziPodatke(String jsonPodatki) {
        String currentTime;
        double currentWindSpeed;
        double currentWindDirection;
        double meanWindSpeed;
        double meanWindBeaufort;
        double meanWindDirection;
        double temperatureSeaSurface;
        double temperatureAir;
        double wavesHeight;
        double wavesDirection;
        double wavesPeriod;

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

            Log.d(LOG, "##>" + jsonObject.getString("currentTime"));
            Log.d(LOG, "##>" + currentTime);
            Log.d(LOG, "##>" + currentWindSpeed);
            Log.d(LOG, "##>" + currentWindDirection);
            Log.d(LOG, "##>" + meanWindSpeed);
            Log.d(LOG, "##>" + meanWindBeaufort);
            Log.d(LOG, "##>" + meanWindDirection);
            Log.d(LOG, "##>" + temperatureSeaSurface);
            Log.d(LOG, "##>" + temperatureAir);
            Log.d(LOG, "##>" + wavesHeight);
            Log.d(LOG, "##>" + wavesDirection);
            Log.d(LOG, "##>" + wavesPeriod);

            //osvežimo zapis na widget-u
            ComponentName thisWidget = new ComponentName(context, MBPWidgetProvider.class);

            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            for (int widgetId : allWidgetIds) {
                Resources res = context.getResources();
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.mbp_layout);
                if (res != null) {
                    String text = String.format(res.getString(R.string.s_currentWindSpeed), "123");
                    Log.e(LOG, text);
                    remoteViews.setTextViewText(R.id.t_currentWindSpeed, String.format(res.getString(R.string.s_currentWindSpeed), currentWindSpeed));
                    remoteViews.setTextViewText(R.id.t_currentWindDirection, String.format(res.getString(R.string.s_currentWindDirection), currentWindDirection));
                    remoteViews.setTextViewText(R.id.t_meanWindSpeed, String.format(res.getString(R.string.s_meanWindSpeed), meanWindSpeed));
                    remoteViews.setTextViewText(R.id.t_meanWindDirection, String.format(res.getString(R.string.s_meanWindDirection), meanWindDirection));
                    remoteViews.setTextViewText(R.id.t_meanWindBeaufort, String.format(res.getString(R.string.s_meanWindBeaufort), meanWindBeaufort));
                    remoteViews.setTextViewText(R.id.t_temperatureSeaSurface, String.format(res.getString(R.string.s_temperatureSeaSurface), temperatureSeaSurface));
                    remoteViews.setTextViewText(R.id.t_temperatureAir, String.format(res.getString(R.string.s_temperatureAir), temperatureAir));
                    //parsanje
                }
                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }

        } catch (JSONException ex) {
            Log.e(LOG, "Napaka pri parsanju JSON");
        }

    }


    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            Log.w("MBPWidget", "poklican AsyncTask" + params[0]);
            return HttpManager.getData(params[0]);
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                osveziPodatke(result);
            }
        }

    }
}