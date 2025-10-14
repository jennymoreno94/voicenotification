package com.driver.voicenotifications.util;

import com.driver.voicenotifications.domain.model.NotificationType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitarios para DriverBehaviorAnalyzer
 */
public class DriverBehaviorAnalyzerTest {

    private DriverBehaviorAnalyzer analyzer;

    @Before
    public void setUp() {
        analyzer = new DriverBehaviorAnalyzer();
    }

    @Test
    public void testIsSpeedExcess_aboveThreshold_returnsTrue() {
        assertTrue(analyzer.isSpeedExcess(95, 80)); // 15 km/h sobre el límite
    }

    @Test
    public void testIsSpeedExcess_belowThreshold_returnsFalse() {
        assertFalse(analyzer.isSpeedExcess(85, 80)); // 5 km/h sobre el límite
    }

    @Test
    public void testIsHarshBraking_belowThreshold_returnsTrue() {
        assertTrue(analyzer.isHarshBraking(-9.0f));
    }

    @Test
    public void testIsHarshBraking_aboveThreshold_returnsFalse() {
        assertFalse(analyzer.isHarshBraking(-5.0f));
    }

    @Test
    public void testIsHarshAcceleration_aboveThreshold_returnsTrue() {
        assertTrue(analyzer.isHarshAcceleration(5.0f));
    }

    @Test
    public void testIsHarshAcceleration_belowThreshold_returnsFalse() {
        assertFalse(analyzer.isHarshAcceleration(3.0f));
    }

    @Test
    public void testIsSharpTurn_aboveThreshold_returnsTrue() {
        assertTrue(analyzer.isSharpTurn(6.0f));
        assertTrue(analyzer.isSharpTurn(-6.0f));
    }

    @Test
    public void testIsSharpTurn_belowThreshold_returnsFalse() {
        assertFalse(analyzer.isSharpTurn(4.0f));
    }

    @Test
    public void testAnalyzeAcceleration_harshBraking() {
        NotificationType type = analyzer.analyzeAcceleration(-9.0f, 2.0f);
        assertEquals(NotificationType.HARSH_BRAKING, type);
    }

    @Test
    public void testAnalyzeAcceleration_harshAcceleration() {
        NotificationType type = analyzer.analyzeAcceleration(5.0f, 2.0f);
        assertEquals(NotificationType.HARSH_ACCELERATION, type);
    }

    @Test
    public void testAnalyzeAcceleration_sharpTurn() {
        NotificationType type = analyzer.analyzeAcceleration(2.0f, 6.0f);
        assertEquals(NotificationType.SHARP_TURN, type);
    }

    @Test
    public void testSetThresholds_customValues() {
        analyzer.setSpeedThreshold(20);
        analyzer.setHarshBrakingThreshold(-10.0f);
        
        assertEquals(20, analyzer.getSpeedThreshold());
        assertEquals(-10.0f, analyzer.getHarshBrakingThreshold(), 0.01f);
    }
}
