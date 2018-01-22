package com.maciejprogramuje.facebook.imagesimilarity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String FLOWER_1 = "https://static.pexels.com/photos/37349/rose-beautiful-beauty-bloom.jpg";
    public static final String FLOWER_2 = "http://sanctum-inle-resort.com/wp-content/uploads/2015/11/Sanctum_Inle_Resort_Myanmar_Flower_Macro_Cherry_Blossom.jpg";

    private ImageView flower1ImageView;
    private ImageView flower2ImageView;
    private EditText toleranceEditText;
    private TextView resultTextView;

    private double toleranceByUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        flower1ImageView = findViewById(R.id.flower_1_image_view);
        flower2ImageView = findViewById(R.id.flower_2_image_view);
        toleranceEditText = findViewById(R.id.tolerance_edit_text);
        resultTextView = findViewById(R.id.result_text_view);

        toleranceByUser = 25;

        final Bitmap flower1 = getBitmapFromUrl(FLOWER_1);
        final Bitmap flower2 = getBitmapFromUrl(FLOWER_2);

        flower1ImageView.setImageBitmap(flower1);
        flower2ImageView.setImageBitmap(flower2);
        toleranceEditText.setText(String.valueOf(toleranceByUser));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // toleranceByUser=0 - pictures exactly the same
                // toleranceByUser=100 - pictures totally different, but method says that they are the same

                toleranceByUser = Double.valueOf(toleranceEditText.getText().toString());
                if(bitmapsSimilarity(flower1, flower2, toleranceByUser)) {
                    resultTextView.setText("Pictures are silimar!");
                } else {
                    resultTextView.setText("Pictures are different!");
                }
            }
        });
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

    private boolean bitmapsSimilarity(Bitmap secondBitmap, Bitmap firstBitmap, double tolerance) {
        Bitmap scaledSecondBitmap = Bitmap.createScaledBitmap(secondBitmap, 32, 32, false);
        Bitmap scaledFirstBitmap = Bitmap.createScaledBitmap(firstBitmap, 32, 32, false);

        int cumulatedColorDistance = 0;

        for (int x = 0; x < scaledSecondBitmap.getWidth(); x++) {
            for (int y = 0; y < scaledSecondBitmap.getHeight(); y++) {
                int secondColor = scaledSecondBitmap.getPixel(x, y);
                int firstColor = scaledFirstBitmap.getPixel(x, y);
                int expectedRed = Color.red(secondColor);
                int actualRed = Color.red(firstColor);
                int expectedBlue = Color.blue(secondColor);
                int actualBlue = Color.blue(firstColor);
                int expectedGreen = Color.green(secondColor);
                int actualGreen = Color.green(firstColor);
                int colorDistance = Math.abs(expectedRed - actualRed) + Math.abs(expectedBlue - actualBlue) + Math.abs(expectedGreen - actualGreen);
                cumulatedColorDistance += colorDistance;
            }
        }

        double difference = 100.0 * cumulatedColorDistance / 3.0 / (32.0 * 32.0) / 255.0;

        if (difference > tolerance) {
            Log.w("UWAGA", "Pictures are different! (Difference " + difference + "% > " + tolerance + "%)");
            return false;
        } else {
            Log.w("UWAGA", "Pictures are silimar! (Difference " + difference + "% < " + tolerance + "%)");
            return true;
        }
    }
}
