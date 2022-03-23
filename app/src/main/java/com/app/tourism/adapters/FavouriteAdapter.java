package com.app.tourism.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tourism.R;
import com.app.tourism.databinding.FavoriteRowBinding;
import com.app.tourism.databinding.GuideRowBinding;
import com.app.tourism.models.FavoriteModel;
import com.app.tourism.models.UserModel;
import com.app.tourism.uis.activity_home.fragments.home_module.FragmentHome;
import com.app.tourism.uis.activity_home.fragments.profile_module.FragmentFavorite;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FavoriteModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public FavouriteAdapter(Context context, Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        FavoriteRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.favorite_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.imageUnFav.setOnClickListener(view -> {
            if (fragment instanceof FragmentFavorite){
                FragmentFavorite fragmentFavorite = (FragmentFavorite) fragment;
                fragmentFavorite.unFavorite(list.get(myHolder.getAdapterPosition()));
            }
        });

        myHolder.binding.call.setOnClickListener(view -> {
            if (fragment instanceof FragmentFavorite){
                FragmentFavorite fragmentFavorite = (FragmentFavorite) fragment;
                String phone = list.get(myHolder.getAdapterPosition()).getGuide_phone();
                fragmentFavorite.call(phone);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private FavoriteRowBinding binding;

        public MyHolder(FavoriteRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    public void updateList(List<FavoriteModel> list){
        if (list!=null){
            this.list = list;
        }
        notifyDataSetChanged();
    }

}
