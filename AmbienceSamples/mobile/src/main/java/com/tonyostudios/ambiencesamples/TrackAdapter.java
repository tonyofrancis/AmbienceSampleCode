package com.tonyostudios.ambiencesamples;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tonyostudios.ambience.Ambience;
import com.tonyostudios.ambience.AmbientTrack;

/**
 * Created by tonyostudios on 11/26/14.
 */
public class TrackAdapter extends ArrayAdapter<AmbientTrack> {

    private Context mContext;
    private AmbientTrack[] mData;

    public TrackAdapter(Context context, int resource, AmbientTrack[] objects) {
        super(context, resource, objects);

        mContext = context;
        mData = objects;
    }

    @Override
    public int getCount() {
        return mData.length;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rootView = convertView;

        if(convertView == null)
        {
            rootView = LayoutInflater.from(mContext).inflate(R.layout.track,parent,false);

            rootView.setTag(new ViewHolder(rootView));
        }

        ViewHolder holder = (ViewHolder) rootView.getTag();



        //Get track data from ambient track
        holder.setTrackName(mData[position].getName());


        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Create a one track playlist and send to ambient service
                Ambience.activeInstance()
                        .setPlaylistTo(new AmbientTrack[]{mData[position]})
                        .setNotificationLaunchActivity(MainActivity.packageName,MainActivity.ActivityName)
                        .play();

            }
        });


        return rootView;
    }

    public static class ViewHolder
    {
        private TextView trackName;
        private View view;

        public ViewHolder(View view)
        {
            this.view = view;
            trackName = (TextView) view.findViewById(R.id.track_name);
        }

        public TextView getTrackName() {
            return trackName;
        }

        public void setTrackName(String name) {
            trackName.setText(name);
        }

        public View getView() {
            return view;
        }
    }
}
