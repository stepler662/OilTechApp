// FirebaseManager.java
package com.oiltechapp.utils;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseManager {
    private static final String TAG = "FirebaseManager";
    private final FirebaseAuth auth;
    private final FirebaseFirestore db;

    public FirebaseManager() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void checkFirebaseConnection() {
        auth.signInAnonymously()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Firebase connected successfully. User ID: " +
                                task.getResult().getUser().getUid());
                    } else {
                        Log.e(TAG, "Firebase connection failed", task.getException());
                    }
                });
    }
}