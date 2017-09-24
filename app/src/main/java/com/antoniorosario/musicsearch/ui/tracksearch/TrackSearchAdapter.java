package com.antoniorosario.musicsearch.ui.tracksearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.antoniorosario.musicsearch.R;
import com.antoniorosario.musicsearch.models.Track;
import com.antoniorosario.musicsearch.ui.lyrics.LyricsActivity;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackSearchAdapter extends RecyclerView.Adapter<TrackSearchAdapter.TrackHolder> {

    private List<Track> tracks = Collections.emptyList();

    public TrackSearchAdapter() {
    }

    @Override
    public TrackHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_list_item, parent, false);
        return new TrackHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackHolder holder, int position) {
        Track track = tracks.get(position);
        holder.bindTrack(track);
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setResults(List<Track> tracks) {
        this.tracks = tracks;
    }

    public void clear() {
        int size = tracks.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                tracks.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }

    private Track getItem(int position) {
        return tracks.get(position);
    }

    public static class TrackHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.album_name) TextView albumNameTextView;
        @BindView(R.id.track_name) TextView trackNameTextView;
        @BindView(R.id.artist_name) TextView artistNameTextView;
        @BindView(R.id.artwork) ImageView artworkImageView;

        private Context context;
        private Track track;

        public TrackHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bindTrack(Track track) {
            this.track = track;
            String albumName = track.getAlbumName();
            String trackName = track.getTrackName();
            String artistName = track.getArtistName();
            String artworkUrl = track.getArtworkUrl100();

            artistNameTextView.setText(String.format("%s • ", artistName));
            albumNameTextView.setText(albumName);
            trackNameTextView.setText(trackName);
            if (artworkUrl != null) {
                Picasso
                        .with(context)
                        .load(artworkUrl)
                        .fit()
                        .into(artworkImageView);
            } else {
                artworkImageView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {
            context.startActivity(LyricsActivity.newIntent(context, track));
        }

        @Override
        public boolean onLongClick(View v) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_song_subject));
            intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_song_text, track.getTrackName(),
                    track.getArtistName()));
            intent = Intent.createChooser(intent, context.getString(R.string.send_track));

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
            return true;
        }
    }
}
