package pl.edu.pwr.wiz.wzorlaboratorium5;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface JsonlaceholderInterface {
    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("posts")
    Call<List<Post>> getPostsByUserId(@Query("userId") Integer userId);

    @GET("posts")
    Call<List<Post>> getPostById(@Query("id") Integer id);

    @FormUrlEncoded
    @POST("posts")
    Call<Post> addPost(@Field("userId") Integer userId, @Field("title") String title,
                       @Field("body") String body);

    @GET("comments")
    Call<List<Comment>> getComments();

    @GET("comments")
    Call<List<Comment>> getCommentsByPostId(@Query("postId") Integer postId);

    @FormUrlEncoded
    @POST("comments")
    Call<Post> addComment(@Field("id") Integer commentId, @Field("name") String commentName,
                          @Field("email") String email, @Field("body") String body);

    @FormUrlEncoded
    @PUT("posts")
    Call<Post> updatePost(@Field("userId") Integer userId, @Field("title") String title,
                          @Field("body") String body);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
