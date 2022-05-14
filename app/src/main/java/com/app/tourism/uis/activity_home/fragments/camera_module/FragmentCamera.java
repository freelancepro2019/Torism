package com.app.tourism.uis.activity_home.fragments.camera_module;


import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.tourism.BuildConfig;
import com.app.tourism.R;
import com.app.tourism.adapters.GuideAdapter;
import com.app.tourism.adapters.SearchVrAdapter;
import com.app.tourism.databinding.FragmentCameraBinding;
import com.app.tourism.databinding.FragmentHomeBinding;
import com.app.tourism.models.SearchVrModel;
import com.app.tourism.models.UserModel;
import com.app.tourism.uis.activity_home.HomeActivity;
import com.app.tourism.uis.activity_send_order.SendOrderActivity;
import com.app.tourism.uis.common_ui.activity_base.ActivityBase;
import com.app.tourism.uis.common_ui.activity_base.FragmentBase;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FragmentCamera extends FragmentBase {
    private HomeActivity activity;
    private FragmentCameraBinding binding;
    private SearchVrAdapter searchVrAdapter;
    private List<SearchVrModel> searchVrModels;
    private String  image;
    String title, link, displayed_link, snippet;
    private Bitmap bitmap;
    private ActivityResultLauncher<String[]> permissions;
    private ActivityResultLauncher<Intent> launcher;
    private int req=1;
    private Uri cameraUri = null;
    private String cameraImagePath;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        permissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
            Log.e("permissions", isGranted.toString());
            if (!isGranted.containsValue(false)) {


                    openCamera();

            } else {
                Toast.makeText(activity, "Permission to access photo denied", Toast.LENGTH_SHORT).show();

            }
        });
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
          if (req == 1 && result.getResultCode() == Activity.RESULT_OK) {
                if (cameraUri != null) {
                    image=cameraUri.toString();
                    binding.icon.setVisibility(View.GONE);
                    binding.image.setImageURI(cameraUri);

                    Glide.with(activity)
                            .asBitmap()
                            .load(cameraUri)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    FragmentCamera.this.bitmap=resource;

                                }
                            });


                }

            }

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        searchVrModels = new ArrayList<>();
        searchVrAdapter = new SearchVrAdapter(activity, searchVrModels);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(searchVrAdapter);

        binding.btnSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchVrModels.clear();
                searchVrAdapter.notifyDataSetChanged();
                checkCameraPermission();
            }
        });
        binding.btnShowResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchVrModels.clear();
                searchVrAdapter.notifyDataSetChanged();
             if (bitmap!=null){

                 getResult(bitmap);

             }else {

             }
            }
        });


    }


    private void getResult(Bitmap imageBitmap) {
       // try {
            FirebaseVisionImage image;
            image = FirebaseVisionImage.fromBitmap(imageBitmap);
            FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler();
            labeler.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                @Override
                public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
                    String searchQuery = firebaseVisionImageLabels.get(0).getText();
                    getSearchResults(searchQuery);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity, "Failed To Detect Image"+"    "+e, Toast.LENGTH_SHORT).show();
               Log.e("eeeee",e+"");
                }
            });
     /*   } catch (Exception e) {

        }*/


    }

    private void getSearchResults(String searchQuery) {
        String apikey = "89bb969b41006602b5b3f1fd14f2f2c7192afbc3928038297eb15c297ea1cd54";
        Log.e("xxxxx",searchQuery.trim() );
        String url = "https://serpapi.com/search.json?q=" + searchQuery.trim() + "&location=Saudi Arabia&hl=ar&gl=sa&google_domain=google.com&api_key=" + apikey;
        RequestQueue queue = Volley.newRequestQueue(activity);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray organicArray = response.getJSONArray("organic_results");
                    for (int i = 0; i < organicArray.length(); i++) {
                        JSONObject organicObj = organicArray.getJSONObject(i);
                        if (organicObj.has("title")) {
                            title = organicObj.getString("title");

                        }
                        if (organicObj.has("link")) {
                            link = organicObj.getString("link");
                        }
                        if (organicObj.has("displayed_link")) {
                            displayed_link = organicObj.getString("displayed_link");
                        }
                        if (organicObj.has("snippet")) {
                            snippet = organicObj.getString("snippet");
                        }
                        searchVrModels.add(new SearchVrModel(title, link, displayed_link, snippet));


                    }
                    searchVrAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "No results found", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);


    }

    public void setItemData(SearchVrModel searchVrModel) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(searchVrModel.getLink()));
        startActivity(intent);
    }




    private void checkCameraPermission() {
        req = 1;
        String[] permissions = new String[]{ActivityBase.WRITE_REQ, ActivityBase.CAM_REQ};
        if (ActivityCompat.checkSelfPermission(activity, permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, permissions[1]) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            this.permissions.launch(permissions);

        }
    }




    private void openCamera() {
        req = 1;
        File fileImage = createImageFile();
        if (fileImage != null) {
            cameraUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", fileImage);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            launcher.launch(intent);
        } else {
            Toast.makeText(activity, "You don't allow to access photos", Toast.LENGTH_SHORT).show();
        }

    }

    private File createImageFile() {
        File imageFile = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH:mm", Locale.ENGLISH).format(new Date());
        String imageName = "JPEG_" + timeStamp + "_";
        File file = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(), "tourism_app_photos");
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            imageFile = File.createTempFile(imageName, ".jpg", file);
            cameraImagePath = imageFile.getAbsolutePath();
            Log.e("path",cameraImagePath+"");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("err", e.getMessage());
        }

        return imageFile;

    }


}