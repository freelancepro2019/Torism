package com.app.tourism.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tourism.R;
import com.app.tourism.databinding.GuideRowBinding;
import com.app.tourism.models.UserModel;

import java.util.List;

public class GuideAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    public GuideAdapter(Context context,String lang) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.lang = lang;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        GuideRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.guide_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.setLang(lang);


    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private GuideRowBinding binding;

        public MyHolder(GuideRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    public void updateList(List<UserModel> list){
        if (list!=null){
            this.list = list;
        }
        notifyDataSetChanged();
    }

}
