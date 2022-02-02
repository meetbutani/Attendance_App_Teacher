package com.meetbutani.attendanceapp_teacher.AdapterClass;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.meetbutani.attendanceapp_teacher.AttendanceSheetFragment;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelCourse;
import com.meetbutani.attendanceapp_teacher.ModelClass.ModelStudentData;
import com.meetbutani.attendanceapp_teacher.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterCourse extends RecyclerView.Adapter<AdapterCourse.ViewHolder> {

    private final FragmentActivity CONTEXT;
    private final String COURSESPATH = "/app/app/courses";
    private final ArrayList<ModelCourse> arrayListModelCourse;

    private ModelCourse modelCourse;
    private ModelStudentData modelStudentData;
    private String COURSEID;
    private ArrayList<ModelStudentData> aLMSDLec;
    private ArrayList<ModelStudentData> aLMSDA;
    private ArrayList<ModelStudentData> aLMSDB;

    public AdapterCourse(FragmentActivity CONTEXT, ArrayList<ModelCourse> arrayListModelCourse) {
        this.CONTEXT = CONTEXT;
        this.arrayListModelCourse = arrayListModelCourse;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_frag_courses, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvRecViewCourseListCourseName.setText(arrayListModelCourse.get(position).courseName);
    }

    @Override
    public int getItemCount() {
        return arrayListModelCourse.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvRecViewCourseListCourseName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            tvRecViewCourseListCourseName = itemView.findViewById(R.id.tvLayCoursesCourseName);

        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();

            modelCourse = arrayListModelCourse.get(position);
            COURSEID = modelCourse.courseId;

            storeArrayList();

//            Toast.makeText(CONTEXT, position + "", Toast.LENGTH_SHORT).show();
        }

        private void storeArrayList() {
            try {
                aLMSDLec = new ArrayList<>();
                aLMSDA = new ArrayList<>();
                aLMSDB = new ArrayList<>();

                FirebaseFirestore.getInstance().collection(COURSESPATH + "/" + COURSEID + "/students").orderBy("rollNo", Query.Direction.ASCENDING).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                                for (DocumentSnapshot documentSnapshot : list) {
                                    modelStudentData = documentSnapshot.toObject(ModelStudentData.class);

                                    aLMSDLec.add(modelStudentData);

                                    if (modelStudentData.Class.equalsIgnoreCase("A"))
                                        aLMSDA.add(modelStudentData);

                                    if (modelStudentData.Class.equalsIgnoreCase("B"))
                                        aLMSDB.add(modelStudentData);
                                }

                                setFragment();

                            }
                        });
            } catch (Exception e) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(CONTEXT);
                dialog.setMessage("Error: " + e.getMessage()).create().show();
            }
        }

        private void setFragment() {
            Bundle bundleCourse = new Bundle();
            bundleCourse.putSerializable("modelCourse", modelCourse);
            bundleCourse.putSerializable("aLMSDLec", aLMSDLec);
            bundleCourse.putSerializable("aLMSDA", aLMSDA);
            bundleCourse.putSerializable("aLMSDB", aLMSDB);

            AttendanceSheetFragment fragment = new AttendanceSheetFragment();
            fragment.setArguments(bundleCourse);
            CONTEXT.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayMain, fragment, "AttendanceSheetFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }
}
