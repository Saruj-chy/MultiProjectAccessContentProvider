package com.example.controllerproject.notify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.controllerproject.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView ;
    List<NotifyModel> productList;
    ArrayList<NotifyModel> sqLiteList;
    DatabaseHandler db = new DatabaseHandler(this) ;
    private NotificationAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mRecyclerView = findViewById(R.id.recyclerView_notify) ;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>() ;

        showDataAtListView();
    }

    public void showDataAtListView() {
        sqLiteList = db.getAllInfo(getApplicationContext());
//        Toast.makeText(this, "Yes", Toast.LENGTH_SHORT).show();

        Log.e("TAG", "size:  "+ sqLiteList.size() ) ;

        for (int i = 0; i < sqLiteList.size(); i++) {

            if (!sqLiteList.get(i).getTopic().isEmpty())
            {
                productList.add(
                        new NotifyModel(
                                sqLiteList.get(i).getId(),
                                sqLiteList.get(i).getTitle(),
                                sqLiteList.get(i).getBody_text(),
                                sqLiteList.get(i).getTopic()
                        )
                );
            }


        }
        Log.d("TAG", "tempList: "+productList) ;

        adapter = new NotificationAdapter(getApplicationContext(), productList);
        mRecyclerView.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

    }
}