package com.nguyenhuudung.add2num;

import java.util.logging.Logger;

/**
 * Core class of the Add2Num (A2N) challenge.
 *
 * <p>Adds two non-negative integers that are given as decimal strings, using the
 * pen-and-paper algorithm a primary-school pupil uses: walk both strings from
 * right to left, add one pair of digits at a time, keep the carry.</p>
 *
 * <p>The whole point is to <em>not</em> convert the strings into a numeric type,
 * so the operands may be far longer than {@code long} or {@code double} can hold.
 * {@link java.math.BigInteger} is deliberately not used here; it appears only in
 * the unit tests, as an independent oracle to check the result against.</p>
 *
 * <p><strong>Input assumption.</strong> As stated in the requirement, both
 * arguments are assumed to be valid: non-empty, digits {@code '0'}-{@code '9'}
 * only, no sign, no separators, no whitespace. The class performs no validation
 * and therefore no error handling.</p>
 *
 * <p><strong>Logging.</strong> Every step of the addition is written to
 * {@code java.util.logging} at {@code INFO}, as the requirement asks for the
 * history of the operation to be recorded. A caller that finds this too chatty
 * can silence it without touching this class:</p>
 *
 * <pre>{@code
 * Logger.getLogger(MyBigNumber.class.getName()).setLevel(Level.WARNING);
 * }</pre>
 *
 * <p>This class is stateless and therefore safe to share between threads.</p>
 */
public class MyBigNumber {

    private static final Logger LOG = Logger.getLogger(MyBigNumber.class.getName());

    /** Decimal base. Named rather than inlined so the algorithm reads as arithmetic. */
    private static final int BASE = 10;

    /**
     * Adds two non-negative integers represented as decimal strings.
     *
     * <p>Example: {@code sum("1234", "897")} returns {@code "2131"}.</p>
     *
     * @param stn1 first operand, a non-empty string of digits
     * @param stn2 second operand, a non-empty string of digits
     * @return the sum, as a decimal string without leading zeros
     */
    public String sum(String stn1, String stn2) {
        LOG.info(String.format("sum(\"%s\", \"%s\") - begin", stn1, stn2));

        StringBuilder reversedDigits = new StringBuilder();

        // Read both operands from the right-hand end, the way you would on paper.
        int i = stn1.length() - 1;
        int j = stn2.length() - 1;
        int carry = 0;
        int step = 0;

        // Keep going while either operand still has digits, or a carry is pending.
        // That last condition is what turns 999 + 1 into 1000 instead of 000.
        while (i >= 0 || j >= 0 || carry > 0) {
            step++;

            // A string that has run out of digits contributes 0, which is how
            // operands of different lengths line up (1234 + 897).
            int left = (i >= 0) ? toDigit(stn1.charAt(i)) : 0;
            int right = (j >= 0) ? toDigit(stn2.charAt(j)) : 0;

            int total = left + right + carry;
            int digitToKeep = total % BASE;
            int carryOut = total / BASE;

            LOG.info(String.format(
                    "step %d: %d + %d + carry %d = %d -> keep %d, carry %d",
                    step, left, right, carry, total, digitToKeep, carryOut));

            reversedDigits.append(digitToKeep);
            carry = carryOut;

            i--;
            j--;
        }

        // Digits were produced least-significant first, so reverse them.
        String result = stripLeadingZeros(reversedDigits.reverse().toString());

        LOG.info(String.format("sum(\"%s\", \"%s\") = \"%s\" in %d step(s)", stn1, stn2, result, step));
        return result;
    }

    /**
     * Converts a digit character to its numeric value.
     *
     * <p>{@code '7' - '0' == 7} because the digit characters are contiguous in
     * the character set. Writing {@code Integer.parseInt} here would work too but
     * would allocate a string per digit for no benefit.</p>
     */
    private static int toDigit(char character) {
        return character - '0';
    }

    /**
     * Removes leading zeros so the result is a canonical decimal string.
     *
     * <p>The addition itself never produces a leading zero: a carry is only
     * prepended when it is non-zero. A leading zero can therefore only come from
     * an operand that had one, for example {@code sum("007", "3")}, which returns
     * {@code "10"} rather than {@code "010"}. The single zero of the value zero is
     * preserved, so {@code sum("0", "0")} returns {@code "0"} and not the empty
     * string.</p>
     */
    private static String stripLeadingZeros(String value) {
        int firstSignificant = 0;
        while (firstSignificant < value.length() - 1 && value.charAt(firstSignificant) == '0') {
            firstSignificant++;
        }
        return value.substring(firstSignificant);
    }
}
