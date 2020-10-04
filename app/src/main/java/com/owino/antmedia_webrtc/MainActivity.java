package com.owino.antmedia_webrtc;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

import java.util.Map;
import java.util.Queue;
import java.util.Timer;

import de.tavendo.autobahn.WebSocket;
import io.antmedia.webrtcandroidframework.IWebRTCClient;
import io.antmedia.webrtcandroidframework.IWebRTCListener;
import io.antmedia.webrtcandroidframework.WebRTCClient;
import io.antmedia.webrtcandroidframework.apprtc.CallActivity;
import io.antmedia.webrtcandroidframework.apprtc.CallFragment;

import static io.antmedia.webrtcandroidframework.apprtc.CallActivity.EXTRA_CAPTURETOTEXTURE_ENABLED;
import static io.antmedia.webrtcandroidframework.apprtc.CallActivity.EXTRA_VIDEO_BITRATE;
import static io.antmedia.webrtcandroidframework.apprtc.CallActivity.EXTRA_VIDEO_FPS;

public class MainActivity extends Activity implements IWebRTCListener {

    private static final String TAG = "MainActivity";

    /**
     * Change this address with your Ant Media Server address
     */
    public static final String SERVER_ADDRESS = "<server-domain-or-ip>:5080";
    public static final int REQUEST_CODE = 1201;

    /**
     * Mode can Publish, Play or P2P
     */
    private String webRTCMode = IWebRTCClient.MODE_PLAY;


    public static final String SERVER_URL = "ws://" + SERVER_ADDRESS + "/WebRTCAppEE/websocket";
    public static final String REST_URL = "http://" + SERVER_ADDRESS + "/WebRTCAppEE/rest/v2";
    private CallFragment callFragment;

    private WebRTCClient webRTCClient;
    private SurfaceViewRenderer cameraViewRenderer;
    private SurfaceViewRenderer pipViewRenderer;

    private Button startStreamingButton;
    private String operationName = "";
    private Timer timer;
    private String streamId;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set window styles for fullscreen-window size. Needs to be done before
        // adding content.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        //getWindow().getDecorView().setSystemUiVisibility(getSystemUiVisibility());

