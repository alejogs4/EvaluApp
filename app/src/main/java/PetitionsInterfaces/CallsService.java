package PetitionsInterfaces;

import java.util.List;

import POJOS.Notes;
import POJOS.Questions;
import POJOS.Student;
import POJOS.Teacher;
import POJOS.Tests;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Alejandro Garcia on 18/03/2018.
 */

public interface CallsService {
    String API_ROUTE = "api/v1/";
    @FormUrlEncoded
    @POST(API_ROUTE  + "teacher")
    Call<List<Teacher>> loginTeacher(@Field("username") String username, @Field("pass") String pass);

    @GET(API_ROUTE + "tests")
    Call<List<Tests>> getTests();

    @FormUrlEncoded
    @POST(API_ROUTE + "test")
    Call<List<Tests>> addTest(@Field("title") String title,
                              @Field("value_test") String value,
                              @Field("id_teacher") String id,
                              @Field("type")String type);

    @FormUrlEncoded
    @POST(API_ROUTE + "questions")
    Call<List<Questions>> addQuestion(@Field("question") String question,
                                      @Field("answer") String answer,
                                      @Field("id_test") String id_test,
                                      @Field("is_correct")boolean correct);

    @FormUrlEncoded
    @POST(API_ROUTE  + "student/login")
    Call<List<Student>> loginStudent(@Field("username") String username, @Field("pass") String pass);

    @GET(API_ROUTE + "questions/test/{id}")
    Call<List<Questions>> getQuestionsByTest(@Path("id") int id);

    @FormUrlEncoded
    @POST(API_ROUTE + "note")
    Call<List<Object>> saveNotes(@Field("id_test") int test,
                                 @Field("id_student") String idStudent,
                                 @Field("value_test") double value);

    @GET(API_ROUTE + "note/{id}")
    Call<List<Notes>> getNotesByUser(@Path("id") String id);

    @FormUrlEncoded
    @POST(API_ROUTE + "student")
    Call<List<Student>> registryStudent(@Field("id") String  id,
                                        @Field("username") String username,
                                        @Field("pass") String  pass);

    @DELETE(API_ROUTE + "question/{id}")
    Call<List<Questions>> deleteQuestion(@Path("id") int id);

    @FormUrlEncoded
    @PUT(API_ROUTE + "question/{id}")
    Call<List<Questions>> editQuestion(@Field("question") String question,
                                       @Field("answer") String answer,
                                       @Field("is_correct") boolean isCorrect,
                                        @Path("id") int id);
}
