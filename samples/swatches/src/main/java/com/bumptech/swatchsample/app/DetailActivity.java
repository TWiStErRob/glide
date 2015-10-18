package com.bumptech.swatchsample.app;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.palette.PaletteBitmap;
import com.bumptech.glide.integration.palette.PaletteBitmapViewTarget;
import com.bumptech.glide.integration.palette.PaletteBitmapViewTarget.Builder;

/**
 * Displays an image from an Uri given in the intent and the swatches found for it.
 */
public class DetailActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    View root = findViewById(R.id.layoutRoot);
    ImageView image = (ImageView) findViewById(R.id.image);
    TextView title = (TextView) findViewById(R.id.titleText);
    TextView vibrant = (TextView) findViewById(R.id.vibrant);
    TextView muted = (TextView) findViewById(R.id.muted);
    TextView darkVibrant = (TextView) findViewById(R.id.darkVibrant);
    TextView lightVibrant = (TextView) findViewById(R.id.lightVibrant);
    TextView darkMuted = (TextView) findViewById(R.id.darkMuted);
    TextView lightMuted = (TextView) findViewById(R.id.lightMuted);

    Uri uri = getIntent().getData();

    title.setText(uri.getPath());

    Glide
        .with(this)
        .as(PaletteBitmap.class)
        .load(uri)
        .into(new PaletteBitmapViewTarget.Builder(image)
            .defaultFallback(Color.BLACK, Color.YELLOW)
            .swatch(new MaxPopulationSwatchSelector()).background(root)
            .swatch(Builder.VIBRANT).titleText(title).background(title, 0x80)
            .swatch(Builder.MUTED, Color.BLACK, Color.RED).body(muted)
            .swatch(Builder.MUTED_DARK).body(darkMuted)
            .swatch(Builder.MUTED_LIGHT).body(lightMuted)
            .swatch(Builder.VIBRANT, Color.BLACK, Color.RED).body(vibrant)
            .swatch(Builder.VIBRANT_DARK).body(darkVibrant)
            .swatch(Builder.VIBRANT_LIGHT).body(lightVibrant)
            .build()
        )
    ;
  }
}
