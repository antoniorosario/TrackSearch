package com.antoniorosario.musicsearch.ui.lyrics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.antoniorosario.musicsearch.R;
import com.antoniorosario.musicsearch.models.Track;
import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LyricsFragment extends Fragment implements LyricsView {

    private static final String ARG_RESULT = "arg_result";
    private static final String LYRICS_KEY = "lyrics_key";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.lyric_container) LinearLayout lyricContainer;
    @BindView(R.id.album_name) TextView albumNameTextView;
    @BindView(R.id.track_name) TextView trackNameTextView;
    @BindView(R.id.artist_name) TextView artistNameTextView;
    @BindView(R.id.lyrics) TextView lyricsTextView;
    @BindView(R.id.artwork) ImageView artworkImageView;

    private Track currentTrack;
    private LyricsPresenter lyricsPresenter;

    private String albumName;
    private String artistName;
    private String trackName;
    private String artworkUrl;
    private String lyrics;

    public static Fragment newInstance(Track track) {

        Bundle args = new Bundle();

        args.putParcelable(ARG_RESULT, Parcels.wrap(track));

        LyricsFragment fragment = new LyricsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        currentTrack = Parcels.unwrap(arguments.getParcelable(ARG_RESULT));
        lyricsPresenter = new LyricsPresenter();
        lyricsPresenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lyrics, container, false);
        ButterKnife.bind(this, view);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        activity.setSupportActionBar(toolbar);

        toolbar.setBackgroundResource(R.drawable.background_toolbar_translucent);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (currentTrack != null) {
            //Set up the UI for a track we selected from a search
            albumName = currentTrack.getAlbumName();
            artistName = currentTrack.getArtistName();
            trackName = currentTrack.getTrackName();
            artworkUrl = currentTrack.getArtworkUrl100();
            populateViewsWithTrackData();
        }

        if (savedInstanceState != null) {
            lyrics = savedInstanceState.getString(LYRICS_KEY);
            populateLyrics(lyrics);
        } else {
            lyricsPresenter.getLyrics(artistName, trackName);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LYRICS_KEY, lyrics);
    }

    private void populateViewsWithTrackData() {
        artistNameTextView.setText(String.format("%s • ", artistName));
        albumNameTextView.setText(albumName);
        trackNameTextView.setText(trackName);
        Picasso
                .with(getActivity())
                .load(artworkUrl)
                .fit()
                .into(artworkImageView, PicassoPalette.with(artworkUrl, artworkImageView)
                        .use(PicassoPalette.Profile.VIBRANT_LIGHT)
                        .intoBackground(lyricContainer, PicassoPalette.Swatch.RGB)
                );
    }

    @Override
    public void onLyricsLoaded(String lyrics) {
        this.lyrics = lyrics;
        populateLyrics(lyrics);
    }

    @Override
    public void onLyricsFailedToLoad() {
        lyricsTextView.setText(getString(R.string.error_finding_lyrics, trackName, artistName));
    }

    private void populateLyrics(String lyrics) {
        lyricsTextView.setText(lyrics);
    }
}


