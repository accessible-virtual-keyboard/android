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

    // The current output buffer
    private StringBuilder currentBuffer;

    public CoreKeyboard() {
        currentBuffer = new StringBuilder();
        outputDevices = new ArrayList<>();
    }


    /**
     * Register an output device to the keyboard
     *
     * @param output the output device to register
     */
    public void addOutputDevice(OutputDevice output) {
        outputDevices.add(output);
    }

    /**
     * Removes a registered output device from the keyboard
     *
     * @param output the output device to unregister
     */
    public void removeOutputDevice(OutputDevice output) {
        outputDevices.remove(output);
    }


    @Override
    public void addToCurrentBuffer(String value) {
        currentBuffer.append(value);
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
        currentBuffer.setLength(0);
    }
}
