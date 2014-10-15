package net.cs76.projects.student10340912.DomoticaInterface;

import android.content.Intent;
import android.database.DataSetObserver;
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
import net.cs76.projects.student10340912.DomoticaInterface.utils.CallBackInterface;
import net.cs76.projects.student10340912.DomoticaInterface.utils.Device;
import net.cs76.projects.student10340912.DomoticaInterface.utils.Group;
import net.cs76.projects.student10340912.DomoticaInterface.utils.ImageViewButton;

import java.util.ArrayList;

public class GroupActivity extends ActionBarActivity implements CallBackInterface {

    private DataManagerSingleton dataManager_ = null;
    public ArrayList<Device> devices_;

    private int groupId_;
    private Group group;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupId_ = getIntent().getIntExtra( DataManagerSingleton.MESSAGE_GROUP_ID, -1 );

        dataManager_ = DataManagerSingleton.getInstance(this);
        devices_ = dataManager_.getDevicesByGroupId(groupId_);
        group = dataManager_.getGroupById( groupId_ );

        Log.d( "GroupActivity", "Getting devices for group: " + groupId_ );

        setContentView(R.layout.activity_group);

        getSupportActionBar().setTitle( group.name_ );

        ListView group_list = (ListView)findViewById(R.id.device_list);

        ListAdapter theListAdapter = new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return true;
            }

            @Override
            public boolean isEnabled(int position) {
                return position < devices_.size();
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return devices_.size();
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

                TextView theTextView = new TextView( parent.getContext() );

                theTextView.setText(devices_.get(position).name_);
                theTextView.setTextColor(getResources().getColor(R.color.text_color));
                theTextView.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
                theTextView.setId(devices_.get(position).id_);

                theTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent jump = new Intent(getBaseContext(), DeviceActivity.class);

                        jump.putExtra(dataManager_.MESSAGE_DEVICE_ID, v.getId());

                    }
                });

                ImageView button = new ImageViewButton( parent.getContext(), devices_.get(position), dataManager_ );

                RelativeLayout rowLayout = new RelativeLayout( parent.getContext() );

                rowLayout.addView( theTextView );
                rowLayout.addView( button );

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)button.getLayoutParams();

                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.height = 50;

                return rowLayout;

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
        getMenuInflater().inflate(R.menu.group, menu);
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

    public void onCallBack() {
        devices_ = dataManager_.getDevicesByGroupId( groupId_ );
        ListView device_list = (ListView)findViewById( R.id.device_list);
        device_list.invalidate();
    }
}
