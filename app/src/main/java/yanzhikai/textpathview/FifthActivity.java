package yanzhikai.textpathview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import yanzhikai.textpath.AsyncPathView;
import yanzhikai.textpath.AsyncTextPathView;
import yanzhikai.textpath.SyncPathView;
import yanzhikai.textpath.SyncTextPathView;
import yanzhikai.textpath.painter.FireworksPainter;

public class FifthActivity extends AppCompatActivity implements View.OnClickListener {

    private AsyncTextPathView atpv;
    private SyncTextPathView stpv;
    private SyncPathView spv;
    private AsyncPathView aspv;
    private Button btnStart;
    private Button btnStop;
    private SeekBar sbStart, sbStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);
        initView();
    }

    private void initView() {
        atpv = (AsyncTextPathView) findViewById(R.id.atpv);
        stpv = (SyncTextPathView) findViewById(R.id.stpv);
        spv = (SyncPathView) findViewById(R.id.spv);
        aspv = (AsyncPathView) findViewById(R.id.aspv);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);
        sbStart = (SeekBar) findViewById(R.id.sb_start);
        sbStop = (SeekBar) findViewById(R.id.sb_stop);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        aspv.setPath(new TestPath());

        spv.setPath(new TestPath());
        spv.setPathPainter(new FireworksPainter());

        sbStart.setMax(1000);
        sbStop.setMax(1000);
        sbStart.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        sbStop.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
    }

    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        private int start, stop;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seekBar.getId() == R.id.sb_start) {
                start = progress;
                if (start > stop) {
                    stop = start;
                    sbStop.setProgress(stop);
                }
            } else {
                stop = progress;
                if (stop < start) {
                    start = stop;
                    sbStart.setProgress(start);
                }
            }

            float startF = start / 1000f;
            float stopF = stop / 1000f;
            atpv.drawPath(startF, stopF);
            stpv.drawPath(startF, stopF);
            spv.drawPath(startF, stopF);
            aspv.drawPath(startF, stopF);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                atpv.startAnimation(0, 1);
                stpv.startAnimation(0, 1);
                spv.startAnimation(0, 1);
                aspv.startAnimation(0, 1);
                break;
            case R.id.btn_stop:
                atpv.stopAnimation();
                stpv.stopAnimation();
                spv.stopAnimation();
                aspv.stopAnimation();
                break;
        }
    }
}
