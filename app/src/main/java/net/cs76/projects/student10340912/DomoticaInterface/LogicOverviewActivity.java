package net.cs76.projects.student10340912.DomoticaInterface;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.cs76.projects.student10340912.DomoticaInterface.DataManagement.DataManagerSingleton;
import net.cs76.projects.student10340912.DomoticaInterface.utils.DatabaseUpdatedInterface;
import net.cs76.projects.student10340912.DomoticaInterface.utils.ListViewAdapter;
import net.cs76.projects.student10340912.DomoticaInterface.utils.LogicRule;

import java.util.ArrayList;


public class LogicOverviewActivity extends ActionBarActivity implements DatabaseUpdatedInterface {

    private DataManagerSingleton dataManager_;
    private ArrayList<LogicRule> logicRules_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logic_overview);

        dataManager_ = DataManagerSingleton.getInstance( this );

    }

    @Override
    public void onStart() {
        super.onStart();

        dataManager_.setActiveActivity( this );
        getSupportActionBar().setTitle( "Logic Rules" );

        logicRules_ = dataManager_.getLogicRules();

        ListView listView = (ListView)findViewById( R.id.logic_rules_list);
        ListViewAdapter listAdapter = new ListViewAdapter() {
            @Override
            public int getCount() {
                return logicRules_.size();
            }

            @Override
            public View getView(int position, View convertView, final ViewGroup parent) {
                LogicRule logicRule = logicRules_.get(position);

                RelativeLayout relativeLayout = new RelativeLayout( parent.getContext() );

                ImageView conditionStateImage = new ImageView( parent.getContext() );
                ImageView actionStateImage = new ImageView( parent.getContext() );

                if( logicRule.condition_.state_ == LogicRule.STATE_ON ) conditionStateImage.setImageResource( R.drawable.state_on );
                else conditionStateImage.setImageResource( R.drawable.state_off );


                if( logicRule.action_.state_ == LogicRule.STATE_ON ) actionStateImage.setImageResource( R.drawable.state_on );
                else actionStateImage.setImageResource( R.drawable.state_off );


                TextView conditionText = new TextView( parent.getContext() );
                TextView actionText = new TextView( parent.getContext() );
                TextView arrowText = new TextView( parent.getContext() );

                conditionText.setTextColor( getResources().getColor( R.color.text_color ));
                actionText.setTextColor( getResources().getColor( R.color.text_color ));
                arrowText.setTextColor( getResources().getColor( R.color.text_color ));

                conditionText.setTextSize( getResources().getDimension( R.dimen.normal_text_size ));
                actionText.setTextSize( getResources().getDimension( R.dimen.normal_text_size ));
                arrowText.setTextSize( getResources().getDimension( R.dimen.normal_text_size ));

                conditionText.setText( logicRule.condition_.device_.name_ );
                actionText.setText( logicRule.action_.device_.name_ );
                arrowText.setText( "→" );

                relativeLayout.addView( conditionStateImage );
                relativeLayout.addView( conditionText );
                relativeLayout.addView( arrowText );
                relativeLayout.addView( actionStateImage );
                relativeLayout.addView( actionText );

                conditionStateImage.setId( R.id.condition_image_id );
                conditionText.setId( R.id.condition_text_id );
                arrowText.setId( R.id.arrow_text_id);
                actionStateImage.setId( R.id.action_image_id );
                actionText.setId( R.id.action_text_id );



                RelativeLayout.LayoutParams params;

                params = (RelativeLayout.LayoutParams)conditionStateImage.getLayoutParams();
                params.width  = 2*getResources().getDimensionPixelSize( R.dimen.normal_text_size );
                params.height = getResources().getDimensionPixelSize( R.dimen.normal_text_size );
                params.addRule( RelativeLayout.CENTER_VERTICAL );

                params = (RelativeLayout.LayoutParams)conditionText.getLayoutParams();
                params.addRule( RelativeLayout.RIGHT_OF, conditionStateImage.getId() );

                params = (RelativeLayout.LayoutParams)arrowText.getLayoutParams();
                params.addRule( RelativeLayout.CENTER_HORIZONTAL );

                params = (RelativeLayout.LayoutParams)actionStateImage.getLayoutParams();
                params.addRule( RelativeLayout.RIGHT_OF, arrowText.getId() );
                params.width  = 2*getResources().getDimensionPixelSize( R.dimen.normal_text_size );
                params.height = getResources().getDimensionPixelSize( R.dimen.normal_text_size );
                params.addRule( RelativeLayout.CENTER_VERTICAL );

                params = (RelativeLayout.LayoutParams)actionText.getLayoutParams();
                params.addRule( RelativeLayout.RIGHT_OF, actionStateImage.getId() );

                relativeLayout.setTag( logicRule.id_ );
                
                relativeLayout.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent jump = new Intent( parent.getContext(), LogicRuleActivity.class );
                        jump.putExtra( DataManagerSingleton.MESSAGE_LOGIC_RULE_ID, (Integer)v.getTag());

                        startActivity( jump );
                    }
                });

                return relativeLayout;
            }
        };

        int[] colors = { getResources().getColor( R.color.divider_color), 0 };
        listView.setDivider( new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors));
        listView.setDividerHeight(getResources().getDimensionPixelSize(R.dimen.divider_height));

        listView.setAdapter( listAdapter );

    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_logic_overview, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_overview_add ) {
            dataManager_.addLogicRule();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDatabaseUpdated() {
        onStart();
    }
}
