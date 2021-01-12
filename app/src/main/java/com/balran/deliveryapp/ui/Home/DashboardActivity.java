package com.balran.deliveryapp.ui.Home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.balran.deliveryapp.Common.Constantes;
import com.balran.deliveryapp.Common.SharedPreferencesManager;
import com.balran.deliveryapp.R;
import com.balran.deliveryapp.retrofit.Response.Restorant;
import com.balran.deliveryapp.ui.CreateRestorantActivity;
import com.balran.deliveryapp.ui.RestorantActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    protected static Activity activity;
    public  static DashboardActivity ctx;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ctx = this; activity=this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_user_restorants)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        setUserData();
    }

    private void setUserData() {
        String name= SharedPreferencesManager.getSomeStringValue(getApplicationContext(),Constantes.PREF_NAME);
        String photo = SharedPreferencesManager.getSomeStringValue(getApplicationContext(),Constantes.PREF_PHOTOURL);

        View headerView = navigationView.getHeaderView(0);
        TextView tv = headerView.findViewById(R.id.tv_user_name);
        tv.setText(name);

        CircleImageView imageView = headerView.findViewById(R.id.iv_user_photo);
        try {
            Glide.with(getApplicationContext())
                    .load(Constantes.API_MINITWITTER_FILES_URL + photo)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(imageView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void getFoods(int idRestorant, String idUser){
        Intent i = new Intent(DashboardActivity.this, FoodsActivity.class);
        i.putExtra("idRestorant", idRestorant);
        i.putExtra("idUser",idUser);
        startActivity(i);
    }

    public static void restorantFoods(int idRestorant, String idUser){
        DashboardActivity.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DashboardActivity.ctx.getFoods(idRestorant, idUser);
            }
        });
    }

    public static void showRestorant(Restorant restorant){
        DashboardActivity.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RestorantActivity restorantActivity = new RestorantActivity();
                Intent i = new Intent(DashboardActivity.ctx, restorantActivity.getClass());
                i.putExtra("restorant", restorant);
                DashboardActivity.ctx.startActivity(i);
            }
        });
    }

    public void startCreateRestorant(){
        Intent i = new Intent(DashboardActivity.this, CreateRestorantActivity.class);
        startActivity(i);
    }
}