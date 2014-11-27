package com.tonyostudios.ambiencesamples;

import android.net.Uri;

import com.tonyostudios.ambience.AmbientTrack;

/**
 * Created by tonyostudios on 11/26/14.
 */
public class SampleData {





    public static AmbientTrack[] getmData()
    {
        //Create tracks and set meta data values
        AmbientTrack track1 = AmbientTrack.newInstance();
        AmbientTrack track2 = AmbientTrack.newInstance();

        track1.setName("track1")
                .setId(101)
                .setArtistName("Tonyo Studios")
                .setAlbumName("Complete Ambience")
                .setAudioUri(Uri.parse("android.resource://"+MainActivity.packageName + "/" + R.raw.track1))
                .setAlbumImageUri(Uri.parse("android.resource://"+MainActivity.packageName + "/" + R.drawable.track1));



        track2.setName("track2")
                .setId(102)
                .setArtistName("Tonyo Studios")
                .setAlbumName("Complete Ambience")
                .setAudioUri(Uri.parse("android.resource://"+MainActivity.packageName + "/" + R.raw.track2))
                .setAlbumImageUri(Uri.parse("android.resource://"+MainActivity.packageName + "/" + R.drawable.track2));



        return new AmbientTrack[]{track1,track2};
    }




}
