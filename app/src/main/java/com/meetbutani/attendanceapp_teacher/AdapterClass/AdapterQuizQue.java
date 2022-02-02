package com.meetbutani.attendanceapp_teacher.AdapterClass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.meetbutani.attendanceapp_teacher.R;

import java.util.Map;

public class AdapterQuizQue extends RecyclerView.Adapter<AdapterQuizQue.ViewHolder> {

    private final FragmentActivity CONTEXT;
    private final Map<String, Object> quizData;
    private String[] keyArray;

    public AdapterQuizQue(FragmentActivity CONTEXT, Map<String, Object> quizData) {
        this.CONTEXT = CONTEXT;
        this.quizData = quizData;

        this.keyArray = this.quizData.keySet().toArray(new String[0]);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_frag_quiz_que, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String data = String.valueOf(quizData.get(keyArray[position]));

        if (data.startsWith("mcq")) {
            holder.mcqRBtnOptA = holder.itemView.findViewById(R.id.mcqRBtnOptA);
            holder.mcqRBtnOptB = holder.itemView.findViewById(R.id.mcqRBtnOptB);
            holder.mcqRBtnOptC = holder.itemView.findViewById(R.id.mcqRBtnOptC);
            holder.mcqRBtnOptD = holder.itemView.findViewById(R.id.mcqRBtnOptD);

            String[] dataArray = data.split("~");

            holder.hideMcq.setVisibility(View.VISIBLE);
            holder.hideTF.setVisibility(View.GONE);
            holder.hideMul.setVisibility(View.GONE);

            holder.tvQuestion.setText(dataArray[1]);

            holder.mcqRBtnOptA.setText(dataArray[2]);
            holder.mcqRBtnOptB.setText(dataArray[3]);
            holder.mcqRBtnOptC.setText(dataArray[4]);
            holder.mcqRBtnOptD.setText(dataArray[5]);

            if (dataArray[6].equalsIgnoreCase("A")) holder.mcqRBtnOptA.setChecked(true);
            else if (dataArray[6].equalsIgnoreCase("B")) holder.mcqRBtnOptB.setChecked(true);
            else if (dataArray[6].equalsIgnoreCase("C")) holder.mcqRBtnOptC.setChecked(true);
            else if (dataArray[6].equalsIgnoreCase("D")) holder.mcqRBtnOptD.setChecked(true);

        } else if (data.startsWith("t/f")) {
            holder.tfRBtnTrue = holder.itemView.findViewById(R.id.tfRBtnTrue);
            holder.tfRBtnFalse = holder.itemView.findViewById(R.id.tfRBtnFalse);

            String[] dataArray = data.split("~");

            holder.hideMcq.setVisibility(View.GONE);
            holder.hideTF.setVisibility(View.VISIBLE);
            holder.hideMul.setVisibility(View.GONE);

            holder.tvQuestion.setText(dataArray[1]);

            if (dataArray[2].equalsIgnoreCase("T")) holder.tfRBtnTrue.setChecked(true);
            else if (dataArray[2].equalsIgnoreCase("F")) holder.tfRBtnFalse.setChecked(true);

        } else if (data.startsWith("mul")) {
            holder.mulCbOptA = holder.itemView.findViewById(R.id.mulCbOptA);
            holder.mulCbOptB = holder.itemView.findViewById(R.id.mulCbOptB);
            holder.mulCbOptC = holder.itemView.findViewById(R.id.mulCbOptC);
            holder.mulCbOptD = holder.itemView.findViewById(R.id.mulCbOptD);

            String[] dataArray = data.split("~");

            holder.hideMcq.setVisibility(View.GONE);
            holder.hideTF.setVisibility(View.GONE);
            holder.hideMul.setVisibility(View.VISIBLE);

            holder.tvQuestion.setText(dataArray[1]);

            holder.mulCbOptA.setText(dataArray[2]);
            holder.mulCbOptB.setText(dataArray[3]);
            holder.mulCbOptC.setText(dataArray[4]);
            holder.mulCbOptD.setText(dataArray[5]);

            if (dataArray[6].contains("A")) holder.mulCbOptA.setChecked(true);
            if (dataArray[6].contains("B")) holder.mulCbOptB.setChecked(true);
            if (dataArray[6].contains("C")) holder.mulCbOptC.setChecked(true);
            if (dataArray[6].contains("D")) holder.mulCbOptD.setChecked(true);

        }
/*
        holder.tvRVFragASDate.setText(model.date);
        holder.tvRVFragASTime.setText((model.startTime + " - " + model.endTime));
        holder.tvRVFragASClass.setText((model.Class));
        holder.tvRVFragASType.setText((model.type));
*/
    }

