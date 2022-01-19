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

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.meetbutani.attendanceapp.ModelClass.ModelCourse;
import com.meetbutani.attendanceapp.ModelClass.ModelStudentData;
import com.meetbutani.attendanceapp.ViewPagerAdapter.AttendanceSheetVPAdapter;

import java.util.ArrayList;

public class AttendanceSheetFragment extends BaseFragment {

    protected Bundle bundleCourse;
    private View view;
    private TabLayout tlAttendanceSheet;
    private ViewPager vpAttendanceSheet;
    private AttendanceSheetVPAdapter adapter;

    private ModelCourse modelCourse;
    private ModelStudentData modelStudentData;
    private ShapeableImageView ivCopyCourseId;
    private TextView tvDisASCourseName, tvDisASCourseId;

    private ArrayList<ModelStudentData> aLMSDLec;
    private ArrayList<ModelStudentData> aLMSDA;
    private ArrayList<ModelStudentData> aLMSDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundleCourse = this.getArguments();

        if (bundleCourse != null) {
            aLMSDLec = (ArrayList<ModelStudentData>) bundleCourse.getSerializable("aLMSDLec");
            aLMSDA = (ArrayList<ModelStudentData>) bundleCourse.getSerializable("aLMSDA");
            aLMSDB = (ArrayList<ModelStudentData>) bundleCourse.getSerializable("aLMSDB");
            modelCourse = (ModelCourse) bundleCourse.getSerializable("modelCourse");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_attendance_sheet, container, false);

        tvDisASCourseName = view.findViewById(R.id.tvDisASCourseName);
        tvDisASCourseId = view.findViewById(R.id.tvDisASCourseId);
        ivCopyCourseId = view.findViewById(R.id.ivCopyCourseId);
        tlAttendanceSheet = view.findViewById(R.id.tlAttendanceSheet);
        vpAttendanceSheet = view.findViewById(R.id.vpAttendanceSheet);

        tvDisASCourseName.setText(modelCourse.courseName);
        tvDisASCourseId.setText(modelCourse.courseId);

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

        setViewPager();

        return view;
    }

    private void setViewPager() {

        adapter = new AttendanceSheetVPAdapter(getChildFragmentManager());

        Bundle bundleAS = new Bundle();
        bundleAS.putSerializable("modelCourse", modelCourse);
        bundleAS.putSerializable("aLMSDLec", aLMSDLec);
        bundleAS.putSerializable("aLMSDA", aLMSDA);
        bundleAS.putSerializable("aLMSDB", aLMSDB);

        AttendanceListFragment attendanceListFragment = new AttendanceListFragment();
        attendanceListFragment.setArguments(bundleAS);

        StudentsFragment studentsFragment = new StudentsFragment();
        studentsFragment.setArguments(bundleAS);

        adapter.addFragmentInList(attendanceListFragment, "Attendance Sheet");
        adapter.addFragmentInList(studentsFragment, "Students");

        vpAttendanceSheet.setAdapter(adapter);

        tlAttendanceSheet.setupWithViewPager(vpAttendanceSheet);

    }

/*
    private void storeArrayList() {
        try {
            aLMSDLec = new ArrayList<>();
            aLMSDA = new ArrayList<>();
            aLMSDB = new ArrayList<>();

            firebaseFirestore.collection(COURSESPATH + "/" + modelCourse.courseId + "/students").orderBy("rollNo", Query.Direction.ASCENDING).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {
                                modelStudentData = documentSnapshot.toObject(ModelStudentData.class);

                                aLMSDLec.add(modelStudentData);

                                if (modelStudentData.Class.equalsIgnoreCase("A"))
                                    aLMSDA.add(modelStudentData);

                                if (modelStudentData.Class.equalsIgnoreCase("B"))
                                    aLMSDB.add(modelStudentData);
                            }

                            setViewPager();

                        }
                    });
        } catch (Exception e) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(CONTEXT);
            dialog.setMessage("Error: " + e.getMessage()).create().show();
//            Toast.makeText(CONTEXT, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
*/

}