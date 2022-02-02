package com.meetbutani.attendanceapp_teacher;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.meetbutani.attendanceapp_teacher.AdapterClass.AdapterManualAttendance;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelAttendanceSheet;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelCourse;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelStudentData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressLint("NotifyDataSetChanged")
public class ManualAttendanceFragment extends BaseFragment {

    public HashMap<String, String> attendanceList;
    public RadioButton rBtnFragManAttAll, rBtnFragManAttPresent, rBtnFragManAttAbsent;
    private View view;
    private Bundle bundleAS;
    private Context CONTEXT;
    private ModelStudentData modelStudentData;
    private ArrayList<ModelStudentData> arrayListPresent, arrayListAbsent;
    private AdapterManualAttendance adapterManualAttendance;
    private ArrayList<ModelStudentData> aLMSD;
    private ModelCourse modelCourse;
    private ModelAttendanceSheet modelAttendanceSheet;
    private String COURSEID;
    private TextView tvFragMADate, tvFragMAClass, tvFragMATime, tvFragMAType;
    private RecyclerView rvFragMA;
    private Button btnPresent, btnAbsent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_manual_attendance, container, false);

        CONTEXT = getContext();

        tvFragMADate = view.findViewById(R.id.tvFragMADate);
        tvFragMAClass = view.findViewById(R.id.tvFragMAClass);
        tvFragMATime = view.findViewById(R.id.tvFragMATime);
        tvFragMAType = view.findViewById(R.id.tvFragMAType);
        btnPresent = view.findViewById(R.id.btnPresent);
        btnAbsent = view.findViewById(R.id.btnAbsent);
        rBtnFragManAttAll = view.findViewById(R.id.rBtnFragManAttAll);
        rBtnFragManAttPresent = view.findViewById(R.id.rBtnFragManAttPresent);
        rBtnFragManAttAbsent = view.findViewById(R.id.rBtnFragManAttAbsent);
        rvFragMA = view.findViewById(R.id.rvFragMA);

        bundleAS = this.getArguments();

        if (bundleAS != null) {
            aLMSD = (ArrayList<ModelStudentData>) bundleAS.getSerializable("aLMSD");
            modelAttendanceSheet = (ModelAttendanceSheet) bundleAS.getSerializable("modelAttendanceSheet");
            modelCourse = (ModelCourse) bundleAS.getSerializable("modelCourse");
            COURSEID = modelCourse.courseId;
        }

        tvFragMADate.setText(modelAttendanceSheet.date);
        tvFragMATime.setText((modelAttendanceSheet.startTime + " - " + modelAttendanceSheet.endTime));
        tvFragMAClass.setText(modelAttendanceSheet.Class);
        tvFragMAType.setText(modelAttendanceSheet.type);

        arrayListPresent = new ArrayList<>();
        arrayListAbsent = new ArrayList<>();

        displayStudentAttendance();

        return view;
    }

    private void displayStudentAttendance() {
        try {
            rvFragMA.setHasFixedSize(true);

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
                            attendanceList.put(rollNo, "un-mark");
                    }

                    displayAll();

                    btnPresent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setAllPresent();
                        }
                    });

                    btnAbsent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setAllAbsent();
                        }
                    });

                    rBtnFragManAttPresent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            displayPresent();
                        }
                    });

                    rBtnFragManAttAbsent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            displayAbsent();
                        }
                    });

                    rBtnFragManAttAll.setOnClickListener(new View.OnClickListener() {
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

    private void setAllPresent() {
        attendanceList.clear();
        Map<String, Object> attendance = new HashMap<>();

        for (ModelStudentData studentData : aLMSD) {
            String rollNo = studentData.rollNo;
            attendance.put(rollNo, "present");
            attendanceList.put(rollNo, "present");
        }

        firebaseFirestore.collection(COURSESPATH + "/" + COURSEID + "/sheets/" + modelAttendanceSheet.sheetId + "/attendance")
                .document("attendance").update(attendance);

        if (rBtnFragManAttPresent.isChecked())
            displayPresent();
        else if (rBtnFragManAttAbsent.isChecked())
            displayAbsent();

        arrayListAbsent.clear();
        adapterManualAttendance.notifyDataSetChanged();
    }

    private void setAllAbsent() {
        attendanceList.clear();
        Map<String, Object> attendance = new HashMap<>();

        for (ModelStudentData studentData : aLMSD) {
            String rollNo = studentData.rollNo;
            attendance.put(rollNo, "absent");
            attendanceList.put(rollNo, "absent");
        }

        firebaseFirestore.collection(COURSESPATH + "/" + COURSEID + "/sheets/" + modelAttendanceSheet.sheetId + "/attendance")
                .document("attendance").update(attendance);

        if (rBtnFragManAttPresent.isChecked())
            displayPresent();
        else if (rBtnFragManAttAbsent.isChecked())
            displayAbsent();

        arrayListPresent.clear();
        adapterManualAttendance.notifyDataSetChanged();
    }

    public void displayAll() {
        adapterManualAttendance = new AdapterManualAttendance(getActivity(), aLMSD, modelCourse, modelAttendanceSheet, attendanceList, this);
        rvFragMA.setAdapter(adapterManualAttendance);
        adapterManualAttendance.notifyDataSetChanged();
    }

    public void displayAbsent() {

        if (attendanceList.size() != 0) {
            for (ModelStudentData studentData : aLMSD) {
                String rollNo = studentData.rollNo;
                if (Objects.requireNonNull(attendanceList.get(rollNo)).equalsIgnoreCase("absent"))
                    arrayListAbsent.add(studentData);
            }
        }

        adapterManualAttendance = new AdapterManualAttendance(getActivity(), arrayListAbsent, modelCourse, modelAttendanceSheet, attendanceList, this);
        rvFragMA.setAdapter(adapterManualAttendance);
        adapterManualAttendance.notifyDataSetChanged();
    }

    public void displayPresent() {

        if (attendanceList.size() != 0) {
            for (ModelStudentData studentData : aLMSD) {
                String rollNo = studentData.rollNo;
                if (Objects.requireNonNull(attendanceList.get(rollNo)).equalsIgnoreCase("present"))
                    arrayListPresent.add(studentData);
            }
        }

        adapterManualAttendance = new AdapterManualAttendance(getActivity(), arrayListPresent, modelCourse, modelAttendanceSheet, attendanceList, this);
        rvFragMA.setAdapter(adapterManualAttendance);
        adapterManualAttendance.notifyDataSetChanged();
    }
}