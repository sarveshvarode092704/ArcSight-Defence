package com.example.arcsightdefence;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GunRecommendationAdapter extends RecyclerView.Adapter<GunRecommendationAdapter.ViewHolder> {
    private List<String> gunList;

    public GunRecommendationAdapter(List<String> gunList) {
        this.gunList = gunList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gun, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String gun = gunList.get(position);
        holder.gunName.setText(gun);
    }

    @Override
    public int getItemCount() {
        return gunList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView gunName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gunName = itemView.findViewById(R.id.gunName);
        }
    }
}
