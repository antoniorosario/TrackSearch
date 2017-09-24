package com.antoniorosario.musicsearch.ui.lyrics;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.antoniorosario.musicsearch.models.Track;
import com.antoniorosario.musicsearch.ui.SingleFragmentActivity;

import org.parceler.Parcels;

public class LyricsActivity extends SingleFragmentActivity {

    private static final String EXTRA_RESULT = "extra_result";

    public static Intent newIntent(Context packageContext, Track track) {
        Intent intent = new Intent(packageContext, LyricsActivity.class);
        intent.putExtra(EXTRA_RESULT, Parcels.wrap(track));
        return intent;
    }

    @Override
    protected Fragment createFragment() {

        Intent intent = getIntent();

        // Track object we return via search
        Track currentTrack = Parcels.unwrap(intent.getParcelableExtra(EXTRA_RESULT));


        return LyricsFragment.newInstance(currentTrack);
    }
}
