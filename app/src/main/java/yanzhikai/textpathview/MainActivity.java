package yanzhikai.textpathview;

import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import yanzhikai.textpath.painter.ArrowPainter;
import yanzhikai.textpath.painter.FireworksPainter;
import yanzhikai.textpath.SyncTextPathView;
import yanzhikai.textpath.AsyncTextPathView;

public class MainActivity extends AppCompatActivity {
    private SeekBar sb_progress;
    private AsyncTextPathView atpv1,atpv2;
    private SyncTextPathView stpv_2017,stpv_2018,stpv_wish,stpv_chicken,stpv_dog;
    private Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sb_progress = findViewById(R.id.sb_progress);
        atpv1 = findViewById(R.id.atpv_1);
        atpv2 = findViewById(R.id.atpv_2);
        stpv_2017 = findViewById(R.id.stpv_2017);
        stpv_2018 = findViewById(R.id.stpv_2018);
        stpv_wish = findViewById(R.id.stpv_wish);
        stpv_chicken = findViewById(R.id.stpv_chicken);
        stpv_dog = findViewById(R.id.stpv_dog);
        btn_start = findViewById(R.id.btn_start);


        stpv_2017.setTextPainter(new FireworksPainter());
        stpv_2018.setTextPainter(new FireworksPainter());
        stpv_dog.setTextPainter(new FireworksPainter());
        stpv_chicken.setTextPainter(new FireworksPainter());
        stpv_wish.setTextPainter(new ArrowPainter());

        atpv2.setTextPainter(new AsyncTextPathView.AsyncTextPainter() {
            @Override
            public void onDrawPaintPath(float x, float y, Path paintPath) {
                paintPath.addCircle(x,y,6, Path.Direction.CCW);
            }
        });

        sb_progress.setMax(1000);
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                atpv1.drawPath(progress / 1000f);
                atpv2.drawPath(progress / 1000f);
                stpv_2017.drawPath(progress / 1000f);
                stpv_2018.drawPath(progress / 1000f);
                stpv_chicken.drawPath(progress / 1000f);
                stpv_dog.drawPath(progress / 1000f);
                stpv_wish.drawPath(progress / 1000f);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                stpv_2017.setText("sad");
                Log.d("TestView", "onStopTrackingTouch: ");
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atpv1.startAnimation(0,1);
                atpv2.startAnimation(0,1);
                stpv_2017.startAnimation(1,0);
                stpv_2018.startAnimation(0,1);
                stpv_chicken.startAnimation(1,0);
                stpv_dog.startAnimation(0,1);
                stpv_wish.startAnimation(0,1);
            }
        });
    }
}
