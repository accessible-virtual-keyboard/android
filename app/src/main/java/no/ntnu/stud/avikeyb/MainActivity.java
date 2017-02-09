package no.ntnu.stud.avikeyb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.OutputDevice;
import no.ntnu.stud.avikeyb.backend.core.CoreKeyboard;
import no.ntnu.stud.avikeyb.backend.layouts.SimpleExampleLayout;
import no.ntnu.stud.avikeyb.gui.SimpleExampleLayoutGUI;

public class MainActivity extends AppCompatActivity {

    private CoreKeyboard keyboard;
    private SimpleExampleLayout layout;
    private SimpleExampleLayoutGUI layoutGUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keyboard = new CoreKeyboard();
        keyboard.addOutputDevice(new ToastOutput());

        layout = new SimpleExampleLayout(keyboard);
        layoutGUI = new SimpleExampleLayoutGUI(this, keyboard, layout);


        ViewGroup layoutWrapper = (ViewGroup) findViewById(R.id.layoutWrapper);
        layoutWrapper.addView(layoutGUI.buildGUI());


        // Use buttons to register the inputs
        ((Button) findViewById(R.id.buttonRight)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Turn the input on and off for each click
                layout.setInputState(InputType.INPUT2, true);
                layout.setInputState(InputType.INPUT2, false);
            }
        });
        ((Button) findViewById(R.id.buttonSelect)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.setInputState(InputType.INPUT1, true);
                layout.setInputState(InputType.INPUT1, false);
            }
        });

    }


    // Shows the keyboard output in a toast message
    private class ToastOutput implements OutputDevice {
        @Override
        public void sendOutput(String output) {
            Toast.makeText(MainActivity.this, output, Toast.LENGTH_SHORT).show();
        }
    }
}
