package com.nwoodthorpe.busstop;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Nathaniel on 4/22/2016.
 */
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        System.out.println("CALLED");
        return (new WidgetRemoteViewsFactory(this.getApplicationContext(), intent));
    }

}
