package no.ntnu.stud.avikeyb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import no.ntnu.stud.avikeyb.backend.InputInterface;
import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.OutputDevice;
import no.ntnu.stud.avikeyb.backend.core.CoreKeyboard;
import no.ntnu.stud.avikeyb.backend.layouts.BinarySearchLayout;
import no.ntnu.stud.avikeyb.backend.layouts.SimpleExampleLayout;
import no.ntnu.stud.avikeyb.gui.BinarySearchLayoutGUI;
import no.ntnu.stud.avikeyb.gui.LayoutGUI;
import no.ntnu.stud.avikeyb.gui.SimpleExampleLayoutGUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CoreKeyboard keyboard = new CoreKeyboard();
        keyboard.addOutputDevice(new ToastOutput());


        //SimpleExampleLayout layout = new SimpleExampleLayout(keyboard);
        //LayoutGUI layoutGUI = new SimpleExampleLayoutGUI(this, keyboard, layout);
        BinarySearchLayout layout = new BinarySearchLayout(keyboard);
        LayoutGUI layoutGUI = new BinarySearchLayoutGUI(this, keyboard, layout);

        ViewGroup layoutWrapper = (ViewGroup) findViewById(R.id.layoutWrapper);
        layoutWrapper.addView(layoutGUI.createGUI());

        final InputInterface input = layout;


        // Use buttons to register the inputs
        ((Button) findViewById(R.id.buttonRight)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Turn the input on and off for each click
                input.setInputState(InputType.INPUT2, true);
                input.setInputState(InputType.INPUT2, false);
            }
        });
        ((Button) findViewById(R.id.buttonSelect)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setInputState(InputType.INPUT1, true);
                input.setInputState(InputType.INPUT1, false);
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
