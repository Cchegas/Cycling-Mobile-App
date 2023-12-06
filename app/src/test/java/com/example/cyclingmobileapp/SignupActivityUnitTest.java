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
public class SignupActivityUnitTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();
    @Mock
    Context mMockContext;

    @Test
    public void testValidInputReturnsTrue() {
        SignupActivity signupActivity = new SignupActivity(mMockContext);
        assertTrue(signupActivity.validateSignupInfo("John", "Doe", "johndoe", "john.doe@gmail.com", "password"));
    }

    @Test
    public void testInvalidFirstNameReturnsFalse() {
        SignupActivity signupActivity = new SignupActivity(mMockContext);
        assertFalse(signupActivity.validateSignupInfo("John_123", "Doe", "johndoe", "john.doe@example.com", "password"));
    }

    @Test
    public void testInValidLastNameReturnsFalse() {
        SignupActivity signupActivity = new SignupActivity(mMockContext);
        assertFalse(signupActivity.validateSignupInfo("John", "Doe_421", "johndoe", "john.doe@example.com", "password"));
    }

    @Test
    public void testInvalidEmailReturnsFalse() {
        SignupActivity signupActivity = new SignupActivity(mMockContext);
        assertFalse(signupActivity.validateSignupInfo("John", "Doe", "johndoe", "invalid_email!?", "password"));
    }

    @Test
    public void testInValidPasswordReturnsFalse() {
        SignupActivity signupActivity = new SignupActivity(mMockContext);
        assertFalse(signupActivity.validateSignupInfo("John", "Doe", "johndoe", "john.doe@example.com", "joh"));
    }
}
