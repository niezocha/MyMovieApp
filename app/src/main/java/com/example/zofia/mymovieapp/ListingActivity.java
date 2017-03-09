package com.example.zofia.mymovieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static io.reactivex.schedulers.Schedulers.io;

@RequiresPresenter(ListingPresenter.class)
public class ListingActivity extends NucleusAppCompatActivity<ListingPresenter> {

    private static final String SEARCH_TITLE = "search_title";
    private MovieListAdapter adapter;

    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;

    @BindView(R.id.no_internet)
    ImageView noInternetImage;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        ButterKnife.bind(this);

        String title = getIntent().getStringExtra(SEARCH_TITLE);
        adapter = new MovieListAdapter();
        recyclerView.setAdapter(adapter);

        getPresenter().getDataAsync(title)
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe(this::succes, this::error);
    }

    @OnClick(R.id.no_internet)
    public void onNoInternetViewClick(View view){
        Toast.makeText(this, "klikłeś w no_internet image view", Toast.LENGTH_LONG).show();

    }

    private void error(Throwable throwable) {
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noInternetImage));
    }

    private void succes(SearchResult searchResult) {
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(recyclerView));
        adapter.setItems(searchResult.getItems());
    }

    public static Intent createIntent(Context context, String title) {
        Intent intent = new Intent(context, ListingActivity.class);
        intent.putExtra(SEARCH_TITLE, title);
        return intent;
    }
}
