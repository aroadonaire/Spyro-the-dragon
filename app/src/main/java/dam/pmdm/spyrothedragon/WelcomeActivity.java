package dam.pmdm.spyrothedragon;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ImageButton btnComenzar = findViewById(R.id.btn_comenzar);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        btnComenzar.startAnimation(fadeIn);

        btnComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slice_right, R.anim.slice_left);
                finish();
            }
        });
    }
}
