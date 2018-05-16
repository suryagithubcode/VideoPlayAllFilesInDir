package com.samplenewvideoplayer;

import android.Manifest;
import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {

    private static final int REQUEST_CODE_PERMISSION = 200;
    private MediaPlayer player;
    private SurfaceView surface;
    private SurfaceHolder holder;
    private SongsManager songManager = null;
    private String playUrl = "";
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    private int i = 0;
    public File logf = null;
    int pos = 0;
    MarqueeLayout marqueeLayout;

    public TextView textView = null;

    //ViewFlipper viewFlipper = null;

    String storagePathPenDrive;

    int gallery_grid_Images[]={R.drawable.cakeone, R.drawable.caketwo, R.drawable.giveup
            , R.drawable.iceone, R.drawable.icetwo, R.drawable.icethree, R.drawable.icefour,R.drawable.flowerone,};

    @SuppressWarnings("deprecation")
    public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Marshmallow+
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA


            }, REQUEST_CODE_PERMISSION);

        } else {
            // Pre-Marshmallow
        }


        /*UsbMassStorageDevice[] devices = UsbMassStorageDevice.getMassStorageDevices(this *//* Context or Activity *//*);

        for(UsbMassStorageDevice device: devices) {

            // before interacting with a device you need to call init()!
            try {
                device.init();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Only uses the first partition on the device
            FileSystem currentFs = device.getPartitions().get(0).getFileSystem();
            Log.d("mmm", "Capacity: " + currentFs.getCapacity());
            Log.d("mmm", "Occupied Space: " + currentFs.getOccupiedSpace());
            Log.d("mmm", "Free Space: " + currentFs.getFreeSpace());
            Log.d("mmm", "Chunk size: " + currentFs.getChunkSize());

            String dir = device.getUsbDevice().getDeviceName();
            Log.d("mmm dir", "dir: " + dir);

            String currentString =dir;
            String[] separated = currentString.split("/");

            storagePathPenDrive = "usb://1"+separated[5]+"/UsbStorage/QDSS";
            Log.d("mmm","Display Storage Path"+storagePathPenDrive);

        }*/

        try {
            File logfile = Environment.getExternalStorageDirectory();
            logf = new File(logfile + "/SampleVideoPlayer.txt");
            if (!logf.exists())
                logf.createNewFile();
        } catch (Exception e) {
            writeFile("Log file Creation Error : " + e.toString());
        }
        surface = (SurfaceView) findViewById(R.id.surface);
        songManager = new SongsManager();
        // Getting all songs list
        songsList = songManager.getPlayList(getApplicationContext());
        holder = surface.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        File extDir = Environment.getExternalStorageDirectory();
        System.out.println("Long 1" + extDir);
        // File clip = new File(extDir+"/Movies/Coca-Cola.mp4");
        System.out.println("DSS index = " + i);
        // new BackgroundAsyncTask().execute(songsList.get(0).get("songPath"));
        //  marqueeLayout = new MarqueeLayout(this);
        LinearLayout ll = (LinearLayout) findViewById(R.id.rp);
        ll.setBackgroundColor(Color.TRANSPARENT);
        textView = new TextView(getApplicationContext());

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(param);

       /* RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)textView.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);*/
        //  params.addRule(RelativeLayout.LEFT_OF, R.id.id_to_be_left_of);
        System.out.println("aaaa=");
        textView.setFreezesText(false);
        textView.setMaxLines(1);
        textView.setText("1. When the going gets tough, the tough get going... People who live in glass houses should not throw stones.");
        textView.setTextSize(50);
        textView.setTextColor(Color.WHITE);
        textView.setSingleLine();
        textView.setHorizontallyScrolling(true);
        textView.setSelected(true);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setMarqueeRepeatLimit(Animation.INFINITE);

        //ani.addView(textView);
        //marqueeLayout.startAnimation();
       // ll.addView(textView);
        //addContentView(textView,param);
        String ramSize = getTotalRAM();
        System.out.println("Get Total Ram : "+ramSize);
        //	viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
        for(int i=0;i<gallery_grid_Images.length;i++)
        {
            //  This will create dynamic image view and add them to ViewFlipper
            //  setFlipperImage(gallery_grid_Images[i]);
        }
	/*	viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.right_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.right_out));
	    viewFlipper.setAutoStart(true);
	    viewFlipper.setFlipInterval(5000);
	    viewFlipper.startFlipping();*/


        // textView.setSelected(true);
        playVideo(songsList.get(0).get("songPath"));
    }

    /***
     *
     * @param res
     */

    private void setFlipperImage(int res) {
        Log.i("Set Filpper Called", res+"");
        ImageView image = new ImageView(getApplicationContext());
        image.setBackgroundResource(res);
        //  viewFlipper.addView(image);
    }

    public void onCompletion(MediaPlayer arg0) {
        File dir = getCacheDir();
        // textView.setSelected(false);

        for (int j = 0; j < songsList.size(); j++) {
            System.out.println("FOR DSS index = " + songsList.size()
                    + " current " + i);
            if (i < (songsList.size())) {
                System.out.println("FOR IF DSS index = " + i);
                //if(i!=0)
                i += 1;
                System.out.println("FOR DSS index completion= " + i
                        + " Song Path " + songsList.get(i).get("songPath"));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // new
                        // BackgroundAsyncTask().execute(songsList.get(i).get("songPath"));
                        playVideo(songsList.get(i).get("songPath"));

                        if (i == (songsList.size() - 1)) {
                            i = -1;
                        }
                    }
                }).start();
                break;
            }
        }
    }

    public String getTotalRAM() {

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }
        return lastValue;
    }

    public void logcatDisplay() {
        try {
            File logcatFile = null;
            File logfile = Environment.getExternalStorageDirectory();
            logcatFile = new File(logfile + "/SVPLogcat.txt");
            if (!logcatFile.exists())
                logcatFile.createNewFile();
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder logcat = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                logcat.append(line);
            }
            FileWriter fileWritter = new FileWriter(logcatFile, true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(logcat.toString());
            bufferWritter.close();
        }
        catch (IOException e) {
        }
    }

    public void onPrepared(MediaPlayer mediaplayer) {
        try {
            holder.setFixedSize(player.getVideoWidth(), player.getVideoHeight());
            player.start();
            // textView.setSelected(true);

            String currentDateTimeString = DateFormat.getDateTimeInstance()
                    .format(new Date());
            writeFile("\n player Start play :: " + " TIME "
                    + currentDateTimeString);
            System.out.println("PALYLIST start play :: " + "TIME "
                    + currentDateTimeString);
        } catch (Exception e) {
            writeFile("Set Holder and Start the player in android :"
                    + e.toString());
        }
    }

    private void playVideo(String url) {
        playUrl = url;
        try {
            if (player == null) {
                player = new MediaPlayer();
                player.setScreenOnWhilePlaying(true);
                player.setWakeMode(getApplicationContext(),
                        PowerManager.PARTIAL_WAKE_LOCK);
            } else {
                String currentDateTimeString = DateFormat.getDateTimeInstance()
                        .format(new Date());
                writeFile("\n Player End of Play :: " + currentDateTimeString);
                writeFile("\n --------------------------------------------------------------------");
                player.stop();
                player.reset();
            }
            player.setDataSource(url);
            player.setOnPreparedListener(MainActivity.this);
            player.prepareAsync();
            String currentDateTimeString = DateFormat.getDateTimeInstance()
                    .format(new Date());
            writeFile("\n Prepare player  :: " + url + " TIME "
                    + currentDateTimeString);

            System.out.println("Prepare Player For :: " + url + "   TIME ="
                    + currentDateTimeString);
            player.setOnCompletionListener(MainActivity.this);
            // textView.setSelected(true);
        } catch (Throwable t) {
            Log.e("ERROR", "Exception Error", t);
            writeFile("\n Set The Player Exception Error :" + t.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
        player = null;
        logcatDisplay();
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        try {
            player.setDisplay(holder);
        } catch (Exception e) {
            writeFile("\n Surface creatation error : " + e.toString());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }

    public void writeFile(String log) {
        try {
            FileWriter fileWritter = new FileWriter(logf, true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(log);
            bufferWritter.close();
        } catch (Exception e) {
        }
    }
}

/**
 * Class to filter files which are having .mp3 extension
 **/

class FileExtensionFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".mp4") || name.endsWith(".MP4"));
    }
}
