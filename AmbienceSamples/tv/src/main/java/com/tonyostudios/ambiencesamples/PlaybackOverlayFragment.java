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


import android.content.Context;
import android.os.Bundle;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.ControlButtonPresenterSelector;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.PlaybackControlsRow;
import android.support.v17.leanback.widget.PlaybackControlsRow.FastForwardAction;
import android.support.v17.leanback.widget.PlaybackControlsRow.PlayPauseAction;
import android.support.v17.leanback.widget.PlaybackControlsRow.RewindAction;
import android.support.v17.leanback.widget.PlaybackControlsRow.SkipNextAction;
import android.support.v17.leanback.widget.PlaybackControlsRow.SkipPreviousAction;
import android.support.v17.leanback.widget.PlaybackControlsRowPresenter;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.Log;

import com.tonyostudios.ambience.Ambience;
import com.tonyostudios.ambience.AmbientTrack;


public class PlaybackOverlayFragment extends android.support.v17.leanback.app.PlaybackOverlayFragment
implements Ambience.AmbientListener {
    private static final String TAG = "PlaybackControlsFragment";

    private final int TEN_SECONDS = 10000;

    private ArrayObjectAdapter mRowsAdapter;
    private ArrayObjectAdapter mPrimaryActionsAdapter;
    private ArrayObjectAdapter mSecondaryActionsAdapter;

    private PlayPauseAction mPlayPauseAction;
    private SkipNextAction mSkipNextAction;
    private SkipPreviousAction mSkipPreviousAction;
    private RewindAction mRewindAction;
    private FastForwardAction mFastForwardAction;

    private PlaybackControlsRow mPlaybackControlsRow;
    private RowPresenter mRowPresenter;

    private Context sContext;

    private AmbientTrack mTrack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        sContext = getActivity();
        setBackgroundType(BG_NONE);


        setupRows();


    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        //get track
        mTrack = args.getParcelable(MainFragment.track_id);

    }

    @Override
    public void onResume() {
        super.onResume();

        //Listen for AmbientUpdates
        Ambience.activeInstance()
                .listenForUpdatesWith(this)
                .setNotificationLaunchActivity(getString(R.string.notification_identifier));

        play(); //start playing track

    }

    @Override
    public void onPause() {
        super.onPause();

        //stop listening for ambient updates
        Ambience.activeInstance()
                .stopListeningForUpdates();

    }

    private void setupRows() {
        ClassPresenterSelector cp = new ClassPresenterSelector();

        PlaybackControlsRowPresenter playbackControlsRowPresenter = new PlaybackControlsRowPresenter();
        playbackControlsRowPresenter.setBackgroundColor(getResources().getColor(R.color.primary));
        playbackControlsRowPresenter.setProgressColor(getResources().getColor(R.color.primary_dark));


        playbackControlsRowPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {

                if(action.getId() == mPlayPauseAction.getId())
                {
                    if(mPlayPauseAction.getIndex() == PlaybackControlsRow.PlayPauseAction.PLAY)
                    {
                        resume();
                    }
                    else
                    {
                        pause();
                    }
                }
                else if(action.getId() == mSkipPreviousAction.getId())
                {
                    prev();
                }
                else if(action.getId() == mSkipNextAction.getId())
                {
                    next();
                }
                else if(action.getId() == mFastForwardAction.getId())
                {
                    forward();
                }
                else if(action.getId() == mRewindAction.getId())
                {
                    rewind();
                }


                if (action instanceof PlaybackControlsRow.MultiAction) {
                    ((PlaybackControlsRow.MultiAction) action).nextIndex();
                    notifyChanged(action);
                }

            }
        });

        cp.addClassPresenter(PlaybackControlsRow.class, playbackControlsRowPresenter);

        mRowPresenter = new ListRowPresenter();
        cp.addClassPresenter(ListRow.class, mRowPresenter);

        mRowsAdapter = new ArrayObjectAdapter(cp);


        addPlaybackControlsRow();

    }


    private void notifyChanged(Action action) {

        ArrayObjectAdapter adapter = mPrimaryActionsAdapter;
        if (adapter.indexOf(action) >= 0) {
            adapter.notifyArrayItemRangeChanged(adapter.indexOf(action), 1);
        }
    }

    private void forward()
    {
        int progress = mPlaybackControlsRow.getCurrentTime() + TEN_SECONDS;

        if(progress >= mPlaybackControlsRow.getTotalTime())
        {
            progress = mPlaybackControlsRow.getTotalTime();
        }

        //Seek Ambient Track
        Ambience.activeInstance()
                .seekTo(progress);

    }

    private void repeatOne()
    {

        Ambience.activeInstance()
                .repeatASingleTrack();
    }

    private void repeatAll()
    {

        Ambience.activeInstance()
                .repeatAllTracks();
    }

    private void turnOffRepeat()
    {
        Ambience.activeInstance()
                .turnRepeatOff();
    }

    private void rewind()
    {

        int progress = mPlaybackControlsRow.getCurrentTime() - TEN_SECONDS;

        if(progress < 0)
        {
            progress = 0;
        }

        //Seek Ambient Track
        Ambience.activeInstance()
                .seekTo(progress);

    }


    private void shufflePlaylist()
    {
        //Seek Ambient Track
        Ambience.activeInstance()
                .shufflePlaylist();
    }

    private void unShufflePlaylist()
    {
        Ambience.activeInstance()
                .unShufflePlaylist();
    }

    private void resume()
    {

        //Resume Ambient Track
        Ambience.activeInstance()
                .resume();

    }

    private void pause()
    {
        Ambience.activeInstance()
                .pause();


    }

    private void next()
    {

        Ambience.activeInstance()
                .skip();

    }

    private void prev()
    {
        Ambience.activeInstance().previous();
    }

    private void switchPlayPauseControls(int index)
    {
        mPlayPauseAction.setIndex(index);
        notifyChanged(mPlayPauseAction);
    }

    private void play()
    {
        switchPlayPauseControls(PlaybackControlsRow.PlayPauseAction.PAUSE);

        //send play request to service
        Ambience.activeInstance()
                .setPlaylistTo(new AmbientTrack[]{mTrack})
                .play();



    }

    private void addPlaybackControlsRow() {

        mPlaybackControlsRow = new PlaybackControlsRow();

        mRowsAdapter.add(mPlaybackControlsRow);

        ControlButtonPresenterSelector presenterSelector = new ControlButtonPresenterSelector();

        mPrimaryActionsAdapter = new ArrayObjectAdapter(presenterSelector);
        mSecondaryActionsAdapter = new ArrayObjectAdapter(presenterSelector);
        mPlaybackControlsRow.setPrimaryActionsAdapter(mPrimaryActionsAdapter);
        mPlaybackControlsRow.setSecondaryActionsAdapter(mSecondaryActionsAdapter);

        mPlayPauseAction = new PlaybackControlsRow.PlayPauseAction(sContext);
        mSkipNextAction = new PlaybackControlsRow.SkipNextAction(sContext);
        mRewindAction = new PlaybackControlsRow.RewindAction(sContext);
        mSkipPreviousAction = new PlaybackControlsRow.SkipPreviousAction(sContext);
        mFastForwardAction = new PlaybackControlsRow.FastForwardAction(sContext);


        mPrimaryActionsAdapter.add(mSkipPreviousAction);
        mPrimaryActionsAdapter.add(mRewindAction);
        mPrimaryActionsAdapter.add(mPlayPauseAction);
        mPrimaryActionsAdapter.add(mFastForwardAction);
        mPrimaryActionsAdapter.add(mSkipNextAction);


        setAdapter(mRowsAdapter);
    }


    @Override
    public void ambienceIsPreppingTrack() {

    }

    @Override
    public void ambienceTrackDuration(int time) {
        mRowsAdapter.notifyArrayItemRangeChanged(0, 1);
        mPlaybackControlsRow.setTotalTime(time);
    }

    @Override
    public void ambiencePlayingTrack(AmbientTrack track) {
      //not implemented
    }

    @Override
    public void ambienceTrackCurrentProgress(int time) {
        mRowsAdapter.notifyArrayItemRangeChanged(0, 1);

        mPlaybackControlsRow.setCurrentTime(time);
    }

    @Override
    public void ambienceTrackIsPlaying() {
        mRowsAdapter.notifyArrayItemRangeChanged(0, 1);
        switchPlayPauseControls(PlaybackControlsRow.PlayPauseAction.PAUSE);
    }

    @Override
    public void ambienceTrackIsPaused() {
        mRowsAdapter.notifyArrayItemRangeChanged(0, 1);
        switchPlayPauseControls(PlaybackControlsRow.PlayPauseAction.PLAY);
    }

    @Override
    public void ambienceTrackHasStopped() {
        mRowsAdapter.notifyArrayItemRangeChanged(0, 1);
        switchPlayPauseControls(PlaybackControlsRow.PlayPauseAction.PLAY);
    }

    @Override
    public void ambiencePlaylistCompleted() {
        //not implemented
    }

    @Override
    public void ambienceErrorOccurred() {
        //not implemented
    }

    @Override
    public void ambienceServiceStarted(Ambience activeInstance) {
        //not implemented
    }

    @Override
    public void ambienceServiceStopped(Ambience activeInstance) {
        //not implemented
    }
}
