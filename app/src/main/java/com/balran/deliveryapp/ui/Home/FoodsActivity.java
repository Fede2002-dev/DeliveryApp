package com.balran.deliveryapp.ui.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.balran.deliveryapp.R;
import com.balran.deliveryapp.ui.Home.Fragments.FoodsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

public class FoodsActivity extends AppCompatActivity {
    private FloatingActionButton fabShop;
    private int idRestorant;
    private String idUser;
    private FoodsFragment foodsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods);
        getSupportActionBar().hide();

        //Init variables
        fabShop = findViewById(R.id.fab_shop);
        idRestorant= getIntent().getExtras().getInt("idRestorant");
        idUser = getIntent().getExtras().getString("idUser");
        foodsFragment = FoodsFragment.newInstance(idRestorant,null);

        //Loading foodFragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_food_container, foodsFragment)
                .commit();

        fabShop.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_shopping_cart_24));

        //OnClick methods

        fabShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<Integer, Integer> foodMap = (HashMap)foodsFragment.getFoodMap();
                if(foodMap!=null){
                    Intent i = new Intent(FoodsActivity.this, ShopActivity.class);
                    Bundle extras = new Bundle();
                    extras.putSerializable("foodMap", foodMap);
                    extras.putInt("idRestorant", idRestorant);
                    extras.putString("idUser", idUser);

                    i.putExtras(extras);
                    startActivity(i);

                }
            }
        });
    }
}