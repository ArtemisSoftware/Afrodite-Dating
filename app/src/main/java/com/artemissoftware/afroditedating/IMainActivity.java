package com.artemissoftware.afroditedating;


import com.artemissoftware.afroditedating.models.Message;
import com.artemissoftware.afroditedating.models.User;

public interface IMainActivity {

    void inflateViewProfileFragment(User user);

    void onMessageSelected(Message message);

    void onBackPressed();
}
