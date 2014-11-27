/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tonyostudios.ambiencesamples;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tonyostudios.ambience.AmbientTrack;


public class PlaybackOverlayActivity extends Activity {
    private static final String TAG = "PlaybackOverlayActivity";

    private PlaybackOverlayFragment playbackOverlayFragment;

    private TextView trackTitle;
    private ImageView albumImage;
    private TextView albumName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        //create and pass ambient track to fragment
        playbackOverlayFragment = new PlaybackOverlayFragment();
        playbackOverlayFragment.setArguments(getIntent().getExtras()); // pass bundle to fragment


        setUpView();


        if(savedInstanceState == null)
        {
            getFragmentManager().beginTransaction()
                    .add(R.id.container,playbackOverlayFragment)
                    .commit();
        }

    }

    private void setUpView() {

        trackTitle = (TextView) findViewById(R.id.track_title_id);
        albumImage = (ImageView) findViewById(R.id.album_image_id);
        albumName = (TextView) findViewById(R.id.album_title_id);


        AmbientTrack track = getIntent().getParcelableExtra(MainFragment.track_id);

        trackTitle.setText(track.getName());
        albumName.setText(track.getAlbumName());


        Picasso.with(this).load(track.getAlbumImageUri())
                .into(albumImage);
    }


}
