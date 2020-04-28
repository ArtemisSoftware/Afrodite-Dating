package com.artemissoftware.afroditedating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.artemissoftware.afroditedating.models.Message;
import com.artemissoftware.afroditedating.models.User;
import com.artemissoftware.afroditedating.util.PreferenceKeys;
import com.artemissoftware.afroditedating.util.Resources;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity implements IMainActivity,
        BottomNavigationViewEx.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private BottomNavigationViewEx mBottomNavigationViewEx;
    private ImageView mHeaderImage;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationViewEx = findViewById(R.id.bottom_nav_view);
        mBottomNavigationViewEx.setOnNavigationItemSelectedListener(this);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        mHeaderImage = headerView.findViewById(R.id.header_image);


        isFirstLogin();
        initBottomNavigationView();
        setNavigationViewListener();
        setHeaderImage();
        init();
    }


    private void initBottomNavigationView(){
        Log.d(TAG, "initBottomNavigationView: initializing the bottom navigation view");
        mBottomNavigationViewEx.enableAnimation(false);

    }


    private void setHeaderImage(){
        Log.d(TAG, "setHeaderImage: setting image");

        Glide.with(this).load(R.drawable.couple).into(mHeaderImage);


    }


    private void init(){
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content_frame, homeFragment, getString(R.string.tag_fragment_home));
        transaction.addToBackStack(getString(R.string.tag_fragment_home));
        transaction.commit();
    }


    private void setNavigationViewListener(){

        Log.d(TAG, "setNavigationViewListener: initializing navigation drawer listener");

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void isFirstLogin(){
        Log.d(TAG, "isFirstLogin: checking if this is the first login");

        final SharedPreferences preferences = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        boolean isFirstLogin = preferences.getBoolean(PreferenceKeys.FIRST_TIME_LOGIN, true);

        if(isFirstLogin){
            Log.d(TAG, "isFirstLogin: lauching alert dialog");

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(getString(R.string.first_time_user_message));
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "onClick: closing dialog");

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(PreferenceKeys.FIRST_TIME_LOGIN, false);
                    editor.commit();
                    dialog.dismiss();
                }
            });

            alertDialogBuilder.setIcon(R.drawable.tabian_dating);
            alertDialogBuilder.setTitle(" ");
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }

    @Override
    public void inflateViewProfileFragment(User user) {

        ViewProfileFragment fragment = new ViewProfileFragment();

        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.intent_user), user);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content_frame, fragment, getString(R.string.tag_fragment_view_profile));
        transaction.addToBackStack(getString(R.string.tag_fragment_view_profile));
        transaction.commit();
    }

    @Override
    public void onMessageSelected(Message message) {
        ChatFragment fragment = new ChatFragment();

        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.intent_message), message);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content_frame, fragment, getString(R.string.tag_fragment_chat));
        transaction.addToBackStack(getString(R.string.tag_fragment_chat));
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        FragmentTransaction transaction;

        switch (menuItem.getItemId()){

            case R.id.home:

                Log.d(TAG, "onNavigationItemSelected: Home fragment");

                init();
                break;

            case R.id.settings:

                Log.d(TAG, "onNavigationItemSelected: Settings fragment");

                SettingsFragment settingsFragment = new SettingsFragment();
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_content_frame, settingsFragment, getString(R.string.tag_fragment_settings));
                transaction.addToBackStack(getString(R.string.tag_fragment_settings));
                transaction.commit();
                break;

            case R.id.agreement:

                Log.d(TAG, "onNavigationItemSelected: Agreement fragment");

                AgreementFragment agreementFragment = new AgreementFragment();
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_content_frame, agreementFragment, getString(R.string.tag_fragment_agreement));
                transaction.addToBackStack(getString(R.string.tag_fragment_agreement));
                transaction.commit();
                break;



            case R.id.bottom_nav_home:

                Log.d(TAG, "onNavigationItemSelected: Home fragment");

                HomeFragment homeFragment = new HomeFragment();
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_content_frame, homeFragment, getString(R.string.tag_fragment_home));
                transaction.addToBackStack(getString(R.string.tag_fragment_home));
                transaction.commit();

                menuItem.setChecked(true);
                break;


            case R.id.bottom_nav_connections:

                Log.d(TAG, "onNavigationItemSelected: Connections fragment");

                SavedConnectionsFragment savedConnectionsFragment = new SavedConnectionsFragment();
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_content_frame, savedConnectionsFragment, getString(R.string.tag_fragment_saved_connections));
                transaction.addToBackStack(getString(R.string.tag_fragment_saved_connections));
                transaction.commit();

                menuItem.setChecked(true);
                break;


            case R.id.bottom_nav_messages:

                Log.d(TAG, "onNavigationItemSelected: Messages fragment");

                MessagesFragment messagesFragment = new MessagesFragment();
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_content_frame, messagesFragment, getString(R.string.tag_fragment_messages));
                transaction.addToBackStack(getString(R.string.tag_fragment_messages));
                transaction.commit();

                menuItem.setChecked(true);
                break;

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}
