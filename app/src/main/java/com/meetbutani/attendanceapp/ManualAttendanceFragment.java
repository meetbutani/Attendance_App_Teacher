package com.meetbutani.attendanceapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.meetbutani.attendanceapp.AdapterClass.AdapterStudentAttendance;
import com.meetbutani.attendanceapp.ModelClass.ModelAttendanceSheet;
import com.meetbutani.attendanceapp.ModelClass.ModelCourse;
import com.meetbutani.attendanceapp.ModelClass.ModelStudentData;

import java.util.ArrayList;
import java.util.List;

public class ManualAttendanceFragment extends BaseFragment {

    private View view;
    private Bundle bundleAS;
    private Context CONTEXT;
    private ModelStudentData modelStudentData;

    private ArrayList<ModelStudentData> arrayListModelStudentData;
    private AdapterStudentAttendance adapterStudentAttendance;

    private ArrayList<ModelStudentData> aLMSD;
    private ModelCourse modelCourse;
    private ModelAttendanceSheet modelAttendanceSheet;

    private String COURSEID;

    private TextView tvRVFragMADate, tvRVFragMAClass, tvRVFragMATime, tvRVFragMAType;
    private RecyclerView rvFragMA;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_manual_attendance, container, false);

        CONTEXT = getContext();

        tvRVFragMADate = view.findViewById(R.id.tvRVFragMADate);
        tvRVFragMAClass = view.findViewById(R.id.tvRVFragMAClass);
        tvRVFragMATime = view.findViewById(R.id.tvRVFragMATime);
        tvRVFragMAType = view.findViewById(R.id.tvRVFragMAType);
        rvFragMA = view.findViewById(R.id.rvFragMA);

        bundleAS = this.getArguments();

        if (bundleAS != null) {
            aLMSD = (ArrayList<ModelStudentData>) bundleAS.getSerializable("aLMSD");
            modelAttendanceSheet = (ModelAttendanceSheet) bundleAS.getSerializable("modelAttendanceSheet");
            modelCourse = (ModelCourse) bundleAS.getSerializable("modelCourse");
            COURSEID = modelCourse.courseId;
        }

        tvRVFragMADate.setText(modelAttendanceSheet.date);
        tvRVFragMATime.setText((modelAttendanceSheet.startTime + " - " + modelAttendanceSheet.endTime));
        tvRVFragMAClass.setText(modelAttendanceSheet.Class);
        tvRVFragMAType.setText(modelAttendanceSheet.type);

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

                    adapterStudentAttendance = new AdapterStudentAttendance(getActivity(), aLMSD, modelCourse, modelAttendanceSheet, documentSnapshot);
                    rvFragMA.setAdapter(adapterStudentAttendance);
                    adapterStudentAttendance.notifyDataSetChanged();


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

                                    adapterStudentAttendance.notifyDataSetChanged();
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
}