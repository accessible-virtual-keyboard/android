package no.ntnu.stud.avikeyb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import no.ntnu.stud.avikeyb.backend.InputInterface;
import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.OutputDevice;
import no.ntnu.stud.avikeyb.backend.core.CoreKeyboard;
import no.ntnu.stud.avikeyb.backend.layouts.BinarySearchLayout;
import no.ntnu.stud.avikeyb.backend.layouts.SimpleExampleLayout;
import no.ntnu.stud.avikeyb.gui.BinarySearchLayoutGUI;
import no.ntnu.stud.avikeyb.gui.LayoutGUI;
import no.ntnu.stud.avikeyb.backend.layouts.ETOSLayout;
import no.ntnu.stud.avikeyb.backend.layouts.SimpleExampleLayout;
import no.ntnu.stud.avikeyb.gui.ETOSLayoutGUI;
import no.ntnu.stud.avikeyb.gui.SimpleExampleLayoutGUI;

public class MainActivity extends AppCompatActivity {


    private SimpleExampleLayout layout;
    private SimpleExampleLayoutGUI layoutGUI;
    private ETOSLayout etosLayout;
    private ETOSLayoutGUI etosGUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Keyboard keyboard = new CoreKeyboard();
        keyboard.addOutputDevice(new ToastOutput());


        //SimpleExampleLayout layout = new SimpleExampleLayout(keyboard);
        //LayoutGUI layoutGUI = new SimpleExampleLayoutGUI(this, keyboard, layout);
        BinarySearchLayout layout = new BinarySearchLayout(keyboard);
        LayoutGUI layoutGUI = new BinarySearchLayoutGUI(this, keyboard, layout);

        etosLayout = new ETOSLayout(keyboard);
        etosGUI = new ETOSLayoutGUI(this, keyboard, etosLayout);

        ViewGroup layoutWrapper = (ViewGroup) findViewById(R.id.layoutWrapper);
        // layoutWrapper.addView(layoutGUI.createGUI());


        //  layoutWrapper.addView(layoutGUI.buildGUI());
        layoutWrapper.addView(etosGUI.buildGUI()); // shows the ETOS gui.




        // Update the buffer view
        keyboard.addStateListener(new Keyboard.KeyboardListener() {
            @Override
            public void onOutputBufferChange(String oldBuffer, String newBuffer) {
                ((TextView) MainActivity.this.findViewById(R.id.currentBuffer)).setText(newBuffer);
            }
        });

        final InputInterface input = layout;

        // Use buttons to register the inputs
        ((Button) findViewById(R.id.buttonInput1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setInputState(InputType.INPUT1, true);
                input.setInputState(InputType.INPUT1, false);
            }
        });
        ((Button) findViewById(R.id.buttonInput2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Turn the input on and off for each click
                input.setInputState(InputType.INPUT2, true);
                input.setInputState(InputType.INPUT2, false);
            }
        });
        ((Button) findViewById(R.id.buttonInput3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Turn the input on and off for each click
                input.setInputState(InputType.INPUT3, true);
                input.setInputState(InputType.INPUT3, false);
            }
        });
        ((Button) findViewById(R.id.buttonInput4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setInputState(InputType.INPUT4, true);
                input.setInputState(InputType.INPUT4, false);
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
