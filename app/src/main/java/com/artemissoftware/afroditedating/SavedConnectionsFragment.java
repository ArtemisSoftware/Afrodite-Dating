package com.artemissoftware.afroditedating;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.artemissoftware.afroditedating.adapters.MainRecyclerViewAdapter;
import com.artemissoftware.afroditedating.models.User;
import com.artemissoftware.afroditedating.util.PreferenceKeys;
import com.artemissoftware.afroditedating.util.Users;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;



public class SavedConnectionsFragment extends Fragment {

    private static final String TAG = "SavedConnFragment";

    //constants
    private static final int NUM_GRID_COLUMNS = 2;

    //widgets

    private MainRecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView mRecyclerView;

    //vars
    private ArrayList<User> mUsers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_connections, container, false);
        Log.d(TAG, "onCreateView: started.");

        mRecyclerView = view.findViewById(R.id.recycler_view);

        getConnections();

        return view;
    }

    private void getConnections(){

        final SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        Set<String> savedNames = preferences.getStringSet(PreferenceKeys.SAVED_CONNECTIONS, new HashSet<String>());

        Users users = new Users();
        if(mUsers != null){
            mUsers.clear();
        }
        for(User user: users.USERS){
            if(savedNames.contains(user.getName())){
                mUsers.add(user);
            }
        }
        if(mRecyclerViewAdapter == null){
            initRecyclerView();
        }
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        mRecyclerViewAdapter = new MainRecyclerViewAdapter(getActivity(), mUsers);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_GRID_COLUMNS, LinearLayoutManager.VERTICAL);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }



}
