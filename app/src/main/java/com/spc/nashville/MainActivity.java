package com.spc.nashville;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "Nashville";
    ImageButton imageButton;
    TextView textView1;
    TextView textView2;
    float fontsize;
    boolean timerRunning = false;
    Random random = new Random();
    MediaPlayer mediaPlayer;
    CountDownTimer countDownTimer;

    String[] org_name = new String[10];
    String[] org_title = new String[10];
    String[] org_piccie = new String[10];

    int randomColour;
    int red;
    int green;
    int blue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Select the instruction text to set it scrolling...
        textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setSelected(true);

        // get the other UI elements
        imageButton = (ImageButton) findViewById(R.id.imageButton1);
        textView2 = (TextView) findViewById(R.id.textView2);


        // enable the ImageButton listener - this will stop/start/etc depending on state
        Log.v(TAG, "OnCreate - addListenerOnButton");
        addListenerOnButton();

        //Load the organisation arrays
        loadOrgArrays();

        // Set the countdown timer off and running...
        Log.v(TAG, "OnCreate - startCountDownTimer");
        startCountdownTimer();

        // Get ready to play the song...
        Log.v(TAG, "OnCreate - creating the link to Dolly MP3");
        mediaPlayer = MediaPlayer.create(this, R.raw.dolly);

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

        switch (item.getItemId()) {

            case R.id.showorg:
                startCountdownTimer();
                return true;

            case R.id.leave:
                Toast.makeText(getApplicationContext(), "See ya cowboy!",
                        Toast.LENGTH_LONG).show();

                // tidy things up & close the app
                //mediaPlayer.release();
                //countDownTimer.cancel();
                finish();
                // comment
                return true;

            case R.id.stay:
                Toast.makeText(getApplicationContext(), "Good choice!",
                        Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void startCountdownTimer() {

        // check not already running...
        if (timerRunning == false) {

            // reset the font size starting point
            fontsize = 40;

            // Define a 10sec countdown with a tick every second
            countDownTimer = new CountDownTimer(10000, 1000) {

                // define what happens each tick
                public void onTick(long millisUntilFinished) {

                    // Log.v(TAG, "In onTick, fontsize is " + fontsize);
                    // shrink the font size a bit each time
                    fontsize = fontsize - 2;
                    textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                            fontsize);

                    // Get some random RGB numbers
                    randomColour = getRandomColour();
                    textView2.setTextColor(randomColour);

                    // display how long left
                    textView2.setText("> " + millisUntilFinished / 1000 + " <");

                    // Get some more random RGB numbers for main text
                    randomColour = getRandomColour();
                    textView1.setTextColor(randomColour);

                    // show alert passing this element of org array
                    showCustomAlert((int) millisUntilFinished / 1000);

                }

                // define what happens when finished
                public void onFinish() {

                    // Get some random RGB numbers
                    randomColour = getRandomColour();
                    textView2.setTextColor(randomColour);

                    textView2.setText("...");
                    timerRunning = false;
                    // start Dolly if not already going...
                    if (mediaPlayer != null && mediaPlayer.isPlaying() == false) {
                        mediaPlayer.start();
                        textView2.setText("...here we go...");
                    }

                    // show the last alert for Maria!
                    showCustomAlert(0);
                }

                // start the thing going
            }.start();

            timerRunning = true;
        }

    }


    public int getRandomColour() {
        red = Math.abs(random.nextInt() % 256);
        green = Math.abs(random.nextInt() % 256);
        blue = Math.abs(random.nextInt() % 256);
        return Color.rgb(red, green, blue);
    }


    public void addListenerOnButton() {

        imageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Take action based on state of music & timer
                if (timerRunning == false) {
                    if (mediaPlayer != null && mediaPlayer.isPlaying() == false) {
                        // timer not running and music not playing
                        Toast.makeText(getApplicationContext(),
                                "Okay, show's over, time to go...bye! ", Toast.LENGTH_SHORT)
                                .show();
                        // tidy things up & close the app
                        if (mediaPlayer != null && mediaPlayer.isPlaying())
                            mediaPlayer.stop();
                        countDownTimer.cancel();
                        timerRunning = false;
                        finish();


                    } else {
                        // timer not running but music IS playing
                        Toast.makeText(getApplicationContext(),
                                "Had enough of Dolly, huh?", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    if (mediaPlayer.isPlaying() == false) {
                        // timer IS running but music not playing
                        Toast.makeText(getApplicationContext(),
                                "Hold tight... more coming!", Toast.LENGTH_SHORT)
                                .show();

                    } else {
                        // timer IS running and music IS playing
                        Toast.makeText(getApplicationContext(),
                                "Stay a bit more...?", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                ;

            }

            ;

        });

    }

    @Override
    protected void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();
        // The activity is about to become visible.
    }

    @Override
    protected void onRestart() {
        Log.v(TAG, "onRestart");
        super.onRestart();
        // The activity is about to become visible.
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume - timerRunning is " + timerRunning);
        if (timerRunning == false) startCountdownTimer();
        super.onResume();
        // The activity has become visible (it is now "resumed").
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "onPause - pausing MP and cancelling Timer");
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.pause();
        if (countDownTimer != null && timerRunning == true) {
            countDownTimer.cancel();
            timerRunning = false;
        }
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
    }

    @Override
    protected void onStop() {
        Log.v(TAG, "onStop- stopping MP and cancelling Timer");
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.stop();
        if (countDownTimer != null && timerRunning == true) {
            countDownTimer.cancel();
            timerRunning = false;
        }
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy - releasing MP");
        if (mediaPlayer != null) mediaPlayer.release();
        super.onDestroy();
        // The activity is about to be destroyed.
    }

    public void showCustomAlert(int index) {
        Log.v(TAG, "in showCustomAlert with index " + index);

        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getLayoutInflater();

        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout_root));

        // Set the image (org_piccie)
        ImageView image = (ImageView) toastRoot.findViewById(R.id.toast_imageview);
        int drawableId = getResources().getIdentifier(org_piccie[index], "drawable", getPackageName());
        image.setImageResource(drawableId);


        // Set the first text (org_name)
        TextView text1 = (TextView) toastRoot.findViewById(R.id.toast_textview1);
        text1.setText(org_name[index]);

        // Set the first text (org_title)
        TextView text2 = (TextView) toastRoot.findViewById(R.id.toast_textview2);
        text2.setText(org_title[index]);

        Toast toast = new Toast(getApplicationContext());

        // Set layout to toast
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                0, 250);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();

    }

    public void loadOrgArrays() {

        org_name[9] = "Alla Turetsky";
        org_title[9] = "WMA Operations IT";
        org_piccie[9] = "alla";

        org_name[8] = "Ian Bond";
        org_title[8] = "IB Securities Operations IT";
        org_piccie[8] = "ian";

        org_name[7] = "Tim Ryan";
        org_title[7] = "IB Securities Operations IT";
        org_piccie[7] = "tim";

        org_name[6] = "Tom Carr";
        org_title[6] = "IB FX Operations & RM IT";
        org_piccie[6] = "tom";

        org_name[5] = "Shaun Cotter";
        org_title[5] = "IB Derivatives & Middle Office IT";
        org_piccie[5] = "shaun";

        org_name[4] = "Sean Livingston";
        org_title[4] = "APAC IT";
        org_piccie[4] = "sean";

        org_name[3] = "Daniele Carulli";
        org_title[3] = "Global Reconciliations Utility IT";
        org_piccie[3] = "daniele";

        org_name[2] = "Mark Daplyn";
        org_title[2] = "Cross Operations IT ";
        org_piccie[2] = "mark";

        org_name[1] = "Vikram Dewan";
        org_title[1] = "Group Operations IT Delivery Management";
        org_piccie[1] = "vikram";

        org_name[0] = "Maria Kitmiridis";
        org_title[0] = "Strategic Interaction Evolution Lead";
        org_piccie[0] = "maria";
    }
}

