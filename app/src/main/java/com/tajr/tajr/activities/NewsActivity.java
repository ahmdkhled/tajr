package com.tajr.tajr.activities;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tajr.tajr.R;
import com.tajr.tajr.adapters.NewsAdapter;
import com.tajr.tajr.models.News;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewsActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @BindView(R.id.news_recycler_view)
    RecyclerView newsRecyclerView;

    LinearLayoutManager newsLinearLayoutManager;
    NewsAdapter adapter;
    List<News> newsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);

        newsList = new ArrayList<>();
        newsList.add(new News("eslam","eslam","eslam"));
        newsList.add(new News("eslam","eslam","eslam"));
        newsList.add(new News("eslam","eslam","eslam"));
        adapter = new NewsAdapter(this,newsList);
        newsLinearLayoutManager = new LinearLayoutManager(this);
        newsRecyclerView.setLayoutManager(newsLinearLayoutManager);
        newsRecyclerView.setAdapter(adapter);
    }
}
