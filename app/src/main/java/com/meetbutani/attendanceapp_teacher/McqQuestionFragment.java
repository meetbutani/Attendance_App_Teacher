package com.meetbutani.attendanceapp_teacher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelAttendanceSheet;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelCourse;


public class McqQuestionFragment extends QuestionsAddFragment {

    private View view;
    private Bundle bundleQue;
    private ModelAttendanceSheet modelAttendanceSheet;
    private ModelCourse modelCourse;
    private TextInputEditText etQuestion;
    private RadioButton rBtnOptA, rBtnOptB, rBtnOptC, rBtnOptD;
    private TextInputEditText etOptA, etOptB, etOptC, etOptD;
    private Button btnSubmit;
    private String COURSEID;
    private String sheetId;
    private QuizQuestionFragment quizQuestionFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mcq_question, container, false);

        bundleQue = this.getArguments();

        if (bundleQue != null) {
            modelAttendanceSheet = (ModelAttendanceSheet) bundleQue.getSerializable("modelAttendanceSheet");
            modelCourse = (ModelCourse) bundleQue.getSerializable("modelCourse");
            quizQuestionFragment = (QuizQuestionFragment) bundleQue.getSerializable("THIS");
            COURSEID = modelCourse.courseId;
            sheetId = modelAttendanceSheet.sheetId;
        }

        etQuestion = view.findViewById(R.id.etQuestion);
        etOptA = view.findViewById(R.id.etOptA);
        etOptB = view.findViewById(R.id.etOptB);
        etOptC = view.findViewById(R.id.etOptC);
        etOptD = view.findViewById(R.id.etOptD);
        rBtnOptA = view.findViewById(R.id.rBtnOptA);
        rBtnOptB = view.findViewById(R.id.rBtnOptB);
        rBtnOptC = view.findViewById(R.id.rBtnOptC);
        rBtnOptD = view.findViewById(R.id.rBtnOptD);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = String.valueOf(etQuestion.getText()).trim();
                String optA = String.valueOf(etOptA.getText()).trim();
                String optB = String.valueOf(etOptB.getText()).trim();
                String optC = String.valueOf(etOptC.getText()).trim();
                String optD = String.valueOf(etOptD.getText()).trim();

                String ans = "";
                if (rBtnOptA.isChecked()) ans = "A";
                else if (rBtnOptB.isChecked()) ans = "B";
                else if (rBtnOptC.isChecked()) ans = "C";
                else if (rBtnOptD.isChecked()) ans = "D";

                String store = "mcq" + "~" + question + "~" + optA + "~" + optB + "~" + optC + "~" + optD + "~" + ans;
                String key = Timestamp.now().getSeconds() + "";

                firebaseFirestore.collection(COURSESPATH + "/" + COURSEID + "/sheets/" + sheetId + "/attendance")
                        .document("quiz").update(key, store);

                quizQuestionFragment.quizData.put(key, store);
                quizQuestionFragment.setAdapterQuizQue();

                requireActivity().onBackPressed();

/*
                Bundle bundleQue = new Bundle();
                bundleQue.putString("key", key);
                bundleQue.putString("que", store);

                QuizQuestionFragment quizQuestionFragment = new QuizQuestionFragment();
                quizQuestionFragment.setArguments(bundleQue);

                setFragment(quizQuestionFragment, "QuizQuestionFragment");
*/
            }
        });

        return view;
    }

}