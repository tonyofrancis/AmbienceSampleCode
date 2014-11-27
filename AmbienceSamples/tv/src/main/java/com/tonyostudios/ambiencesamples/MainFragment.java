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


import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;

import android.util.Log;

import com.tonyostudios.ambience.AmbientTrack;


public class MainFragment extends BrowseFragment {

    private static final String TAG = "MainFragment";

    public static final String track_id = "ambient_track";

    private ArrayObjectAdapter mRowsAdapter;

    public MainFragment() {
        super();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);


        setTitle(getString(R.string.app_name));

        setOnItemViewClickedListener(new ItemViewClickedListener());

        loadRows(); //create list

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    //set fragment rows
    private void loadRows() {

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());


        AmbientTrack[] mData = SampleData.getmData();

        TrackPresenter trackPresenter = new TrackPresenter();


        HeaderItem trackHeader = new HeaderItem("Tracks", null);

        ArrayObjectAdapter tracksAdapter = new ArrayObjectAdapter(trackPresenter);

        for(int x = 0;x < mData.length; x ++)
        {
            tracksAdapter.add(mData[x]);
        }

        mRowsAdapter.add(new ListRow(trackHeader, tracksAdapter));

        setAdapter(mRowsAdapter);

    }


    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {


            AmbientTrack track = (AmbientTrack) item;


            Intent intent = new Intent(getActivity(),PlaybackOverlayActivity.class);
            intent.putExtra(track_id,track);

            startActivity(intent); // call the playback activity;


        }
    }




}
