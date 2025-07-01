package com.tuchwords.wordquiz;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    Context con;
    int _resource;
    List<String> lival1;
    List<Integer> lival2;
    List<Integer> lival3;
    int dimensions;
    int size;
    boolean hide;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            itemView = view;
        }

        public View getView() {
            return itemView;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public CustomAdapter(Context context, int resource, List<String> li1, List<Integer> li2, List<Integer> li3, int columns, int font) {
        con = context;
        _resource = resource;
        lival1 = li1;
        lival2 = li2;
        lival3 = li3;
        dimensions = columns;
        size = font;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cell, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        String lival = lival1.get(position);
        int li = lival2.get(position);
        int livalue = lival3.get(position);
        View v = viewHolder.getView();

        LinearLayout l1 = v.findViewById(R.id.linearlayout1);
        TextView t1 = v.findViewById(R.id.textview2);
        TextView t2 = v.findViewById(R.id.textview3);

        t1.setText(lival);
        t2.setText(hide ? "" : superscript(livalue - li) + "/" + subscript(livalue));
        t1.setTextSize(size);
        t2.setTextSize(size);

        if (li == 0)
        {
            l1.setBackgroundColor(Color.GREEN);
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) con;
                mainActivity.onItemClick(viewHolder.getAdapterPosition());
            }
        });

        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MainActivity mainActivity = (MainActivity) con;
                mainActivity.onItemLongClick(viewHolder.getAdapterPosition());
                return true;
            }
        });

        v.post(new Runnable() {
            @Override
            public void run() {
                int measuredWidth = v.getWidth(); // Width is ready
                DisplayMetrics displayMetrics = new DisplayMetrics();
                MainActivity parentActivity = (MainActivity) con;
                parentActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int screenWidth = displayMetrics.widthPixels;
                if (measuredWidth * dimensions < screenWidth) {
                    v.setMinimumWidth(screenWidth / dimensions);
                    t1.setMinimumWidth((screenWidth / dimensions) - t2.getWidth());
                }
            }
        });
    }

    public void setHidden(boolean hidden)
    {
        hide = hidden;
    }

    public String superscript(int value) {
        char[] characters = "⁰¹²³⁴⁵⁶⁷⁸⁹".toCharArray();
        String chars = Integer.toString(value);
        char[] character = chars.toCharArray();
        char[] sub = new char[chars.length()];
        for (int values = 0; values < chars.length(); values++)
        {
            sub[values] = characters[character[values] - 48];
        }
        return new String(sub);
    }

    public String subscript(int value) {
        char[] characters = "₀₁₂₃₄₅₆₇₈₉".toCharArray();
        String chars = Integer.toString(value);
        char[] character = chars.toCharArray();
        char[] sub = new char[chars.length()];
        for (int values = 0; values < chars.length(); values++)
        {
            sub[values] = characters[character[values] - 48];
        }
        return new String(sub);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return lival1.size();
    }
}