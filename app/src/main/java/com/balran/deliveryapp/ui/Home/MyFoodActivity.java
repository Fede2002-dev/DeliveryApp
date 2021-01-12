package com.balran.deliveryapp.ui.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.balran.deliveryapp.R;
import com.balran.deliveryapp.retrofit.Response.Food;
import com.balran.deliveryapp.ui.EditFoodActivity;
import com.balran.deliveryapp.ui.FoodActivity;
import com.balran.deliveryapp.ui.Home.Fragments.MyFoodsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyFoodActivity extends AppCompatActivity {
    private FloatingActionButton fabShop;
    private int idRestorant;
    private MyFoodsFragment foodsFragment;
    public static MyFoodActivity myFoodActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods);
        setTitle("Mis platos");

        //Init variables
        fabShop = findViewById(R.id.fab_shop);
        idRestorant= getIntent().getExtras().getInt("idRestorant");
        foodsFragment = MyFoodsFragment.newInstance(idRestorant);
        myFoodActivity = this;

        //Loading foodFragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_food_container, foodsFragment)
                .commit();

        //OnClick methods

        fabShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyFoodActivity.this, FoodActivity.class);
                i.putExtra("idRestorant", idRestorant);
                startActivity(i);
            }
        });
    }


    public void startEditActivity(Food food){
        Intent i = new Intent(MyFoodActivity.this, EditFoodActivity.class);
        i.putExtra("food", food);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();

        foodsFragment = MyFoodsFragment.newInstance(idRestorant);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_food_container, foodsFragment)
                .commit();
    }
}