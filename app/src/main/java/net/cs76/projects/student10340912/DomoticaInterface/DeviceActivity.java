package net.cs76.projects.student10340912.DomoticaInterface;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.cs76.projects.student10340912.DomoticaInterface.DataManagement.DataManagerSingleton;
import net.cs76.projects.student10340912.DomoticaInterface.utils.DatabaseUpdatedInterface;
import net.cs76.projects.student10340912.DomoticaInterface.ConfigurableObjects.Device;
import net.cs76.projects.student10340912.DomoticaInterface.utils.ImageViewButton;
import net.cs76.projects.student10340912.DomoticaInterface.utils.ListViewAdapter;
import net.cs76.projects.student10340912.DomoticaInterface.utils.ParameterAlertDialog;


public class DeviceActivity extends ActionBarActivity implements DatabaseUpdatedInterface {

    private DataManagerSingleton dataManager_;
    private int deviceId_;

    private Device device_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deviceId_ = getIntent().getIntExtra( DataManagerSingleton.MESSAGE_DEVICE_ID, -1 );

        dataManager_ = DataManagerSingleton.getInstance( this );

    }

    @Override
    public void onStart() {
        super.onStart();
        dataManager_.setActiveActivity( this );

        device_ = dataManager_.getDeviceById( deviceId_ );

        getSupportActionBar().setTitle( device_.name_ );

        setContentView(R.layout.activity_device);

        ListView groupList = (ListView)findViewById(R.id.device_list);

        ListAdapter theListAdapter = new ListViewAdapter() {
            @Override
            public int getCount() {
                return dataManager_.getDevicePropertyNames( deviceId_ ).length;
            }

            public View getView(int position, View convertView, final ViewGroup parent) {

                TextView textView = new TextView( parent.getContext() );

                textView.setText(dataManager_.getDevicePropertyNames(deviceId_)[position]);
                textView.setTextColor(getResources().getColor(R.color.text_color));
                textView.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
                textView.setId(dataManager_.getDevicePropertyIds(deviceId_)[position]);

                if( textView.getText().equals("State")) {

                    ImageView button = new ImageViewButton( parent.getContext(), device_, dataManager_ );

                    RelativeLayout rowLayout = new RelativeLayout( parent.getContext() );

                    rowLayout.addView( textView );
                    rowLayout.addView( button );

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)button.getLayoutParams();

                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.height = 50;

                    return rowLayout;
                }

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String paramName = (String) ((TextView) v).getText();

                        if (paramName.equals("Name")) {

                            ParameterAlertDialog alert = new ParameterAlertDialog(parent.getContext(),
                                    dataManager_,
                                    device_.name_,
                                    device_,
                                    ParameterAlertDialog.TYPE_DEVICE_NAME);

                            alert.show();


                        } else if (paramName.equals("Color")) {
                            // TODO Call color picker
                            Toast.makeText(parent.getContext(), "Not implemented just yet", Toast.LENGTH_SHORT).show();

                        } else if (paramName.equals("Notification")) {

                            ParameterAlertDialog alert = new ParameterAlertDialog(parent.getContext(),
                                    dataManager_,
                                    device_.name_,
                                    device_,
                                    ParameterAlertDialog.TYPE_DEVICE_NOTIFICATION);

                            alert.show();

                        }

//

                    }
                });

                return textView;

            }
        };

        groupList.setAdapter(theListAdapter);

        int[] colors = { getResources().getColor( R.color.divider_color), 0 };
        groupList.setDivider(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors));
        groupList.setDividerHeight( getResources().getDimensionPixelSize( R.dimen.divider_height ) );

    }


    @Override
    public void onBackPressed() {

        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDatabaseUpdated() {
        onStart();
    }
}
