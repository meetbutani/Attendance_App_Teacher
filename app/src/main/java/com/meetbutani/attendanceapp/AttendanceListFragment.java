package com.meetbutani.attendanceapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.meetbutani.attendanceapp.AttendanceSheetData.AdapterAttendanceSheet;
import com.meetbutani.attendanceapp.AttendanceSheetData.ModelAttendanceSheet;
import com.meetbutani.attendanceapp.CourseData.ModelCourse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceListFragment extends BaseFragment {

    private View view;
    private Context CONTEXT;
    private Bundle bundle;
    private ModelCourse modelCourse;
    private ModelAttendanceSheet modelAttendanceSheet;
    private String COURSEID;
    private int selYear, selMonth, selDay, selStartHour, selStartMinute, selEndHour, selEndMinute;

    private RecyclerView rvFragAS;
    private FloatingActionButton fabFragAS;

    private ArrayList<ModelAttendanceSheet> arrayListModelAttendanceSheet;
    private AdapterAttendanceSheet adapterAttendanceSheet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_attendance_list, container, false);

        CONTEXT = getContext();

        rvFragAS = view.findViewById(R.id.rvFragAS);
        fabFragAS = view.findViewById(R.id.fabFragAS);

        modelCourse = getModelCourse();

        fabFragAS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAttendanceSheet();
            }
        });

        displayAttendanceSheet();

        return view;
    }

    private void createAttendanceSheet() {

        final View newSheetDialog = getLayoutInflater().inflate(R.layout.dialog_new_attendance_sheet, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(CONTEXT);

        LinearLayout linearSelectDate = newSheetDialog.findViewById(R.id.linearSelectDate);
        LinearLayout linearSelectStartTime = newSheetDialog.findViewById(R.id.linearSelectStartTime);
        LinearLayout linearSelectEndTime = newSheetDialog.findViewById(R.id.linearSelectEndTime);

        TextView tvSelectedDate = newSheetDialog.findViewById(R.id.tvSelectedDate);
        TextView tvSelectedStartTime = newSheetDialog.findViewById(R.id.tvSelectedStartTime);
        TextView tvSelectedEndTime = newSheetDialog.findViewById(R.id.tvSelectedEndTime);

        RadioButton rBtnManual = newSheetDialog.findViewById(R.id.rBtnManual);
        RadioButton rBtnQuiz = newSheetDialog.findViewById(R.id.rBtnQuiz);

        RadioButton rBtnLec = newSheetDialog.findViewById(R.id.rBtnLec);
        RadioButton rBtnBatchA = newSheetDialog.findViewById(R.id.rBtnBatchA);
        RadioButton rBtnBatchB = newSheetDialog.findViewById(R.id.rBtnBatchB);

        final Calendar calendar = Calendar.getInstance();
        selYear = calendar.get(Calendar.YEAR);
        selMonth = calendar.get(Calendar.MONTH);
        selDay = calendar.get(Calendar.DAY_OF_MONTH);
        selStartHour = selEndHour = calendar.get(Calendar.HOUR_OF_DAY);
        selStartMinute = selEndMinute = calendar.get(Calendar.MINUTE);

        calendar.set(selYear, selMonth, selDay, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        tvSelectedDate.setText(DateFormat.format("dd-MM-yyyy", calendar));
        tvSelectedStartTime.setText(DateFormat.format("hh:mm aa", calendar));
        tvSelectedEndTime.setText(DateFormat.format("hh:mm aa", calendar));

        linearSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        selYear = year;
                        selMonth = monthOfYear;
                        selDay = dayOfMonth;
                        calendar.set(year, monthOfYear, dayOfMonth, 0, 0);
                        tvSelectedDate.setText(DateFormat.format("dd-MM-yyyy", calendar));
                    }
                }, selYear, selMonth, selDay);
                datePickerDialog.show();
            }
        });

        linearSelectStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        selStartHour = hour;
                        selStartMinute = minute;
                        calendar.set(0, 0, 0, hour, minute);
                        tvSelectedStartTime.setText(DateFormat.format("hh:mm aa", calendar));
                    }
                }, selStartHour, selStartMinute, false);
                timePickerDialog.show();
            }
        });

        linearSelectEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        selEndHour = hour;
                        selEndMinute = minute;
                        calendar.set(0, 0, 0, hour, minute);
                        tvSelectedEndTime.setText(DateFormat.format("hh:mm aa", calendar));
                    }
                }, selEndHour, selEndMinute, false);
                timePickerDialog.show();
            }
        });

        rBtnLec.setChecked(true);

        rBtnLec.setOnClickListener(v -> {
            rBtnLec.setChecked(true);
            rBtnBatchA.setChecked(false);
            rBtnBatchB.setChecked(false);
        });

        rBtnBatchA.setOnClickListener(v -> {
            rBtnLec.setChecked(false);
            rBtnBatchA.setChecked(true);
            rBtnBatchB.setChecked(false);
        });

        rBtnBatchB.setOnClickListener(v -> {
            rBtnLec.setChecked(false);
            rBtnBatchA.setChecked(false);
            rBtnBatchB.setChecked(true);
        });

        builder.setView(newSheetDialog)
                .setTitle("Create New Attendance Sheet")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String date = tvSelectedDate.getText().toString().trim();
                        String startTime = tvSelectedStartTime.getText().toString().trim();
                        String endTime = tvSelectedEndTime.getText().toString().trim();
                        String type = " ";
                        String Class = " ";

                        if (rBtnManual.isChecked())
                            type = "Manual";
                        if (rBtnQuiz.isChecked())
                            type = "Quiz";

                        if (rBtnLec.isChecked())
                            Class = "Lecture";
                        if (rBtnBatchA.isChecked())
                            Class = "A Batch";
                        if (rBtnBatchB.isChecked())
                            Class = "B Batch";

                        Map<String, Object> addSheet = new HashMap<>();

                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            Date d = sdf.parse(date);
                            assert d != null;
                            long milliseconds = d.getTime();
                            addSheet.put("timestamp", String.valueOf(milliseconds));
                        } catch (ParseException e) {
                            Log.e("===error", e.getMessage(), e);
                        }

                        addSheet.put("date", date);
                        addSheet.put("startTime", startTime);
                        addSheet.put("endTime", endTime);
                        addSheet.put("type", type);
                        addSheet.put("Class", Class);
                        addSheet.put("status", "close");

                        String sheetId = Timestamp.now().getSeconds() + "";
                        addSheet.put("sheetId", sheetId);

                        firebaseFirestore.collection(COURSESPATH + "/" + COURSEID + "/sheets").document(sheetId).set(addSheet).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Map<String, Object> attendance = new HashMap<>();
                                attendance.put("date", date);
                                FirebaseFirestore.getInstance().collection(COURSESPATH + "/" + COURSEID + "/sheets/" + sheetId + "/attendance")
                                        .document("attendance").set(attendance);
                                displayAttendanceSheet();
                            }
                        });

//                                Toast.makeText(getApplicationContext(), date + "\n" + startTime + "\n" + endTime + "\n" + type, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                });

        builder.create().show();

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

    private void displayAttendanceSheet() {
        try {
            rvFragAS.setHasFixedSize(true);

            arrayListModelAttendanceSheet = new ArrayList<>();
            adapterAttendanceSheet = new AdapterAttendanceSheet(getActivity(), arrayListModelAttendanceSheet, getModelCourse());
            rvFragAS.setAdapter(adapterAttendanceSheet);

            getModelCourse();
            firebaseFirestore.collection(COURSESPATH + "/" + COURSEID + "/sheets").orderBy("timestamp", Query.Direction.DESCENDING).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {
                                modelAttendanceSheet = documentSnapshot.toObject(ModelAttendanceSheet.class);
                                arrayListModelAttendanceSheet.add(modelAttendanceSheet);
                            }

                            adapterAttendanceSheet.notifyDataSetChanged();
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(CONTEXT, "No Data", Toast.LENGTH_SHORT).show();
        }
    }
}