        setContentView(R.layout.activity_main);
        cameraViewRenderer = findViewById(R.id.camera_view_renderer);
        pipViewRenderer = findViewById(R.id.pip_view_renderer);
        startStreamingButton = findViewById(R.id.start_streaming_button);


    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermissions();
    }

    private void handleInitializeWebrtc() {
        // Check for mandatory permissions.
        for (String permission : CallActivity.MANDATORY_PERMISSIONS) {
            if (this.checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission " + permission + " is not granted", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (webRTCMode.equals(IWebRTCClient.MODE_PUBLISH)) {
            startStreamingButton.setText("Start Publishing");
            operationName = "Publishing";
        } else if (webRTCMode.equals(IWebRTCClient.MODE_PLAY)) {
            startStreamingButton.setText("Start Playing");
            operationName = "Playing";
        } else if (webRTCMode.equals(IWebRTCClient.MODE_JOIN)) {
            startStreamingButton.setText("Start P2P");
            operationName = "P2P";
        }

        this.getIntent().putExtra(EXTRA_CAPTURETOTEXTURE_ENABLED, true);
        this.getIntent().putExtra(EXTRA_VIDEO_FPS, 30);
        this.getIntent().putExtra(EXTRA_VIDEO_BITRATE, 2500);
        this.getIntent().putExtra(EXTRA_CAPTURETOTEXTURE_ENABLED, true);

        webRTCClient = new WebRTCClient(this, this);

        //webRTCClient.setOpenFrontCamera(false);

        streamId = "stream1";
        String tokenId = "tokenId";

        webRTCClient.setVideoRenderers(pipViewRenderer, cameraViewRenderer);

        this.getIntent().putExtra(CallActivity.EXTRA_VIDEO_FPS, 24);
        webRTCClient.init(SERVER_URL, streamId, webRTCMode, tokenId, this.getIntent());
    }

    public void requestPermissions() {
        Log.e(TAG, "requestPermissions: ");
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            handleInitializeWebrtc();
        } else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, REQUEST_CODE);
    }


    public void startStreaming(View v) {

        if (!webRTCClient.isStreaming()) {
            ((Button) v).setText("Stop " + operationName);
            webRTCClient.startStream();
        } else {
            ((Button) v).setText("Start " + operationName);
            webRTCClient.stopStream();
        }


    }


    @Override
    public void onPlayStarted() {
        Log.w(getClass().getSimpleName(), "onPlayStarted");
        Toast.makeText(this, "Play started", Toast.LENGTH_LONG).show();
        webRTCClient.switchVideoScaling(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
    }

    @Override
    public void onPublishStarted() {
        Log.w(getClass().getSimpleName(), "onPublishStarted");
        Toast.makeText(this, "Publish started", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPublishFinished() {
        Log.w(getClass().getSimpleName(), "onPublishFinished");
        Toast.makeText(this, "Publish finished", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlayFinished() {
        Log.w(getClass().getSimpleName(), "onPlayFinished");
        Toast.makeText(this, "Play finished", Toast.LENGTH_LONG).show();
    }

    @Override
    public void noStreamExistsToPlay() {
        Log.w(getClass().getSimpleName(), "noStreamExistsToPlay");
        Toast.makeText(this, "No stream exist to play", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onError(String description) {
        Toast.makeText(this, "Error: " + description, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        webRTCClient.stopStream();
    }

    @Override
    public void onSignalChannelClosed(WebSocket.WebSocketConnectionObserver.WebSocketCloseNotification code) {
        Toast.makeText(this, "Signal channel closed with code " + code, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDisconnected() {

        Log.w(getClass().getSimpleName(), "disconnected");
        Toast.makeText(this, "Disconnected", Toast.LENGTH_LONG).show();

        startStreamingButton.setText("Start " + operationName);
        //finish();
    }

    @Override
    public void onIceConnected() {
        //it is called when connected to ice
    }

    @Override
    public void onIceDisconnected() {
        //it's called when ice is disconnected
    }

    public void onOffVideo(View view) {
        if (webRTCClient.isVideoOn()) {
            webRTCClient.disableVideo();
        } else {
            webRTCClient.enableVideo();
        }
    }

    public void onOffAudio(View view) {
        if (webRTCClient.isAudioOn()) {
            webRTCClient.disableAudio();
        } else {
            webRTCClient.enableAudio();
        }
    }

    @Override
    public void onTrackList(String[] tracks) {

    }

    @Override
    public void onBitrateMeasurement(String streamId, int targetBitrate, int videoBitrate, int audioBitrate) {
        Log.e(getClass().getSimpleName(), "st:" + streamId + " tb:" + targetBitrate + " vb:" + videoBitrate + " ab:" + audioBitrate);
        if (targetBitrate < (videoBitrate + audioBitrate)) {
            Toast.makeText(this, "low bandwidth", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is used in an experiment. It's not for production
     *
     * @param streamId
     */
    public void calculateAbsoluteLatency(String streamId) {
        String url = REST_URL + "/broadcasts/" + streamId + "/rtmp-to-webrtc-stats";

        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("MainActivity", "recevied response " + response);
                            JSONObject jsonObject = new JSONObject(response);
                            long absoluteStartTimeMs = jsonObject.getLong("absoluteTimeMs");
                            //this is the frame id in sending the rtp packet. Actually it's rtp timestamp
                            long frameId = jsonObject.getLong("frameId");
                            long relativeCaptureTimeMs = jsonObject.getLong("captureTimeMs");
                            long captureTimeMs = frameId / 90;
                            Map<Long, Long> captureTimeMsList = WebRTCClient.getCaptureTimeMsMapList();

                            long absoluteDecodeTimeMs = 0;
                            if (captureTimeMsList.containsKey(captureTimeMs)) {
                                absoluteDecodeTimeMs = captureTimeMsList.get(captureTimeMs);
                            }

                            long absoluteLatency = absoluteDecodeTimeMs - relativeCaptureTimeMs - absoluteStartTimeMs;
                            Log.i("MainActivity", "recevied absolute start time: " + absoluteStartTimeMs
                                    + " frameId: " + frameId + " relativeLatencyMs : " + relativeCaptureTimeMs
                                    + " absoluteDecodeTimeMs: " + absoluteDecodeTimeMs
                                    + " absoluteLatency: " + absoluteLatency);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MainActivity", "That didn't work!");

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        handleInitializeWebrtc();
    }
}