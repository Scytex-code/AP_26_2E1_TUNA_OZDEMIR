package com.example.lab12advanced.sample;

public class SampleActions {
    @LabTest
    public void hello() {
        System.out.println("Hello from an annotated no-argument method.");
    }

    @LabTest
    public int doubled(int value) {
        int result = value * 2;
        System.out.println("Mock value doubled: " + result);
        return result;
    }

    @LabTest
    public void unsupported(String text) {
        System.out.println(text);
    }

    public void helper() {
        System.out.println("This method is not annotated.");
    }
}
