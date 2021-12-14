package com.meetbutani.attendanceapp.CourseData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.meetbutani.attendanceapp.MainActivity;
import com.meetbutani.attendanceapp.R;
import com.meetbutani.attendanceapp.StudentsFragment;

import java.util.ArrayList;

public class AdapterCourse extends RecyclerView.Adapter<AdapterCourse.ViewHolder> {

    private final FragmentActivity CONTEXT;
    private final ArrayList<ModelCourse> arrayListModelCourse;

    public AdapterCourse(FragmentActivity CONTEXT, ArrayList<ModelCourse> arrayListModelCourse) {
        this.CONTEXT = CONTEXT;
        this.arrayListModelCourse = arrayListModelCourse;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_courses, parent, false);
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

            FragmentTransaction fragmentTransaction = CONTEXT.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayMain, new StudentsFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            /*ModelCourse modelCourse = arrayListModelCourse.get(position);
            Intent intent = new Intent(CONTEXT, CourseHomepageActivity.class);
            intent.putExtra("modelCourse", modelCourse);
            CONTEXT.startActivity(intent);*/
            Toast.makeText(CONTEXT, position + "", Toast.LENGTH_SHORT).show();
        }
    }
}
