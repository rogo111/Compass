package com.friendlyphire.compass;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void validatePassword_isCorrect(){
        PasswordUtility utility = new PasswordUtility("barcelona","r");
        assertTrue(utility.validatePassword("cel"));
        assertFalse(utility.validatePassword("celo"));

        utility = new PasswordUtility("barcelona","l");
        assertTrue(utility.validatePassword("ona"));
        assertFalse(utility.validatePassword("on"));

        utility = new PasswordUtility("barcelona","n");
        assertTrue(utility.validatePassword("aba"));
        assertFalse(utility.validatePassword("celo"));

        utility = new PasswordUtility("barcelonax","x");
        assertTrue(utility.validatePassword("bar"));
        assertFalse(utility.validatePassword("celo"));
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}