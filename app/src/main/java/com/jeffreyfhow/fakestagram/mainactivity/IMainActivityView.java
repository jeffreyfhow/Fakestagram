package com.jeffreyfhow.fakestagram.mainactivity;

import com.jeffreyfhow.fakestagram.data.Post;
import com.jeffreyfhow.fakestagram.data.PostAdapter;

import java.util.List;

public interface IMainActivityView {
    void displayGridView(List<Post> posts, PostAdapter.Listener listener, boolean hasRecencyText);
    void displayDetailView(Post post, PostAdapter.Listener listener, boolean hasRecencyText);
    void setUserIcon(String profileUrl);
}
