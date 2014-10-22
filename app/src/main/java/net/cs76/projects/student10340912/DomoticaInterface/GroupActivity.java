package net.cs76.projects.student10340912.DomoticaInterface;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.cs76.projects.student10340912.DomoticaInterface.DataManagement.DataManagerSingleton;
import net.cs76.projects.student10340912.DomoticaInterface.utils.DatabaseUpdatedInterface;
import net.cs76.projects.student10340912.DomoticaInterface.utils.Device;
import net.cs76.projects.student10340912.DomoticaInterface.utils.Group;
import net.cs76.projects.student10340912.DomoticaInterface.utils.ImageViewButton;
import net.cs76.projects.student10340912.DomoticaInterface.utils.ListViewAdapter;
import net.cs76.projects.student10340912.DomoticaInterface.utils.ParameterAlertDialog;

import java.util.ArrayList;

public class GroupActivity extends ActionBarActivity implements DatabaseUpdatedInterface {

    private DataManagerSingleton dataManager_ = null;
    public ArrayList<Device> devices_;

    private int groupId_;
    private Group group_;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupId_ = getIntent().getIntExtra( DataManagerSingleton.MESSAGE_GROUP_ID, -1 );

        dataManager_ = DataManagerSingleton.getInstance(this);

    }

    @Override
    public void onStart() {
        super.onStart();

        dataManager_.setActiveActivity( this );

        devices_ = dataManager_.getDevicesByGroupId(groupId_);
        group_ = dataManager_.getGroupById( groupId_ );

        Log.d("GroupActivity", "Getting devices for menu_group: " + groupId_);

        setContentView(R.layout.activity_group);

        getSupportActionBar().setTitle( group_.name_ );

        ListView group_list = (ListView)findViewById(R.id.device_list);

        ListAdapter theListAdapter = new ListViewAdapter() {
            @Override
            public int getCount() {
                return devices_.size();
            }

            @Override
            public View getView(int position, View convertView, final ViewGroup parent) {

                TextView theTextView = new TextView( parent.getContext() );

                theTextView.setText(devices_.get(position).name_);
                theTextView.setTextColor(getResources().getColor(R.color.text_color));
                theTextView.setTextSize(getResources().getDimension(R.dimen.normal_text_size));

                ImageView button = new ImageViewButton( parent.getContext(), devices_.get(position), dataManager_ );

                RelativeLayout rowLayout = new RelativeLayout( parent.getContext() );

                rowLayout.addView( theTextView );
                rowLayout.addView( button );

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)button.getLayoutParams();

                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.height = 50;

                rowLayout.setId(devices_.get(position).id_);
                rowLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent jump = new Intent(getBaseContext(), DeviceActivity.class);

                        jump.putExtra(dataManager_.MESSAGE_DEVICE_ID, v.getId());

                        startActivity( jump );

                    }
                });

                return rowLayout;

            }
        };

        group_list.setAdapter( theListAdapter );

        int[] colors = { getResources().getColor( R.color.divider_color), 0 };
        group_list.setDivider( new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors));
        group_list.setDividerHeight( getResources().getDimensionPixelSize( R.dimen.divider_height ) );
    }

    @Override
    public void onBackPressed() {

        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_group, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_group_name ) {
            ParameterAlertDialog dialog = new ParameterAlertDialog( this, dataManager_, group_.name_, group_, ParameterAlertDialog.TYPE_GROUP_NAME );
            dialog.show();
            return true;
        }
        else if (id == R.id.action_group_add ) {
            ParameterAlertDialog dialog = new ParameterAlertDialog( this, dataManager_, group_.name_, group_, ParameterAlertDialog.TYPE_GROUP_ADD );
            dialog.show();
            return true;
        }
        else if (id == R.id.action_group_remove ) {
            ParameterAlertDialog dialog = new ParameterAlertDialog( this, dataManager_, group_.name_, group_, ParameterAlertDialog.TYPE_GROUP_REMOVE );
            dialog.show();
            return true;
        }
        else if( id == R.id.action_group_remove_group ) {
            dataManager_.removeGroupById( groupId_ );
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onDatabaseUpdated() {
        devices_ = dataManager_.getDevicesByGroupId( groupId_ );
        group_ = dataManager_.getGroupById( groupId_ );
        ListView device_list = (ListView)findViewById( R.id.device_list);
        device_list.invalidate();

        getSupportActionBar().setTitle( group_.name_ );

        onStart();

    }
}
