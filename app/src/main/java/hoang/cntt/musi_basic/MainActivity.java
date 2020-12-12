package hoang.cntt.musi_basic;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView imagePlayPause;
    private TextView textCurrentTime, textTotalDuration;
    private SeekBar playerSeekBar;
    private MediaPlayer mediaPlayer;
    private Handler handler= new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagePlayPause= findViewById(R.id.imagePlayPause);
        textCurrentTime= findViewById(R.id.textCurentTime);
        textTotalDuration= findViewById(R.id.textTotalDuration);

        playerSeekBar= findViewById(R.id.playSeekbar);
        mediaPlayer= new MediaPlayer();

        playerSeekBar.setMax(100);

        imagePlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    handler.removeCallbacks(updater);
                    mediaPlayer.pause();
                    imagePlayPause.setImageResource(R.drawable.ic_play);
                }else{
                    mediaPlayer.start();
                    imagePlayPause.setImageResource(R.drawable.ic_pause);
                    updateSeekbar();
                }
            }
        });
        prepareMediaPlayer();

        playerSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SeekBar seekBar= (SeekBar) v;
                int playPosition= (mediaPlayer.getDuration()/100)*seekBar.getProgress();
                mediaPlayer.seekTo(playPosition);
                textCurrentTime.setText(milliSencondsToTimer(mediaPlayer.getCurrentPosition()));
                return false;
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                playerSeekBar.setSecondaryProgress(percent);
            }
        });
    }

    private void prepareMediaPlayer(){
        try {
            mediaPlayer=MediaPlayer.create(this,R.raw.loinho);
            textTotalDuration.setText(milliSencondsToTimer(mediaPlayer.getDuration()));
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Runnable updater=new Runnable() {
        @Override
        public void run() {
            updateSeekbar();
            long currentDuration = mediaPlayer.getCurrentPosition();
            textCurrentTime.setText(milliSencondsToTimer(currentDuration));
        }
    };

    private void updateSeekbar(){
        if(mediaPlayer.isPlaying()){
            playerSeekBar.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration())*100));
            handler.postDelayed(updater,1000);
        }
    }

    private String milliSencondsToTimer(long milliSeconds){
        String timerString = "";
        String secondsString ;

        int hours= (int) (milliSeconds/(1000*60*60));
        int munites =(int)(milliSeconds % (1000*60*60) ) / (1000 * 60 );
        int seconds= (int)((milliSeconds%(1000*60*60))%(1000*60)/1000);

        if(hours>0){
            timerString=hours+":";
        }
        if(seconds<10){
            secondsString="0"+seconds;
        }else{
            secondsString=""+seconds;
        }

        timerString= timerString + munites + ":" + secondsString;
        return timerString;
    }
}













