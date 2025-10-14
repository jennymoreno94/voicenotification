package com.driver.voicenotifications.domain.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitarios para VoiceNotification
 */
public class VoiceNotificationTest {

    @Test
    public void testBuilder_withValidData_createsNotification() {
        VoiceNotification notification = new VoiceNotification.Builder()
                .setType(NotificationType.SPEED_EXCESS)
                .setMessage("Test message")
                .setPriority(VoiceNotification.Priority.HIGH)
                .build();

        assertNotNull(notification);
        assertEquals(NotificationType.SPEED_EXCESS, notification.getType());
        assertEquals("Test message", notification.getMessage());
        assertEquals(VoiceNotification.Priority.HIGH, notification.getPriority());
    }

    @Test(expected = IllegalStateException.class)
    public void testBuilder_withoutType_throwsException() {
        new VoiceNotification.Builder()
                .setMessage("Test message")
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuilder_withoutMessage_throwsException() {
        new VoiceNotification.Builder()
                .setType(NotificationType.SPEED_EXCESS)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuilder_withEmptyMessage_throwsException() {
        new VoiceNotification.Builder()
                .setType(NotificationType.SPEED_EXCESS)
                .setMessage("")
                .build();
    }

    @Test
    public void testPriority_levels() {
        assertEquals(0, VoiceNotification.Priority.LOW.getLevel());
        assertEquals(1, VoiceNotification.Priority.NORMAL.getLevel());
        assertEquals(2, VoiceNotification.Priority.HIGH.getLevel());
        assertEquals(3, VoiceNotification.Priority.URGENT.getLevel());
    }

    @Test
    public void testEquals_sameData_returnsTrue() {
        long timestamp = System.currentTimeMillis();
        
        VoiceNotification notification1 = new VoiceNotification.Builder()
                .setType(NotificationType.SPEED_EXCESS)
                .setMessage("Test")
                .setTimestamp(timestamp)
                .build();

        VoiceNotification notification2 = new VoiceNotification.Builder()
                .setType(NotificationType.SPEED_EXCESS)
                .setMessage("Test")
                .setTimestamp(timestamp)
                .build();

        assertEquals(notification1, notification2);
    }
}
