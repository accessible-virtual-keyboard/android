package no.ntnu.stud.avikeyb.backend.core;

import java.util.ArrayList;

import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.OutputDevice;


/**
 * Implementation of the core keyboard state logic
 */
public class CoreKeyboard implements Keyboard {

    // List of output devices
    private ArrayList<OutputDevice> outputDevices;
    private ArrayList<KeyboardListener> listeners;

    // The current output buffer
    private StringBuilder currentBuffer;

    public CoreKeyboard() {
        currentBuffer = new StringBuilder();
        outputDevices = new ArrayList<>();
        listeners = new ArrayList<>();
    }


    @Override
    public void addOutputDevice(OutputDevice output) {
        outputDevices.add(output);
    }

    @Override
    public void removeOutputDevice(OutputDevice output) {
        outputDevices.remove(output);
    }


    @Override
    public void addStateListener(KeyboardListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeStateListener(KeyboardListener listener) {
        listeners.remove(listener);
    }


    @Override
    public void addToCurrentBuffer(String value) {
        String oldBuffer = getCurrentBuffer();
        currentBuffer.append(value);
        notifyBufferChanged(oldBuffer, getCurrentBuffer());
    }


    @Override
    public void sendCurrentBuffer() {
        String buf = getCurrentBuffer();
        for (OutputDevice output : outputDevices) {
            output.sendOutput(buf);
        }
        clearCurrentBuffer();
    }

    @Override
    public String getCurrentWord() {
        // Very basic implementation that do not take into consideration punctuation or other special characters
        String[] buffer = getCurrentBuffer().split(" ");
        return buffer[buffer.length - 1];
    }

    @Override
    public String getCurrentBuffer() {
        return currentBuffer.toString();
    }

    @Override
    public void clearCurrentBuffer() {
        String oldBuffer = getCurrentBuffer();
        currentBuffer.setLength(0);
        notifyBufferChanged(oldBuffer, getCurrentBuffer());
    }


    private void notifyBufferChanged(String oldBuffer, String newBuffer) {
        for (KeyboardListener listener : listeners) {
            listener.onOutputBufferChange(oldBuffer, newBuffer);
        }
    }
}
