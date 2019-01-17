package com.attra.attralive.adapter;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.attra.attralive.R;
import com.attra.attralive.Service.ApiService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderAdapter extends PagerAdapter {
    Context context;
    ArrayList<String> images;
    LayoutInflater layoutInflater;
    int image;
    VideoView mVideoView;

    ApiService apiService;
    Uri picUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    //FloatingActionButton fabCamera, fabUpload;
    Bitmap mBitmap;
    TextView textView;
    private ProgressDialog bar;

    private MediaController ctlr;
   // private VideoView mVideoView;
    private static final String VIDEO_SAMPLE =
            "https://developers.google.com/training/images/tacoma_narrows.mp4";


    private TextView mBufferingTextView;

    // Current playback position (in milliseconds).
    private int mCurrentPosition = 0;

    // Tag for the instance state bundle.
    private static final String PLAYBACK_TIME = "play_time";
    public SliderAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.slide_row, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        mVideoView= itemView.findViewById(R.id.videocon);
       // callvideo(images[position]);
       /* Picasso.with(context)
                .load(images[position])
                .into(imageView);*/
       // imageView.setImageResource(images[position]);
//        if(images[position].contains("mp3")) {
//            callvideo(images[position]);
//        }
//        else
//        {
            Picasso.with(context)
                    .load(images.get(position))
                    .into(imageView);
       // }
        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
    private void callvideo(String video) {
        MediaController controller = new MediaController(context);
        controller.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(controller);
        initializePlayer(video);
       /* mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializePlayer();
            }
        });*/


    }

    /*@Override
    protected void onStart() {
        super.onStart();

        // Load the media each time onStart() is called.

    }

    @Override
    protected void onPause() {
        super.onPause();

        // In Android versions less than N (7.0, API 24), onPause() is the
        // end of the visual lifecycle of the app.  Pausing the video here
        // prevents the sound from continuing to play even after the app
        // disappears.
        //
        // This is not a problem for more recent versions of Android because
        // onStop() is now the end of the visual lifecycle, and that is where
        // most of the app teardown should take place.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Media playback takes a lot of resources, so everything should be
        // stopped and released at this time.
        releasePlayer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current playback position (in milliseconds) to the
        // instance state bundle.
        outState.putInt(PLAYBACK_TIME, mVideoView.getCurrentPosition());
    }*/

        private void initializePlayer (String video) {
            // Show the "Buffering..." message while the video loads.
            //   mBufferingTextView.setVisibility(VideoView.VISIBLE);

            // Buffer and decode the video sample.
            Uri videoUri = getMedia(video);
            mVideoView.setVideoURI(videoUri);

            // Listener for onPrepared() event (runs after the media is prepared).
            mVideoView.setOnPreparedListener(
                    new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {

                            // Hide buffering message.
                            // mBufferingTextView.setVisibility(VideoView.INVISIBLE);

                            // Restore saved position, if available.
                            if (mCurrentPosition > 0) {
                                mVideoView.seekTo(mCurrentPosition);
                            } else {
                                // Skipping to 1 shows the first frame of the video.
                                mVideoView.seekTo(1);
                            }

                            // Start playing!
                            mVideoView.start();
                        }
                    });

            // Listener for onCompletion() event (runs after media has finished
            // playing).
            mVideoView.setOnCompletionListener(
                    new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {


                            // Return the video position to the start.
                            mVideoView.seekTo(0);
                        }
                    });
        }


        // Release all media-related resources. In a more complicated app this
        // might involve unregistering listeners or releasing audio focus.
        private void releasePlayer() {
            mVideoView.stopPlayback();
        }

        // Get a Uri for the media sample regardless of whether that sample is
        // embedded in the app resources or available on the internet.
        private Uri getMedia (String mediaName){
            if (URLUtil.isValidUrl(mediaName)) {
                // Media name is an external URL.
                return Uri.parse(mediaName);
            } else {
                // Media name is a raw resource embedded in the app.
                return Uri.parse("android.resource://" + context.getPackageName() +
                        "/raw/" + mediaName);
            }
        }

    /*@Override
    public void onClick(View v) {

    }*/
    }
