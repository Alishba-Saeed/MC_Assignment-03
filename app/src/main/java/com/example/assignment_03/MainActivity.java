package com.example.assignment_03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       RecyclerView rv;
       rv = findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        ArrayList<String> title=new ArrayList<String>();
        ArrayList<String> level=new ArrayList<String>();
        ArrayList<String> info=new ArrayList<String>();
        ArrayList<String> imageids=new ArrayList<String>();
        ArrayList<String> url=new ArrayList<String>();

//        String[] title=new String[1000];
//        String[] level=new String[1000];
//        String[] info=new String[1000];
//        String[] imageids=new String[1000];

        try {
            String json="";

            InputStream is = getResources().openRawResource(R.raw.issues);
            byte data[] = new byte[is.available()];
            while (is.read(data) != -1)
            {

            }
            json=new String(data);

            //Log.i("issues.json","data =>" + json);
            try {
                JSONObject jsonObject=new JSONObject(json);
                JSONArray obj=jsonObject.getJSONArray("books");
                for(int i=0;i<obj.length();i++)
                {
                    JSONObject temp=obj.getJSONObject(i);
                    title.add(temp.getString("title"));
                    level.add(temp.getString("level"));
                    info.add(temp.getString("info"));
                    imageids.add(temp.getString("cover"));
                    url.add(temp.getString("url"));

                }
                    CustomRecyclerView adapter=new CustomRecyclerView(this,title,level,info,imageids,url);
                       rv.setAdapter(adapter);

            }
            catch (JSONException e)
            {

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
