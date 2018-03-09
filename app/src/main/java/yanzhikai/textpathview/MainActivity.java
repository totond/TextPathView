package yanzhikai.textpathview;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import yanzhikai.textpath.TextPathAnimatorListener;
import yanzhikai.textpath.painter.ArrowPainter;
import yanzhikai.textpath.painter.FireworksPainter;
import yanzhikai.textpath.SyncTextPathView;
import yanzhikai.textpath.AsyncTextPathView;
import yanzhikai.textpath.painter.PenPainter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_first,btn_second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_first = findViewById(R.id.btn_first);
        btn_second = findViewById(R.id.btn_second);
        btn_first.setOnClickListener(this);
        btn_second.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_first:
                startActivity(new Intent(this,FirstActivity.class));
                break;
            case R.id.btn_second:
                startActivity(new Intent(this,SecondActivity.class));
                break;
        }
    }
}
