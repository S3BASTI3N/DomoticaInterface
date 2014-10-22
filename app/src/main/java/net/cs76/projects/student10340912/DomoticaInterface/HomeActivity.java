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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.cs76.projects.student10340912.DomoticaInterface.DataManagement.DataManagerSingleton;
import net.cs76.projects.student10340912.DomoticaInterface.DataManagement.DatabaseHelper;
import net.cs76.projects.student10340912.DomoticaInterface.UpdateChecking.Alarm;
import net.cs76.projects.student10340912.DomoticaInterface.utils.DatabaseUpdatedInterface;
import net.cs76.projects.student10340912.DomoticaInterface.ConfigurableObjects.Group;
import net.cs76.projects.student10340912.DomoticaInterface.utils.ListViewAdapter;

import java.util.ArrayList;


public class HomeActivity extends ActionBarActivity implements DatabaseUpdatedInterface {

    private DataManagerSingleton dataManager_;
    private ArrayList<Group> groups_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        dataManager_ = DataManagerSingleton.getInstance( this );

        Alarm alarm = new Alarm();
        alarm.SetAlarm( this );

    }

    @Override
    public void onStart() {
        super.onStart();

        dataManager_.setActiveActivity( this );

        // Check if database is loaded.
        if( !dataManager_.isInitialised() ) {
            Toast.makeText( this, "Getting items from server", Toast.LENGTH_SHORT ).show();

        } else {
            onDatabaseUpdated();
        }

    }

    public void onDatabaseUpdated() {

        Log.d( "HomeActivity", "Database is ready, creating menu_group list");

        groups_ = dataManager_.getGroups();

        final ListView groupList = (ListView)findViewById(R.id.group_list);

        ListAdapter theListAdapter = new ListViewAdapter() {
            @Override
            public int getCount() {
                return groups_.size();
            }

            @Override
            public View getView(int position, View convertView, final ViewGroup parent) {

                RelativeLayout rowLayout = new RelativeLayout( parent.getContext() );

                TextView theTextView = new TextView( parent.getContext() );

                theTextView.setText(groups_.get(position).name_);
                theTextView.setTextColor(getResources().getColor(R.color.text_color));
                theTextView.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
                rowLayout.setId(groups_.get(position).id_);

                rowLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent jump = new Intent(getBaseContext(), GroupActivity.class);

                        jump.putExtra(dataManager_.MESSAGE_GROUP_ID, v.getId());

                        startActivity(jump);


                    }
                });

                rowLayout.addView( theTextView );

                return rowLayout;

            }
        };

        groupList.setAdapter(theListAdapter);

        int[] colors = { getResources().getColor( R.color.divider_color), 0 };
        groupList.setDivider(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors));
        groupList.setDividerHeight(getResources().getDimensionPixelSize(R.dimen.divider_height));

        groupList.invalidate();

        Log.d( "HomeActivity", "Displaying groups" );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_home, menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_edit_logic) {
            Intent intent = new Intent( this, LogicOverviewActivity.class );

            startActivity( intent );


            return true;
        }
        else if( id == R.id.action_delete_database ) {
            deleteDatabase(DatabaseHelper.DB_NAME );
            onStart();

        }
        else if( id == R.id.action_add_group ) {
            dataManager_.addGroup();
            onDatabaseUpdated();
        }
        return super.onOptionsItemSelected(item);
    }
}
