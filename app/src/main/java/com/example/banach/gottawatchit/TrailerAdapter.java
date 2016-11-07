package com.example.banach.gottawatchit;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BANACH on 02.09.2016.
 */
public class TrailerAdapter extends ArrayAdapter<Trailer> {
    private List<Trailer> items;

    public TrailerAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public TrailerAdapter(Context context, List<Trailer> items) {
        super(context, 0, items);
        this.items=items;
    }

    @Override
    public int getCount() {
        if(items!=null)
        return items.size();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.trailer_item, null);
        }

        final Trailer p = getItem(position);

        TextView textView = (TextView) v.findViewById(R.id.text_view_trailer);
        textView.setText(p.getmName());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(p.getmUrl()));
                getContext().startActivity(youtubeIntent);
            }
        });
        return v;
    }

}

