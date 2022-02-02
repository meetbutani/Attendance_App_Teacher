package com.meetbutani.attendanceapp_teacher;

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
import com.meetbutani.attendanceapp_teacher.AdapterClass.AdapterQuizAttendance;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelAttendanceSheet;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelCourse;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelStudentData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@SuppressLint("NotifyDataSetChanged")
public class QuizStudentAttendanceFragment extends QuizAttendanceFragment {

    public HashMap<String, String> attendanceList;
    public RadioButton rBtnFragQuizAttAll, rBtnFragQuizAttPresent, rBtnFragQuizAttAbsent;
    private View view;
    private Bundle bundleAS;
    private Context CONTEXT;
    private ModelAttendanceSheet modelAttendanceSheet;
    private ModelCourse modelCourse;
    private String COURSEID;
    private RecyclerView rvFragQuizAtt;
    private ArrayList<ModelStudentData> arrayListPresent, arrayListAbsent;
    private ArrayList<ModelStudentData> aLMSD;
    private AdapterQuizAttendance adapterQuizAttendance;
    private String[] data;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_quiz_student_attendance, container, false);

        CONTEXT = getContext();

        rBtnFragQuizAttAll = view.findViewById(R.id.rBtnFragQuizAttAll);
        rBtnFragQuizAttPresent = view.findViewById(R.id.rBtnFragQuizAttPresent);
        rBtnFragQuizAttAbsent = view.findViewById(R.id.rBtnFragQuizAttAbsent);
        rvFragQuizAtt = view.findViewById(R.id.rvFragQuizAtt);

        bundleAS = this.getArguments();

        if (bundleAS != null) {
            aLMSD = (ArrayList<ModelStudentData>) bundleAS.getSerializable("aLMSD");
            modelAttendanceSheet = (ModelAttendanceSheet) bundleAS.getSerializable("modelAttendanceSheet");
            modelCourse = (ModelCourse) bundleAS.getSerializable("modelCourse");
            COURSEID = modelCourse.courseId;
        }

        displayStudentAttendance();

        return view;
    }

    private void displayStudentAttendance() {
        try {
            rvFragQuizAtt.setHasFixedSize(true);

            firebaseFirestore.collection(COURSESPATH + "/" + COURSEID + "/sheets/" + modelAttendanceSheet.sheetId + "/attendance")
                    .document("attendance").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    attendanceList = new HashMap<>();

                    for (ModelStudentData studentData : aLMSD) {
                        String rollNo = studentData.rollNo;
                        String docRoll = documentSnapshot.getString(rollNo);
                        if (docRoll != null && !docRoll.isEmpty())
                            attendanceList.put(rollNo, documentSnapshot.getString(rollNo));
                        else
                            attendanceList.put(rollNo, "un-mark~" + (char) '\u2298' + "~0");
                    }

                    displayAll();

                    rBtnFragQuizAttPresent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            displayPresent();
                        }
                    });

                    rBtnFragQuizAttAbsent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            displayAbsent();
                        }
                    });

                    rBtnFragQuizAttAll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            displayAll();
                        }
                    });

//                    arrayListModelStudentData = new ArrayList<>();
//                    arrayListModelStudentData = aLMSDA;

/*
                    firebaseFirestore.collection(COURSESPATH + "/" + COURSEID + "/students").orderBy("rollNo", Query.Direction.ASCENDING).get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                                    for (DocumentSnapshot documentSnapshot : list) {
                                        ModelStudentData modelStudentData = documentSnapshot.toObject(ModelStudentData.class);
                                        arrayListModelStudentData.add(modelStudentData);
                                    }

                                    adapterManualAttendance.notifyDataSetChanged();
                                }
                            });
*/
                }
            });
        } catch (Exception e) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(CONTEXT);
            dialog.setMessage("Error: attendance sheet" + e.getMessage()).create().show();
//            Toast.makeText(CONTEXT, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void displayAll() {
        adapterQuizAttendance = new AdapterQuizAttendance(getActivity(), aLMSD, modelCourse, modelAttendanceSheet, attendanceList, this);
        rvFragQuizAtt.setAdapter(adapterQuizAttendance);
        adapterQuizAttendance.notifyDataSetChanged();
    }

    public void displayAbsent() {
        arrayListAbsent = new ArrayList<>();

        if (attendanceList.size() != 0) {
            for (ModelStudentData studentData : aLMSD) {
                String rollNo = studentData.rollNo;
                data = Objects.requireNonNull(attendanceList.get(rollNo)).split("~");
                if (data[0].equalsIgnoreCase("absent"))
                    arrayListAbsent.add(studentData);
            }
        }

        adapterQuizAttendance = new AdapterQuizAttendance(getActivity(), arrayListAbsent, modelCourse, modelAttendanceSheet, attendanceList, this);
        rvFragQuizAtt.setAdapter(adapterQuizAttendance);
        adapterQuizAttendance.notifyDataSetChanged();
    }

    public void displayPresent() {
        arrayListPresent = new ArrayList<>();

        if (attendanceList.size() != 0) {
            for (ModelStudentData studentData : aLMSD) {
                String rollNo = studentData.rollNo;
                data = Objects.requireNonNull(attendanceList.get(rollNo)).split("~");
                if (data[0].equalsIgnoreCase("present"))
                    arrayListPresent.add(studentData);
            }
        }

        adapterQuizAttendance = new AdapterQuizAttendance(getActivity(), arrayListPresent, modelCourse, modelAttendanceSheet, attendanceList, this);
        rvFragQuizAtt.setAdapter(adapterQuizAttendance);
        adapterQuizAttendance.notifyDataSetChanged();
    }
}