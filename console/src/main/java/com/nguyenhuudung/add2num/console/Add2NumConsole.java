package com.nguyenhuudung.add2num.console;

import com.nguyenhuudung.add2num.MyBigNumber;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * Small console front end for the {@code core} module, so the library can be tried out
 * without writing code.
 *
 * <p>The requirement says {@link MyBigNumber} will be handed to another team that
 * builds the user interface. This class stands in for that team: it is the only
 * place that decides how log output should look, because configuring logging is
 * the application's job, not the library's.</p>
 *
 * <pre>
 * java -cp "core-classes;console-classes" com.nguyenhuudung.add2num.console.Add2NumConsole 1234 897
 * </pre>
 */
public final class Add2NumConsole {

    private Add2NumConsole() {
        // Utility class, not meant to be instantiated.
    }

    public static void main(String[] args) {
        configureCompactConsoleLogging();

        MyBigNumber calculator = new MyBigNumber();

        if (args.length == 2) {
            System.out.println("Result: " + calculator.sum(args[0], args[1]));
            return;
        }

        if (args.length != 0) {
            System.out.println("Expected exactly two arguments, got " + args.length + ".");
            System.out.println();
        }

        System.out.println("Usage: java -cp <core-jar>;<console-jar> com.nguyenhuudung.add2num.console.Add2NumConsole <number1> <number2>");
        System.out.println("No arguments given, so here is the example from the requirement.");
        System.out.println();
        System.out.println("Result: " + calculator.sum("1234", "897"));
    }

    /**
     * Prints log records as a single plain line on stdout, instead of the two-line
     * timestamped format {@code java.util.logging} uses by default, so the addition
     * history stays readable next to the printed result.
     */
    private static void configureCompactConsoleLogging() {
        Logger rootLogger = Logger.getLogger("");
        for (Handler existing : rootLogger.getHandlers()) {
            rootLogger.removeHandler(existing);
        }

        StreamHandler handler = new StreamHandler(System.out, new SimpleFormatter() {
            @Override
            public synchronized String format(LogRecord record) {
                return "[LOG] " + record.getMessage() + System.lineSeparator();
            }
        }) {
            @Override
            public synchronized void publish(LogRecord record) {
                super.publish(record);
                // StreamHandler buffers; flushing per record keeps the log lines
                // in order with the System.out lines printed above.
                flush();
            }
        };

        handler.setLevel(Level.ALL);
        rootLogger.addHandler(handler);
        rootLogger.setLevel(Level.ALL);
    }
}
