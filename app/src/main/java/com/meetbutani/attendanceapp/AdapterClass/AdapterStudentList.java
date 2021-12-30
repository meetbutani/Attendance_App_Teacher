package com.meetbutani.attendanceapp.AdapterClass;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.meetbutani.attendanceapp.ModelClass.ModelCourse;
import com.meetbutani.attendanceapp.ModelClass.ModelStudentData;
import com.meetbutani.attendanceapp.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterStudentList extends RecyclerView.Adapter<AdapterStudentList.ViewHolder> {

    private final FragmentActivity CONTEXT;
    private final ArrayList<ModelStudentData> arrayListModelStudentData;
    private final ModelCourse modelCourse;
    public ViewHolder holder;
    private ModelStudentData modelStudentData;

    public AdapterStudentList(FragmentActivity CONTEXT, ArrayList<ModelStudentData> arrayListModelStudentData, ModelCourse modelCourse) {
        this.CONTEXT = CONTEXT;
        this.arrayListModelStudentData = arrayListModelStudentData;
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
        modelStudentData = arrayListModelStudentData.get(position);
        try {
            String imageURL = modelStudentData.imageURL;
            if (!imageURL.equalsIgnoreCase(" "))
                Picasso.get().load(Uri.parse(imageURL)).into(holder.ivStudentProfilePic);
        } catch (Exception ignored) {
        }

        String firstName = modelStudentData.firstName;
        String lastName = modelStudentData.lastName;
        String rollNo = modelStudentData.rollNo;

        holder.tvRecViewSheetHomepageName.setText((firstName + " " + lastName));
        holder.tvRecViewSheetHomepageRollNo.setText(rollNo);

        this.holder = holder;

    }

    @Override
    public int getItemCount() {
        return arrayListModelStudentData.size();
    }

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CircularImageView ivStudentProfilePic;
        private final TextView tvRecViewSheetHomepageName, tvRecViewSheetHomepageRollNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivStudentProfilePic = itemView.findViewById(R.id.ivStudentProfilePic);
            tvRecViewSheetHomepageName = itemView.findViewById(R.id.tvRVSAName);
            tvRecViewSheetHomepageRollNo = itemView.findViewById(R.id.tvRVSARollNo);

            ivStudentProfilePic.setOnClickListener(this);
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            holder = ViewHolder.this;
//            Toast.makeText(CONTEXT, position + "", Toast.LENGTH_SHORT).show();

            if (view.getId() == R.id.ivStudentProfilePic) {
                displayStudentProfile(position);
            }

        }
    }
}
