package com.example.burakarslan.instagramfirebaseclone;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostClass extends ArrayAdapter<String> {

    private final ArrayList<String> userEmail;
    private final ArrayList<String> userImage;
    private final ArrayList<String> userComment;
    private final Activity context;

    public PostClass(ArrayList<String> userEmail, ArrayList<String> userImage, ArrayList<String> userComment, Activity context) {
        super(context, R.layout.customview, userEmail);
        this.userEmail = userEmail;
        this.userImage = userImage;
        this.userComment = userComment;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.customview, null, true);

        TextView userEmailText = (TextView) customView.findViewById(R.id.txtUserName);
        TextView userCommentText = (TextView) customView.findViewById(R.id.commentText);
        ImageView ımageView = (ImageView) customView.findViewById(R.id.customImageView);

        userEmailText.setText(userEmail.get(position));
        userCommentText.setText(userComment.get(position));

        Picasso.with(context).load(userImage.get(position)).into(ımageView);


        return customView;
    }
}
