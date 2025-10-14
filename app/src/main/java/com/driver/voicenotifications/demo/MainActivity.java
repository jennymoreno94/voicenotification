package com.driver.voicenotifications.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.driver.voicenotifications.demo.databinding.ActivityMainBinding;
import com.driver.voicenotifications.domain.model.NotificationType;
import com.driver.voicenotifications.domain.model.VoiceConfig;
import com.driver.voicenotifications.presentation.VoiceNotificationManager;
import com.driver.voicenotifications.util.DriverBehaviorAnalyzer;
import com.driver.voicenotifications.util.NotificationThrottler;

import java.util.Locale;

/**
 * Actividad de demostración de la librería de notificaciones de voz
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private VoiceNotificationManager voiceManager;
    private NotificationThrottler throttler;
    private DriverBehaviorAnalyzer analyzer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar componentes
        voiceManager = VoiceNotificationManager.getInstance(this);
        throttler = new NotificationThrottler(10000); // 10 segundos de cooldown
        analyzer = new DriverBehaviorAnalyzer();

        // Registrar lifecycle observer
        getLifecycle().addObserver(voiceManager);

        // Configurar voz
        setupVoiceConfiguration();

        // Observar eventos
        observeVoiceEvents();

        // Configurar botones
        setupButtons();

        // Actualizar estado
        updateStatus();
    }

    private void setupVoiceConfiguration() {
        VoiceConfig config = VoiceConfig.builder()
                .setLocale(new Locale("es", "ES"))
                .setSpeechRate(1.0f)
                .setPitch(1.0f)
                .setEnabled(true)
                .build();

        voiceManager.configure(config);
    }

    private void observeVoiceEvents() {
        voiceManager.getEvents().observe(this, new Observer<VoiceNotificationManager.NotificationEvent>() {
            @Override
            public void onChanged(VoiceNotificationManager.NotificationEvent event) {
                String message = "Evento: " + event.getType() + " - " + event.getMessage();
                binding.textStatus.setText(message);
                
                if (event.getType() == VoiceNotificationManager.NotificationEvent.Type.ERROR) {
                    Toast.makeText(MainActivity.this, "Error: " + event.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupButtons() {
        // Exceso de velocidad
        binding.btnSpeedExcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (throttler.tryNotify(NotificationType.SPEED_EXCESS)) {
                    voiceManager.speakSpeedExcess(120, 80);
                } else {
                    showThrottleMessage(NotificationType.SPEED_EXCESS);
                }
            }
        });

        // Frenada brusca
        binding.btnHarshBraking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (throttler.tryNotify(NotificationType.HARSH_BRAKING)) {
                    voiceManager.speak(NotificationType.HARSH_BRAKING);
                } else {
                    showThrottleMessage(NotificationType.HARSH_BRAKING);
                }
            }
        });

        // Aceleración brusca
        binding.btnHarshAcceleration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (throttler.tryNotify(NotificationType.HARSH_ACCELERATION)) {
                    voiceManager.speak(NotificationType.HARSH_ACCELERATION);
                } else {
                    showThrottleMessage(NotificationType.HARSH_ACCELERATION);
                }
            }
        });

        // Giro brusco
        binding.btnSharpTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (throttler.tryNotify(NotificationType.SHARP_TURN)) {
                    voiceManager.speak(NotificationType.SHARP_TURN);
                } else {
                    showThrottleMessage(NotificationType.SHARP_TURN);
                }
            }
        });

        // Detener
        binding.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceManager.stop();
                Toast.makeText(MainActivity.this, "Notificación detenida", Toast.LENGTH_SHORT).show();
            }
        });

        // Simular conducción
        binding.btnSimulateDriving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateDriving();
            }
        });
    }

    private void simulateDriving() {
        // Simular diferentes escenarios de conducción
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Simular aceleración brusca
                    Thread.sleep(2000);
                    if (analyzer.isHarshAcceleration(5.0f)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (throttler.tryNotify(NotificationType.HARSH_ACCELERATION)) {
                                    voiceManager.speak(NotificationType.HARSH_ACCELERATION);
                                }
                            }
                        });
                    }

                    // Simular exceso de velocidad
                    Thread.sleep(3000);
                    if (analyzer.isSpeedExcess(120, 80)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (throttler.tryNotify(NotificationType.SPEED_EXCESS)) {
                                    voiceManager.speakSpeedExcess(120, 80);
                                }
                            }
                        });
                    }

                    // Simular frenada brusca
                    Thread.sleep(3000);
                    if (analyzer.isHarshBraking(-9.0f)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (throttler.tryNotify(NotificationType.HARSH_BRAKING)) {
                                    voiceManager.speak(NotificationType.HARSH_BRAKING);
                                }
                            }
                        });
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Toast.makeText(this, "Simulación iniciada...", Toast.LENGTH_SHORT).show();
    }

    private void showThrottleMessage(NotificationType type) {
        long remaining = throttler.getRemainingCooldown(type);
        String message = String.format(Locale.getDefault(),
                "Espere %d segundos antes de la siguiente notificación",
                remaining / 1000);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void updateStatus() {
        String status = voiceManager.isAvailable() 
                ? "✓ Servicio de voz disponible" 
                : "✗ Servicio de voz no disponible";
        binding.textStatus.setText(status);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
