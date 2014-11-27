package com.tonyostudios.ambiencesamples;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tonyostudios.ambience.Ambience;
import com.tonyostudios.ambience.AmbientTrack;

import java.util.concurrent.TimeUnit;


public class MainActivity extends ActionBarActivity {

    public static final String packageName = "com.tonyostudios.ambiencesamples";
    public static final String ActivityName = "com.tonyostudios.ambiencesamples.MainActivity";

    private static MainActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MusicFragment())
                    .commit();
        }

        mActivity = this;

        // Turn the ambience service on
        Ambience.turnOn(getApplicationContext());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Turn the ambience service off
        Ambience.turnOff();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A music fragment containing a simple view.
     */
    public static class MusicFragment extends Fragment implements Ambience.AmbientListener {

        private SeekBar mSeekBar;
        private TextView mPlaybackTotalTime;
        private TextView mPlaybackCurrentTime;
        private View mPlayButton;
        private View mPauseButton;
        private View mFowardButton;
        private View mPreviousButton;
        private TextView mNowPlayingTrack;
        private TextView mNowPlayingAlbum;


        private TrackAdapter mAdapter;

        public MusicFragment() {

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //create adapter for track list;
            mAdapter = new TrackAdapter(mActivity,0,SampleData.getmData());
        }

        @Override
        public void onResume() {
            super.onResume();

            //start getting updates for the ambient service
            Ambience.activeInstance()
                    .listenForUpdatesWith(this);
        }

        @Override
        public void onPause() {
            super.onPause();

            //Stop getting updates for the ambient service
            Ambience.activeInstance()
                    .stopListeningForUpdates();
        }


        public static String getFormattedTimeString(int position)
        {
            int totalDuration = position;


            // set total time as the audio is being played
            String formattedString = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes((long) totalDuration),
                    TimeUnit.MILLISECONDS.toSeconds((long) totalDuration)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes((long) totalDuration)));


            return formattedString;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            //set adapter to musicList ListView
            ListView list = (ListView) rootView.findViewById(R.id.musicList);
            list.setAdapter(mAdapter);



            setupPlayerView(rootView);

            return rootView;
        }

        private void setupPlayerView(View view) {

            mSeekBar = (SeekBar) view.findViewById(R.id.media_seekbar);
            mPauseButton = view.findViewById(R.id.pause);
            mPlayButton = view.findViewById(R.id.play);
            mPlaybackCurrentTime = (TextView) view.findViewById(R.id.playback_time);
            mPlaybackTotalTime = (TextView) view.findViewById(R.id.duration_time);
            mFowardButton = view.findViewById(R.id.forward);
            mPreviousButton = view.findViewById(R.id.rewind);
            mNowPlayingTrack = (TextView) view.findViewById(R.id.display_track_name);
            mNowPlayingAlbum = (TextView) view.findViewById(R.id.display_track_album);

            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    Ambience.activeInstance().seekTo(seekBar.getProgress());
                }
            });

            mPauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Ambience.activeInstance().pause();
                }
            });


            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Ambience.activeInstance().previous();
                }
            });

            mFowardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Ambience.activeInstance().skip();
                }
            });


            disablePlayerButton();


            TextView repeatView = (TextView) view.findViewById(R.id.repeatOne);

            repeatView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Ambience.activeInstance().repeatASingleTrack();


                    Toast.makeText(mActivity, "Repeat One", Toast.LENGTH_SHORT).show();


                }
            });


            TextView repeatViewAll = (TextView) view.findViewById(R.id.repeatAll);

            repeatViewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Ambience.activeInstance().repeatAllTracks();

                    Toast.makeText(mActivity,"Repeat All",Toast.LENGTH_SHORT).show();


                }
            });


            TextView shuffleView = (TextView) view.findViewById(R.id.shuffle);

            shuffleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Ambience.activeInstance().shufflePlaylist();

                    Toast.makeText(mActivity,"Shuffle On",Toast.LENGTH_SHORT).show();

                }
            });


            TextView repeatViewOff = (TextView) view.findViewById(R.id.repeatOff);

            repeatViewOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //send turnRepeatOff request
                    Ambience.activeInstance().turnRepeatOff();

                    Toast.makeText(mActivity,"Repeat Off",Toast.LENGTH_SHORT).show();

                }
            });


            TextView shuffleOffView = (TextView) view.findViewById(R.id.shuffleOff);

            shuffleOffView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //send unshuffle request
                    Ambience.activeInstance().unShufflePlaylist();


                    Toast.makeText(mActivity,"Shuffle Off",Toast.LENGTH_SHORT).show();

                }
            });


            SeekBar volume = (SeekBar) view.findViewById(R.id.volume);

            volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    float value = ((float) progress / (float) 10);


                    //send volume request
                    Ambience.activeInstance().setVolumeTo(value);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        }

        public void setPauseView() {

            if (mPlayButton != null) {
                mPlayButton.setVisibility(View.VISIBLE);
            }

            if (mPauseButton != null) {
                mPauseButton.setVisibility(View.GONE);
            }
        }

        public void setPlayView()
        {
            if (mPlayButton != null) {
                mPlayButton.setVisibility(View.GONE);
            }

            if (mPauseButton != null) {
                mPauseButton.setVisibility(View.VISIBLE);
            }
        }



        public void hideNowPlayingTextViews()
        {
            if(mNowPlayingTrack != null)
            {
                mNowPlayingTrack.setVisibility(View.GONE);
            }

            if(mNowPlayingAlbum != null)
            {
                mNowPlayingAlbum.setVisibility(View.GONE);
            }
        }


        private void disablePlayerButton()
        {
            mSeekBar.setEnabled(false);
            mPreviousButton.setEnabled(false);
            mFowardButton.setEnabled(false);
            mPauseButton.setEnabled(false);
            mPlayButton.setEnabled(false);


        }

        private void enablePlayerButtons()
        {
            mSeekBar.setEnabled(true);
            mPreviousButton.setEnabled(true);
            mFowardButton.setEnabled(true);
            mPauseButton.setEnabled(true);
            mPlayButton.setEnabled(true);
        }

        public void showNowPlayingTextViews()
        {

            if(mNowPlayingTrack != null)
            {
                mNowPlayingTrack.setVisibility(View.VISIBLE);
            }

            if(mNowPlayingAlbum != null)
            {
                mNowPlayingAlbum.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void ambienceIsPreppingTrack() {
            disablePlayerButton();
        }

        @Override
        public void ambienceTrackDuration(int time) {
            if (mPlaybackTotalTime != null) {
                mPlaybackTotalTime.setText(getFormattedTimeString(time));
            }

            if(mSeekBar != null)
            {
                mSeekBar.setMax(time);
            }
        }

        @Override
        public void ambiencePlayingTrack(AmbientTrack track) {
            if(mNowPlayingTrack != null)
            {
                mNowPlayingTrack.setText(track.getName());
            }

            if(mNowPlayingAlbum != null)
            {
                mNowPlayingAlbum.setText(track.getAlbumName());

            }

            showNowPlayingTextViews();
        }

        @Override
        public void ambienceTrackCurrentProgress(int time) {
            if (mPlaybackTotalTime != null) {

                mPlaybackCurrentTime.setText(getFormattedTimeString(time));
            }

            if(mSeekBar != null)
            {
                mSeekBar.setProgress(time);
            }
        }

        @Override
        public void ambienceTrackIsPlaying() {
            enablePlayerButtons();
            setPlayView();
        }

        @Override
        public void ambienceTrackIsPaused() {
            setPauseView();

            mPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //send resume request
                    Ambience.activeInstance()
                            .resume();

                }
            });
        }

        @Override
        public void ambienceTrackHasStopped() {
            mPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //send resume request
                    Ambience.activeInstance()
                            .resume();



                }
            });
        }

        @Override
        public void ambiencePlaylistCompleted() {

            setPauseView();
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
}
