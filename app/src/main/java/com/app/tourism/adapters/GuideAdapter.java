package com.app.tourism.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tourism.R;
import com.app.tourism.databinding.GuideRowBinding;
import com.app.tourism.models.UserModel;
import com.app.tourism.uis.activity_home.fragments.home_module.FragmentHome;
import com.app.tourism.uis.activity_home.fragments.home_module.FragmentSearch;

import java.util.List;

public class GuideAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private Fragment fragment;

    public GuideAdapter(Context context, String lang, Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.lang = lang;
        this.fragment = fragment;
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
        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentHome){
                FragmentHome fragmentHome = (FragmentHome) fragment;
                fragmentHome.setItemData(list.get(myHolder.getAdapterPosition()));
            }else if (fragment instanceof FragmentSearch){
                FragmentSearch fragmentSearch = (FragmentSearch) fragment;
                fragmentSearch.setItemData(list.get(myHolder.getAdapterPosition()));
            }
        });


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
