package com.android.kirannote;

import com.android.kirannote.entities.db.DBHelper;
import com.android.kirannote.injection.component.ApplicationComponent;

import javax.inject.Inject;

/**
 * Created by kiran.kumar on 8/3/16.
 */

/**
 * This class is used for initializing the application level Objects
 */
public class AppInitializer {
    public void appInitProcess(ApplicationComponent component) {
        component.inject(this);

        //Initializing Database
        dbHelper.loadDB();
    }

    @Inject
    DBHelper dbHelper;

}
