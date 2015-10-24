package com.bumptech.glide.samples.palette;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Displays images loaded from the Internet and adjust colors to them.
 *
 * @see com.bumptech.glide.samples.palette.MainActivity#onCreate(android.os.Bundle)
 * @see com.bumptech.glide.samples.palette.PaletteAdapter.ViewHolder
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
