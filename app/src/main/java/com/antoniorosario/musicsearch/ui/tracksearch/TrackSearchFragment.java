package com.antoniorosario.musicsearch.ui.tracksearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.antoniorosario.musicsearch.R;
import com.antoniorosario.musicsearch.models.Track;
import com.antoniorosario.musicsearch.ui.SimpleItemDecoration;
import com.antoniorosario.musicsearch.utils.ConnectivityUtils;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TrackSearchFragment extends Fragment implements TrackSearchView, SearchView.OnQueryTextListener {

    private static final String QUERY_STRING_KEY = "query_string";

    @BindView(R.id.loading_indicator) ProgressBar progressBar;
    @BindView(R.id.search_title_text) TextView searchTitleTextView;
    @BindView(R.id.search_subtitle_text) TextView searchSubtitleTextView;
    @BindView(R.id.search_icon) ImageView searchIcon;
    @BindView(R.id.retry_query_button) ImageButton retryQueryButton;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.track_list) RecyclerView recyclerView;

    private SearchView searchView;
    private TrackSearchAdapter trackSearchAdapter;
    private TrackSearchPresenter trackSearchPresenter;
    private List<Track> trackList = Collections.emptyList();

    private String query;

    public static TrackSearchFragment newInstance() {
        return new TrackSearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        trackSearchAdapter = new TrackSearchAdapter();
        trackSearchPresenter = new TrackSearchPresenter();
        trackSearchPresenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_track, container, false);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            query = savedInstanceState.getString(QUERY_STRING_KEY);
        }

        if (trackList == null || trackList.isEmpty()) {
            showActiveSearch();
        }
        setHasOptionsMenu(true);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleItemDecoration(getActivity()));

        recyclerView.setAdapter(trackSearchAdapter);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QUERY_STRING_KEY, query);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // initialize searchview
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search_track);
        // work around for saving query state
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (!TextUtils.isEmpty(query)) {
            searchItem.expandActionView();
            searchView.setQuery(query, true);
            searchView.clearFocus();
        }
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        trackSearchPresenter.setView(null);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // Check whether or not there is an active network connection
        if (ConnectivityUtils.isConnected(getActivity())) {
            // Search submitted with an active connection
            // Fetch the data remotely
            trackSearchPresenter.executeQuery(query);
        } else {
            // Search submitted without an active connection
            showOfflineSearchView();
        }
        // Reset searchView query
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.query = newText;
        // Let the user know they don't have an active network connection while typing, else resume search
        if (!(ConnectivityUtils.isConnected(getActivity()))) {
            showDeviceIsOfflineView();
        } else {
            trackSearchAdapter.clear();
            showActiveSearch();
        }
        return false;
    }

    //Allow user to retry query, only visible if there is no internet connection
    @OnClick(R.id.retry_query_button)
    public void retryQuery() {
        onQueryTextSubmit(query);
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        if (isRefreshing) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTracksLoaded(List<Track> tracks) {
        this.trackList = tracks;
        searchIcon.setVisibility(View.GONE);
        searchSubtitleTextView.setText("");
        searchTitleTextView.setText("");
        progressBar.setVisibility(View.GONE);
        retryQueryButton.setVisibility(View.GONE);
        trackSearchAdapter.clear();

        // If we have a list of tracks add it to the adapter
        if (tracks != null && !tracks.isEmpty()) {
            trackSearchAdapter.clear();
            trackSearchAdapter.setResults(tracks);
            recyclerView.setAdapter(trackSearchAdapter);
            trackSearchAdapter.notifyDataSetChanged();
        } else {
            // If a users search did not return tracks let them know
            searchTitleTextView.setText(R.string.search_no_results_title_text);
            searchSubtitleTextView.setText("");
        }
    }

    @Override
    public void onTracksFailedToLoad() {
        Toast.makeText(getActivity(), R.string.error_string, Toast.LENGTH_SHORT).show();
        showActiveSearch();
    }

    @Override
    public void showActiveSearch() {
        searchIcon.setVisibility(View.VISIBLE);
        searchTitleTextView.setText(getString(R.string.search_active_string_title));
        searchSubtitleTextView.setText(getString(R.string.search_active_state_string_subtitle));
        searchIcon.setVisibility(View.VISIBLE);
        retryQueryButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showOfflineSearchView() {
        trackSearchAdapter.clear();
        progressBar.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);
        searchTitleTextView.setText(R.string.search_offline_title_text);
        searchSubtitleTextView.setText(R.string.search_offline_subtitle_text);
        retryQueryButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDeviceIsOfflineView() {
        trackSearchAdapter.clear();
        progressBar.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);
        searchTitleTextView.setText(R.string.search_device_is_offline_title);
        searchSubtitleTextView.setText(R.string.search_device_is_offline_subtitle);
        retryQueryButton.setVisibility(View.GONE);
    }

    @Override
    public void showSearchingView() {
        progressBar.setVisibility(View.VISIBLE);
        searchIcon.setVisibility(View.GONE);
        searchSubtitleTextView.setText("");
        searchTitleTextView.setText("");
    }

}
