package rs.ac.metropolitan.cs330.znamenitosti.admin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import rs.ac.metropolitan.cs330.znamenitosti.admin.config.AsyncActivity;
import rs.ac.metropolitan.cs330.znamenitosti.admin.dto.Sight;

/**
 *
 * @author nikola
 */
public class AddSightImage extends AsyncActivity implements SurfaceHolder.Callback {

    private Camera camera;
    private boolean cameraview = false;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    @Override
    public void onCreate(Bundle icicle) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(icicle);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        setContentView(R.layout.camera);

        surfaceView = (SurfaceView) findViewById(R.id.camera_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        View view = inflater.inflate(R.layout.camera_overlay, null);
        LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addContentView(view, layoutParamsControl);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        camera = getCameraInstance();
        Pair<Integer, Integer> minSize = minSize(16d, 9d);
        minSize = (minSize == null) ? minSize(16d, 10d) : minSize;
        Camera.Parameters cp = camera.getParameters();
        cp.setPictureSize(minSize.first, minSize.second);
        cp.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        camera.setParameters(cp);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (cameraview) {
            camera.stopPreview();
            cameraview = false;
        }
        if (camera != null) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                cameraview = true;
            } catch (IOException e) {
            }
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
        cameraview = false;
    }

    public void onPicture(View view) {
        camera.takePicture(null, null, new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                new SaveSight().execute(data);
            }
        });
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }

    private Pair<Integer, Integer> minSize(double aspectWidth, double aspectHeight) {
        Camera.Parameters cp = camera.getParameters();
        List<Camera.Size> supportedSizes = cp.getSupportedPictureSizes();
        int w = Integer.MAX_VALUE, h = Integer.MAX_VALUE;
        for (Camera.Size size : supportedSizes) {
            if (size.width > 900 && size.width < w && Math.abs((double) size.width / (double) size.height - aspectWidth / aspectHeight) < 0.05) {
                w = size.width;
                h = size.height;
            }
        }
        return (w != Integer.MAX_VALUE && h != Integer.MAX_VALUE) ? new Pair<Integer, Integer>(w, h) : null;
    }

    private class SaveSight extends AsyncTask<byte[], Void, String> {

        private MultiValueMap<String, Object> formData;

        @Override
        protected void onPreExecute() {
            showProgressDialog(getString(R.string.adding));
            formData = new LinkedMultiValueMap<String, Object>();
        }

        @Override
        protected String doInBackground(byte[]... params) {
            try {
                String serverAddress = getString(R.string.server_address);
                String serverPort = getString(R.string.server_port);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
                String result = restTemplate.postForObject(serverAddress + ":" + serverPort + "/sights", Sight.SightForPosting.INSTANCE.sight, String.class);

                formData.add("description", "sight image");
                formData.add("file", new ByteArrayResource(params[0]) {
                    @Override
                    public String getFilename() throws IllegalStateException {
                        Sight s = Sight.SightForPosting.INSTANCE.sight;
                        return s.getCity().getName().replace(" ", "_") + "-" + s.getName().replace(" ", "_") + ".jpg";
                    }
                });
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, requestHeaders);
                RestTemplate restTemplate2 = new RestTemplate(true);
                ResponseEntity<String> response = restTemplate2.exchange(serverAddress + ":" + serverPort + "/images/" + result, HttpMethod.POST, requestEntity, String.class);
                return response.getBody();
            } catch (Exception ex) {
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            Toast.makeText(AddSightImage.this, result, Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(AddSightImage.this, Main.class));
        }
    }
}
