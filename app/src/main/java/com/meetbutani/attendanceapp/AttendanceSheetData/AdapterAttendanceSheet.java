package com.meetbutani.attendanceapp.AttendanceSheetData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.meetbutani.attendanceapp.CourseData.ModelCourse;
import com.meetbutani.attendanceapp.R;

import java.util.ArrayList;

public class AdapterAttendanceSheet extends RecyclerView.Adapter<AdapterAttendanceSheet.ViewHolder> {

    private final FragmentActivity CONTEXT;
    private final ArrayList<ModelAttendanceSheet> arrayListModelAttendanceSheet;
    private final ModelCourse modelCourse;

    public AdapterAttendanceSheet(FragmentActivity CONTEXT, ArrayList<ModelAttendanceSheet> arrayListModelAttendanceSheet, ModelCourse modelCourse) {
        this.CONTEXT = CONTEXT;
        this.arrayListModelAttendanceSheet = arrayListModelAttendanceSheet;
        this.modelCourse = modelCourse;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_frag_attendance_sheet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvRecViewCourseHomepageDate.setText(arrayListModelAttendanceSheet.get(position).date);
        holder.tvRecViewCourseHomepageTime.setText((arrayListModelAttendanceSheet.get(position).startTime + " - " + arrayListModelAttendanceSheet.get(position).endTime));
    }

    @Override
    public int getItemCount() {
        return arrayListModelAttendanceSheet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvRecViewCourseHomepageDate, tvRecViewCourseHomepageTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            tvRecViewCourseHomepageDate = itemView.findViewById(R.id.tvRecViewCourseHomepageDate);
            tvRecViewCourseHomepageTime = itemView.findViewById(R.id.tvRecViewCourseHomepageTime);

        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            Toast.makeText(CONTEXT, position + "", Toast.LENGTH_SHORT).show();
//            ModelAttendanceSheet modelAttendanceSheet = dataListCourseHomepage.get(position);
//            Intent intent = new Intent(CONTEXT, SheetHomepageActivity.class);
//            intent.putExtra("modelAttendanceSheet", modelAttendanceSheet);
//            intent.putExtra("modelCourseList", modelCourse);
//            CONTEXT.startActivity(intent);
        }
    }
}
