package pl.edu.pwr.wiz.wzorlaboratorium5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CommentsAdapter extends ArrayAdapter<Comment> {

    Context mcontext;

    CommentsAdapter(Context context, Comment[] comments) {
        super(context, android.R.layout.simple_list_item_1, comments);

        mcontext = context;
    }

    CommentsAdapter(Context context, List<Comment> comments) {
        super(context, android.R.layout.simple_list_item_1, comments);

        mcontext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.comment_info, parent, false);

        TextView postId = row.findViewById(R.id.comment_post_id);
        postId.setText(Integer.toString(getItem(position).postId));

        TextView id = (TextView) row.findViewById(R.id.id);
        id.setText(Integer.toString(getItem(position).id));

        TextView title = (TextView) row.findViewById(R.id.title);
        title.setText(getItem(position).name);

        TextView email = row.findViewById(R.id.email);
        email.setText(getItem(position).email);

        TextView body = row.findViewById(R.id.commentBody);
        body.setText(getItem(position).body);

        return(row);
    }
}
