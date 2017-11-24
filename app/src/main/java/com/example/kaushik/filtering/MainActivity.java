package com.example.kaushik.filtering;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("Here1", "Here1");
        setContentView(R.layout.activity_main);
        Log.v("Here2", "Here2");
    }

    public void RGBA(View view)
    {
        Bundle b = new Bundle();
        b.putInt("num", 0);
        Intent intent =new Intent(this, ImageManipulationsActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void HIST(View view)
    {
        Bundle b = new Bundle();
        b.putInt("num", 1);
        Intent intent =new Intent(this, ImageManipulationsActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void CANNY(View view)
    {
        Bundle b = new Bundle();
        b.putInt("num", 2);
        Intent intent =new Intent(this, ImageManipulationsActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void SEPTA(View view)
    {
        Bundle b = new Bundle();
        b.putInt("num", 3);
        Intent intent =new Intent(this, ImageManipulationsActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void SOBEL(View view)
    {
        Bundle b = new Bundle();
        b.putInt("num", 4);
        Intent intent =new Intent(this, ImageManipulationsActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void ZOOM(View view)
    {
        Bundle b = new Bundle();
        b.putInt("num", 5);
        Intent intent =new Intent(this, ImageManipulationsActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void PIXELIZE(View view)
    {
        Bundle b = new Bundle();
        b.putInt("num", 6);
        Intent intent =new Intent(this, ImageManipulationsActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void POSTERIZE(View view)
    {
        Bundle b = new Bundle();
        b.putInt("num", 7);
        Intent intent =new Intent(this, ImageManipulationsActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

}
