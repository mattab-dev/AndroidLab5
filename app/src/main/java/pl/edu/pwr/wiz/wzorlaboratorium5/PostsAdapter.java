package pl.edu.pwr.wiz.wzorlaboratorium5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class PostsAdapter extends ArrayAdapter<Post> {
    Context mcontext;

    PostsAdapter(Context context, Post[] posts) {
        super(context, android.R.layout.simple_list_item_1, posts);

        mcontext = context;
    }

    PostsAdapter(Context context, List<Post> posts) {
        super(context, android.R.layout.simple_list_item_1, posts);

        mcontext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.post_info, parent, false);

        TextView id = (TextView) row.findViewById(R.id.id);
        id.setText(Integer.toString(getItem(position).id));

        TextView title = (TextView) row.findViewById(R.id.title);
        title.setText(getItem(position).title);

        return (row);
    }
}