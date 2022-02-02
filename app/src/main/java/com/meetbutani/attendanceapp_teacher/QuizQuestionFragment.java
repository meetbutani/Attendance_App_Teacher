package com.meetbutani.attendanceapp_teacher;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.meetbutani.attendanceapp_teacher.AdapterClass.AdapterQuizQue;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelAttendanceSheet;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelCourse;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class QuizQuestionFragment extends QuizAttendanceFragment implements Serializable {

    private View view;
    private Bundle bundleAS;
    private Bundle bundleQue;
    private ModelAttendanceSheet modelAttendanceSheet;
    private ModelCourse modelCourse;
    private RecyclerView rvFragQuizQue;
    private FloatingActionButton fabFragQuizQue;
    private QuizQuestionFragment quizQuestionFragment;
    private Context CONTEXT;
    private String COURSEID;
    private String sheetId;
    public Map<String, Object> quizData;
    private String[] keyArray;
    private AdapterQuizQue adapterQuizQue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_quiz_question, container, false);

        CONTEXT = getContext();
        quizQuestionFragment = this;

        bundleAS = this.getArguments();

        if (bundleAS != null) {
            modelAttendanceSheet = (ModelAttendanceSheet) bundleAS.getSerializable("modelAttendanceSheet");
            modelCourse = (ModelCourse) bundleAS.getSerializable("modelCourse");
            COURSEID = modelCourse.courseId;
            sheetId = modelAttendanceSheet.sheetId;
        }

        rvFragQuizQue = view.findViewById(R.id.rvFragQuizQue);
        fabFragQuizQue = view.findViewById(R.id.fabFragQuizQue);

        displayQues();

        fabFragQuizQue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundleQue = new Bundle();
                bundleQue.putSerializable("THIS", quizQuestionFragment);
                bundleQue.putSerializable("modelAttendanceSheet", modelAttendanceSheet);
                bundleQue.putSerializable("modelCourse", modelCourse);

                QuestionsAddFragment fragment = new QuestionsAddFragment();
                fragment.setArguments(bundleQue);

                setFragment(fragment, "QuestionsAddFragment");
            }
        });

        return view;
    }

    private void displayQues() {
        try {
            rvFragQuizQue.setHasFixedSize(true);

/*
            arrayListModelAttendanceSheet = new ArrayList<>();
            adapterAttendanceSheet = new AdapterAttendanceSheet(getActivity(), arrayListModelAttendanceSheet, bundleAS);
            rvFragQuizQue.setAdapter(adapterAttendanceSheet);
*/

            firebaseFirestore.collection(COURSESPATH + "/" + COURSEID + "/sheets/" + sheetId + "/attendance")
                    .document("quiz").get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            quizData = documentSnapshot.getData();

                            if (quizData != null) {
                                quizData.remove("default");
                                quizData = new TreeMap<>(quizData);

                                setAdapterQuizQue();

//                                keyArray = quizData.keySet().toArray(new String[0]);
                            }
                        }
                    });
        } catch (Exception e) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(CONTEXT);
            dialog.setMessage("Error: " + e.getMessage()).create().show();
//            Toast.makeText(CONTEXT, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void setAdapterQuizQue() {
        adapterQuizQue = new AdapterQuizQue(getActivity(), quizData);
        rvFragQuizQue.setAdapter(adapterQuizQue);
    }

}