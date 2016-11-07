package com.example.banach.gottawatchit;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Artur on 19-Aug-16.
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
