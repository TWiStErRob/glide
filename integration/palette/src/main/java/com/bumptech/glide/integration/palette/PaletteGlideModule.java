package com.bumptech.glide.integration.palette;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.module.GlideModule;

/**
 * A {@link com.bumptech.glide.module.GlideModule} implementation to add an ability to use {@link
 * android.support.v7.graphics.Palette} in an app with ease.
 *
 * <p> If you're using gradle, you can include this module simply by depending on the aar, the
 * module will be merged in by manifest merger. For other build systems or for more more
 * information, see {@link com.bumptech.glide.module.GlideModule}. </p>
 */
public class PaletteGlideModule implements GlideModule {
  @Override
  public void applyOptions(Context context, GlideBuilder builder) {

  }

  @Override
  public void registerComponents(Context context, Registry registry) {
    registry.register(Bitmap.class, PaletteBitmap.class, new PaletteBitmapTranscoder(context));
  }
}
