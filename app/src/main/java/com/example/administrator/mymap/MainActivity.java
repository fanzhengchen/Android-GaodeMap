package com.example.administrator.mymap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private MainAcitivityAdapter adapter;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<Class> next = new ArrayList<Class>();
    private ArrayList<Intent> intents = new ArrayList<Intent>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpData();
        setUpImageLoader();
    }

    private void setUpData() {
        listView = (ListView) findViewById(R.id.main_acitvity_list_view);

        list.add("Location");
        list.add("Circle");
        intents.add(new Intent(MainActivity.this, LocationAcitivity.class));
        intents.add(new Intent(MainActivity.this, CircleAcitivity.class));

        adapter = new MainAcitivityAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void setUpImageLoader() {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.stub_image)
                .showImageOnLoading(R.drawable.stub_image)
                .showImageOnFail(R.drawable.stub_image)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .resetViewBeforeLoading(true).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(displayImageOptions);

        ImageLoader.getInstance().init(builder.build());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(intents.get(position));
    }
}
