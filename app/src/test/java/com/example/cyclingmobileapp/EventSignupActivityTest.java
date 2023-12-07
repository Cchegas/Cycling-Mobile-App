package com.example.cyclingmobileapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class EventSignupActivityTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();
    @Mock
    Context mMockContext;

    @Test
    public void testcanRegisterForEventInValid() {
        EventSignupActivity validator = new EventSignupActivity(mMockContext);
        assertFalse(validator.canRegisterForEvent("2023-11-06T12:00:00Z"));
    }

    @Test
    public void testcanRegisterForEventValid() {
        EventSignupActivity validator = new EventSignupActivity(mMockContext);
        assertTrue(validator.canRegisterForEvent("2024-12-09T12:00:00Z"));
    }




}
