package com.app.tourism.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tourism.R;
import com.app.tourism.databinding.SearchVrRowBinding;
import com.app.tourism.models.SearchVrModel;
import com.app.tourism.uis.activity_home.fragments.camera_module.FragmentCamera;

import java.util.List;

public class SearchVrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SearchVrModel> searchVrModels;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private Fragment fragment;

    public SearchVrAdapter(Context context,List<SearchVrModel> searchVrModels) {
        this.context = context;
        this.searchVrModels=searchVrModels;
        inflater = LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SearchVrRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.search_vr_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(searchVrModels.get(position));
        myHolder.binding.setLang(lang);
        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentCamera){
                FragmentCamera fragmentCamera = (FragmentCamera) fragment;
                fragmentCamera.setItemData(searchVrModels.get(myHolder.getAdapterPosition()));
               }
        });


    }

    @Override
    public int getItemCount() {
        return searchVrModels!=null?searchVrModels.size():0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private SearchVrRowBinding binding;

        public MyHolder(SearchVrRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    public void updateList(List<SearchVrModel> list){
        if (list!=null){
            this.searchVrModels = list;
        }
        notifyDataSetChanged();
    }

}
