package com.attra.attralive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.adapter.AllAnswersAdapter;
import com.attra.attralive.model.AllAnswers;


import java.util.ArrayList;

import javax.annotation.Nonnull;

import graphqlandroid.AddWatch;
import graphqlandroid.GetDetailedForum;
import graphqlandroid.LikeDislikeQuestion;
import graphqlandroid.WriteForumQuestionAnswer;

public class DetailedForumScreen extends AppCompatActivity {
    LinearLayoutManager linearLayoutManager1;
    ArrayList<AllAnswers> allanswerslist;
    ArrayList<String> allansIdlist;
    AllAnswersAdapter allAnswersAdapter;
    AllAnswers allAnswers;
    RecyclerView allanswers;
    int likecountq=0;
    int dislikecountq=0;
    Intent intent;
    String quesId;
    TextView watchQuestion,like,dislike,questionheading,quesdescription,quesaddedby,quesaddedtime,questionComments;
    ImageView likeques,dislikeques;
    EditText et_forum_answer;
    Button bt_save_forum_answer;
    TextView tv_questionheading;
    TextView answersCount;
   // RecyclerView questioncommentsrecyclerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_forum_screen);

        watchQuestion=findViewById(R.id.tv_watch);

        et_forum_answer = findViewById(R.id.et_forum_answer);
        bt_save_forum_answer = findViewById(R.id.bt_save_forum_answer);
        tv_questionheading = findViewById(R.id.tv_questionheading);

        questionComments = findViewById(R.id.tv_showcomments);

      //  questioncommentsrecyclerview=findViewById(R.id.rv_questioncomments);

        answersCount = findViewById(R.id.tv_allanswers);

   //     questioncommentsrecyclerview=findViewById(R.id.rv_questioncomments);


