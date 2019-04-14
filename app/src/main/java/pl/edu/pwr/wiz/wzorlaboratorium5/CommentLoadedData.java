package pl.edu.pwr.wiz.wzorlaboratorium5;

public class CommentLoadedData {

    private Comment[] contents=null;

    public CommentLoadedData(Comment[] contents) {
        this.contents=contents;
    }
    public Comment[] getComments() {
        return (contents);
    }
}
