package yanzhikai.textpathview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_first,btn_second,btn_third,btn_forth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_first = findViewById(R.id.btn_first);
        btn_second = findViewById(R.id.btn_second);
        btn_third = findViewById(R.id.btn_third);
        btn_forth = findViewById(R.id.btn_forth);
        btn_first.setOnClickListener(this);
        btn_second.setOnClickListener(this);
        btn_third.setOnClickListener(this);
        btn_forth.setOnClickListener(this);
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
            case R.id.btn_third:
                startActivity(new Intent(this,ThirdActivity.class));
                break;
            case R.id.btn_forth:
                startActivity(new Intent(this,ForthActivity.class));
                break;
        }
    }
}
