package com.bumptech.glide.integration.palette;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.integration.palette.PaletteActionGroup.SwatchSelector;
import com.bumptech.glide.integration.palette.PaletteActionGroup.SwatchTarget;
import com.bumptech.glide.integration.palette.PaletteBitmapViewTarget.PaletteAction;
import com.bumptech.glide.util.Preconditions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A builder to easily handle the different swatches and set the background and text colors.
 *
 * <p>The following examples all do the same, with different levels of complexity and style. They
 * all can be repeated and combined on a single builder with ease.</p> <ol>
 *
 * <li>
 *
 * Shorthand style. Simple methods for simple use cases, one Swatch from the Palette is used for a
 * single View's single property.
 * <code><pre>
 * .into(new PaletteTargetBuilder(imageView)
 *     .background(MUTED, rootView)
 *     .title(MUTED, titleView)
 *     .build()
 * );
 * </pre></code>
 * <i>Notice that the swatch needs to be duplicated to set two properties or on different
 * Views.</i>
 *
 * </li>
 *
 * <li>
 *
 * Advanced usage to apply a single swatch into multiple items.
 * <code><pre>
 * .into(new PaletteTargetBuilder(imageView)
 *     .swatch(MUTED).background(rootView).title(titleView).finish()
 *     .build()
 * );
 * </pre></code>
 * Method calls are in a single flow.
 *
 * </li>
 *
 * <li>
 *
 * Reusable swatch setup which has the benefit of formatting sub-builders better,
 * <code><pre>
 *
 * .into(new PaletteTargetBuilder(imageView)
 *     .apply(MUTED.as()
 *         .background(rootView)
 *         .title(titleView)
 *     )
 * );
 * </pre></code>
 * ... and can be re-used if necessary (e.g. with multiple builders):
 * <code><pre>
 * ReusableSwatchBuilder reusable = MUTED.as().background(rootView).title(titleView);
 * .into(new PaletteTargetBuilder(imageView)
 *     .apply(reusable)
 *     .build()
 * );
 * </pre></code>
 * Re-using custom selectors and targets:
 * <code><pre>
 * PaletteActionGroup.SwatchSelector FANCIEST = ...;
 * PaletteActionGroup.SwatchTarget MY_TARGET = ...;
 * ReusableSwatchBuilder reusable = PaletteTargetBuilder.preApply(FANCIEST).custom(MY_TARGET);
 * .into(new PaletteTargetBuilder(imageView)
 *     .apply(reusable)
 *     .build()
 * );
 * </pre></code>
 *
 * </li>
 *
 * </ol>
 */
public final class PaletteTargetBuilder {
  public static abstract class BuiltinSwatchSelector implements SwatchSelector {
    public ReusableSwatchBuilder as() {
      return preApply(this);
    }
  }

  public static final BuiltinSwatchSelector VIBRANT = new BuiltinSwatchSelector() {
    public Palette.Swatch select(@NonNull Palette palette) {
      return palette.getVibrantSwatch();
    }
  };
  public static final BuiltinSwatchSelector VIBRANT_LIGHT = new BuiltinSwatchSelector() {
    public Palette.Swatch select(@NonNull Palette palette) {
      return palette.getLightVibrantSwatch();
    }
  };
  public static final BuiltinSwatchSelector VIBRANT_DARK = new BuiltinSwatchSelector() {
    public Palette.Swatch select(@NonNull Palette palette) {
      return palette.getDarkVibrantSwatch();
    }
  };

  public static final BuiltinSwatchSelector MUTED = new BuiltinSwatchSelector() {
    public Palette.Swatch select(@NonNull Palette palette) {
      return palette.getMutedSwatch();
    }
  };
  public static final BuiltinSwatchSelector MUTED_LIGHT = new BuiltinSwatchSelector() {
    public Palette.Swatch select(@NonNull Palette palette) {
      return palette.getLightMutedSwatch();
    }
  };
  public static final BuiltinSwatchSelector MUTED_DARK = new BuiltinSwatchSelector() {
    public Palette.Swatch select(@NonNull Palette palette) {
      return palette.getDarkMutedSwatch();
    }
  };

