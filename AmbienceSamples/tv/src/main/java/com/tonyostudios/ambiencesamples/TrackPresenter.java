package com.tonyostudios.ambiencesamples;

import android.content.Context;
import android.support.v17.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tonyostudios.ambience.AmbientTrack;

/**
 * Created by tonyostudios on 11/26/14.
 */
public class TrackPresenter extends Presenter {

    private static Context mContext;



    public static class ViewHolder extends Presenter.ViewHolder
    {
        private View mView;
        private ImageView AlbumImageView;
        private TextView TrackNameTextView;
        private TextView AlbumNameTextView;


        public ViewHolder(View view) {
            super(view);

            mView = view;
            AlbumImageView = (ImageView) view.findViewById(R.id.album_image_id);
            TrackNameTextView = (TextView) view.findViewById(R.id.album_title_id);
            AlbumNameTextView = (TextView) view.findViewById(R.id.album_subtitle_id);

        }

        public View getmView() {
            return mView;
        }

        public ImageView getAlbumImageView() {
            return AlbumImageView;
        }

        public TextView getTrackNameTextView() {
            return TrackNameTextView;
        }

        public TextView getAlbumNameTextView() {
            return AlbumNameTextView;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {

        //inflate card here

        mContext = parent.getContext();
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.track_card,parent,false);

        //Can focus on card
        rootView.setFocusable(true);
        rootView.setFocusableInTouchMode(true);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {

        //bind ambient track data to view

        final AmbientTrack track = (AmbientTrack) item;
        ViewHolder holder = (ViewHolder) viewHolder;



        Picasso.with(mContext)
                .load(track.getAlbumImageUri())
                .resize(150,150)
                .into(holder.getAlbumImageView());

        holder.getTrackNameTextView()
                .setText(track.getName());

        holder.getAlbumNameTextView()
                .setText(track.getAlbumName());

    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        //Clean up resources here
    }
}
