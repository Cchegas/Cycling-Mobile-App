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
public class DateTimeUnitTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();
    @Mock
    Context mMockContext;

    @Test
    public void testValidDateInFuture() {
        EventActivity validator = new EventActivity(mMockContext);
        assertTrue(validator.isValidDate("2024/12/01", "date", true));
    }

    @Test
    public void testValidDateNotInFutureValid() {
        EventActivity validator = new EventActivity(mMockContext);
        assertTrue(validator.isValidDate("2020/12/05", "date", false));
    }

    @Test
    public void testValidTime() {
        EventActivity validator = new EventActivity(mMockContext);
        assertTrue(validator.isValidTime("12:30", "time"));
    }
}
