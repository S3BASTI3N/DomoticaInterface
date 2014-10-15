package net.cs76.projects.student10340912.DomoticaInterface.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import net.cs76.projects.student10340912.DomoticaInterface.DataManagement.DataManagerSingleton;
import net.cs76.projects.student10340912.DomoticaInterface.R;

/**
 * Created by sebastien on 15-10-14.
 */
public class ImageViewButton extends ImageView {

    private Context context_;
    private Device device_;
    private DataManagerSingleton dataManager_ = null;

    public ImageViewButton(Context context, Device device, DataManagerSingleton dataManager ) {
        super(context);

        context_ = context;
        device_ = device;
        dataManager_ = dataManager;

        setId( device.id_ );

        if( device.state_ == DataManagerSingleton.STATE_OFF ) {
            setImageResource( R.drawable.off_button );
        } else {
            setImageResource( R.drawable.on_button );
        }

        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switchState();

            }
        });
    }

    public void switchState() {

        if (device_.state_ == DataManagerSingleton.STATE_ON) {
            setImageResource(R.drawable.off_button);
            device_.state_ = DataManagerSingleton.STATE_OFF;
            dataManager_.setProperty( device_.id_, "state", DataManagerSingleton.STATE_OFF, Device.class );
        } else {
            setImageResource(R.drawable.on_button);
            device_.state_ = DataManagerSingleton.STATE_ON;
            dataManager_.setProperty( device_.id_, "state", DataManagerSingleton.STATE_ON, Device.class );
        }

        invalidate();

    }
}
