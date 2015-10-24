package com.bumptech.swatchsample.app;

import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;

import com.bumptech.glide.integration.palette.PaletteActionGroup;

class MaxPopulationSwatchSelector implements PaletteActionGroup.SwatchSelector {
  @Override
  public Palette.Swatch select(@NonNull Palette palette) {
    Palette.Swatch maxSwatch = null;
    for (Palette.Swatch swatch : palette.getSwatches()) {
      if (maxSwatch == null || maxSwatch.getPopulation() < swatch.getPopulation()) {
        maxSwatch = swatch;
      }
    }

    return maxSwatch;
  }
}
