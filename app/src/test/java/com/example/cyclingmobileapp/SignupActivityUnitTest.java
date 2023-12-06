package com.example.cyclingmobileapp;

import org.junit.Before;
import org.junit.Test;

import android.content.Context;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;


@RunWith(MockitoJUnitRunner.class)
 public class SignupActivityUnitTest {

    @Mock
    Context mMockContext;

    @Test
    public void testValidInputReturnsTrue() {
        SignupActivity signupActivity = new SignupActivity(mMockContext);
        assertTrue(signupActivity.validateSignupInfo("John", "Doe", "john_doe", "john.doe@example.com", "password"));
    }

    @Test
    public void testInvalidFirstNameReturnsFalse() {
        SignupActivity signupActivity = new SignupActivity(mMockContext);
        boolean yes = signupActivity.validateSignupInfo("John 123", "Doe", "john_doe", "john.doe@example.com", "password");
        assertTrue(yes);
    }

    @Test
    public void testInvalidEmailReturnsFalse() {
        SignupActivity signupActivity = new SignupActivity(mMockContext);
        assertFalse(signupActivity.validateSignupInfo("John", "Doe", "john_doe", "invalid_email", "password"));
    }

}
