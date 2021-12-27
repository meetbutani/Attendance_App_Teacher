package com.meetbutani.attendanceapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private MaterialToolbar tbMainActivity;
    private NavigationView navViewMain;

    private long backPressedTime;
    private Toast toast;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.tbMainActivity));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.drawerLayout);
        tbMainActivity = findViewById(R.id.tbMainActivity);
        navViewMain = findViewById(R.id.navViewMain);

        CONTEXT = MainActivity.this;

        setFragment(new CourseFragment(), "CourseFragment");

        tbMainActivity.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navViewMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id) {
                    case R.id.itCourse:
                        FragmentManager fm = getSupportFragmentManager();
                        for (int i = 0; i < fm.getBackStackEntryCount(); i++)
                            fm.popBackStack();
                        setFragment(new CourseFragment(), "CourseFragment");
                        break;

                    case R.id.nav_home2:
                        Toast.makeText(CONTEXT, "Home 2", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_home3:
                        Toast.makeText(CONTEXT, "Home 3", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_home4:
                        Toast.makeText(CONTEXT, "Home 4", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_home5:
                        Toast.makeText(CONTEXT, "Home 5", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_home6:
                        Toast.makeText(CONTEXT, "Home 6", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_home7:
                        Toast.makeText(CONTEXT, "Home 7", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_home8:
                        Toast.makeText(CONTEXT, "Home 8", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_home9:
                        Toast.makeText(CONTEXT, "Home 9", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_home10:
                        Toast.makeText(CONTEXT, "Home 10", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_home11:
                        Toast.makeText(CONTEXT, "Home 11", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itCreateNewCourse:
                createNewCourse();
                break;

            case R.id.itLogout:
                firebaseAuth.signOut();
                startActivity(new Intent(CONTEXT, LoginActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createNewCourse() {

        final View newCourseDialog = getLayoutInflater().inflate(R.layout.dialog_single_field, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(CONTEXT);

        TextInputEditText etSingleField = newCourseDialog.findViewById(R.id.etSingleField);
        TextInputLayout etErSingleField = newCourseDialog.findViewById(R.id.etErSingleField);

        etErSingleField.setHint("Course Name");

        builder.setView(newCourseDialog)
                .setCancelable(false)
                .setTitle("Create New Course")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String courseName = Objects.requireNonNull(etSingleField.getText()).toString().trim();
                        String courseId = Timestamp.now().getSeconds() + "";

                        if (!courseName.isEmpty()) {
                            if (courseName.getBytes().length > 2) {
                                Map<String, Object> addCourse = new HashMap<>();
                                addCourse.put("courseName", courseName);
                                addCourse.put("courseId", courseId);
                                firebaseFirestore.collection(COURSESPATH).document(courseId).set(addCourse).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        firebaseFirestore.collection(TEACHERPATH + "/" + getUid() + "/courses").document(courseId).set(addCourse)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(CONTEXT, "Course Added Successfully", Toast.LENGTH_SHORT).show();
                                                        setFragment(new CourseFragment(), "CourseFragment");
                                                    }
                                                });
                                    }
                                });
                            } else {
                                Toast.makeText(CONTEXT, "Course Name Length Should greater then 2", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CONTEXT, "Course name must required", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                });

        builder.create().show();
    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag("CourseFragment");

        if (fragment instanceof CourseFragment && fragment.isVisible()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setMessage("Are you sure you want to exit form Attendance App - Teacher");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.super.onBackPressed();
                    MainActivity.super.onBackPressed();
                }
            });
            dialog.setNegativeButton("No", (dialog1, which) -> {
            });
            dialog.create().show();
        } else super.onBackPressed();


/*
        if (fragment instanceof CourseFragment && fragment.isVisible()) {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                toast.cancel();
                super.onBackPressed();
                super.onBackPressed();
            } else {
                toast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                toast.show();
            }
            backPressedTime = System.currentTimeMillis();
        } else super.onBackPressed();
*/
    }
}