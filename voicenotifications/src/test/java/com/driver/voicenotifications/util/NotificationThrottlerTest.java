package com.driver.voicenotifications.util;

import com.driver.voicenotifications.domain.model.NotificationType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitarios para NotificationThrottler
 */
public class NotificationThrottlerTest {

    private NotificationThrottler throttler;

    @Before
    public void setUp() {
        throttler = new NotificationThrottler(1000); // 1 segundo de cooldown
    }

    @Test
    public void testCanNotify_firstTime_returnsTrue() {
        assertTrue(throttler.canNotify(NotificationType.SPEED_EXCESS));
    }

    @Test
    public void testCanNotify_withinCooldown_returnsFalse() {
        throttler.recordNotification(NotificationType.SPEED_EXCESS);
        assertFalse(throttler.canNotify(NotificationType.SPEED_EXCESS));
    }

    @Test
    public void testCanNotify_afterCooldown_returnsTrue() throws InterruptedException {
        throttler.recordNotification(NotificationType.SPEED_EXCESS);
        Thread.sleep(1100); // Esperar más que el cooldown
        assertTrue(throttler.canNotify(NotificationType.SPEED_EXCESS));
    }

    @Test
    public void testTryNotify_firstTime_returnsTrue() {
        assertTrue(throttler.tryNotify(NotificationType.SPEED_EXCESS));
    }

    @Test
    public void testTryNotify_withinCooldown_returnsFalse() {
        throttler.tryNotify(NotificationType.SPEED_EXCESS);
        assertFalse(throttler.tryNotify(NotificationType.SPEED_EXCESS));
    }

    @Test
    public void testReset_allowsImmediateNotification() {
        throttler.recordNotification(NotificationType.SPEED_EXCESS);
        assertFalse(throttler.canNotify(NotificationType.SPEED_EXCESS));
        
        throttler.reset(NotificationType.SPEED_EXCESS);
        assertTrue(throttler.canNotify(NotificationType.SPEED_EXCESS));
    }

    @Test
    public void testGetRemainingCooldown_returnsCorrectValue() {
        throttler.recordNotification(NotificationType.SPEED_EXCESS);
        long remaining = throttler.getRemainingCooldown(NotificationType.SPEED_EXCESS);
        
        assertTrue(remaining > 0);
        assertTrue(remaining <= 1000);
    }

    @Test
    public void testDifferentTypes_independentCooldowns() {
        throttler.recordNotification(NotificationType.SPEED_EXCESS);
        
        assertFalse(throttler.canNotify(NotificationType.SPEED_EXCESS));
        assertTrue(throttler.canNotify(NotificationType.HARSH_BRAKING));
    }
}
