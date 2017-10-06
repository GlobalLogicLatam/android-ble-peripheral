package com.globallogic.workshopble;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by Javier Bianciotto on 10/04/2017.
 * GlobalLogic | javier.bianciotto@globallogic.com
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(this, AdvertiseService.class));
    }
}
