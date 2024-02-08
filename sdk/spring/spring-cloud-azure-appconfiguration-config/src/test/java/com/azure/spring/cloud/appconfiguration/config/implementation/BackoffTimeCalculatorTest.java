// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.appconfiguration.config.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Test;

public class BackoffTimeCalculatorTest {

    /**
     * Testing the calculated time is some time in the future, multiple attempts don't guarantee longer wait times.
     */
    @Test
    public void testCalculate() {
        int testTime = 10;

        BackoffTimeCalculator.setDefaults((long) 600, (long) 30);
        Long testDate = BackoffTimeCalculator.calculateBackoff(1);

        assertNotNull(testDate);

        assertTrue(testDate > 1);

        Long calculatedTime = BackoffTimeCalculator.calculateBackoff(1);

        assertTrue(calculatedTime > testTime);

        calculatedTime = BackoffTimeCalculator.calculateBackoff(2);

        assertTrue(calculatedTime > testTime);

        calculatedTime = BackoffTimeCalculator.calculateBackoff(3);

        assertTrue(calculatedTime > testTime);
    }
    
    @Test
    public void testErrors() {
        BackoffTimeCalculator.setDefaults((long) 600, (long) -1);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> BackoffTimeCalculator.calculateBackoff(0,(long) 0));
        assertEquals("Minimum Backoff time needs to be greater than or equal to 0.", e.getMessage());
        
        BackoffTimeCalculator.setDefaults((long) 600, (long) 1);
        e = assertThrows(IllegalArgumentException.class,
            () -> BackoffTimeCalculator.calculateBackoff(0,(long) -1));
        assertEquals("Maximum Backoff time needs to be greater than or equal to 0.", e.getMessage());
        
        e = assertThrows(IllegalArgumentException.class,
            () -> BackoffTimeCalculator.calculateBackoff(-1,(long) 1));
        assertEquals("Number of previous attempts needs to be a positive number.", e.getMessage());
    }
    
    @Test
    public void testStartupBackoff() {
        BackoffTimeCalculator.setDefaults((long) 600, (long) 30);
        assertNull(BackoffTimeCalculator.calculateBackoffStartup(null));
        
        Instant elapsed = Instant.now().minusSeconds(0);
        Long backoff = BackoffTimeCalculator.calculateBackoffStartup(elapsed);
        assertTrue(backoff <= 6);
        assertTrue(backoff >= 1);
        
        elapsed = Instant.now().minusSeconds(99);
        backoff = BackoffTimeCalculator.calculateBackoffStartup(elapsed);
        assertTrue(backoff <= 6);
        assertTrue(backoff >= 1);
        
        elapsed = Instant.now().minusSeconds(101);
        backoff = BackoffTimeCalculator.calculateBackoffStartup(elapsed);
        assertTrue(backoff <= 12);
        assertTrue(backoff >= 2);
        
        elapsed = Instant.now().minusSeconds(199);
        backoff = BackoffTimeCalculator.calculateBackoffStartup(elapsed);
        assertTrue(backoff <= 12);
        assertTrue(backoff >= 2);
        
        elapsed = Instant.now().minusSeconds(201);
        backoff = BackoffTimeCalculator.calculateBackoffStartup(elapsed);
        assertTrue(backoff <= 37);
        assertTrue(backoff >= 7);
        
        elapsed = Instant.now().minusSeconds(599);
        backoff = BackoffTimeCalculator.calculateBackoffStartup(elapsed);
        assertTrue(backoff <= 37);
        assertTrue(backoff >= 7);
        
        elapsed = Instant.now().minusSeconds(601);
        backoff = BackoffTimeCalculator.calculateBackoffStartup(elapsed);
        assertNull(backoff);
    }
}
