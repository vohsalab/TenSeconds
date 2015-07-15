package com.ibalashov.tenseconds;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class EditFriendsActivity extends AppCompatActivity {

    private static final String TAG = EditFriendsActivity.class.getSimpleName();
    protected List<ParseUser> mUsers;
    protected ListView myList;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);
        myList = (ListView)findViewById(R.id.friends_list);
        myList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (myList.isItemChecked(position)) {
                    mFriendsRelation.add(mUsers.get(position));

                }
                else {
                    mFriendsRelation.remove(mUsers.get(position));
                   }
                mCurrentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                
                if (e == null) {
                    //Success
                    mUsers = users;
                    String[] usernames = new String[mUsers.size()];
                    int i = 0;
                    for (ParseUser user:mUsers ) {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            EditFriendsActivity.this,
                            android.R.layout.simple_list_item_checked, usernames);
                    //myList = (ListView)findViewById(R.id.friends_list);
                    myList.setAdapter(adapter);

                    addFriendCheckmarks();

                    
                }
                else {
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

    }

    private void addFriendCheckmarks() {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if(e == null) {
                    for (int i = 0; i < mUsers.size(); i++) {
                        ParseUser user = mUsers.get(i);
                        for (ParseUser friend: friends) {
                            if (friend.getObjectId().equals(user.getObjectId())) {
                                myList.setItemChecked(i, true);
                            }
                        }
                    }
                }
                else {
                    Log.e(TAG, e.getMessage());
                }

            }
        });
    }
}
