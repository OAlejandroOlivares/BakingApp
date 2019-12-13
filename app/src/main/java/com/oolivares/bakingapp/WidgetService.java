package com.oolivares.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

public class WidgetService extends IntentService {
    public static final String ACTION_UPDATE_RECIPIES = "com.oolivares.bakingapp.action.update_recipies";
    public static final String ACTION_POPULATE_LIST = "com.oolivares.bakingapp.action.populate_list";
    public static final String ACTION_RECIPIE_DETAIL = "com.oolivares.bakingapp.action.detail_list" ;

    public WidgetService() {
        super("WidgetService");
    }

    public static void startActionUpdateRecipies(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_UPDATE_RECIPIES);
        context.startService(intent);
    }

    public static void startActionUpdateList(Context context, String _list, String name) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_POPULATE_LIST);
        intent.putExtra("list",_list);
        intent.putExtra("name",name);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPIES.equals(action)) {
                handleActionUpdateRecipies(getApplicationContext());
            }else if (ACTION_POPULATE_LIST.equals(action)){
                handleActionUpdateList(getApplicationContext(),intent.getStringExtra("list"),intent.getStringExtra("name"),false);
            }else if (ACTION_RECIPIE_DETAIL.equals(action)){
                handleActionDetailRecipie(getApplicationContext(),intent.getStringExtra("list"),intent.getIntExtra("recipie",-1),true);
            }
        }
    }

    private void handleActionDetailRecipie(Context applicationContext, String list, int recipie, boolean b) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipieWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        RecipieWidgetProvider.updateAppWidgetRecipie(applicationContext,appWidgetManager,appWidgetIds,list,b,recipie);
    }

    private void handleActionUpdateList(Context applicationContext, String list, String name, boolean b) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipieWidgetProvider.class));
        RecipieWidgetProvider.updateAppWidget(applicationContext,appWidgetManager,appWidgetIds,list,name,b);
    }

    private void handleActionUpdateRecipies(Context applicationContext) {
        //FIRST TIME CREATED
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipieWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        RecipieWidgetProvider.updateAppWidget(applicationContext,appWidgetManager,appWidgetIds);
    }

}
