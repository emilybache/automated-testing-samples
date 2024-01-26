// This file is not included in the Apache license Copyright (C) 2020 innoQ Deutschland GmbH that applies to the rest of the project.
// The copyright for this file is owned by ApprovalTests.Java under their Apache license.
package com.innoq.automatedtesting.samples.shoppingcart;

import org.approvaltests.strings.Printable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * This code is generic and not supposed to be part of this project
 * - it should be part of org.approvaltests.strings
 */
public class PrintableScenario {

    private final StringBuilder toVerify = new StringBuilder();
    private final String name;
    private final String description;
    private final ArrayList<Printable> printables = new ArrayList<>();

    public PrintableScenario(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public PrintableScenario(String name) {
        this(name, "");
    }

    public void startOfTest(Printable... printables) {
        this.printables.addAll(Arrays.asList(printables));
        toVerify.append(makeHeading(name, description));
        toVerify.append(printAll());
    }

    public String makeHeading(String heading, String summary) {
        var count = heading.length();
        var underlineHeading = "=".repeat(count);
        return STR."""
            \{heading}
            \{underlineHeading}
            \{summary}

            """;
    }

    public String printAll() {
        var result = new StringBuilder();
        for (var printable : this.printables) {
            result.append(printable.toString());
            result.append("\n");
        }
        return result.toString();
    }

    public void act(String action) {
        toVerify.append(makeSubheading(action));
        toVerify.append(printAll());
    }

    public String makeSubheading(String action) {
        var count = action.length();
        var underlineHeading2 = "-".repeat(count);
        return STR."""
            \{action}
            \{underlineHeading2}
            """;
    }

    public void given(Printable... printables) {
        startOfTest(printables);
    }

    public void when(String action) {
        act(action);
    }

    public String then() {
        return print();
    }
    public String print() {
        return toVerify.toString();
    }

    public void when(String action, Callable function) throws Exception {
        function.call();
        when(action);
    }
}
