package com.sreekanth.dev.ilahianz.Notifications;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseIdService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String refreshTocken = FirebaseInstanceId.getInstance().getToken();

        if (firebaseUser != null) {
            updateTocken(refreshTocken);
        }
    }

    private void updateTocken(String refreshTocken) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(refreshTocken);
        assert firebaseUser != null;
        reference.child(firebaseUser.getUid()).setValue(token);
    }
}
