package com.meetbutani.attendanceapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.meetbutani.attendanceapp.AdapterClass.AdapterStudentList;
import com.meetbutani.attendanceapp.ModelClass.ModelCourse;
import com.meetbutani.attendanceapp.ModelClass.ModelStudentData;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NotifyDataSetChanged")
public class StudentsFragment extends AttendanceSheetFragment {

    private View view;
    private Context CONTEXT;
    private Bundle bundleAS;
    private ModelCourse modelCourse;
    private ModelStudentData modelStudentData;
    private String COURSEID;

    private RadioButton rBtnFragStuLecture, rBtnFragStuBatchA, rBtnFragStuBatchB;

    private RecyclerView rvFragStudents;
    private ArrayList<ModelStudentData> aLMSDLec;
    private ArrayList<ModelStudentData> aLMSDA;
    private ArrayList<ModelStudentData> aLMSDB;
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

        rBtnFragStuLecture = view.findViewById(R.id.rBtnFragStuLecture);
        rBtnFragStuBatchA = view.findViewById(R.id.rBtnFragStuBatchA);
        rBtnFragStuBatchB = view.findViewById(R.id.rBtnFragStuBatchB);

        rvFragStudents = view.findViewById(R.id.rvFragStudents);

        bundleAS = this.getArguments();

        if (bundleAS != null) {
            aLMSDLec = (ArrayList<ModelStudentData>) bundleCourse.getSerializable("aLMSDLec");
            aLMSDA = (ArrayList<ModelStudentData>) bundleCourse.getSerializable("aLMSDA");
            aLMSDB = (ArrayList<ModelStudentData>) bundleCourse.getSerializable("aLMSDB");
            modelCourse = (ModelCourse) bundleAS.getSerializable("modelCourse");
            COURSEID = modelCourse.courseId;
        }

        rvFragStudents.setHasFixedSize(true);
        displayStudentsLec();

        rBtnFragStuLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayStudentsLec();
            }
        });

        rBtnFragStuBatchA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayStudentsA();
            }
        });

        rBtnFragStuBatchB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayStudentsB();
            }
        });

        return view;
    }

    private void displayStudents() {
        try {
            aLMSDLec = new ArrayList<>();
            aLMSDA = new ArrayList<>();
            aLMSDB = new ArrayList<>();

            firebaseFirestore.collection(COURSESPATH + "/" + COURSEID + "/students").orderBy("rollNo", Query.Direction.ASCENDING).get()
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

                            displayStudentsLec();
                        }
                    });
        } catch (Exception e) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(CONTEXT);
            dialog.setMessage("Error: student" + e.getMessage()).create().show();
//            Toast.makeText(CONTEXT, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void displayStudentsB() {
        adapterStudentList = new AdapterStudentList(getActivity(), aLMSDB, modelCourse);
        rvFragStudents.setAdapter(adapterStudentList);
        adapterStudentList.notifyDataSetChanged();
    }

    private void displayStudentsA() {
        adapterStudentList = new AdapterStudentList(getActivity(), aLMSDA, modelCourse);
        rvFragStudents.setAdapter(adapterStudentList);
        adapterStudentList.notifyDataSetChanged();
    }

    private void displayStudentsLec() {
        adapterStudentList = new AdapterStudentList(getActivity(), aLMSDLec, modelCourse);
        rvFragStudents.setAdapter(adapterStudentList);
        adapterStudentList.notifyDataSetChanged();
    }


//    private ModelCourse getModelCourse() {
//        AttendanceSheetFragment fragment = new AttendanceSheetFragment();
//        bundle = fragment.bundle;
//
//        if (bundle != null) {
//            modelCourse = (ModelCourse) bundle.getSerializable("modelCourse");
//            COURSEID = modelCourse.courseId;
//        }
//
//        return modelCourse;
//    }

}