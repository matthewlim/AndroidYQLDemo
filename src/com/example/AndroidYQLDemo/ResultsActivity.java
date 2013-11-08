package com.example.AndroidYQLDemo;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

/**
 * User: mattlim
 * Date: 11/8/13
 * Time: 11:54 AM
 * Copyright (c) 2013 Yahoo! Inc. All rights reserved.
 */
public class ResultsActivity extends Activity {


    public ListView listView;
    public ListAdapter listAdapter;
    public ArrayList<String> resultData;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
        boolean hasExtras = getIntent() != null && getIntent().getExtras() != null;
        if (hasExtras) {
            resultData = getIntent().getExtras().getStringArrayList("results");
        }

        listView = (ListView) findViewById(R.id.results_list);
        listAdapter = new ListAdapter() {

            @Override
            public boolean areAllItemsEnabled() {
                return true;
            }

            @Override
            public boolean isEnabled(int position) {
                return false;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return resultData.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = new TextView(ResultsActivity.this);
                }
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.listitem_height));
                convertView.setLayoutParams(params);
                ((TextView) convertView).setText(resultData.get(position));
                return convertView;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };
        listView.setAdapter(listAdapter);

    }
}