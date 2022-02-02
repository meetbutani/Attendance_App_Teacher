package com.meetbutani.attendanceapp_teacher;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.meetbutani.attendanceapp_teacher.ViewPagerAdapter.QuestionsAddVPAdapter;

public class QuestionsAddFragment extends BaseFragment {

    private View view;
    private QuestionsAddVPAdapter adapter;
    private TabLayout tlQuestionsAdd;
    private ViewPager vpQuestionsAdd;
    private Bundle bundleQue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_questions_add, container, false);

        bundleQue = this.getArguments();

        tlQuestionsAdd = view.findViewById(R.id.tlQuestionsAdd);
        vpQuestionsAdd = view.findViewById(R.id.vpQuestionsAdd);


        setViewPager();

        return view;
    }

    private void setViewPager() {

        adapter = new QuestionsAddVPAdapter(getChildFragmentManager());

        McqQuestionFragment mcqQuestionFragment = new McqQuestionFragment();
        mcqQuestionFragment.setArguments(bundleQue);

        TrueFalseQuestionFragment trueFalseQuestionFragment = new TrueFalseQuestionFragment();
        trueFalseQuestionFragment.setArguments(bundleQue);

        MultipleChoiceFragment multipleChoiceFragment = new MultipleChoiceFragment();
        multipleChoiceFragment.setArguments(bundleQue);

        adapter.addFragmentInList(mcqQuestionFragment, "MCQ");
        adapter.addFragmentInList(trueFalseQuestionFragment, "True/False");
        adapter.addFragmentInList(multipleChoiceFragment, "Multi Choice");

        vpQuestionsAdd.setAdapter(adapter);

        tlQuestionsAdd.setupWithViewPager(vpQuestionsAdd);

    }

}