package com.example.myapplication.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class YearPickerAdapter extends RecyclerView.Adapter<YearPickerAdapter.Year> {
    List<String> dataList;
    int mSelectedPosition = -1; //   默认没有选中任何item
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public YearPickerAdapter(List<String> dataList) {

        this.dataList = dataList;
    }


    @NonNull
    @Override
    public Year onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_year, parent, false);

        return new Year(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Year holder, int position) {
        holder.mTextView.setText(dataList.get(position));
        holder.bind(position,mSelectedPosition);
    }


    @Override
    public int getItemCount() {
        return  dataList.size();
    }

    public void setSelectedPosition(int position) {

        mSelectedPosition = position;

        notifyDataSetChanged();

    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }


    public class Year extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mTextView;

        public Year(@NonNull View itemView) {
            super(itemView);
            mTextView=itemView.findViewById(R.id.year_1);
            mTextView.setOnClickListener(this);
        }

        public void bind(int position, int selectedPosition) {

            if (position == selectedPosition) {
                mTextView.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.year_selected));
                mTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
            }else{
                mTextView.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.year_unselected));
                mTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.grey_707070));
            }
        }


        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(getAdapterPosition());
            }
        }

    }

}
