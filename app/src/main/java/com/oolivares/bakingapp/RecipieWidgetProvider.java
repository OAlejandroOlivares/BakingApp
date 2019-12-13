package com.oolivares.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class RecipieWidgetProvider extends AppWidgetProvider {


    public static final String EXTRA = "1";

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, String _list, String name, Boolean _detail) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipie_widget_provider);
        Intent intent = new Intent(context,WidgetRemoteViewsService.class);
        intent.putExtra("list",_list);
        views.setRemoteAdapter(R.id.wList,intent);
        Intent loadlist = new Intent(context,WidgetService.class);
        loadlist.setAction(WidgetService.ACTION_POPULATE_LIST);
        PendingIntent loadlistpendingintent =PendingIntent.getService(context,0,loadlist,0);
        Intent startActivityIntent = new Intent(context, RecipieWidgetProvider.class);
        PendingIntent startActivityPendingIntent = PendingIntent.getService(context, 0, startActivityIntent, 0);
        views.setPendingIntentTemplate(R.id.wList, startActivityPendingIntent);
        views.setTextViewText(R.id.w_recipie_name,name);
        views.setViewVisibility(R.id.no_recipie,View.GONE);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    public static void updateAppWidgetRecipie(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, String _list, boolean _detail, int recipie) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipie_widget_provider);
        Intent intent = new Intent(context,WidgetRemoteViewsService.class);
        intent.putExtra("list",_list);
        intent.putExtra("recipie",recipie);
        views.setRemoteAdapter(R.id.wList,intent);
        Intent loadlist = new Intent(context,WidgetService.class);
        loadlist.setAction(WidgetService.ACTION_POPULATE_LIST);
        PendingIntent loadlistpendingintent =PendingIntent.getService(context,0,loadlist,0);
        //views.setOnClickPendingIntent(R.id.no_data_tv,pendingIntent);
        // Instruct the widget manager to update the widget

        Intent startActivityIntent = new Intent(context, MainActivity.class);
        PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, 0);
        views.setPendingIntentTemplate(R.id.wList, startActivityPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    public static void updateAppWidget(Context applicationContext, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews views = new RemoteViews(applicationContext.getPackageName(), R.layout.recipie_widget_provider);
        views.setTextViewText(R.id.w_recipie_name,"Baking App");
        views.setViewVisibility(R.id.no_recipie,View.VISIBLE);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetService.startActionUpdateRecipies(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

