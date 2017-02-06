package com.github.i72jilej.barcodegrader;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
    implements  LoadCsvFragment.OnFragmentInteractionListener,
                GradeFragment.OnFragmentInteractionListener,
                CreateCodesFragment.OnFragmentInteractionListener{
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    LoadCsvFragment loadCsvFragment = null;;
    FragmentManager fragmentManager = getSupportFragmentManager();

    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    public String label_filename_text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         **/

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Insert the fragment by replacing any existing fragment
        Class fragmentClass = LoadCsvFragment.class;
        //LoadCsvFragment loadCsvFragment = null;;
        //FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;

        if(loadCsvFragment == null)
            loadCsvFragment = new LoadCsvFragment();

        fragmentManager.beginTransaction().replace(R.id.flContent, loadCsvFragment).commit();

        /*
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment).commit();
        */

        contextOfApplication = getApplicationContext();


    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
         **/

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        CreateCodesFragment createCodesFragment = null;
        switch(menuItem.getItemId()){
            case R.id.nav_fragment_loadCsv:
                if(loadCsvFragment == null)
                    loadCsvFragment = new LoadCsvFragment();

                setVisual(menuItem);
                fragmentManager.beginTransaction().replace(R.id.flContent, loadCsvFragment).commit();
                break;
            case R.id.nav_fragment_grade:
                if(GlobalVars.getInstance().getCsvArray().isEmpty()){
                    //nvDrawer.getMenu().findItem(R.id.nav_fragment_loadCsv).setChecked(true);
                    //FIXME setChecked() method is bugged?
                    menuItem.setChecked(false);
                    //nvDrawer.getMenu().findItem(R.id.nav_fragment_loadCsv).setChecked(true);
                    nvDrawer.setCheckedItem(R.id.nav_fragment_loadCsv);
                    //setVisual(nvDrawer.getMenu().findItem(R.id.nav_fragment_loadCsv));

                    Toast.makeText(getApplicationContext(), R.string.alert_noFileLoad, Toast.LENGTH_LONG).show();
                }
                else{
                    if(GlobalVars.getInstance().getGradeFragment() == null) {
                        GlobalVars.getInstance().setGradeFragment(new GradeFragment());
                    }
                    setVisual(menuItem);
                    fragmentManager.beginTransaction().replace(R.id.flContent, GlobalVars.getInstance().getGradeFragment()).commit();
                }
                break;
            case R.id.nav_fragment_createCodes:
                if(GlobalVars.getInstance().getCsvArray().isEmpty()){
                    //FIXME setChecked() method is bugged?
                    menuItem.setChecked(false);
                    nvDrawer.setCheckedItem(R.id.nav_fragment_createCodes);

                    Toast.makeText(getApplicationContext(), R.string.alert_noFileLoad, Toast.LENGTH_LONG).show();
                }
                else {
                    if (createCodesFragment == null)
                        createCodesFragment = new CreateCodesFragment();

                    setVisual(menuItem);
                    fragmentManager.beginTransaction().replace(R.id.flContent, createCodesFragment).commit();
                }
                break;
            default:
                if(loadCsvFragment == null)
                    loadCsvFragment = new LoadCsvFragment();

                setVisual(menuItem);
                fragmentManager.beginTransaction().replace(R.id.flContent, loadCsvFragment).commit();
        }

        // Highlight the selected item has been done by NavigationView
        //menuItem.setChecked(true);
        // Set action bar title
        //setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    public Context contextOfApplication;
    public Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    public void setVisual(MenuItem item){
        item.setChecked(true);
        setTitle(item.getTitle());
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.label_exit_title)
                .setMessage(R.string.label_exit_text)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}



