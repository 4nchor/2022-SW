package com.libienz.se_2022_closet.startApp_1.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseReference {

    public static final FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static DatabaseReference boardRef = database.getReference("board");
    public static DatabaseReference userRef = database.getReference("user");
    public static final StorageReference reference = FirebaseStorage.getInstance().getReference();
}
