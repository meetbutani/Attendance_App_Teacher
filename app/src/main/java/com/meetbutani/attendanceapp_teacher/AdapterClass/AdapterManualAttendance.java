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
import com.meetbutani.attendanceapp_teacher.ManualAttendanceFragment;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelAttendanceSheet;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelCourse;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelStudentData;
import com.meetbutani.attendanceapp_teacher.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterManualAttendance extends RecyclerView.Adapter<AdapterManualAttendance.ViewHolder> {

    private final ArrayList<ModelStudentData> arrayListModelStudentData;
    private final ModelCourse modelCourse;
    private final ModelAttendanceSheet modelAttendanceSheet;
    private final HashMap<String, String> attendanceList;
    private final String COURSEPATH = "/app/app/courses";
    private final ManualAttendanceFragment manualAttendanceFragment;
    public ViewHolder holder;
    private ModelStudentData modelStudentAttendance;
    private FragmentActivity CONTEXT;
    private Context context;

    public AdapterManualAttendance(FragmentActivity CONTEXT, ArrayList<ModelStudentData> arrayListModelStudentData, ModelCourse modelCourse, ModelAttendanceSheet modelAttendanceSheet, HashMap<String, String> attendanceList, ManualAttendanceFragment manualAttendanceFragment) {
        this.CONTEXT = CONTEXT;
        this.arrayListModelStudentData = arrayListModelStudentData;
        this.modelCourse = modelCourse;
        this.modelAttendanceSheet = modelAttendanceSheet;
        this.attendanceList = attendanceList;
        this.manualAttendanceFragment = manualAttendanceFragment;
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

        this.holder = holder;

        String attendance = attendanceList.get(rollNo);

        if (attendance != null && !attendance.isEmpty()) {
//            if (attendance.equalsIgnoreCase("unMark")) selectUnMark();
            if (attendance.equalsIgnoreCase("present")) selectPresent();
            else if (attendance.equalsIgnoreCase("absent")) selectAbsent();
//            else selectUnMark();
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
        FirebaseFirestore.getInstance().collection(COURSEPATH + "/" + modelCourse.courseId + "/sheets/" + modelAttendanceSheet.sheetId + "/attendance")
                .document("attendance").update(rollNo, "absent");

        manualAttendanceFragment.attendanceList.replace(rollNo, "absent");
        updateAdapter();
    }

    private void updateAdapter() {
        if (manualAttendanceFragment.rBtnFragManAttPresent.isChecked())
            manualAttendanceFragment.displayPresent();
        else if (manualAttendanceFragment.rBtnFragManAttAbsent.isChecked())
            manualAttendanceFragment.displayAbsent();
        else if (manualAttendanceFragment.rBtnFragManAttAll.isChecked())
            manualAttendanceFragment.displayAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setPresent(int position) {
        String rollNo = arrayListModelStudentData.get(position).rollNo;
        FirebaseFirestore.getInstance().collection(COURSEPATH + "/" + modelCourse.courseId + "/sheets/" + modelAttendanceSheet.sheetId + "/attendance")
                .document("attendance").update(rollNo, "present");

        manualAttendanceFragment.attendanceList.replace(rollNo, "present");
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
