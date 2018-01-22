package com.maciejprogramuje.facebook.imagesimilarity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String FLOWER_1 = "https://static.pexels.com/photos/37349/rose-beautiful-beauty-bloom.jpg";
    public static final String FLOWER_2 = "http://sanctum-inle-resort.com/wp-content/uploads/2015/11/Sanctum_Inle_Resort_Myanmar_Flower_Macro_Cherry_Blossom.jpg";

    private ImageView flower1ImageView;
    private ImageView flower2ImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        flower1ImageView = findViewById(R.id.flower_1_image_view);
        flower2ImageView = findViewById(R.id.flower_2_image_view);

        Bitmap flower1 = getBitmapFromUrl(FLOWER_1);
        Bitmap flower2 = getBitmapFromUrl(FLOWER_2);

        flower1ImageView.setImageBitmap(flower1);
        flower2ImageView.setImageBitmap(flower2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static Bitmap getBitmapFromUrl(String coverUrl) {
        Bitmap bitMap = null;
        try {
            URL url = new URL(coverUrl);
            InputStream is = url.openConnection().getInputStream();
            bitMap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitMap;
    }

    private static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
}
