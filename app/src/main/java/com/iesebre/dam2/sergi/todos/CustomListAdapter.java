package com.iesebre.dam2.sergi.todos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sergi on 20/11/15.
 */
public class CustomListAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<TodoItem> list;
    private final LayoutInflater layoutInflater;

    public CustomListAdapter(Context context, ArrayList listData) {
        this.context = context;
        this.list = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.listitem,
                    null);
        } else {

        }

        TextView tv  = (TextView) convertView.findViewById(R.id.todolistitemtext);

        //tv.setText("PROVA");
        tv.setText(list.get(position).getName()
                + " p: " + list.get(position).getPriority() +
                " done: " + list.get(position).isDone());

        return convertView;
    }
}
