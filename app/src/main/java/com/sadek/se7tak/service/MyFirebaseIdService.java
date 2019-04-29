package com.sadek.se7tak.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.sadek.se7tak.model.Token;
import com.sadek.se7tak.utils.Common;

/**
 * Created by Mahmoud Sadek on 8/16/2018.
 */

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String tokenRefreshed = FirebaseInstanceId.getInstance().getToken();
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            updateTokenToFirebase(tokenRefreshed);
    }

    private void updateTokenToFirebase(String tokenRefreshed) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref_tokens = db.getReference("Tokens");
        Token token = new Token(tokenRefreshed, false);
        ref_tokens.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }
}
