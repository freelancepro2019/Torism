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
import com.app.tourism.databinding.UserOrderRowBinding;
import com.app.tourism.models.OfferModel;
import com.app.tourism.models.UserModel;
import com.app.tourism.uis.activity_home.fragments.home_module.FragmentHome;
import com.app.tourism.uis.activity_home.fragments.profile_module.FragmentOffers;

import java.util.List;

public class UserOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OfferModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private Fragment fragment;

    public UserOrderAdapter(Context context, Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.lang = lang;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        UserOrderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.user_order_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.addRate.setOnClickListener(view -> {
            if (fragment instanceof FragmentOffers) {
                FragmentOffers fragmentOffers = (FragmentOffers) fragment;
                fragmentOffers.createRateSheetDialog(list.get(myHolder.getAdapterPosition()), myHolder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private UserOrderRowBinding binding;

        public MyHolder(UserOrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    public void updateList(List<OfferModel> list) {
        if (list != null) {
            this.list = list;
        }
        notifyDataSetChanged();
    }

    public void updateItem(OfferModel offerModel, int pos) {
        if (list != null) {
            list.set(pos, offerModel);
            notifyItemChanged(pos);
        }
    }

}
