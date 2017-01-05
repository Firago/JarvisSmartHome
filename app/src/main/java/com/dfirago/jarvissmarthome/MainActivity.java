package com.dfirago.jarvissmarthome;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dfirago.jarvissmarthome.fragments.MainFragment;
import com.dfirago.jarvissmarthome.fragments.NetworkSelectFragment;

public class MainActivity extends Activity {

    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new MainFragment(), MainFragment.TAG)
                    .commit();
        }
    }

    public void chooseNetwork(View v) {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.anim_slide_in_left,
                        R.animator.anim_slide_out_left,
                        R.animator.anim_slide_in_right,
                        R.animator.anim_slide_out_right)
                .replace(R.id.container, new NetworkSelectFragment(), NetworkSelectFragment.TAG)
                .addToBackStack(null)
                .commit();
    }
}
