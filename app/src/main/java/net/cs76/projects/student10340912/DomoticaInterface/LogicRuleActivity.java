package net.cs76.projects.student10340912.DomoticaInterface;

import android.content.Intent;
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
import net.cs76.projects.student10340912.DomoticaInterface.utils.ParameterAlertDialog;


public class LogicRuleActivity extends ActionBarActivity implements DatabaseUpdatedInterface {

    private int logicRuleId_;
    private DataManagerSingleton dataManager_;

    private LogicRule logicRule_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logic_rule);
        dataManager_ = DataManagerSingleton.getInstance( this );

        Intent intent = getIntent();
        logicRuleId_ = intent.getIntExtra(DataManagerSingleton.MESSAGE_LOGIC_RULE_ID, -1 );

    }

    @Override
    public void onStart() {
        super.onStart();

        dataManager_.setActiveActivity( this );

        logicRule_ = dataManager_.getLogicRuleById( logicRuleId_ );
        getSupportActionBar().setTitle( "Logic Rule" );

        ListView conditions = (ListView)findViewById(R.id.logic_conditions );

        ListViewAdapter conditionAdapter = new ListViewAdapter() {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                RelativeLayout relativeLayout = new RelativeLayout( parent.getContext() );

                ImageView conditionStateImage = new ImageView( parent.getContext() );

                if( logicRule_.condition_.state_ == LogicRule.STATE_ON ) conditionStateImage.setImageResource( R.drawable.state_on );
                else conditionStateImage.setImageResource( R.drawable.state_off );

                TextView conditionText = new TextView( parent.getContext() );

                conditionText.setTextColor( getResources().getColor( R.color.text_color ));

                conditionText.setTextSize( getResources().getDimension( R.dimen.normal_text_size ));

                conditionText.setText( logicRule_.condition_.device_.name_ );

                relativeLayout.addView( conditionStateImage );
                relativeLayout.addView( conditionText );

                conditionStateImage.setId( R.id.condition_image_id );
                conditionText.setId( R.id.condition_text_id );

                RelativeLayout.LayoutParams params;

                params = (RelativeLayout.LayoutParams)conditionStateImage.getLayoutParams();
                params.width  = 2*getResources().getDimensionPixelSize( R.dimen.normal_text_size );
                params.height = getResources().getDimensionPixelSize( R.dimen.normal_text_size );
                params.addRule( RelativeLayout.CENTER_VERTICAL );

                params = (RelativeLayout.LayoutParams)conditionText.getLayoutParams();
                params.addRule( RelativeLayout.RIGHT_OF, conditionStateImage.getId() );

                return relativeLayout;
            }
        };

        conditions.setAdapter( conditionAdapter );


        ListView actions = (ListView)findViewById(R.id.logic_actions );

        ListViewAdapter actionsAdapter = new ListViewAdapter() {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                RelativeLayout relativeLayout = new RelativeLayout( parent.getContext() );

                ImageView actionStateImage = new ImageView( parent.getContext() );

                if( logicRule_.action_.state_ == LogicRule.STATE_ON ) actionStateImage.setImageResource( R.drawable.state_on );
                else actionStateImage.setImageResource( R.drawable.state_off );

                TextView actionText = new TextView( parent.getContext() );

                actionText.setTextColor(getResources().getColor(R.color.text_color));

                actionText.setTextSize(getResources().getDimension(R.dimen.normal_text_size));

                actionText.setText(logicRule_.action_.device_.name_);

                relativeLayout.addView(actionStateImage);
                relativeLayout.addView(actionText);

                actionStateImage.setId(R.id.action_image_id);
                actionText.setId(R.id.action_text_id );

                RelativeLayout.LayoutParams params;

                params = (RelativeLayout.LayoutParams)actionStateImage.getLayoutParams();
                params.width  = 2*getResources().getDimensionPixelSize( R.dimen.normal_text_size );
                params.height = getResources().getDimensionPixelSize( R.dimen.normal_text_size );
                params.addRule( RelativeLayout.CENTER_VERTICAL );

                params = (RelativeLayout.LayoutParams)actionText.getLayoutParams();
                params.addRule( RelativeLayout.RIGHT_OF, actionStateImage.getId() );

                return relativeLayout;
            }
        };

        actions.setAdapter(actionsAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_logic_rule, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_logic_change_con ) {
            ParameterAlertDialog dialog = new ParameterAlertDialog(this, dataManager_, null, logicRule_.condition_, ParameterAlertDialog.TYPE_LOGIC_RULE_DEVICE );
            dialog.show();
            return true;
        }
        else if (id == R.id.action_logic_change_act ) {
            ParameterAlertDialog dialog = new ParameterAlertDialog(this, dataManager_, null, logicRule_.action_, ParameterAlertDialog.TYPE_LOGIC_RULE_DEVICE );
            dialog.show();
            return true;
        }
        else if( id == R.id.action_logic_change_act_state ) {
            ParameterAlertDialog dialog = new ParameterAlertDialog(this, dataManager_, null, logicRule_.action_, ParameterAlertDialog.TYPE_LOGIC_RULE_STATES );
            dialog.show();
            return true;

        }
        else if( id == R.id.action_logic_change_con_state ) {
            ParameterAlertDialog dialog = new ParameterAlertDialog(this, dataManager_, null, logicRule_.condition_, ParameterAlertDialog.TYPE_LOGIC_RULE_STATES );
            dialog.show();
            return true;

        }
        else if (id == R.id.action_logic_remove ) {
            dataManager_.removeLogicRuleById( logicRuleId_ );
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDatabaseUpdated() {
        onStart();
    }
}
