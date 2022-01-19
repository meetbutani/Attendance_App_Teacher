package com.meetbutani.attendanceapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.meetbutani.attendanceapp.ModelClass.ModelAttendanceSheet;
import com.meetbutani.attendanceapp.ModelClass.ModelCourse;


public class TrueFalseQuestionFragment extends QuestionsAddFragment {

    private View view;
    private Bundle bundleAS;
    private ModelAttendanceSheet modelAttendanceSheet;
    private ModelCourse modelCourse;
    private String COURSEID;
    private String sheetId;
    private RadioButton rBtnTrue, rBtnFalse;
    private TextInputEditText etQuestion;
    private Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_true_false_question, container, false);

        bundleAS = this.getArguments();

        if (bundleAS != null) {
            modelAttendanceSheet = (ModelAttendanceSheet) bundleAS.getSerializable("modelAttendanceSheet");
            modelCourse = (ModelCourse) bundleAS.getSerializable("modelCourse");
            COURSEID = modelCourse.courseId;
            sheetId = modelAttendanceSheet.sheetId;
        }

        etQuestion = view.findViewById(R.id.etQuestion);
        rBtnTrue = view.findViewById(R.id.rBtnTrue);
        rBtnFalse = view.findViewById(R.id.rBtnFalse);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = String.valueOf(etQuestion.getText()).trim();

                String ans = "";
                if (rBtnTrue.isChecked()) ans = "true";
                else if (rBtnFalse.isChecked()) ans = "false";

                String store = "t/f" + "~" + question + "~" + ans;
                String key = Timestamp.now().getSeconds() + "";

                firebaseFirestore.collection(COURSESPATH + "/" + COURSEID + "/sheets/" + sheetId + "/attendance")
                        .document("quiz").update(key, store);

            }
        });

        return view;
    }
}