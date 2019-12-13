package com.oolivares.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private String list;
    private ArrayList arrayList = new ArrayList<String>();
    private int recipie = -1;

    public WidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        int appwidgetid = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        list = intent.getStringExtra("list");
        if (intent.hasExtra("recipie")){
            recipie = intent.getIntExtra("recipie",-1);
        }
        populateListItem();
    }

    private void populateListItem() {
        try {
            JSONArray jsonArray = new JSONArray(list);
                for(int i = 0 ; i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    arrayList.add(jsonObject.getString("ingredient"));
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        final long identityToken = Binder.clearCallingIdentity();

        Binder.restoreCallingIdentity(identityToken);

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        int label = position + 1;
        rv.setTextViewText(R.id.tv_widget,label+": "+ arrayList.get(position).toString());
        Intent loadlist = new Intent(mContext,WidgetService.class);
        loadlist.setAction(WidgetService.ACTION_RECIPIE_DETAIL);
        loadlist.putExtra("recipie",position);
        loadlist.putExtra("list",list);
        PendingIntent loadlistpendingintent =PendingIntent.getService(mContext,0,loadlist,0);
        Bundle extras = new Bundle();
        extras.putInt(RecipieWidgetProvider.EXTRA, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        fillInIntent.putExtra(WidgetService.ACTION_RECIPIE_DETAIL, position);
        if (recipie == -1) {
            rv.setOnClickFillInIntent(R.id.item_container, fillInIntent);
            //rv.setOnClickPendingIntent(R.id.item_container,loadlistpendingintent);
            //rv.setOnClickPendingIntent(R.id.tv_widget,loadlistpendingintent);
            //rv.setOnClickFillInIntent(R.id.tv_widget,fillInIntent);
            //rv.setOnClickFillInIntent(R.id.item_container,fillInIntent);
        }

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
