package com.example.planmyweek.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planmyweek.Comman.Activity;
import com.example.planmyweek.R;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    private Context mContext;
    private List<Activity> mActivityList;
    private OnItemClickListener mListener;

    // Interface to handle click events
    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
        void onCheckboxClick(int position);
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ActivityAdapter(Context context, List<Activity> activityList) {
        mContext = context;
        mActivityList = activityList;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_card_layout, parent, false);
        return new ActivityViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        Activity currentActivity = mActivityList.get(position);

        // Bind data to the views
        holder.mTextViewTitle.setText(currentActivity.getTitle());
        holder.mTextViewDate.setText("Date: " + currentActivity.getDueDate());
        holder.mTextViewTime.setText("Time: " + currentActivity.getDueTime());
        holder.text_category.setText("Category: " + currentActivity.getCategory());
        holder.text_priority.setText("Priority: " + currentActivity.getPriority());
        holder.text_description.setText("Description: " + currentActivity.getDescription());
        holder.mCheckBox.setChecked(currentActivity.isCompleted());

        // Change the background based on isCompleted status
        if (currentActivity.isCompleted()) {
            holder.mLinearLayout.setBackgroundResource(R.drawable.completed_activity_shape); // New background for completed activities
        } else {
            holder.mLinearLayout.setBackgroundResource(R.drawable.shape); // Default background
        }

        // Set click listener for the activity block (navigates to details)
        holder.mLinearLayout.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(position); // Navigate to details
            }
        });

        // Set click listener for the edit button
        holder.mButtonEdit.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onEditClick(position); // Navigate to edit
            }
        });

        // Set click listener for the delete button
        holder.mButtonDelete.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onDeleteClick(position); // Delete the activity
            }
        });

        // Set click listener for the checkbox
        holder.mCheckBox.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onCheckboxClick(position); // Mark activity as completed or not
            }
        });
    }

    @Override
    public int getItemCount() {
        return mActivityList.size();
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextViewTitle;
        public TextView mTextViewDate;
        public TextView mTextViewTime;
        public TextView text_category;
        public TextView text_priority;
        public TextView text_description;
        public CheckBox mCheckBox;
        public Button mButtonEdit;
        public Button mButtonDelete;
        public LinearLayout mLinearLayout;

        public ActivityViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.text_title);
            mTextViewDate = itemView.findViewById(R.id.text_date);
            mTextViewTime = itemView.findViewById(R.id.text_time);
            text_category = itemView.findViewById(R.id.text_category);
            text_priority = itemView.findViewById(R.id.text_priority);
            text_description = itemView.findViewById(R.id.text_description);
            mCheckBox = itemView.findViewById(R.id.check_box);
            mButtonEdit = itemView.findViewById(R.id.btn_edit);
            mButtonDelete = itemView.findViewById(R.id.btn_delete);
            mLinearLayout = itemView.findViewById(R.id.activity_card_layout_block);

            // Set up click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

            mButtonEdit.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onEditClick(position);
                    }
                }
            });

            mButtonDelete.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position);
                    }
                }
            });

            mCheckBox.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onCheckboxClick(position);
                    }
                }
            });
        }
    }
}
