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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.artemissoftware.afroditedating.models.FragmentTag;
import com.artemissoftware.afroditedating.models.Message;
import com.artemissoftware.afroditedating.models.User;
import com.artemissoftware.afroditedating.settings.SettingsFragment;
import com.artemissoftware.afroditedating.util.PreferenceKeys;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IMainActivity,
        BottomNavigationViewEx.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private BottomNavigationViewEx mBottomNavigationViewEx;
    private ImageView mHeaderImage;
    private DrawerLayout mDrawerLayout;


    //constants
    private static final int HOME_FRAGMENT = 0;
    private static final int CONNECTIONS_FRAGMENT = 1;
    private static final int MESSAGES_FRAGMENT = 2;


    //Fragments
    private HomeFragment mHomeFragment;
    private SavedConnectionsFragment mSavedConnectionsFragment;
    private MessagesFragment mMessagesFragment;
    private SettingsFragment mSettingsFragment;
    private ViewProfileFragment mViewProfileFragment;
    private ChatFragment mChatFragment;
    private AgreementFragment  mAgreementFragment;


    private ArrayList<String> mFragmentTags = new ArrayList<String>();
    private ArrayList<FragmentTag> mFragments = new ArrayList<FragmentTag>();
    private int mExitCount = 0;

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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        int backStackCount = mFragmentTags.size();

        if(backStackCount > 1){

            //nav backwards
            String topFragmentTag = mFragmentTags.get(backStackCount - 1);

            String newTopFragmentTag = mFragmentTags.get(backStackCount - 2);

            setFragmentVisibilities(newTopFragmentTag);
            mFragmentTags.remove(topFragmentTag);

            mExitCount = 0;
        }
        else if(backStackCount == 1){

            String topFragmentTag = mFragmentTags.get(backStackCount - 1);

            if(topFragmentTag.equals(getString(R.string.tag_fragment_home))){

                mHomeFragment.scrollToTop();
                mExitCount++;
                Toast.makeText(this, "1 more click to exit", Toast.LENGTH_SHORT).show();
            }
            else {

                mExitCount++;
                Toast.makeText(this, "1 more click to exit", Toast.LENGTH_SHORT).show();
            }
        }

        if(mExitCount >= 2){
            super.onBackPressed();
        }
    }


    private void setNavigationIcon(String tagname){

        Menu menu = mBottomNavigationViewEx.getMenu();
        MenuItem menuItem = null;

        if(tagname.equals(getString(R.string.tag_fragment_home))){

            Log.d(TAG, "setNavigationIcon: home fragment is visible");
            menuItem = menu.getItem(HOME_FRAGMENT);
            menuItem.setChecked(true);
        }
        else if(tagname.equals(getString(R.string.tag_fragment_saved_connections))){

            Log.d(TAG, "setNavigationIcon: saved connections fragment is visible");
            menuItem = menu.getItem(CONNECTIONS_FRAGMENT);
            menuItem.setChecked(true);
        }
        else if(tagname.equals(getString(R.string.tag_fragment_messages))){

            Log.d(TAG, "setNavigationIcon: messages fragment is visible");
            menuItem = menu.getItem(MESSAGES_FRAGMENT);
            menuItem.setChecked(true);
        }
    }

    private void initBottomNavigationView(){
        Log.d(TAG, "initBottomNavigationView: initializing the bottom navigation view");
        mBottomNavigationViewEx.enableAnimation(false);
    }


    private void setHeaderImage(){
        Log.d(TAG, "setHeaderImage: setting image");
        Glide.with(this)
                .load(R.drawable.aphrodite)
                .into(mHeaderImage);
    }


    private void init(){

        if(mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content_frame, mHomeFragment, getString(R.string.tag_fragment_home));
            transaction.commit();

            mFragmentTags.add(getString(R.string.tag_fragment_home));
            mFragments.add(new FragmentTag(mHomeFragment, getString(R.string.tag_fragment_home)));
        }
        else{
            mFragmentTags.remove(getString(R.string.tag_fragment_home));
            mFragmentTags.add(getString(R.string.tag_fragment_home));
        }

        setFragmentVisibilities(getString(R.string.tag_fragment_home));
    }


    private void setFragmentVisibilities(String tagName){

        if(tagName.equals(getString(R.string.tag_fragment_home)))
            showBottomNavigation();

        else if(tagName.equals(getString(R.string.tag_fragment_saved_connections)))
            showBottomNavigation();

        else if(tagName.equals(getString(R.string.tag_fragment_messages)))
            showBottomNavigation();

        else if(tagName.equals(getString(R.string.tag_fragment_settings)))
            hideBottomNavigation();

        else if(tagName.equals(getString(R.string.tag_fragment_view_profile)))
            hideBottomNavigation();

        else if(tagName.equals(getString(R.string.tag_fragment_chat)))
            hideBottomNavigation();

        else if(tagName.equals(getString(R.string.tag_fragment_agreement)))
            hideBottomNavigation();

        for(int i = 0; i < mFragments.size(); i++){

            if(tagName.equals(mFragments.get(i).getTag())){
                //show
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.show(mFragments.get(i).getFragment());
                transaction.commit();
            }
            else{
                //dont show

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.hide(mFragments.get(i).getFragment());
                transaction.commit();
            }
        }

        setNavigationIcon(tagName);
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

        if(mViewProfileFragment != null) {

            getSupportFragmentManager().beginTransaction().remove(mViewProfileFragment).commitAllowingStateLoss();

        }

        mViewProfileFragment = new ViewProfileFragment();

        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.intent_user), user);
        mViewProfileFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content_frame, mViewProfileFragment, getString(R.string.tag_fragment_view_profile));
        transaction.commit();

        mFragmentTags.add(getString(R.string.tag_fragment_view_profile));
        mFragments.add(new FragmentTag(mViewProfileFragment, getString(R.string.tag_fragment_view_profile)));

        setFragmentVisibilities(getString(R.string.tag_fragment_view_profile));
    }

    @Override
    public void onMessageSelected(Message message) {

        if(mChatFragment != null) {

            getSupportFragmentManager().beginTransaction().remove(mChatFragment).commitAllowingStateLoss();

        }

        mChatFragment = new ChatFragment();

        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.intent_message), message);
        mChatFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content_frame, mChatFragment, getString(R.string.tag_fragment_chat));
        transaction.commit();

        mFragmentTags.add(getString(R.string.tag_fragment_chat));
        mFragments.add(new FragmentTag(mChatFragment, getString(R.string.tag_fragment_chat)));

        setFragmentVisibilities(getString(R.string.tag_fragment_chat));
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        FragmentTransaction transaction;

        switch (menuItem.getItemId()){

            case R.id.home:

                Log.d(TAG, "onNavigationItemSelected: Home fragment");

                mFragmentTags.clear();
                mFragmentTags = new ArrayList<>();
                init();
                break;

            case R.id.settings:

                Log.d(TAG, "onNavigationItemSelected: Settings fragment");

                if(mSettingsFragment == null){

                    mSettingsFragment = new SettingsFragment();
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.main_content_frame, mSettingsFragment, getString(R.string.tag_fragment_settings));
                    transaction.commit();

                    mFragmentTags.add(getString(R.string.tag_fragment_settings));
                    mFragments.add(new FragmentTag(mSettingsFragment, getString(R.string.tag_fragment_settings)));
                }
                else{
                    mFragmentTags.remove(getString(R.string.tag_fragment_settings));
                    mFragmentTags.add(getString(R.string.tag_fragment_settings));
                }
                setFragmentVisibilities(getString(R.string.tag_fragment_settings));
                break;

            case R.id.agreement:

                Log.d(TAG, "onNavigationItemSelected: Agreement fragment");

                if(mAgreementFragment == null) {
                    mAgreementFragment = new AgreementFragment();
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.main_content_frame, mAgreementFragment, getString(R.string.tag_fragment_agreement));
                    transaction.commit();

                    mFragmentTags.add(getString(R.string.tag_fragment_agreement));
                    mFragments.add(new FragmentTag(mAgreementFragment, getString(R.string.tag_fragment_agreement)));
                }
                else{
                    mFragmentTags.remove(getString(R.string.tag_fragment_agreement));
                    mFragmentTags.add(getString(R.string.tag_fragment_agreement));
                }
                setFragmentVisibilities(getString(R.string.tag_fragment_agreement));
                break;



            case R.id.bottom_nav_home:

                Log.d(TAG, "onNavigationItemSelected: Home fragment");
                if(mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.main_content_frame, mHomeFragment, getString(R.string.tag_fragment_home));
                    transaction.commit();


                    mFragmentTags.add(getString(R.string.tag_fragment_home));
                    mFragments.add(new FragmentTag(mHomeFragment, getString(R.string.tag_fragment_home)));
                }
                else{
                    mFragmentTags.remove(getString(R.string.tag_fragment_home));
                    mFragmentTags.add(getString(R.string.tag_fragment_home));
                }

                menuItem.setChecked(true);
                setFragmentVisibilities(getString(R.string.tag_fragment_home));
                break;


            case R.id.bottom_nav_connections:

                Log.d(TAG, "onNavigationItemSelected: Connections fragment");
                if(mSavedConnectionsFragment == null) {
                    mSavedConnectionsFragment = new SavedConnectionsFragment();
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.main_content_frame, mSavedConnectionsFragment, getString(R.string.tag_fragment_saved_connections));
                    transaction.commit();


                    mFragmentTags.add(getString(R.string.tag_fragment_saved_connections));
                    mFragments.add(new FragmentTag(mSavedConnectionsFragment, getString(R.string.tag_fragment_saved_connections)));
                }
                else{
                    mFragmentTags.remove(getString(R.string.tag_fragment_saved_connections));
                    mFragmentTags.add(getString(R.string.tag_fragment_saved_connections));
                }

                menuItem.setChecked(true);
                setFragmentVisibilities(getString(R.string.tag_fragment_saved_connections));
                break;


            case R.id.bottom_nav_messages:

                Log.d(TAG, "onNavigationItemSelected: Messages fragment");
                if(mMessagesFragment == null) {
                    mMessagesFragment = new MessagesFragment();
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.main_content_frame, mMessagesFragment, getString(R.string.tag_fragment_messages));
                    transaction.commit();


                    mFragmentTags.add(getString(R.string.tag_fragment_messages));
                    mFragments.add(new FragmentTag(mMessagesFragment, getString(R.string.tag_fragment_messages)));
                }
                else{
                    mFragmentTags.remove(getString(R.string.tag_fragment_messages));
                    mFragmentTags.add(getString(R.string.tag_fragment_messages));
                }

                menuItem.setChecked(true);
                setFragmentVisibilities(getString(R.string.tag_fragment_messages));
                break;

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }


    private void hideBottomNavigation() {
        if (mBottomNavigationViewEx != null) {
            mBottomNavigationViewEx.setVisibility(View.GONE);
        }
    }

    private void showBottomNavigation() {
        if (mBottomNavigationViewEx != null) {
            mBottomNavigationViewEx.setVisibility(View.VISIBLE);
        }
    }

}
