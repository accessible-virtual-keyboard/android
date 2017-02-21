package no.ntnu.stud.avikeyb;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import no.ntnu.stud.avikeyb.backend.InputInterface;
import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Layout;
import no.ntnu.stud.avikeyb.backend.OutputDevice;
import no.ntnu.stud.avikeyb.backend.Suggestions;
import no.ntnu.stud.avikeyb.backend.core.CoreKeyboard;
import no.ntnu.stud.avikeyb.backend.layouts.AdaptiveLayout;
import no.ntnu.stud.avikeyb.backend.layouts.BinarySearchLayout;
import no.ntnu.stud.avikeyb.backend.layouts.ETOSLayout;
import no.ntnu.stud.avikeyb.backend.layouts.MobileLayout;
import no.ntnu.stud.avikeyb.backend.layouts.SimpleExampleLayout;
import no.ntnu.stud.avikeyb.gui.AdaptiveLayoutGUI;
import no.ntnu.stud.avikeyb.gui.BinarySearchLayoutGUI;
import no.ntnu.stud.avikeyb.gui.ETOSLayoutGUI;
import no.ntnu.stud.avikeyb.gui.LayoutGUI;
import no.ntnu.stud.avikeyb.gui.MobileLayoutGUI;
import no.ntnu.stud.avikeyb.gui.SimpleExampleLayoutGUI;
import no.ntnu.stud.avikeyb.gui.core.SuggestionsAndroid;
import no.ntnu.stud.avikeyb.gui.core.DummyDictionary;

public class MainActivity extends AppCompatActivity {

    private ViewGroup layoutWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Keyboard keyboard = new CoreKeyboard();
        keyboard.addOutputDevice(new ToastOutput());

        final TabLayout layoutTabs = (TabLayout) findViewById(R.id.layoutTabs);
        layoutTabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        layoutTabs.addTab(layoutTabs.newTab().setText("Simple"));
        layoutTabs.addTab(layoutTabs.newTab().setText("ETOS"));
        layoutTabs.addTab(layoutTabs.newTab().setText("BINS"));
        layoutTabs.addTab(layoutTabs.newTab().setText("MOB"));
        layoutTabs.addTab(layoutTabs.newTab().setText("ADAPTIVE"));

        layoutWrapper = (ViewGroup) findViewById(R.id.layoutWrapper);


        TabLayout.OnTabSelectedListener tabSwitcher = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 1: {
                        ETOSLayout l = new ETOSLayout(keyboard);
                        switchLayout(l, new ETOSLayoutGUI(MainActivity.this, keyboard, l));
                        break;
                    }
                    case 2: {
                        BinarySearchLayout l = new BinarySearchLayout(keyboard);
                        switchLayout(l, new BinarySearchLayoutGUI(MainActivity.this, keyboard, l));
                        break;
                    }
                    case 3: {
                        MobileLayout l = new MobileLayout(keyboard);
                        switchLayout(l, new MobileLayoutGUI(MainActivity.this, keyboard, l));
                        break;
                    }
                    case 4: {
                        AdaptiveLayout l = new AdaptiveLayout(keyboard);
                        switchLayout(l, new AdaptiveLayoutGUI(MainActivity.this, keyboard, l));
                        break;
                    }
                    default: { // 0
                        SimpleExampleLayout l = new SimpleExampleLayout(keyboard);
                        switchLayout(l, new SimpleExampleLayoutGUI(MainActivity.this, keyboard, l));
                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        };


        // Update the buffer view
        keyboard.addStateListener(new Keyboard.KeyboardListener() {
            @Override
            public void onOutputBufferChange(String oldBuffer, String newBuffer) {
                ((TextView) MainActivity.this.findViewById(R.id.currentBuffer)).setText(newBuffer);
            }
        });


        Suggestions suggestions = new SuggestionsAndroid(keyboard, new DummyDictionary());

        suggestions.addListener(new Suggestions.Listener() {
            @Override
            public void onSuggestions(List<String> suggestions) {
                System.out.print("Suggestions: ");
                System.out.println(suggestions);
            }
        });



        layoutTabs.addOnTabSelectedListener(tabSwitcher);

        // Trigger the creation of the layout in the first tab
        tabSwitcher.onTabSelected(layoutTabs.getTabAt(0));
    } // end of on create



    @Override // test
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
        return true;
    }

    @Override // test
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:
                //todo
                break;
            case R.id.action_save:
                //todo
                break;
            case R.id.action_option3:
                //todo
                break;
            case R.id.action_option4:
                //todo
                break;
            case R.id.action_option5:
                //todo
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void switchLayout(Layout layout, LayoutGUI layoutGui) {
        layoutWrapper.removeAllViews();
        layoutWrapper.addView(layoutGui.createGUI());
        setupInputButtons(layout);
    }

    private void setupInputButtons(final InputInterface input) {
        findViewById(R.id.buttonInput1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setInputState(InputType.INPUT1, true);
                input.setInputState(InputType.INPUT1, false);
            }
        });
        findViewById(R.id.buttonInput2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Turn the input on and off for each click
                input.setInputState(InputType.INPUT2, true);
                input.setInputState(InputType.INPUT2, false);
            }
        });
        findViewById(R.id.buttonInput3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Turn the input on and off for each click
                input.setInputState(InputType.INPUT3, true);
                input.setInputState(InputType.INPUT3, false);
            }
        });
        findViewById(R.id.buttonInput4).setOnClickListener(new View.OnClickListener() {
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
