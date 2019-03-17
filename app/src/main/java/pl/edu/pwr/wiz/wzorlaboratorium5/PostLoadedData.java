package pl.edu.pwr.wiz.wzorlaboratorium5;

public class PostLoadedData {
    private Post[] contents=null;

    public PostLoadedData(Post[] contents) {
        this.contents=contents;
    }
    public Post[] getPosts() {
        return (contents);
    }
}