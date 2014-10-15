package net.cs76.projects.student10340912.DomoticaInterface;


import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
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
import android.widget.Toast;

import net.cs76.projects.student10340912.DomoticaInterface.DataManagement.DataManagerSingleton;
import net.cs76.projects.student10340912.DomoticaInterface.DataManagement.DatabaseHelper;
import net.cs76.projects.student10340912.DomoticaInterface.utils.CallBackInterface;
import net.cs76.projects.student10340912.DomoticaInterface.utils.Group;

import java.util.ArrayList;


public class HomeActivity extends ActionBarActivity implements CallBackInterface {

    private DataManagerSingleton dataManager_;
    private ArrayList<Group> groups_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO remove delete statement
        deleteDatabase(DatabaseHelper.DB_NAME );

        setContentView(R.layout.activity_home);

        dataManager_ = DataManagerSingleton.getInstance( this );


    }

    @Override
    public void onStart() {
        super.onStart();
        // TODO Check if database is loaded

        if( !dataManager_.isInitialised() ) {
            Toast.makeText( this, "Getting items from server", Toast.LENGTH_SHORT ).show();

        } else {
            onCallBack();
        }

    }

    public void onCallBack() {

        Log.d( "HomeActivity", "Database is ready, creating group list");

        groups_ = dataManager_.getGroups();

        final ListView group_list = (ListView)findViewById(R.id.group_list);


        ListAdapter theListAdapter = new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return true;
            }

            @Override
            public boolean isEnabled(int position) {
                return position < groups_.size();
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return groups_.size();
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

                RelativeLayout rowLayout = new RelativeLayout( parent.getContext() );

                TextView theTextView = new TextView( parent.getContext() );

                theTextView.setText(groups_.get(position).name_);
                theTextView.setTextColor(getResources().getColor(R.color.text_color));
                theTextView.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
                theTextView.setId(groups_.get(position).id_);

                theTextView.setOnClickListener(new View.OnClickListener() {
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

        // TODO figure out a way to refresh the list view

        group_list.invalidate();

        Log.d( "HomeActivity", "Displaying groups" );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.landing, menu);
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
