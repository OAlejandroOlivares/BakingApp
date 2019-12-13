package com.oolivares.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class DetailFragment extends Fragment implements ExoPlayer.EventListener {
    private static final String MEDIA_URL = "mediaUrl";
    private static final String PLAYER_PLAY_WHEN_READY = "playwhenready";
    private static final String PLAYER_CURRENT_WINDOW = "window";
    private static final String DESCRIPTION_VALUE = "description";
    private static final String SAVED_STEP_INDEX = "stepindex";
    private static final String SAVED_STEPS = "savedsteps";
    private String PLAYER_POSITION= "Position";
    private long position = 0;
    private JSONArray steps;
    private int stepindex = -1;
    private boolean flag = true;
    private String medis;
    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;

    @Nullable
    @BindView(R.id.description)
    TextView description;

    @Nullable
    @BindView(R.id.next)
    ImageButton next;

    @Nullable
    @BindView(R.id.previous)
    ImageButton previous;

    private SimpleExoPlayer mExoPlayer;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private String descriptionstring;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.step_detail, container, false);
        ButterKnife.bind(this,rootView);
        if (stepindex != -1 && steps != null) {
            try {
                loaddata(steps.getJSONObject(stepindex));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            position = savedInstanceState.getLong(PLAYER_POSITION, 0);
            medis = savedInstanceState.getString(MEDIA_URL);
            playWhenReady = savedInstanceState.getBoolean(PLAYER_PLAY_WHEN_READY);
            currentWindow = savedInstanceState.getInt(PLAYER_CURRENT_WINDOW, 0);
            descriptionstring = savedInstanceState.getString(DESCRIPTION_VALUE,"");
            stepindex = savedInstanceState.getInt(SAVED_STEP_INDEX);
            String stepsstring = savedInstanceState.getString(SAVED_STEPS, "");
            try {
                steps = new JSONArray(stepsstring);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Optional
    @OnClick(R.id.previous)
    public void submit2() {
        if (stepindex > 0) {
            stepindex -= 1;
            try {
                loaddata(steps.getJSONObject(stepindex));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Optional
    @OnClick(R.id.next)
    public void submit(){
        if (stepindex < steps.length() - 1) {
            stepindex += 1;
            try {
                loaddata(steps.getJSONObject(stepindex));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    public void init(Recetas receta) {
        if (next != null && previous != null) {
            next.setVisibility(View.GONE);
            previous.setVisibility(View.GONE);
        }

        try {
            steps = new JSONArray(receta.getSteps());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            loaddata((JSONObject) steps.get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loaddata(JSONObject object) {
        try {
            descriptionstring = object.getString("description");
            if (description != null) {
                description.setText(descriptionstring);
            }
            medis = object.getString("videoURL");
            initializePlayer(Uri.parse(medis));
            if (stepindex != -1 && next != null && previous != null) {
                if (stepindex == steps.length() - 1) {
                    next.setVisibility(View.GONE);
                } else {
                    next.setVisibility(View.VISIBLE);
                }
                if (stepindex == 0) {
                    previous.setVisibility(View.GONE);
                } else {
                    previous.setVisibility(View.VISIBLE);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void initializePlayer(Uri mediaUri) {
        if (mediaUri.getLastPathSegment() != null) {
            if (mExoPlayer == null) {
                // Create an instance of the ExoPlayer.
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
                mPlayerView.setPlayer(mExoPlayer);

                // Set the ExoPlayer.EventListener to this activity.
                mExoPlayer.addListener(this);
                // Prepare the MediaSource.
                String userAgent = Util.getUserAgent(getContext(), "BakingApp");
                MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                        getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(playWhenReady);
                mExoPlayer.seekTo(currentWindow,position);
            } else {
                String userAgent = Util.getUserAgent(getContext(), "BakingApp");
                MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                        getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(playWhenReady);
                mExoPlayer.seekTo(currentWindow,position);
            }
        }else{
            Toast.makeText(getContext(),"No video Available",Toast.LENGTH_LONG).show();
        }
        if (description != null) {
            description.setText(descriptionstring);
        }
        if (stepindex != -1 && next != null && previous != null) {
            if (stepindex == steps.length() - 1) {
                next.setVisibility(View.GONE);
            } else {
                next.setVisibility(View.VISIBLE);
            }
            if (stepindex == 0) {
                previous.setVisibility(View.GONE);
            } else {
                previous.setVisibility(View.VISIBLE);
            }
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            position = mExoPlayer.getCurrentPosition();
            currentWindow = mExoPlayer.getCurrentWindowIndex();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer(Uri.parse(medis));
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer(Uri.parse(medis));
    }

    public void load(int index) {
        try {
            loaddata(steps.getJSONObject(index));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(PLAYER_POSITION,position);
        outState.putString(MEDIA_URL,medis);
        outState.putBoolean(PLAYER_PLAY_WHEN_READY,playWhenReady);
        outState.putInt(PLAYER_CURRENT_WINDOW,currentWindow);
        outState.putString(DESCRIPTION_VALUE,descriptionstring);
        outState.putInt(SAVED_STEP_INDEX,stepindex);
        outState.putString(SAVED_STEPS,steps.toString());

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }


    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }


    public void setSteps(JSONArray steps, int index) {
        this.steps = steps;
        stepindex = index;
    }

    public int getindex() {
        return stepindex;
    }
}
