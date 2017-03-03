package no.ntnu.stud.avikeyb;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import no.ntnu.stud.avikeyb.backend.InputInterface;
import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Layout;
import no.ntnu.stud.avikeyb.backend.OutputDevice;
import no.ntnu.stud.avikeyb.backend.Suggestions;
import no.ntnu.stud.avikeyb.backend.core.CoreKeyboard;
import no.ntnu.stud.avikeyb.backend.core.WordUpdater;
import no.ntnu.stud.avikeyb.backend.dictionary.DictionaryEntry;
import no.ntnu.stud.avikeyb.backend.dictionary.DictionaryHandler;
import no.ntnu.stud.avikeyb.backend.dictionary.InMemoryDictionary;
import no.ntnu.stud.avikeyb.backend.dictionary.LinearEliminationDictionaryHandler;
import no.ntnu.stud.avikeyb.backend.dictionary.ResourceHandler;
import no.ntnu.stud.avikeyb.backend.layouts.AdaptiveLayout;
import no.ntnu.stud.avikeyb.backend.layouts.BinarySearchLayout;
import no.ntnu.stud.avikeyb.backend.layouts.ETOSLayout;
import no.ntnu.stud.avikeyb.backend.layouts.MobileDictionaryLayout;
import no.ntnu.stud.avikeyb.gui.AdaptiveLayoutGUI;
import no.ntnu.stud.avikeyb.gui.BinarySearchLayoutGUI;
import no.ntnu.stud.avikeyb.gui.ETOSLayoutGUI;
import no.ntnu.stud.avikeyb.gui.LayoutGUI;
import no.ntnu.stud.avikeyb.gui.MobileLayoutGUI;
import no.ntnu.stud.avikeyb.backend.dictionary.AndroidResourceLoader;
import no.ntnu.stud.avikeyb.gui.core.SuggestionsAndroid;

public class MainActivity extends AppCompatActivity{

    private ViewGroup layoutWrapper;
    private final DictionaryHandler dictionaryHandler = new DictionaryHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Keyboard keyboard = new CoreKeyboard();
        keyboard.addOutputDevice(new ToastOutput());

        final TabLayout layoutTabs = (TabLayout) findViewById(R.id.layoutTabs);
        layoutTabs.setTabMode(TabLayout.MODE_SCROLLABLE);

//        layoutTabs.addTab(layoutTabs.newTab().setText("Simple"));
        layoutTabs.addTab(layoutTabs.newTab().setText("ETOS"));
        layoutTabs.addTab(layoutTabs.newTab().setText("Adaptive"));
        layoutTabs.addTab(layoutTabs.newTab().setText("Mobile"));
        layoutTabs.addTab(layoutTabs.newTab().setText("Binary"));

        layoutWrapper = (ViewGroup) findViewById(R.id.layoutWrapper);

        final LinearEliminationDictionaryHandler mobileDictionary = new LinearEliminationDictionaryHandler();

        // Asynchronously load the dictionary enties from a file and set the entries to the
        // two in memory dictionaries.
        loadDictionaryFromFile(Arrays.asList(dictionaryHandler, mobileDictionary), R.raw.dictionary);

        Suggestions suggestions = new SuggestionsAndroid(keyboard, dictionaryHandler);
        final ETOSLayout etosLayout = new ETOSLayout(keyboard, suggestions);
        final BinarySearchLayout binLayout = new BinarySearchLayout(keyboard, suggestions);
        final AdaptiveLayout adaptiveLayout = new AdaptiveLayout(keyboard, suggestions);


        TabLayout.OnTabSelectedListener tabSwitcher = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 1: {
                        switchLayout(adaptiveLayout, new AdaptiveLayoutGUI(MainActivity.this, keyboard, adaptiveLayout));
                        break;
                    }
                    case 2: {
                        MobileDictionaryLayout l = new MobileDictionaryLayout(keyboard, mobileDictionary);
                        MobileLayoutGUI mobileGUI = new MobileLayoutGUI(MainActivity.this, keyboard, l, R.layout.layout_mobile_dictionary, R.layout.layout_mobile);
                        switchLayout(l, mobileGUI);
                        mobileGUI.firstUpdate(); // We need to update gui right after it has been set to mark first mobile layout row.
                        break;
                    }
                    case 3: {
                        switchLayout(binLayout, new BinarySearchLayoutGUI(MainActivity.this, keyboard, binLayout));
                        break;
                    }
                    default: { // 0
                        switchLayout(etosLayout, new ETOSLayoutGUI(MainActivity.this, keyboard, etosLayout));
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


        // Update user word usage count
        keyboard.addOutputDevice(new WordUpdater(dictionaryHandler));


        layoutTabs.addOnTabSelectedListener(tabSwitcher);

        // Trigger the creation of the layout in the first tab
        tabSwitcher.onTabSelected(layoutTabs.getTabAt(0));
    } // end of on create

    @Override
    protected void onPause() {
        super.onPause();

        // Set save location.
        String folderPath = this.getFilesDir().getPath();
        String fileName = "dictionary.txt";
        String filePath = folderPath + "/" + fileName;
//        System.out.println(filePath);

        // Store the dictionary to file.
        try {
            ResourceHandler.storeDictionaryToFile(dictionaryHandler.getDictionary(), filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchLayout(Layout layout, LayoutGUI layoutGui) {
        layoutGui.setLayoutContainer(layoutWrapper);
        layoutGui.onLayoutActivated();
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


    // Fill the in memory dictionaryHandler from a file
    private void loadDictionaryFromFile(final List<InMemoryDictionary> dictionaries, final int resourceId) {
        new AsyncTask<Void, Void, List<DictionaryEntry>>() {
            @Override
            protected List<DictionaryEntry> doInBackground(Void... voids) {
                return AndroidResourceLoader.loadDictionaryFromResource(MainActivity.this, resourceId);
            }

            @Override
            protected void onPostExecute(List<DictionaryEntry> dictionaryEntries) {
                for (InMemoryDictionary dict : dictionaries) {
                    dict.setDictionary(dictionaryEntries);
                }
            }
        }.execute();
    }

    // Shows the keyboard output in a toast message
    private class ToastOutput implements OutputDevice {
        @Override
        public void sendOutput(String output) {
            Toast.makeText(MainActivity.this, output, Toast.LENGTH_SHORT).show();
        }
    }

}
