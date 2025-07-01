package com.tuchwords.wordquiz;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    Context con;
    int _resource;
    List<String> lival1;
    int size;
    int columns = 17;

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
    public ReportAdapter(Context context, int resource, List<String> li1, int font) {
        con = context;
        _resource = resource;
        lival1 = li1;
        size = font;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.word, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        int words = lival1.size() / columns;
        int row = position / words;
        int column = position % words;
        String lival = lival1.get((column * columns) + row);
        View v = viewHolder.getView();

        TextView t1 = v.findViewById(R.id.textview7);

        t1.setText(Html.fromHtml(lival));
        t1.setTextSize(size);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Report report = (Report) con;
                report.onItemClick(viewHolder.getAdapterPosition());
            }
        });

        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Report report = (Report) con;
                report.onItemLongClick(viewHolder.getAdapterPosition());

                return true;
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return lival1.size();
    }
}