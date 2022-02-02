package com.meetbutani.attendanceapp_teacher;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelAttendanceSheet;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelCourse;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelStudentData;
import com.meetbutani.attendanceapp_teacher.ViewPagerAdapter.QuizAttendanceVPAdapter;

import java.util.ArrayList;

public class QuizAttendanceFragment extends BaseFragment {

    private View view;
    private Bundle bundleAS;
    private Context CONTEXT;
    private ArrayList<ModelStudentData> aLMSD;
    private ModelAttendanceSheet modelAttendanceSheet;
    private ModelCourse modelCourse;
    private String COURSEID;
    private TextView tvFragQADate, tvFragQAClass, tvFragQAType, tvFragQATime;
    private QuizAttendanceVPAdapter adapter;
    private ViewPager vpQuizAttendance;
    private TabLayout tlQuizAttendance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_quiz_attendance, container, false);

        CONTEXT = getContext();

        tvFragQADate = view.findViewById(R.id.tvFragQADate);
        tvFragQAClass = view.findViewById(R.id.tvFragQAClass);
        tvFragQAType = view.findViewById(R.id.tvFragQAType);
        tvFragQATime = view.findViewById(R.id.tvFragQATime);

        vpQuizAttendance = view.findViewById(R.id.vpQuizAttendance);
        tlQuizAttendance = view.findViewById(R.id.tlQuizAttendance);


        bundleAS = this.getArguments();

        if (bundleAS != null) {
            aLMSD = (ArrayList<ModelStudentData>) bundleAS.getSerializable("aLMSD");
            modelAttendanceSheet = (ModelAttendanceSheet) bundleAS.getSerializable("modelAttendanceSheet");
            modelCourse = (ModelCourse) bundleAS.getSerializable("modelCourse");
            COURSEID = modelCourse.courseId;
        }

        tvFragQADate.setText(modelAttendanceSheet.date);
        tvFragQATime.setText((modelAttendanceSheet.startTime + " - " + modelAttendanceSheet.endTime));
        tvFragQAClass.setText(modelAttendanceSheet.Class);
        tvFragQAType.setText(modelAttendanceSheet.type);

        setViewPager();

        return view;
    }

    private void setViewPager() {

        adapter = new QuizAttendanceVPAdapter(getChildFragmentManager());

        QuizStudentAttendanceFragment quizStudentAttendanceFragment = new QuizStudentAttendanceFragment();
        quizStudentAttendanceFragment.setArguments(bundleAS);

        QuizQuestionFragment quizQuestionFragment = new QuizQuestionFragment();
        quizQuestionFragment.setArguments(bundleAS);

        adapter.addFragmentInList(quizStudentAttendanceFragment, "Attendance");
        adapter.addFragmentInList(quizQuestionFragment, "Questions");

        vpQuizAttendance.setAdapter(adapter);

        tlQuizAttendance.setupWithViewPager(vpQuizAttendance);

    }

}