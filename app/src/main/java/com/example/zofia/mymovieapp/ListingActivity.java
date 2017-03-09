package com.example.zofia.mymovieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private static final String SEARCH_YEAR = "search_year";
    private static final String SEARCH_TYPE = "search_type";
    public static final int NO_YEAR_SELECTED = 0;
    private MovieListAdapter adapter;

    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;

    @BindView(R.id.no_internet)
    ImageView noInternetImage;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.no_reslts)
    LinearLayout noResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        ButterKnife.bind(this);

        String title = getIntent().getStringExtra(SEARCH_TITLE);
        int year = getIntent().getIntExtra(SEARCH_YEAR, NO_YEAR_SELECTED);
        String type = getIntent().getStringExtra(SEARCH_TYPE);

        adapter = new MovieListAdapter();
        recyclerView.setAdapter(adapter);

        getPresenter().getDataAsync(title, year, type)
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
        if("False".equalsIgnoreCase(searchResult.getResponse())){
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noResults));
        }else {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(recyclerView));
            adapter.setItems(searchResult.getItems());
        }
    }

    public static Intent createIntent(Context context, String title, int year, String type) {
        Intent intent = new Intent(context, ListingActivity.class);
        intent.putExtra(SEARCH_TITLE, title);
        intent.putExtra(SEARCH_YEAR, year);
        intent.putExtra(SEARCH_TYPE, type);
        return intent;
    }
}
