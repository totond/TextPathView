package yanzhikai.textpathview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import yanzhikai.textpath.SyncPathView;
import yanzhikai.textpath.painter.FireworksPathPainter;

public class ThirdActivity extends Activity {
    private Button btn_start, btn_stop;
    private SyncPathView spv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
        spv = findViewById(R.id.spv);

        spv.setPath(new TestPath());
        spv.setPathPainter(new FireworksPathPainter());



        //设置点击开始播放动画
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spv.startAnimation(1,0);
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spv.stopAnimation();
            }
        });
    }
}
