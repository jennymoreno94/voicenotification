package com.driver.voicenotifications.domain.model;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

import java.util.Locale;

/**
 * Configuración para el motor de Text-to-Speech
 */
public class VoiceConfig {
    
    private final float speechRate;
    private final float pitch;
    private final Locale locale;
    private final boolean enabled;
    private final int queueMode;

    private VoiceConfig(Builder builder) {
        this.speechRate = builder.speechRate;
        this.pitch = builder.pitch;
        this.locale = builder.locale;
        this.enabled = builder.enabled;
        this.queueMode = builder.queueMode;
    }

    @FloatRange(from = 0.1, to = 3.0)
    public float getSpeechRate() {
        return speechRate;
    }

    @FloatRange(from = 0.1, to = 2.0)
    public float getPitch() {
        return pitch;
    }

    @NonNull
    public Locale getLocale() {
        return locale;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getQueueMode() {
        return queueMode;
    }

    public static class Builder {
        private float speechRate = 1.0f;
        private float pitch = 1.0f;
        private Locale locale = new Locale("es", "ES");
        private boolean enabled = true;
        private int queueMode = 0; // QUEUE_FLUSH

        public Builder setSpeechRate(@FloatRange(from = 0.1, to = 3.0) float speechRate) {
            if (speechRate < 0.1f || speechRate > 3.0f) {
                throw new IllegalArgumentException("Speech rate must be between 0.1 and 3.0");
            }
            this.speechRate = speechRate;
            return this;
        }

        public Builder setPitch(@FloatRange(from = 0.1, to = 2.0) float pitch) {
            if (pitch < 0.1f || pitch > 2.0f) {
                throw new IllegalArgumentException("Pitch must be between 0.1 and 2.0");
            }
            this.pitch = pitch;
            return this;
        }

        public Builder setLocale(@NonNull Locale locale) {
            this.locale = locale;
            return this;
        }

        public Builder setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder setQueueMode(int queueMode) {
            this.queueMode = queueMode;
            return this;
        }

        public VoiceConfig build() {
            return new VoiceConfig(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static VoiceConfig getDefault() {
        return new Builder().build();
    }
}
