package nz.co.alan.floatingapplication;

import android.app.Application;
import android.content.Context;

/**
 * Created by Aland on 17/02/2017.
 */

public class FloatingApplication extends Application {

    private FloatingFaceBubbleService floatingFaceBubbleService;

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public void setFloatingFaceBubbleService(FloatingFaceBubbleService floatingFaceBubbleService){
        this.floatingFaceBubbleService = floatingFaceBubbleService;
    }

    public FloatingFaceBubbleService getFloatingFaceBubbleService() {
        return this.floatingFaceBubbleService;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }
}
