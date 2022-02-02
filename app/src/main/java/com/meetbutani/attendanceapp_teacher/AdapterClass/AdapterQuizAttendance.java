package com.meetbutani.attendanceapp_teacher.AdapterClass;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelAttendanceSheet;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelCourse;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelStudentData;
import com.meetbutani.attendanceapp_teacher.QuizStudentAttendanceFragment;
import com.meetbutani.attendanceapp_teacher.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AdapterQuizAttendance extends RecyclerView.Adapter<AdapterQuizAttendance.ViewHolder> {

    private final ArrayList<ModelStudentData> arrayListModelStudentData;
    private final ModelCourse modelCourse;
    private final ModelAttendanceSheet modelAttendanceSheet;
    private final HashMap<String, String> attendanceList;
    private final String COURSEPATH = "/app/app/courses";
    private final QuizStudentAttendanceFragment quizStudentAttendanceFragment;
    public ViewHolder holder;
    private ModelStudentData modelStudentAttendance;
    private FragmentActivity CONTEXT;
    private Context context;
    private String[] data;

    public AdapterQuizAttendance(FragmentActivity CONTEXT, ArrayList<ModelStudentData> arrayListModelStudentData, ModelCourse modelCourse, ModelAttendanceSheet modelAttendanceSheet, HashMap<String, String> attendanceList, QuizStudentAttendanceFragment quizStudentAttendanceFragment) {
        this.CONTEXT = CONTEXT;
        this.arrayListModelStudentData = arrayListModelStudentData;
        this.modelCourse = modelCourse;
        this.modelAttendanceSheet = modelAttendanceSheet;
        this.attendanceList = attendanceList;
        this.quizStudentAttendanceFragment = quizStudentAttendanceFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_student_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        modelStudentAttendance = arrayListModelStudentData.get(position);
        try {
            String imageURL = modelStudentAttendance.imageURL;
            if (!imageURL.equalsIgnoreCase(" "))
                Picasso.get().load(Uri.parse(imageURL)).into(holder.ivStudentProfilePic);
        } catch (Exception ignored) {
        }

        String firstName = modelStudentAttendance.firstName;
        String lastName = modelStudentAttendance.lastName;
        String rollNo = modelStudentAttendance.rollNo;

        holder.tvRVSAName.setText((firstName + " " + lastName));
        holder.tvRVSARollNo.setText(rollNo);
        holder.tvQuizScore.setVisibility(View.VISIBLE);

        this.holder = holder;

        String attendance = attendanceList.get(rollNo);

        if (attendance != null && !attendance.isEmpty()) {
            data = attendance.split("~");
//            if (attendance.equalsIgnoreCase("unMark")) selectUnMark();
            if (data[0].equalsIgnoreCase("present")) {
                selectPresent();
                holder.tvQuizScore.setText(data[1]);
            } else if (data[0].equalsIgnoreCase("absent")) {
                selectAbsent();
                holder.tvQuizScore.setText(data[1]);
            } else {
                holder.tvQuizScore.setText(data[1]);
            }
//            else selectUnMark();

            if (data[1].equalsIgnoreCase(Character.toString((char) '\u2298')))
                holder.tvQuizScore.setTextSize(26);
        }

    }

/*
    public void selectUnMark() {
        holder.ivUnMark.setImageResource(R.drawable.ic_unmark_back);
        holder.ivPresent.setImageResource(R.drawable.ic_present);
        holder.ivAbsent.setImageResource(R.drawable.ic_absent);
    }
*/

    public void selectPresent() {
//        holder.ivUnMark.setImageResource(R.drawable.ic_unmark);
        holder.ivPresent.setImageResource(R.drawable.ic_present_back);
        holder.ivAbsent.setImageResource(R.drawable.ic_absent);
    }

    public void selectAbsent() {
//        holder.ivUnMark.setImageResource(R.drawable.ic_unmark);
        holder.ivPresent.setImageResource(R.drawable.ic_present);
        holder.ivAbsent.setImageResource(R.drawable.ic_absent_back);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAbsent(int position) {
        String rollNo = arrayListModelStudentData.get(position).rollNo;
        data = Objects.requireNonNull(attendanceList.get(rollNo)).split("~");
        FirebaseFirestore.getInstance().collection(COURSEPATH + "/" + modelCourse.courseId + "/sheets/" + modelAttendanceSheet.sheetId + "/attendance")
                .document("attendance").update(rollNo, "absent" + "~" + data[1] + "~" + data[2]);

        quizStudentAttendanceFragment.attendanceList.replace(rollNo, "absent" + "~" + data[1] + "~" + data[2]);
        updateAdapter();
    }

    private void updateAdapter() {
        if (quizStudentAttendanceFragment.rBtnFragQuizAttPresent.isChecked())
            quizStudentAttendanceFragment.displayPresent();
        else if (quizStudentAttendanceFragment.rBtnFragQuizAttAbsent.isChecked())
            quizStudentAttendanceFragment.displayAbsent();
        else if (quizStudentAttendanceFragment.rBtnFragQuizAttAll.isChecked())
            quizStudentAttendanceFragment.displayAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setPresent(int position) {
        String rollNo = arrayListModelStudentData.get(position).rollNo;
        FirebaseFirestore.getInstance().collection(COURSEPATH + "/" + modelCourse.courseId + "/sheets/" + modelAttendanceSheet.sheetId + "/attendance")
                .document("attendance").update(rollNo, "present" + "~" + data[1] + "~" + data[2]);

        quizStudentAttendanceFragment.attendanceList.replace(rollNo, "present" + "~" + data[1] + "~" + data[2]);
        updateAdapter();
    }

/*
    private void setUnMark(int position) {
        String rollNo = arrayListModelStudentData.get(position).rollNo;
        FirebaseFirestore.getInstance().collection(COURSEPATH + "/" + modelCourse.courseId + "/sheets/" + modelAttendanceSheet.sheetId + "/attendance")
                .document("attendance").update(rollNo, "unMark");
    }
*/

    private void displayStudentProfile(int position) {
        @SuppressLint("InflateParams")
        View studentProfileDialog = LayoutInflater.from(CONTEXT).inflate(R.layout.dialog_student_profile, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(CONTEXT);

        TextView tvDialogFullName = studentProfileDialog.findViewById(R.id.tvDialogFullName);
        TextView tvDialogRollNo = studentProfileDialog.findViewById(R.id.tvDialogRollNo);
        TextView tvDialogClass = studentProfileDialog.findViewById(R.id.tvDialogClass);
        TextView tvDialogEmail = studentProfileDialog.findViewById(R.id.tvDialogEmail);
        CircularImageView ivDialogStudentPic = studentProfileDialog.findViewById(R.id.ivDialogStudentPic);
        ShapeableImageView ivCopyEmailID = studentProfileDialog.findViewById(R.id.ivCopyEmailID);

        ModelStudentData model = arrayListModelStudentData.get(position);
        tvDialogFullName.setText((model.firstName + " " + model.lastName));
        tvDialogRollNo.setText(model.rollNo);
        tvDialogClass.setText(model.Class);
        tvDialogEmail.setText(model.emailId);
        try {
            String imageURL = model.imageURL;
            if (!imageURL.equalsIgnoreCase(" "))
                Picasso.get().load(Uri.parse(imageURL)).into(ivDialogStudentPic);
        } catch (Exception ignored) {
        }

        ivCopyEmailID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) CONTEXT.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(null, tvDialogEmail.getText().toString());
                if (clipboard == null) return;
                clipboard.setPrimaryClip(clip);

                Toast.makeText(CONTEXT, "Email ID Copied!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(studentProfileDialog);
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return arrayListModelStudentData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CircularImageView ivStudentProfilePic;
        private final TextView tvRVSAName, tvRVSARollNo, tvQuizScore;
        public ShapeableImageView ivUnMark, ivAbsent, ivPresent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivStudentProfilePic = itemView.findViewById(R.id.ivStudentProfilePic);
            tvRVSAName = itemView.findViewById(R.id.tvRVSAName);
            tvRVSARollNo = itemView.findViewById(R.id.tvRVSARollNo);
            tvQuizScore = itemView.findViewById(R.id.tvQuizScore);
//            ivUnMark = itemView.findViewById(R.id.ivUnMark);
            ivAbsent = itemView.findViewById(R.id.ivAbsent);
            ivPresent = itemView.findViewById(R.id.ivPresent);

            ivStudentProfilePic.setOnClickListener(this);
//            ivUnMark.setOnClickListener(this);
            ivPresent.setOnClickListener(this);
            ivAbsent.setOnClickListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {

            int position = this.getAdapterPosition();
            holder = ViewHolder.this;

            switch (view.getId()) {

/*
                case R.id.ivUnMark:
                    selectUnMark();
                    setUnMark(position);
                    break;
*/

                case R.id.ivPresent:
                    selectPresent();
                    setPresent(position);
                    break;

                case R.id.ivAbsent:
                    selectAbsent();
                    setAbsent(position);
                    break;

                case R.id.ivStudentProfilePic:
                    displayStudentProfile(position);
                    break;

            }
        }
    }
}
