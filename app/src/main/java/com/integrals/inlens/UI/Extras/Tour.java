package com.integrals.inlens.UI.Extras;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.integrals.inlens.R;

public class Tour{

    public Boolean StartTour(Activity activity,String TargetString,String DescString,View TargetView)
    {
        TapTargetView.showFor(activity,
                TapTarget.forView(TargetView,TargetString,DescString)
                        .tintTarget(false)
                        .cancelable(false)
                        .outerCircleColor(R.color.colorPrimaryDark)
                        .textColor(R.color.white),
                new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        view.dismiss(true);
                    }
                });
        return true;
    }
}

