package com.bumptech.glide.integration.palette;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.module.GlideModule;

public class PaletteGlideModule implements GlideModule {
  @Override
  public void applyOptions(Context context, GlideBuilder builder) {

  }

  @Override
  public void registerComponents(Context context, Registry registry) {
    registry.register(Bitmap.class, PaletteBitmap.class, new PaletteBitmapTranscoder(context));
  }
}
