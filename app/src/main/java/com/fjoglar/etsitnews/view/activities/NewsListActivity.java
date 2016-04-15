/*
 * Copyright (C) 2016 Felipe Joglar Santos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fjoglar.etsitnews.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fjoglar.etsitnews.R;
import com.fjoglar.etsitnews.executor.ThreadExecutor;
import com.fjoglar.etsitnews.model.entities.NewsItem;
import com.fjoglar.etsitnews.navigation.Navigator;
import com.fjoglar.etsitnews.presenter.NewsListPresenter;
import com.fjoglar.etsitnews.presenter.NewsListPresenterImpl;
import com.fjoglar.etsitnews.threading.MainThreadImpl;
import com.fjoglar.etsitnews.view.adapter.NewsListAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsListActivity extends AppCompatActivity
        implements NewsListPresenter.View, NewsListAdapter.ItemClickListener {

    private static final String ACTIVITY_SOURCE = "NEWS";

    private NewsListPresenter mNewsListPresenter;
    private Context mContext;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.recycler_news_list) RecyclerView recyclerNewsList;
    @Bind(R.id.progress_bar) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mContext = this;
        this.initializeActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNewsListPresenter.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNewsListPresenter.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mNewsListPresenter.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        mNewsListPresenter.destroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_collections_bookmarks:
                Navigator.getInstance().navigateToBookmarksList(getContext());
                return true;
            case R.id.action_filter:
                Toast.makeText(this, "Filtrar", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "Ajustes", Toast.LENGTH_SHORT).show();
                return true;
        }

        return true;
    }

    @Override
    public void itemClicked(long date) {
        Navigator.getInstance().navigateToNewsDetails(getContext(), date, ACTIVITY_SOURCE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showNews(List<NewsItem> newsItemList) {
        NewsListAdapter adapter = (NewsListAdapter) recyclerNewsList.getAdapter();
        adapter.setNewsListAdapter(newsItemList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, NewsListActivity.class);
    }

    private void initializeActivity() {
        mNewsListPresenter = new NewsListPresenterImpl(ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this);
        setUpRecyclerView();
        setUpToolbar();
    }

    private void setUpRecyclerView() {
        final NewsListAdapter adapter = new NewsListAdapter(this);
        recyclerNewsList.setAdapter(adapter);
        recyclerNewsList.setLayoutManager(new LinearLayoutManager(getParent(),
                LinearLayoutManager.VERTICAL,
                false)
        );
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle(R.string.news_list_activity_title);
    }

    private Context getContext() {
        return mContext;
    }

}
