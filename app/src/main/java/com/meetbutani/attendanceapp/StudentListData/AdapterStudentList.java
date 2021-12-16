package com.meetbutani.attendanceapp.StudentListData;

import android.net.Uri;
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
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterStudentList extends RecyclerView.Adapter<AdapterStudentList.ViewHolder> {

    private final FragmentActivity CONTEXT;
    private final ArrayList<ModelStudentList> arrayListModelStudentList;
    private final ModelCourse modelCourse;
    public ViewHolder holder;

    public AdapterStudentList(FragmentActivity CONTEXT, ArrayList<ModelStudentList> arrayListModelStudentList, ModelCourse modelCourse) {
        this.CONTEXT = CONTEXT;
        this.arrayListModelStudentList = arrayListModelStudentList;
        this.modelCourse = modelCourse;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_frag_students, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            String imageURL = arrayListModelStudentList.get(position).imageURL;
            if (!imageURL.equalsIgnoreCase(" "))
                Picasso.get().load(Uri.parse(imageURL)).into(holder.ivStudentProfilePic);
        } catch (Exception ignored) {
        }

        String firstName = arrayListModelStudentList.get(position).firstName;
        String lastName = arrayListModelStudentList.get(position).lastName;
        String rollNo = arrayListModelStudentList.get(position).rollNo;

        holder.tvRecViewSheetHomepageName.setText((firstName + " " + lastName));
        holder.tvRecViewSheetHomepageRollNo.setText(rollNo);

        this.holder = holder;

    }

    @Override
    public int getItemCount() {
        return arrayListModelStudentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CircularImageView ivStudentProfilePic;
        private final TextView tvRecViewSheetHomepageName, tvRecViewSheetHomepageRollNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivStudentProfilePic = itemView.findViewById(R.id.ivStudentProfilePic);
            tvRecViewSheetHomepageName = itemView.findViewById(R.id.tvRecViewSheetHomepageName);
            tvRecViewSheetHomepageRollNo = itemView.findViewById(R.id.tvRecViewSheetHomepageRollNo);

            ivStudentProfilePic.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            holder = ViewHolder.this;

            Toast.makeText(CONTEXT, position + "", Toast.LENGTH_SHORT).show();
//            ModelStudentList modelAttendanceSheet = dataListCourseHomepage.get(position);
//            Intent intent = new Intent(CONTEXT, SheetHomepageActivity.class);
//            intent.putExtra("modelAttendanceSheet", modelAttendanceSheet);
//            intent.putExtra("modelCourseList", modelCourse);
//            CONTEXT.startActivity(intent);
        }
    }
}