    @Override
    public int getItemCount() {
        return quizData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvQuestion;
        private RadioGroup hideMcq, hideTF;
        private LinearLayout hideMul;
        private RadioButton mcqRBtnOptA, mcqRBtnOptB, mcqRBtnOptC, mcqRBtnOptD, tfRBtnTrue, tfRBtnFalse;
        private CheckBox mulCbOptA, mulCbOptB, mulCbOptC, mulCbOptD;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            itemView.setOnClickListener(this);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            hideMcq = itemView.findViewById(R.id.hideMcq);
            hideTF = itemView.findViewById(R.id.hideTF);
            hideMul = itemView.findViewById(R.id.hideMul);
//            mcqRBtnOptA = itemView.findViewById(R.id.mcqRBtnOptA);
//            mcqRBtnOptB = itemView.findViewById(R.id.mcqRBtnOptB);
//            mcqRBtnOptC = itemView.findViewById(R.id.mcqRBtnOptC);
//            mcqRBtnOptD = itemView.findViewById(R.id.mcqRBtnOptD);
//            tfRBtnTrue = itemView.findViewById(R.id.tfRBtnTrue);
//            tfRBtnFalse = itemView.findViewById(R.id.tfRBtnFalse);
//            mulCbOptA = itemView.findViewById(R.id.mulCbOptA);
//            mulCbOptB = itemView.findViewById(R.id.mulCbOptB);
//            mulCbOptC = itemView.findViewById(R.id.mulCbOptC);
//            mulCbOptD = itemView.findViewById(R.id.mulCbOptD);

        }

        @Override
        public void onClick(View view) {
/*
            int position = this.getAdapterPosition();
            ModelAttendanceSheet modelAttendanceSheet = arrayListModelAttendanceSheet.get(position);
//            Toast.makeText(CONTEXT, position + "", Toast.LENGTH_SHORT).show();

            Bundle bundleAS = new Bundle();
            bundleAS.putSerializable("modelCourse", modelCourse);
            bundleAS.putSerializable("modelAttendanceSheet", modelAttendanceSheet);

            if (modelAttendanceSheet.Class.equalsIgnoreCase("Lecture"))
                bundleAS.putSerializable("aLMSD", aLMSDLec);
            else if (modelAttendanceSheet.Class.equalsIgnoreCase("A Batch"))
                bundleAS.putSerializable("aLMSD", aLMSDA);
            else if (modelAttendanceSheet.Class.equalsIgnoreCase("B Batch"))
                bundleAS.putSerializable("aLMSD", aLMSDB);
            else bundleAS.putSerializable("aLMSD", aLMSDLec);


            if (modelAttendanceSheet.type.equalsIgnoreCase("Manual")) {

                ManualAttendanceFragment fragment = new ManualAttendanceFragment();
                fragment.setArguments(bundleAS);
                CONTEXT.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayMain, fragment, "ManualAttendanceFragment")
                        .addToBackStack(null)
                        .commit();

            } else if (modelAttendanceSheet.type.equalsIgnoreCase("Quiz")) {

                QuizAttendanceFragment fragment = new QuizAttendanceFragment();
                fragment.setArguments(bundleAS);
                CONTEXT.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayMain, fragment, "QuizAttendanceFragment")
                        .addToBackStack(null)
                        .commit();

            }
*/
        }
    }
}
