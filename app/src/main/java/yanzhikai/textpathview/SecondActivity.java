package yanzhikai.textpathview;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;

import yanzhikai.textpath.AsyncTextPathView;
import yanzhikai.textpath.SyncTextPathView;

public class SecondActivity extends Activity {
    private Button btn_start, btn_stop, btn_pause, btn_resume;
    private SyncTextPathView stpv_laugh;
    private AsyncTextPathView atpv_totond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        stpv_laugh = findViewById(R.id.stpv_laugh);
        atpv_totond = findViewById(R.id.atpv_totond);
        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
        btn_pause = findViewById(R.id.btn_pause);
        btn_resume = findViewById(R.id.btn_resume);

        //设置点击开始播放动画
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stpv_laugh.startAnimation(0,1);
                atpv_totond.startAnimation(0,1);
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stpv_laugh.stopAnimation();
                atpv_totond.stopAnimation();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            btn_pause.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    stpv_laugh.pauseAnimation();
                    atpv_totond.pauseAnimation();
                }
            });
            btn_resume.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    stpv_laugh.resumeAnimation();
                    atpv_totond.resumeAnimation();
                }
            });
        } else {
            findViewById(R.id.pauseLayout).setVisibility(View.GONE);
        }
    }
}
