package ch.digitalmeat.ld28.core;

import android.os.Bundle;
import android.view.WindowManager.LayoutParams;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Config gameConfig = new Config(800, 600);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = true;
        getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
        initialize(new ConcertSmugglers(gameConfig), cfg);
    }
}