package com.bumptech.swatchsample.app;

import static com.bumptech.glide.integration.palette.PaletteTargetBuilder.MUTED;
import static com.bumptech.glide.integration.palette.PaletteTargetBuilder.MUTED_DARK;
import static com.bumptech.glide.integration.palette.PaletteTargetBuilder.MUTED_LIGHT;
import static com.bumptech.glide.integration.palette.PaletteTargetBuilder.VIBRANT;
import static com.bumptech.glide.integration.palette.PaletteTargetBuilder.VIBRANT_DARK;
import static com.bumptech.glide.integration.palette.PaletteTargetBuilder.VIBRANT_LIGHT;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.palette.PaletteBitmap;
import com.bumptech.glide.integration.palette.PaletteTargetBuilder;

/**
 * Displays an image from an Uri given in the intent and the swatches found for it.
 */
public class DetailActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    View rootView = findViewById(R.id.layoutRoot);
    ImageView imageView = (ImageView) findViewById(R.id.image);
    TextView titleView = (TextView) findViewById(R.id.titleText);
    TextView vibrantView = (TextView) findViewById(R.id.vibrant);
    TextView mutedView = (TextView) findViewById(R.id.muted);
    TextView darkVibrantView = (TextView) findViewById(R.id.darkVibrant);
    TextView lightVibrantView = (TextView) findViewById(R.id.lightVibrant);
    TextView darkMutedView = (TextView) findViewById(R.id.darkMuted);
    TextView lightMutedView = (TextView) findViewById(R.id.lightMuted);

    Uri uri = getIntent().getData();

    titleView.setText(uri.getPath());

    Glide
        .with(this)
        .as(PaletteBitmap.class)
        .load(uri)
        .into(new PaletteTargetBuilder(imageView)
            .background(new MaxPopulationSwatchSelector(), rootView)
            .swatch(VIBRANT).titleText(titleView).background(titleView, 0x80).finish()
            .body(MUTED, mutedView)
            .body(MUTED_DARK, darkMutedView)
            .body(MUTED_LIGHT, lightMutedView)
            .body(VIBRANT, vibrantView)
            .body(VIBRANT_DARK, darkVibrantView)
            .body(VIBRANT_LIGHT, lightVibrantView)
            .build()
        )
    ;
  }
}
