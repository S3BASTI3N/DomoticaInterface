package net.cs76.projects.student10340912.DomoticaInterface;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.cs76.projects.student10340912.DomoticaInterface.DataManagement.DataManagerSingleton;
import net.cs76.projects.student10340912.DomoticaInterface.utils.CallBackInterface;
import net.cs76.projects.student10340912.DomoticaInterface.utils.Device;
import net.cs76.projects.student10340912.DomoticaInterface.utils.ImageViewButton;
import net.cs76.projects.student10340912.DomoticaInterface.utils.ParameterAlertDialog;


public class DeviceActivity extends ActionBarActivity {

    private DataManagerSingleton dataManager_;
    private int deviceId_;

    private Device device_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deviceId_ = getIntent().getIntExtra( DataManagerSingleton.MESSAGE_DEVICE_ID, -1 );

        dataManager_ = DataManagerSingleton.getInstance( this );
        device_ = dataManager_.getDeviceById( deviceId_ );

        getSupportActionBar().setTitle( device_.name_ );

        setContentView(R.layout.activity_device);

        ListView group_list = (ListView)findViewById(R.id.device_list);

        ListAdapter theListAdapter = new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return true;
            }

            @Override
            public boolean isEnabled(int position) {
                return true;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return dataManager_.getDevicePropertyNames(deviceId_).length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, final ViewGroup parent) {



                TextView theView = new TextView( parent.getContext() );

                theView.setText( dataManager_.getDevicePropertyNames(deviceId_)[position]);
                theView.setTextColor( getResources().getColor(R.color.text_color) );
                theView.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
                theView.setId(dataManager_.getDevicePropertyIds(deviceId_)[position]);

                if( theView.getText().equals("State")) {

                    ImageView button = new ImageViewButton( parent.getContext(), device_, dataManager_ );

                    RelativeLayout rowLayout = new RelativeLayout( parent.getContext() );

                    rowLayout.addView( theView );
                    rowLayout.addView( button );

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)button.getLayoutParams();

                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.height = 50;

                    return rowLayout;
                }

                theView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String paramName = (String)((TextView)v).getText();

                        if( paramName.equals("Name")) {

                            ParameterAlertDialog alert = new ParameterAlertDialog(parent.getContext(), dataManager_, device_.name_, device_.id_);
                            alert.show();



                        } else if( paramName.equals("Color")) {
                            // TODO Call color picker
                            Toast.makeText( parent.getContext(), "Not implemented just yet", Toast.LENGTH_SHORT ).show();

                        } else if( paramName.equals("Notification")) {
                            // TODO Show on/off switch
                            Toast.makeText( parent.getContext(), "Not implemented just yet", Toast.LENGTH_SHORT ).show();

                        }

                        ((CallBackInterface)getParent()).onCallBack();

                    }
                });

                return theView;

            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };

        group_list.setAdapter( theListAdapter );

        int[] colors = { getResources().getColor( R.color.divider_color), 0 };
        group_list.setDivider( new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors));
        group_list.setDividerHeight( 1 );
    }

    @Override
    public void onBackPressed() {

        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
