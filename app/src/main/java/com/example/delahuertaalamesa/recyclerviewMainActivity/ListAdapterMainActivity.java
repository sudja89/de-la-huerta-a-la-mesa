package com.example.delahuertaalamesa.recyclerviewMainActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delahuertaalamesa.R;

import java.util.List;

public class ListAdapterMainActivity extends RecyclerView.Adapter<ListAdapterMainActivity.ViewHolder> {

    private List<ListProductsMainActivity> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListAdapterMainActivity(List<ListProductsMainActivity> itemlist, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = itemlist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_products, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListProductsMainActivity item = mData.get(position);
        holder.imageView.setImageResource(item.getPicture());
        holder.name.setText(item.getCommon_name());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_car);
            name = itemView.findViewById(R.id.tv_car);
        }

    }

}
