package dam.pmdm.spyrothedragon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GuideActivity extends AppCompatActivity {

    private int currentStep = 1; // Control de pasos
    private ImageView guideImage;
    private TextView guideText;
    private Button nextButton, skipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide); // Ahora está en la posición correcta

        guideImage = findViewById(R.id.guideImage);
        guideText = findViewById(R.id.guideText);
        nextButton = findViewById(R.id.nextButton);
        skipButton = findViewById(R.id.skipButton);

        updateGuideStep();

        nextButton.setOnClickListener(v -> nextStep());
        skipButton.setOnClickListener(v -> skipGuide());
    }

    private void updateGuideStep() {
        switch (currentStep) {
            case 1:
                guideText.setText("Aquí podrás explorar a todos los personajes del mundo de Spyro.");
                guideImage.setImageResource(R.drawable.spyro);
                break;
            case 2:
                guideText.setText("En esta pestaña puedes ver todos los mundos disponibles.");
                guideImage.setImageResource(R.drawable.autumn_plains);
                break;
            case 3:
                guideText.setText("Aquí puedes consultar los coleccionables.");
                guideImage.setImageResource(R.drawable.dragon_eggs);
                break;
            case 4:
                guideText.setText("Presiona este icono para obtener información sobre la app.");
                guideImage.setImageResource(R.drawable.info);
                break;
            case 5:
                guideText.setText("¡Guía completada! Presiona 'Finalizar' para comenzar.");
                guideImage.setImageResource(R.drawable.finalizar);
                nextButton.setText("Finalizar");
                break;
        }
    }

    private void nextStep() {
        if (currentStep < 5) {
            currentStep++;
            updateGuideStep();
        } else {
            completeGuide();
        }
    }

    private void skipGuide() {
        completeGuide();
    }

    private void completeGuide() {
        SharedPreferences prefs = getSharedPreferences("GuidePrefs", MODE_PRIVATE);
        prefs.edit().putBoolean("GuideCompleted", true).apply();
        startMainActivity();
    }

    private void startMainActivity() {
        Log.d("GuideActivity", "Iniciando MainActivity.");
        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