  @NonNull
  private final ImageView view;
  private final List<PaletteAction> actions = new LinkedList<>();

  public PaletteTargetBuilder(@NonNull ImageView view) {
    this.view = Preconditions.checkNotNull(view);
  }

  @NonNull
  public ImageView getView() {
    return view;
  }

  @NonNull
  public SwatchBuilder swatch(@NonNull SwatchSelector selector) {
    return new PaletteActionGroup(this, selector);
  }

  @NonNull
  public PaletteTargetBuilder action(@NonNull PaletteAction action) {
    actions.add(action);
    return this;
  }

  @NonNull
  public PaletteTargetBuilder apply(@NonNull ReusableSwatchBuilder builder) {
    return this.action(builder.build());
  }

  public static ReusableSwatchBuilder preApply(SwatchSelector selector) {
    return new PaletteActionGroup(selector);
  }


  public PaletteBitmapViewTarget build() {
    // make a copy in case the user keeps calling Builder methods after build()
    return new PaletteBitmapViewTarget(view, new ArrayList<>(PaletteTargetBuilder.this.actions));
  }

  public PaletteTargetBuilder title(@NonNull SwatchSelector selector, @NonNull TextView view) {
    return this.swatch(selector).title(view).finish();
  }

  public PaletteTargetBuilder titleText(@NonNull SwatchSelector selector, @NonNull TextView view) {
    return this.swatch(selector).titleText(view).finish();
  }

  public PaletteTargetBuilder body(@NonNull SwatchSelector selector, @NonNull TextView view) {
    return this.swatch(selector).body(view).finish();
  }

  public PaletteTargetBuilder bodyText(@NonNull SwatchSelector selector, @NonNull TextView view) {
    return this.swatch(selector).bodyText(view).finish();
  }

  public PaletteTargetBuilder background(@NonNull SwatchSelector selector, @NonNull View view) {
    return this.swatch(selector).background(view).finish();
  }

  public PaletteTargetBuilder background(@NonNull SwatchSelector selector, @NonNull View view, int alpha) {
    return this.swatch(selector).background(view, alpha).finish();
  }

  public PaletteTargetBuilder custom(@NonNull SwatchSelector selector, @NonNull SwatchTarget target) {
    return this.swatch(selector).custom(target).finish();
  }

  public interface SwatchApplier {
    SwatchApplier title(@NonNull TextView view);

    SwatchApplier titleText(@NonNull TextView view);

    SwatchApplier body(@NonNull TextView view);

    SwatchApplier bodyText(@NonNull TextView view);

    SwatchApplier background(@NonNull View view);

    SwatchApplier background(@NonNull View view, @IntRange(from = 0, to = 255) int alpha);

    SwatchApplier custom(@NonNull SwatchTarget target);
  }

  public interface ReusableSwatchBuilder extends SwatchApplier {
    ReusableSwatchBuilder title(@NonNull TextView view);

    ReusableSwatchBuilder titleText(@NonNull TextView view);

    ReusableSwatchBuilder body(@NonNull TextView view);

    ReusableSwatchBuilder bodyText(@NonNull TextView view);

    ReusableSwatchBuilder background(@NonNull View view);

    ReusableSwatchBuilder background(@NonNull View view, @IntRange(from = 0, to = 255) int alpha);

    ReusableSwatchBuilder custom(@NonNull SwatchTarget target);

    @NonNull
    PaletteAction build();
  }

  public interface SwatchBuilder extends SwatchApplier {
    SwatchBuilder title(@NonNull TextView view);

    SwatchBuilder titleText(@NonNull TextView view);

    SwatchBuilder body(@NonNull TextView view);

    SwatchBuilder bodyText(@NonNull TextView view);

    SwatchBuilder background(@NonNull View view);

    SwatchBuilder background(@NonNull View view, @IntRange(from = 0, to = 255) int alpha);

    SwatchBuilder custom(@NonNull SwatchTarget target);

    @NonNull
    PaletteTargetBuilder finish();
  }
}
