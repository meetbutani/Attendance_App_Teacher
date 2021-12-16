package com.meetbutani.attendanceapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.meetbutani.attendanceapp.AttendanceSheetData.AdapterAttendanceSheet;
import com.meetbutani.attendanceapp.AttendanceSheetData.ModelAttendanceSheet;
import com.meetbutani.attendanceapp.CourseData.ModelCourse;
import com.meetbutani.attendanceapp.StudentListData.AdapterStudentList;
import com.meetbutani.attendanceapp.StudentListData.ModelStudentList;

import java.util.ArrayList;
import java.util.List;

public class StudentsFragment extends BaseFragment {

    private View view;
    private Context CONTEXT;
    private Bundle bundle;
    private ModelCourse modelCourse;
    private ModelStudentList modelStudentList;
    private String COURSEID;

    private RadioButton rBtnFragStuLecture, rBtnFragStuBatchA, rBtnFragStuBatchB;

    private RecyclerView rvFragStudents;
    private ArrayList<ModelStudentList> arrayListModelStudentList;
    private AdapterStudentList adapterStudentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_students, container, false);

        CONTEXT = getContext();
        modelCourse = getModelCourse();

        rBtnFragStuLecture = view.findViewById(R.id.rBtnFragStuLecture);
        rBtnFragStuBatchA = view.findViewById(R.id.rBtnFragStuBatchA);
        rBtnFragStuBatchB = view.findViewById(R.id.rBtnFragStuBatchB);

        rvFragStudents = view.findViewById(R.id.rvFragStudents);
        
        displayStudents();

        return view;
    }

    private void displayStudents() {
        try {
            rvFragStudents.setHasFixedSize(true);

            arrayListModelStudentList = new ArrayList<>();
            adapterStudentList = new AdapterStudentList(getActivity(), arrayListModelStudentList, getModelCourse());
            rvFragStudents.setAdapter(adapterStudentList);

            firebaseFirestore.collection(COURSESPATH + "/" + COURSEID + "/students").orderBy("rollNo", Query.Direction.ASCENDING).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {
                                modelStudentList = documentSnapshot.toObject(ModelStudentList.class);
                                arrayListModelStudentList.add(modelStudentList);
                            }

                            adapterStudentList.notifyDataSetChanged();
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(CONTEXT, "No Data", Toast.LENGTH_SHORT).show();
        }

    }

    private ModelCourse getModelCourse() {
        AttendanceSheetFragment fragment = new AttendanceSheetFragment();
        bundle = fragment.bundle;

        if (bundle != null) {
            modelCourse = (ModelCourse) bundle.getSerializable("modelCourse");
            COURSEID = modelCourse.courseId;
        }

        return modelCourse;
    }

}