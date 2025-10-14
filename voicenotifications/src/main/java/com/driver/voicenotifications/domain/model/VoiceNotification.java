package com.driver.voicenotifications.domain.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * Modelo de dominio para una notificación de voz
 */
public class VoiceNotification {
    
    private final NotificationType type;
    private final String message;
    private final Priority priority;
    private final long timestamp;
    @Nullable
    private final Object metadata;

    private VoiceNotification(Builder builder) {
        this.type = builder.type;
        this.message = builder.message;
        this.priority = builder.priority;
        this.timestamp = builder.timestamp;
        this.metadata = builder.metadata;
    }

    @NonNull
    public NotificationType getType() {
        return type;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    @NonNull
    public Priority getPriority() {
        return priority;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Nullable
    public Object getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoiceNotification that = (VoiceNotification) o;
        return timestamp == that.timestamp &&
                type == that.type &&
                Objects.equals(message, that.message) &&
                priority == that.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, message, priority, timestamp);
    }

    @Override
    public String toString() {
        return "VoiceNotification{" +
                "type=" + type +
                ", message='" + message + '\'' +
                ", priority=" + priority +
                ", timestamp=" + timestamp +
                '}';
    }

    public static class Builder {
        private NotificationType type;
        private String message;
        private Priority priority = Priority.NORMAL;
        private long timestamp = System.currentTimeMillis();
        private Object metadata;

        public Builder setType(@NonNull NotificationType type) {
            this.type = type;
            return this;
        }

        public Builder setMessage(@NonNull String message) {
            this.message = message;
            return this;
        }

        public Builder setPriority(@NonNull Priority priority) {
            this.priority = priority;
            return this;
        }

        public Builder setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setMetadata(@Nullable Object metadata) {
            this.metadata = metadata;
            return this;
        }

        public VoiceNotification build() {
            if (type == null) {
                throw new IllegalStateException("NotificationType is required");
            }
            if (message == null || message.trim().isEmpty()) {
                throw new IllegalStateException("Message is required");
            }
            return new VoiceNotification(this);
        }
    }

    public enum Priority {
        LOW(0),
        NORMAL(1),
        HIGH(2),
        URGENT(3);

        private final int level;

        Priority(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }
}
