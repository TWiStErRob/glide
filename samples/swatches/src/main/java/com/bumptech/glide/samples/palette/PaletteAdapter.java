package com.bumptech.glide.samples.palette;

import static com.bumptech.glide.integration.palette.PaletteTargetBuilder.MUTED_DARK;
import static com.bumptech.glide.integration.palette.PaletteTargetBuilder.VIBRANT;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.palette.PaletteActionGroup.SwatchSelector;
import com.bumptech.glide.integration.palette.PaletteActionGroup.SwatchTarget;
import com.bumptech.glide.integration.palette.PaletteBitmap;
import com.bumptech.glide.integration.palette.PaletteBitmapViewTarget;
import com.bumptech.glide.integration.palette.PaletteTargetBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.TransitionFactory;

/**
 * Display an image and some text with colors calculated with {@link
 * android.support.v7.graphics.Palette}.
 */
class PaletteAdapter extends BaseAdapter {
  private final Uri[] urls;

  public PaletteAdapter(Uri[] urls) {
    this.urls = urls;
  }

  @Override
  public int getCount() {
    return urls.length;
  }

  @Override
  public Uri getItem(int position) {
    return urls[position];
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      convertView = inflater.inflate(R.layout.list_item, parent, false);
      holder = new ViewHolder(convertView);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    bindView(position, holder);
    return convertView;
  }

  private void bindView(int position, final ViewHolder holder) {
    Uri url = getItem(position);
    holder.titleText.setText(url.getPath());
    holder.bodyText.setText(url.toString());

    Glide
        .with(holder.image.getContext())
        .as(PaletteBitmap.class)
        .load(url)
        .transition(new GenericTransitionOptions<PaletteBitmap>().transition(CROSS_FADE_FACTORY))
        .into(holder.target);
  }

  private static final TransitionFactory<PaletteBitmap> CROSS_FADE_FACTORY
      = new TransitionFactory<PaletteBitmap>() {
    private DrawableCrossFadeFactory realFactory = new DrawableCrossFadeFactory(1000);

    @Override
    public Transition<PaletteBitmap> build(DataSource dataSource, boolean isFirstResource) {
      final Transition<Drawable> transition = realFactory.build(dataSource, isFirstResource);
      return new Transition<PaletteBitmap>() {
        @Override
        public boolean transition(PaletteBitmap current, ViewAdapter adapter) {
          Resources resources = adapter.getView().getResources();
          Drawable currentBitmap = new BitmapDrawable(resources, current.bitmap);
          return transition.transition(currentBitmap, adapter);
        }
      };
    }
  };

  private static class ViewHolder {
    private static final SwatchSelector MAX_POPULATION = new MaxPopulationSwatchSelector();

    final View view;

    final ImageView image;
    final TextView titleText;
    final TextView bodyText;
    final TextView count;

    final PaletteBitmapViewTarget target;

    ViewHolder(View view) {
      this.view = view;
      image = (ImageView) view.findViewById(R.id.image);
      titleText = (TextView) view.findViewById(R.id.titleText);
      bodyText = (TextView) view.findViewById(R.id.bodyText);
      count = (TextView) view.findViewById(R.id.count);

      target = new PaletteTargetBuilder(image)
          .swatch(MUTED_DARK).background(view).bodyText(bodyText).finish()
          .swatch(VIBRANT).background(titleText, 0x60).titleText(titleText).finish()
          .custom(MAX_POPULATION, new PopulationSwatchTarget(count))
          .build();
    }
  }

  private static class PopulationSwatchTarget implements SwatchTarget {
    private final TextView count;

    PopulationSwatchTarget(TextView count) {
      this.count = count;
    }

    @Override
    public void apply(@Nullable Palette.Swatch swatch) {
      if (swatch != null) {
        set(swatch);
      } else {
        reset();
      }
    }

    @SuppressWarnings("deprecation")
    private void set(@NonNull Palette.Swatch swatch) {
      ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
      count.setBackgroundDrawable(drawable);

      drawable.getPaint().setColor(swatch.getRgb());
      // or drawable.setColorFilter(swatch.getRgb(), PorterDuff.Mode.SRC_OVER);
      count.setTextColor(swatch.getBodyTextColor());
      count.setText(getPopulationText(swatch.getPopulation()));
    }

    private void reset() {
      count.setBackgroundColor(Color.WHITE);
      count.setTextColor(Color.BLACK);
      count.setText("N/A");
    }

    private String getPopulationText(int population) {
      if (population < 10000) {
        return Integer.toString(population);
      } else {
        return (population / 1000) + "k";
      }
    }
  }
}
