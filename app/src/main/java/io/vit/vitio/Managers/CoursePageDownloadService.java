package io.vit.vitio.Managers;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.vit.vitio.Managers.Parsers.ParseGeneral;
import io.vit.vitio.R;

/**
 * Created by Prince Bansal Local on 24-01-2016.
 */

public class CoursePageDownloadService extends IntentService {
    public static final int UPDATE_PROGRESS = 8344;
    public static final String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + "/My VIT/";
    private static final String TAG = CoursePageDownloadService.class.getSimpleName();
    private String cookie = "";

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private int lastProgress = 0;

    public CoursePageDownloadService() {
        super("DownloadService");


    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 20) {
            builder = new NotificationCompat.Builder(this)
                    .setContentTitle("My VIT")
                    .setSmallIcon(R.drawable.ic_notiicon)
                    .setContentInfo("Downloading...");
            //.addAction(new NotificationCompat.Action(android.R.drawable.ic_menu_close_clear_cancel, "Cancel", null));
        } else {
            builder = new NotificationCompat.Builder(this)
                    .setContentInfo("Downloading...")
                    .setSmallIcon(R.drawable.ic_notiicon)
                    .setContentTitle("My VIT");
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String link = intent.getStringExtra("link");
        Log.d("downurl", link);
        ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
        try {

            if (login()) {
                for (int j = 0; j < 2; j++) {
                    URL url = new URL(link);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Cookie", cookie.trim());
                    connection.connect();
                    int pos = intent.getIntExtra("pos", -1);
                    String courseCode = intent.getStringExtra("course_code");
                    String courseSlot = intent.getStringExtra("course_slot");
                    String courseFaculty = intent.getStringExtra("course_faculty");
                    builder.setContentText(courseCode + "/" + courseSlot + "/" + courseFaculty + "/fn");
                    int fileLength = connection.getContentLength();
                    String fileName = intent.getStringExtra("name");
                    int notificationId=(courseCode+courseSlot+courseFaculty+fileName).hashCode();
                    Log.i(TAG, "onHandleIntent: type:" + connection.getContentType());
                    String dis = connection.getHeaderField("Content-Disposition");
                    Log.i(TAG, "onHandleIntent: dispo:" + dis);
                    if (dis == null) {
                        continue;
                    }
                    String ext = dis.substring(dis.lastIndexOf("."));

                    if (fileName.lastIndexOf(".") != -1)
                        ext = "";

                    Log.i(TAG, "onHandleIntent: slpiy:" + ext);
                    InputStream input = new BufferedInputStream(connection.getInputStream());
                    File file = new File(Environment.getExternalStorageDirectory().getPath() + "/My VIT/" +
                            courseCode + "/" + courseSlot + "/" + courseFaculty);
                    file.mkdirs();
                    OutputStream output = new FileOutputStream(BASE_PATH +
                            courseCode + "/" + courseSlot + "/" + courseFaculty + "/" + fileName + ext);


                    byte data[] = new byte[2048];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        Bundle resultData = new Bundle();
                        int progress = (int) (total * 100 / fileLength);
                        resultData.putInt("progress", progress);
                        resultData.putInt("pos", pos);
                        resultData.putString("course_code", courseCode);
                        resultData.putString("course_slot", courseSlot);
                        resultData.putString("course_faculty", courseFaculty);
                        if (progress % 5 == 0 && progress != lastProgress) {
                            builder.setProgress(100, progress, false);
                            notificationManager.notify(notificationId, builder.build());
                        }

                        lastProgress = progress;
                        if (j == 1) {
                            receiver.send(UPDATE_PROGRESS, resultData);
                        }
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();


                    Bundle resultData = new Bundle();
                    resultData.putInt("progress", 100);
                    resultData.putInt("pos", pos);
                    resultData.putString("course_code", courseCode);
                    resultData.putString("course_slot", courseSlot);
                    resultData.putString("course_faculty", courseFaculty);
                    //if (Build.VERSION.SDK_INT >= 20)
                    //  builder.addAction(new NotificationCompat.Action(android.R.drawable.stat_sys_download_done, "Open", null));
                    builder.setProgress(0, 0, false)
                            .setContentInfo("Downloaded");

                    notificationManager.notify(notificationId, builder.build());

                    resultData.putString("name", fileName + ext);
                    if (j == 1)
                        receiver.send(UPDATE_PROGRESS, resultData);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean login() throws IOException {
        URL url = new URL("http://myffcs.in:8080/campus/vellore/login");
        disableSSLCertificateChecking();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("regNo", DataHandler.getInstance(getApplicationContext()).getRegNo());
        params.put("psswd", DataHandler.getInstance(getApplicationContext()).getPassword());

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        cookie = conn.getHeaderField("Cookie");
        Log.i(TAG, "login: cookie" + cookie);
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0; )
            sb.append((char) c);
        String response = sb.toString();
        Log.i("DownloadService", "login: " + response);
        if (response != null) {
            ParseGeneral parseGeneral = new ParseGeneral(response);
            if (parseGeneral.validateLogin()) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    private static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
