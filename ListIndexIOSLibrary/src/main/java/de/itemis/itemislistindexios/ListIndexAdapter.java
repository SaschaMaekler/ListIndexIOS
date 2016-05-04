package de.itemis.itemislistindexios;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Maekler on 04.05.16.
 */
public class ListIndexAdapter<T> extends ArrayAdapter<T> {

    protected ListIndex indexView;

    public ListIndexAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ListIndexAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public ListIndexAdapter(Context context, int resource, T[] objects) {
        super(context, resource, objects);
    }

    public ListIndexAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ListIndexAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
    }

    public ListIndexAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if(indexView != null){
            indexView.refreshDrawableState();
            indexView.invalidate();
        }
    }
}
