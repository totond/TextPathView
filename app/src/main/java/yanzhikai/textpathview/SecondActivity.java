package yanzhikai.textpathview;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;

import yanzhikai.textpath.AsyncTextPathView;
import yanzhikai.textpath.SyncTextPathView;

public class SecondActivity extends Activity {
    private Button btn_start, btn_stop, btn_pause, btn_resume;
    private SyncTextPathView stpv_laugh, stpv_type3;
    private AsyncTextPathView atpv_totond,atpv_type1,atpv_type2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        stpv_laugh = findViewById(R.id.stpv_laugh);
        atpv_totond = findViewById(R.id.atpv_totond);
        atpv_type1 = findViewById(R.id.atpv_type1);
        atpv_type2 = findViewById(R.id.atpv_type2);
        stpv_type3 = findViewById(R.id.stpv_type3);
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
                atpv_type1.startAnimation(0,1);
                atpv_type2.startAnimation(0,1);
                stpv_type3.startAnimation(0,1);
            }
        });

        atpv_type1.setTypeface(Typeface.SANS_SERIF);
        atpv_type2.setTypeface(Typeface.MONOSPACE);
        stpv_type3.setTypeface(Typeface.DEFAULT_BOLD);


        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stpv_laugh.stopAnimation();
                atpv_totond.stopAnimation();
                atpv_type1.stopAnimation();
                atpv_type2.stopAnimation();
                stpv_type3.stopAnimation();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            btn_pause.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    stpv_laugh.pauseAnimation();
                    atpv_totond.pauseAnimation();
                    atpv_type1.pauseAnimation();
                    atpv_type2.pauseAnimation();
                    stpv_type3.pauseAnimation();
                }
            });
            btn_resume.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    stpv_laugh.resumeAnimation();
                    atpv_totond.resumeAnimation();
                    atpv_type1.resumeAnimation();
                    atpv_type2.resumeAnimation();
                    stpv_type3.resumeAnimation();
                }
            });
        } else {
            findViewById(R.id.pauseLayout).setVisibility(View.GONE);
        }
    }
}
