package com.nguyenhuudung.add2num;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link MyBigNumber}.
 *
 * <p>Two layers. The named tests below pin down the cases a reviewer will look
 * for, one behaviour each. The last test then cross-checks thousands of random
 * pairs against {@link BigInteger}, which catches the carry mistakes that hand
 * written cases tend to miss.</p>
 */
class MyBigNumberTest {

    private final MyBigNumber calculator = new MyBigNumber();

    @BeforeAll
    static void silenceStepLogging() {
        // The class logs every digit at INFO by design; that would bury the test
        // report under millions of lines once the random test runs.
        Logger.getLogger(MyBigNumber.class.getName()).setLevel(Level.WARNING);
    }

    @Test
    @DisplayName("The worked example from the requirement")
    void addsTheExampleFromTheRequirement() {
        assertEquals("2131", calculator.sum("1234", "897"));
    }

    @Test
    @DisplayName("Operand order does not matter when the lengths differ")
    void isCommutativeForOperandsOfDifferentLength() {
        assertEquals("2131", calculator.sum("897", "1234"));
    }

    @Test
    @DisplayName("Zero plus zero is a single zero, not an empty string")
    void addsZeroToZero() {
        assertEquals("0", calculator.sum("0", "0"));
    }

    @Test
    @DisplayName("Adding zero returns the other operand unchanged")
    void addsZeroToANumber() {
        assertEquals("123", calculator.sum("123", "0"));
        assertEquals("456", calculator.sum("0", "456"));
    }

    @Test
    @DisplayName("A carry out of the last digit grows the result by one digit")
    void carriesPastTheMostSignificantDigit() {
        assertEquals("10", calculator.sum("5", "5"));
    }

    @Test
    @DisplayName("A carry propagates across a run of nines")
    void propagatesCarryAcrossNines() {
        assertEquals("1000", calculator.sum("999", "1"));
    }

    @Test
    @DisplayName("Every position carries at once")
    void carriesAtEveryPosition() {
        assertEquals("199998", calculator.sum("99999", "99999"));
    }

    @Test
    @DisplayName("Operands far beyond the range of long")
    void addsNumbersLargerThanLong() {
        assertEquals(
                "111111111011111111100",
                calculator.sum("12345678901234567890", "98765432109876543210"));
    }

    @Test
    @DisplayName("Thirty nines plus one")
    void addsThirtyNinesToOne() {
        assertEquals(
                "1000000000000000000000000000000",
                calculator.sum("1", "999999999999999999999999999999"));
    }

    @Test
    @DisplayName("Leading zeros in an operand are not carried into the result")
    void normalisesLeadingZeros() {
        assertEquals("10", calculator.sum("007", "3"));
        assertEquals("0", calculator.sum("000", "0"));
    }

    @Test
    @DisplayName("Matches BigInteger on 2000 random pairs of up to ~60 digits")
    void matchesBigIntegerOnRandomInput() {
        // Fixed seed: a failure here has to be reproducible to be worth anything.
        Random random = new Random(20260919L);

        for (int i = 0; i < 2000; i++) {
            BigInteger left = new BigInteger(random.nextInt(200) + 1, random);
            BigInteger right = new BigInteger(random.nextInt(200) + 1, random);

            assertEquals(
                    left.add(right).toString(),
                    calculator.sum(left.toString(), right.toString()),
                    () -> "failed on " + left + " + " + right);
        }
    }
}
