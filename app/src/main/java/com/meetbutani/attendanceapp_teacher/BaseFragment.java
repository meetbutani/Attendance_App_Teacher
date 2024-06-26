package com.meetbutani.attendanceapp_teacher;

import static android.content.Context.MODE_APPEND;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

@SuppressLint("WrongConstant")
public class BaseFragment extends Fragment {

    protected final String TEACHERPATH = "/app/app/teachers";
    protected final String COURSESPATH = "/app/app/courses";
    protected Context CONTEXT = getContext();
    protected FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    protected FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    protected FirebaseUser firebaseUser;
    protected StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    protected SharedPreferences editorSP;
    protected SharedPreferences readSP;
    protected SharedPreferences.Editor editSP;

    protected String getUid() {
        readSP = CONTEXT.getSharedPreferences("userData", MODE_APPEND);
        return readSP.getString("uid", "null");
    }

    protected void setFragment(Fragment fragment, String FragTitle) {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayMain, fragment, FragTitle)
                .addToBackStack(null)
                .commit();
    }


}
