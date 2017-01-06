package com.dfirago.jarvissmarthome.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.dfirago.jarvissmarthome.R;

/**
 * Created by dmfi on 05/01/2017.
 */

public class BaseActivity extends Activity {

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionIn();
    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);
        overridePendingTransitionIn();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransitionIn();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransitionIn();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionOut();
    }

    protected void overridePendingTransitionIn() {
        overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_slide_out_to_left);
    }

    protected void overridePendingTransitionOut() {
        overridePendingTransition(R.anim.anim_slide_in_from_left, R.anim.anim_slide_out_to_right);
    }

}