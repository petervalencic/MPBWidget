package com.petervalencic.mpbwidget;

/**
 * Created by peterv on 1.2.2016.
 */
import java.util.Random;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {
    public UpdateWidgetService()
    {
        super();
        Log.w(LOG, "Konstruktor  ");
    }
    private static final String LOG = "MBPWidget";

    @Override
    public void onStart(Intent intent, int startId) {
        Log.w(LOG, "onStart");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
                .getApplicationContext());

        int[] allWidgetIds = intent
                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

//    ComponentName thisWidget = new ComponentName(getApplicationContext(),
//        MyWidgetProvider.class);
//    int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int widgetId : allWidgetIds) {
            // create some random data
            int number = (new Random().nextInt(100));

            RemoteViews remoteViews = new RemoteViews(this
                    .getApplicationContext().getPackageName(),
                    R.layout.mbp_layout);
            Log.w(LOG, String.valueOf(number));
            // Set the text
            remoteViews.setTextViewText(R.id.update,
                    "Random: " + String.valueOf(number));

            // Register an onClickListener
            Intent clickIntent = new Intent(this.getApplicationContext(),
                    MBPWidgetProvider.class);

            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                    allWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
        stopSelf();

        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}