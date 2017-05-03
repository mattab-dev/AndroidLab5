package pl.edu.pwr.wiz.wzorlaboratorium5;

public class PostLoadedEvent {
    private Post[] contents=null;

    public PostLoadedEvent(Post[] contents) {
        this.contents=contents;
    }
    public Post[] getPosts() {
        return (contents);
    }
}