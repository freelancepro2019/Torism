package com.app.tourism.uis.activity_home;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.app.tourism.R;
import com.app.tourism.adapters.MyPagerAdapter;
import com.app.tourism.databinding.ActivityHomeBinding;
import com.app.tourism.tags.Tags;
import com.app.tourism.uis.common_ui.activity_base.ActivityBase;
import com.app.tourism.uis.common_ui.activity_login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends ActivityBase implements ViewPager.OnPageChangeListener ,NavigationBarView.OnItemSelectedListener{
    private ActivityHomeBinding binding;
    private List<Fragment> fragments;
    private MyPagerAdapter adapter;
    private Map<Integer,Integer> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        initView();
    }

    private void initView() {
        map = new HashMap<>();
        fragments = new ArrayList<>();
        if (getUserModel().getUser_type().equals(Tags.user_normal)){
            binding.bottomNavigationView.inflateMenu(R.menu.bottom_navigation_menu);
            fragments.add(FragmentNavigationBase.newInstance(R.layout.fragment_home_base,R.id.navHomeHostFragment));
            fragments.add(FragmentNavigationBase.newInstance(R.layout.fragment_camera_base,R.id.navCameraHostFragment));
            map.put(0,R.id.home);
            map.put(1,R.id.camera);
            map.put(2,R.id.profile);
        }else {
            binding.bottomNavigationView.inflateMenu(R.menu.bottom_navigation_guide_menu);

            fragments.add(FragmentNavigationBase.newInstance(R.layout.fragment_home_guide_base,R.id.navHomeGuideHostFragment));
            map.put(0,R.id.home);
            map.put(1,R.id.profile);
        }
        fragments.add(FragmentNavigationBase.newInstance(R.layout.fragment_profile_base,R.id.navProfileHostFragment));


        adapter = new MyPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,fragments,new ArrayList<>());
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(fragments.size());
        binding.pager.addOnPageChangeListener(this);
        binding.bottomNavigationView.setOnItemSelectedListener(this);

    }

    public void logout() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null){
            mAuth.signOut();
            clearUserModel();
            navigateToLoginActivity();
        }
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int itemId = map.get(position);
        if (binding.bottomNavigationView.getSelectedItemId()!=itemId){
            binding.bottomNavigationView.setSelectedItemId(itemId);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int page = getPagePos(item.getItemId());
        if (page!=binding.pager.getCurrentItem()){
            binding.pager.setCurrentItem(page);
        }

        return true;
    }

    private int getPagePos(int itemId){
        for (int pos :map.keySet()){
            int id = map.get(pos);
            if (id==itemId){
                return pos;
            }
        }
        return 0;
    }

    @Override
    public void onBackPressed() {
        FragmentNavigationBase fragmentNavigationBase = (FragmentNavigationBase) fragments.get(binding.pager.getCurrentItem());
        if (fragmentNavigationBase.onBackPressed()){

        }else if (binding.pager.getCurrentItem()!=0){
            binding.pager.setCurrentItem(0);
        }else {
            super.onBackPressed();

        }

    }
}