        allanswers=findViewById(R.id.rv_allanswers);
        likeques=findViewById(R.id.im_questionlike);
        dislikeques=findViewById(R.id.im_quesdislike);
        like=findViewById(R.id.tv_queslikescount);
        dislike=findViewById(R.id.tv_quesdislikecount);
        quesdescription=findViewById(R.id.tv_questiondescription);
        questionheading=findViewById(R.id.tv_questionheading);
        quesaddedby=findViewById(R.id.tv_questionaddedby);
        quesaddedtime=findViewById(R.id.tv_questionaddingtime);
        intent=getIntent();
        quesId=intent.getStringExtra("quesid");
        getDetailedForum();
       // dislikeques.setTag(R.drawable.ic_thumb_up_color_24dp);
        likeques.setTag(R.drawable.ic_thumb_up_grey_24dp);
      //  likeques.setTag(R.drawable.ic_thumb_up_color_24dp);
       dislikeques.setTag(R.drawable.ic_thumb_down_grey_24dp);
       dislikeques.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if ((int) (dislikeques.getTag()) == R.drawable.ic_thumb_down_grey_24dp) {
                   if ((int) (likeques.getTag()) == R.drawable.ic_thumb_up_grey_24dp) {
                       getLikeDislikeService("dislike");
                       System.out.println("likescountgayathry" + dislikecountq);
                       dislikeques.setImageResource(R.drawable.ic_thumb_down_color_24dp);
                       dislikeques.setTag(R.drawable.ic_thumb_down_color_24dp);
                       String dlikeno = dislike.getText().toString();
                       int dlikec = Integer.parseInt(dlikeno);
                       dislike.setText("" + (dlikec + 1));
                       // Toast.makeText(context, "hi", Toast.LENGTH_LONG).show();
                   } else {
                       getLikeDislikeService("dislike");
                       // System.out.println("likescountgayathry"+dislikeCount);
                       dislikeques.setImageResource(R.drawable.ic_thumb_down_color_24dp);
                       dislikeques.setTag(R.drawable.ic_thumb_down_color_24dp);
                       String dlikeno = dislike.getText().toString();
                       int dlikec = Integer.parseInt(dlikeno);

                       dislike.setText("" + (dlikec + 1));
                       // Toast.makeText(context, "hi", Toast.LENGTH_LONG).show();
                       likeques.setImageResource(R.drawable.ic_thumb_up_grey_24dp);
                       likeques.setTag(R.drawable.ic_thumb_up_grey_24dp);
                       String likeno = like.getText().toString();
                       int likec = Integer.parseInt(likeno);
                       if (likec != 0)
                           like.setText((likec - 1) + "");
                   }
               } else {
                   Toast.makeText(DetailedForumScreen.this, "not", Toast.LENGTH_LONG).show();
               }
           }
       });

        likeques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((int) (likeques.getTag()) == R.drawable.ic_thumb_up_grey_24dp) {
                    if ((int) (dislikeques.getTag()) == R.drawable.ic_thumb_down_grey_24dp) {
                   getLikeDislikeService("like");
                       // System.out.println("likescountgayathry" + likesCount);
                       likeques.setImageResource(R.drawable.ic_thumb_up_color_24dp);
                        likeques.setTag(R.drawable.ic_thumb_up_color_24dp);
                        String likeno = like.getText().toString();
                        int likec = Integer.parseInt(likeno);
                        like.setText("" + (likec + 1));
                        Toast.makeText(DetailedForumScreen.this, "hi", Toast.LENGTH_LONG).show();
                    } else {
                        getLikeDislikeService("like");
                       // System.out.println("likescountgayathry" + likesCount);
                       likeques.setImageResource(R.drawable.ic_thumb_up_color_24dp);
                        likeques.setTag(R.drawable.ic_thumb_up_color_24dp);
                        String likeno = like.getText().toString();
                        int likec = Integer.parseInt(likeno);
                        like.setText("" + (likec + 1));
                        Toast.makeText(DetailedForumScreen.this, "hi", Toast.LENGTH_LONG).show();
                        dislikeques.setImageResource(R.drawable.ic_thumb_down_grey_24dp);
                        dislikeques.setTag(R.drawable.ic_thumb_down_grey_24dp);
                        String dlikeno = dislike.getText().toString();
                        int dlikec = Integer.parseInt(dlikeno);
                        if (dlikec != 0)
                            dislike.setText((dlikec - 1) + "");

                    }
                } else {
                    Toast.makeText(DetailedForumScreen.this, "not", Toast.LENGTH_LONG).show();
                }
            }
        });

        linearLayoutManager1=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        allanswerslist=new ArrayList<AllAnswers>();

        allansIdlist=new ArrayList<String>();
        //getAllAnswers();


      //  getAllAnswers();
        answersCount.setText(allanswerslist.size()+" Answers");
        /*allAnswersAdapter=new AllAnswersAdapter(this,allanswerslist);
        allanswers.setLayoutManager(linearLayoutManager1);
        allanswers.setAdapter(allAnswersAdapter);*/
        watchQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWatchQuestiontoDB();
            }
        });
     /*   addAnswerComment=findViewById(R.id.addAnswerComments);

        addAnswerComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(DetailedForumScreen.this,AddAnswerComments.class);
                startActivity(i);
            }
        });*/

        et_forum_answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bt_save_forum_answer.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            if(et_forum_answer.length()==0){
                bt_save_forum_answer.setVisibility(View.GONE);
            }
            }
        });
        bt_save_forum_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitQuestionAnswer(v);

            }
        });

        questionComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),com.attra.attralive.activity.QuestionComments.class);
                startActivity(intent);
            }
        });

    }
    private void getDetailedForum() {
        MyAppolloClient.getMyAppolloClient().query(GetDetailedForum.builder().id("5bf3caaa1b50914658c1648a").build()).enqueue(new ApolloCall.Callback<GetDetailedForum.Data>() {
            @Override
            public void onResponse(@Nonnull final Response<GetDetailedForum.Data> response) {
                DetailedForumScreen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        like.setText(""+response.data().Question().likes_count());
                        dislike.setText(""+response.data().Question().unlikes_count());
                        quesdescription.setText(response.data().Question().description());
                        questionheading.setText(response.data().Question().topic());
                        quesaddedby.setText("Added By: "+response.data().Question().user().get(0).name()+" Bangalore");
                        quesaddedtime.setText(response.data().Question().timestamp());
                        for(int loopVar=0;loopVar<response.data().Question().answers().size();loopVar++)
                        {
                            allAnswers=new AllAnswers(1,response.data().Question().watch_count(),response.data().Question().answers().get(loopVar).likes_count(),
                                    response.data().Question().answers().get(loopVar).unlikes_count(),5, response.data().Question().answers().get(loopVar).answer(),
                                    response.data().Question().answers().get(loopVar).user(),response.data().Question().answers().get(loopVar).timestamp(),"Pune",
                                    response.data().Question().answers().get(loopVar).id());
                                    allanswerslist.add(allAnswers);
                                    allansIdlist.add(response.data().Question().answers().get(loopVar).id());
                        }
                        allAnswersAdapter=new AllAnswersAdapter(DetailedForumScreen.this,allanswerslist,allansIdlist);
                        allanswers.setLayoutManager(linearLayoutManager1);
                        allanswers.setAdapter(allAnswersAdapter);
                    }
                });
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }

    private void getLikeDislikeService(String like)
    {
        MyAppolloClient.getMyAppolloClient().mutate(LikeDislikeQuestion.builder().questionId(quesId).userId("5bf526dfcc4bbffcbe4c3a85").like_dislike(like).build()).
                enqueue(new ApolloCall.Callback<LikeDislikeQuestion.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<LikeDislikeQuestion.Data> response) {

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });
    }
    private void addWatchQuestiontoDB()
    {
        MyAppolloClient.getMyAppolloClient().mutate(AddWatch.builder().questionId("5bf3b030a1ec9d29d839ed94").user("5bdc1f635d3211192f245db9").build()).enqueue(new ApolloCall.Callback<AddWatch.Data>() {
            @Override
            public void onResponse(@Nonnull final Response<AddWatch.Data> response) {
                DetailedForumScreen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("response from watch service"+response.data().addWatch().topic());
                    }
                });
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }
    /*private void getAllAnswers()
    {
        allAnswers=new AllAnswers(1,2,3,4,5,"It's so simple. Create an XML file like below one. Set it as background " +
                "for the button. Change the radius attribute to your wish, if you need more curve for the button. \n" +
                "button_background.xml\n" +
                "<shape xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:shape=\"rectangle\">\n" +
                "    <solid android:color=\"@color/primary\" />\n" +
                "    <corners android:radius=\"5dp\" />\n" +
                "</shape>","Raj","nov-2018-7 6:70 PM","Pune");
        allanswerslist.add(allAnswers);
        allAnswers=new AllAnswers(1,2,3,4,5,"It's so simple. Create an XML file like below one. Set it as background " +
                "for the button. Change the radius attribute to your wish, if you need more curve for the button. \n" +
                "button_background.xml\n" +
                "<shape xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:shape=\"rectangle\">\n" +
                "    <solid android:color=\"@color/primary\" />\n" +
                "    <corners android:radius=\"5dp\" />\n" +
                "</shape>","Raj","nov-2018-7 6:70 PM","Pune");
        allanswerslist.add(allAnswers);
        allAnswers=new AllAnswers(1,2,3,4,5,"It's so simple. Create an XML file like below one. Set it as background " +
                "for the button. Change the radius attribute to your wish, if you need more curve for the button. \n" +
                "button_background.xml\n" +
                "<shape xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:shape=\"rectangle\">\n" +
                "    <solid android:color=\"@color/primary\" />\n" +
                "    <corners android:radius=\"5dp\" />\n" +
                "</shape>","Raj","nov-2018-7 6:70 PM","Pune");
        allanswerslist.add(allAnswers);
        allAnswers=new AllAnswers(1,2,3,4,5,"It's so simple. Create an XML file like below one. Set it as background " +
                "for the button. Change the radius attribute to your wish, if you need more curve for the button. \n" +
                "button_background.xml\n" +
                "<shape xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:shape=\"rectangle\">\n" +
                "    <solid android:color=\"@color/primary\" />\n" +
                "    <corners android:radius=\"5dp\" />\n" +
                "</shape>","Raj","nov-2018-7 6:70 PM","Pune");
        allanswerslist.add(allAnswers);
        allAnswers=new AllAnswers(1,2,3,4,5,"It's so simple. Create an XML file like below one. Set it as background " +
                "for the button. Change the radius attribute to your wish, if you need more curve for the button. \n" +
                "button_background.xml\n" +
                "<shape xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:shape=\"rectangle\">\n" +
                "    <solid android:color=\"@color/primary\" />\n" +
                "    <corners android:radius=\"5dp\" />\n" +
                "</shape>","Raj","nov-2018-7 6:70 PM","Pune");
        allanswerslist.add(allAnswers);
<<<<<<< HEAD
    }*/



    public void submitQuestionAnswer(View v){
        MyAppolloClient.getMyAppolloClient().mutate(
                WriteForumQuestionAnswer.builder().questionVal(tv_questionheading.getText().toString())
                        .answerVal(et_forum_answer.getText().toString())
                        .userVal("5bc85640a345bd3ce0b91dd4")
                        .build()).enqueue(
                new ApolloCall.Callback<WriteForumQuestionAnswer.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<WriteForumQuestionAnswer.Data> response) {
                        DetailedForumScreen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //   Snackbar.make(linearLayout,"added",Snackbar.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(),"Added successfully!!!",Toast.LENGTH_SHORT).show();

                                allanswerslist.add(new AllAnswers(0,0,0,0,0,et_forum_answer.getText().toString(),
                                        "5bc85640a345bd3ce0b91dd4","nov-2018-7 6:70 PM","bangalore","5bc85640a345bd3ce0b91dd4"));
                                allAnswersAdapter.notifyDataSetChanged();
                                et_forum_answer.setText("");
                                answersCount.setText(allanswerslist.size()+" Answers");

                            }
                        });
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }


                }
        );
    }
}
