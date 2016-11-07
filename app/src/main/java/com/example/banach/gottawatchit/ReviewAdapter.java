package com.example.banach.gottawatchit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by BANACH on 02.09.2016.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {
    private List<Review> items;

    public ReviewAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ReviewAdapter(Context context, List<Review> items) {
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
            v = vi.inflate(R.layout.review_item, null);
        }

        final Review p = getItem(position);

        TextView reviewTextView = (TextView) v.findViewById(R.id.text1);
        reviewTextView.setText(p.getmReview());
        TextView authorTextView = (TextView) v.findViewById(R.id.text2);
        authorTextView.setText(p.getmAuthor());
        return v;
    }

}

