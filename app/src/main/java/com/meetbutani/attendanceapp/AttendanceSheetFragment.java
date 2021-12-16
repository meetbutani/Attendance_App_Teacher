package com.meetbutani.attendanceapp;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.meetbutani.attendanceapp.CourseData.ModelCourse;

import java.util.Objects;

public class AttendanceSheetFragment extends Fragment {

    private View view;

    private TabLayout tlAttendanceSheet;
    private ViewPager vpAttendanceSheet;
    private ASViewpagerAdapter adapter;
    private ModelCourse modelCourse;
    private ShapeableImageView ivCopyCourseId;
    protected Bundle bundle;

    private TextView tvDisASCourseName, tvDisASCourseId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_attendance_sheet, container, false);

        tvDisASCourseName = view.findViewById(R.id.tvDisASCourseName);
        tvDisASCourseId = view.findViewById(R.id.tvDisASCourseId);
        ivCopyCourseId = view.findViewById(R.id.ivCopyCourseId);

        bundle = this.getArguments();
        if (bundle != null) {
            modelCourse = (ModelCourse) bundle.getSerializable("modelCourse");
            tvDisASCourseName.setText(modelCourse.courseName);
            tvDisASCourseId.setText(modelCourse.courseId);
        }

        ivCopyCourseId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(null, tvDisASCourseId.getText().toString());
                if (clipboard == null) return;
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getContext(), "Course ID Copied!", Toast.LENGTH_SHORT).show();
            }
        });

        tlAttendanceSheet = view.findViewById(R.id.tlAttendanceSheet);
        vpAttendanceSheet = view.findViewById(R.id.vpAttendanceSheet);
        adapter = new ASViewpagerAdapter(getParentFragmentManager());

        adapter.addFragmentInList(new AttendanceListFragment(), "Attendance Sheet");
        adapter.addFragmentInList(new StudentsFragment(), "Students");

        vpAttendanceSheet.setAdapter(adapter);

        tlAttendanceSheet.setupWithViewPager(vpAttendanceSheet);

        return view;
    }
}