package com.example.gaminglibrary.activity;

import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gaminglibrary.R;

public class ContextMenu extends AppCompatActivity {
    private ListView listView;
    private String[] planets;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.MY_LISTVIEW);
        planets = getResources().getStringArray(R.array.Planeten);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,planets);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start_activity_contextmenu, menu);

        index = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        MenuItem share = menu.findViewById(R.id.CONTEXT_SHARE);
        MenuItem rate = menu.findViewById(R.id.CONTEXT_RATE);
        share.setTitle(planets[index]+ " teilen");
        share.setTitle(planets[index]+ " bewerten");


    }

    @Override
    public boolean onContextMenuSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.CONTEXT_SHARE:
                // döner
                return true;
            case R.id.CONTEXT_RATE:
                //mach hier was
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
