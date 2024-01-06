package com.innoq.automatedtesting.samples.shoppingcart;

import org.approvaltests.strings.Printable;

import java.util.ArrayList;
import java.util.Arrays;

public class PrintableStory {
    private final String divider;
    private StringBuilder toVerify = new StringBuilder();
    private final String name;
    private ArrayList<Printable> printables = new ArrayList<>();

    public PrintableStory(String name) {
        this(name, "-");
    }
    public PrintableStory(String name, String divider) {
        this.name = name;
        this.divider = divider;
    }

    public void startOfTest(Printable... printables) {
        this.printables.addAll(Arrays.asList(printables));
        printAll();
    }

    private void printAll() {
        for (Printable printable : this.printables) {
            toVerify.append(printable.toString());
            toVerify.append("\n");
        }
    }

    public void act(String action) {
        var count = action.length();
        var dividingLine = divider.repeat(count);
        toVerify.append(STR."""
            \{dividingLine}
            \{action}
            \{dividingLine}
            """
        );
        printAll();
    }

    public void given(Printable... printables) {
        startOfTest(printables);
    }

    public void when(String action) {
        act(action);
    }

    public String then() {
        return story();
    }
    public String story() {
        return toVerify.toString();
    }
}
