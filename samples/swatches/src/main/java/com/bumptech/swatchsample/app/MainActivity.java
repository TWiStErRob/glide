package com.bumptech.swatchsample.app;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.integration.palette.PaletteBitmap;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;

/**
 * Displays images loaded from the Internet and adjust colors to them.
 *
 * @see com.bumptech.swatchsample.app.MainActivity#onCreate(android.os.Bundle)
 * @see com.bumptech.swatchsample.app.PaletteAdapter.ViewHolder
 */
public class MainActivity extends ListActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Uri[] urls = new LoremPixelUrlGenerator().generateAll();
    setListAdapter(new PaletteAdapter(urls));
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Uri uri = (Uri) l.getAdapter().getItem(position);
    Intent intent = new Intent(this, DetailActivity.class);
    intent.setData(uri);
    startActivity(intent);
  }
}
