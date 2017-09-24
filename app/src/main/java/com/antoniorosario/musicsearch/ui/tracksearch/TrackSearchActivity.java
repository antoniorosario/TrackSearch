package com.antoniorosario.musicsearch.ui.tracksearch;

import android.support.v4.app.Fragment;

import com.antoniorosario.musicsearch.ui.SingleFragmentActivity;

public class TrackSearchActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return TrackSearchFragment.newInstance();
    }
}
