package com.artemissoftware.afroditedating;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.artemissoftware.afroditedating.adapters.MessagesRecyclerViewAdapter;
import com.artemissoftware.afroditedating.models.User;
import com.artemissoftware.afroditedating.util.PreferenceKeys;
import com.artemissoftware.afroditedating.util.Users;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;



public class MessagesFragment extends Fragment {

    private static final String TAG = "MessagesFragment";

    //widgets
    private MessagesRecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    private SearchView mSearchView;

    //vars
    private ArrayList<User> mUsers = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        Log.d(TAG, "onCreateView: started.");
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mSearchView = (SearchView) view.findViewById(R.id.action_search);

        getConnections();
        initSearchView();

        return view;
    }

    private void initSearchView(){
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mRecyclerViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mRecyclerViewAdapter.getFilter().filter(query);
                return false;
            }
        });
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
        mRecyclerViewAdapter = new MessagesRecyclerViewAdapter(getActivity(), mUsers);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }




}
