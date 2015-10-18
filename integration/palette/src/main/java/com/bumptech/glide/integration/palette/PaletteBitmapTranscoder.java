package com.bumptech.glide.integration.palette;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;

/**
 * A {@link com.bumptech.glide.load.resource.transcode.ResourceTranscoder} for generating {@link
 * android.support.v7.graphics.Palette}s from {@link android.graphics.Bitmap}s in the background.
 */
public class PaletteBitmapTranscoder implements ResourceTranscoder<Bitmap, PaletteBitmap> {
  public interface PaletteGenerator {
    Palette generate(Bitmap bitmap);
  }

  private final BitmapPool bitmapPool;
  private final PaletteGenerator generator;

  public PaletteBitmapTranscoder(Context context) {
    this(context, new DefaultPaletteGenerator(null));
  }

  /**
   * @param numColors maximum number of swatches to generate (may be less)
   * @see android.support.v7.graphics.Palette#generate(android.graphics.Bitmap, int)
   */
  public PaletteBitmapTranscoder(Context context, int numColors) {
    this(context, new DefaultPaletteGenerator(numColors));
  }

  public PaletteBitmapTranscoder(Context context, PaletteGenerator generator) {
    this.bitmapPool = Glide.get(context).getBitmapPool();
    this.generator = generator;
  }

  @Override
  public Resource<PaletteBitmap> transcode(Resource<Bitmap> toTranscode) {
    Palette palette = generator.generate(toTranscode.get());
    PaletteBitmap result = new PaletteBitmap(toTranscode.get(), palette);
    return new PaletteBitmapResource(result, bitmapPool);
  }

  private static class DefaultPaletteGenerator implements PaletteGenerator {
    private final Integer numColors;

    public DefaultPaletteGenerator(Integer numColors) {
      this.numColors = numColors;
    }

    @Override
    public Palette generate(Bitmap bitmap) {
      Palette.Builder builder = new Palette.Builder(bitmap);
      if (numColors != null) {
        builder.maximumColorCount(numColors);
      }
      return builder.generate();
    }
  }
}
