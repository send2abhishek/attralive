package com.attra.attralive.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.attra.attralive.R;
import com.attra.attralive.adapter.QuizAdapter;
import com.attra.attralive.model.DigitalQuiz;
import com.attra.attralive.model.QuestionAnswer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DigiquizFragment extends Fragment {

    private RecyclerView mquizView;
    QuizAdapter quizAdapter;
    private String delegateId;
    private Button mSubmit;
    public TextView mQidQname1, mQidQname2, mQidQname3, mQidQname4, mQidQname5;
    private RadioGroup mRadioG1,mRadioG2, mRadioG3, mRadioG4, mRadioG5;
    public RadioButton mOption1_1, mOption2_1, mOption3_1, mOption4_1;
    public RadioButton mOption1_2, mOption2_2, mOption3_2, mOption4_2;
    public RadioButton mOption1_3, mOption2_3, mOption3_3, mOption4_3;
    public RadioButton mOption1_4, mOption2_4, mOption3_4, mOption4_4;
    public RadioButton mOption1_5, mOption2_5, mOption3_5, mOption4_5;
    int selectedRadioButtonID1, selectedRadioButtonID2, selectedRadioButtonID3, selectedRadioButtonID4, selectedRadioButtonID5;
    RadioButton selectedRadioButton1, selectedRadioButton2, selectedRadioButton3,selectedRadioButton4, selectedRadioButton5;
    String selectedRadioButtonText1 = null, selectedRadioButtonText2 = null, selectedRadioButtonText3 = null, selectedRadioButtonText4 = null,selectedRadioButtonText5= null;
    DigitalQuiz digitalQuiz = null;
    ArrayList<DigitalQuiz> digitalQuizList;
    ArrayList<QuestionAnswer> questionAnswerArrayList;
    DigitalQuiz q1, q2, q3,q4, q5;
    int q1Id, q2Id, q3Id, q4Id, q5Id;

    public DigiquizFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(R.string.digiquiz);
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_digiquiz, container, false);
        mRadioG1 = view.findViewById(R.id.radioGrp_1);
        mRadioG2 = view.findViewById(R.id.radioGrp_2);
        mRadioG3 = view.findViewById(R.id.radioGrp_3);
        mRadioG4 = view.findViewById(R.id.radioGrp_4);
        mRadioG5 = view.findViewById(R.id.radioGrp_5);
        mSubmit = view.findViewById(R.id.quizsubmit);
        /*mquizView = view.findViewById(R.id.quizView);*/
        mQidQname1 = view.findViewById(R.id.qid_qname_1);
        mOption1_1 = view.findViewById(R.id.option1_1);
        mOption2_1 = view.findViewById(R.id.option2_1);
        mOption3_1 = view.findViewById(R.id.option3_1);
        mOption4_1 = view.findViewById(R.id.option4_1);

        mQidQname2 = view.findViewById(R.id.qid_qname_2);
        mOption1_2 = view.findViewById(R.id.option1_2);
        mOption2_2 = view.findViewById(R.id.option2_2);
        mOption3_2 = view.findViewById(R.id.option3_2);
        mOption4_2 = view.findViewById(R.id.option4_2);

        mQidQname3 = view.findViewById(R.id.qid_qname_3);
        mOption1_3 = view.findViewById(R.id.option1_3);
        mOption2_3 = view.findViewById(R.id.option2_3);
        mOption3_3 = view.findViewById(R.id.option3_3);
        mOption4_3 = view.findViewById(R.id.option4_3);

        mQidQname4 = view.findViewById(R.id.qid_qname_4);
        mOption1_4 = view.findViewById(R.id.option1_4);
        mOption2_4 = view.findViewById(R.id.option2_4);
        mOption3_4 = view.findViewById(R.id.option3_4);
        mOption4_4 = view.findViewById(R.id.option4_4);

        mQidQname5 = view.findViewById(R.id.qid_qname_5);
        mOption1_5 = view.findViewById(R.id.option1_5);
        mOption2_5 = view.findViewById(R.id.option2_5);
        mOption3_5 = view.findViewById(R.id.option3_5);
        mOption4_5 = view.findViewById(R.id.option4_5);
        /*quizAdapter = new QuizAdapter(getContext(), getActivity());
        mquizView.setAdapter(quizAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mquizView.setLayoutManager(linearLayoutManager);*/
        delegateId = getArguments().getString("delegateId");
        try {
            invokeFetchQuiz(delegateId);
            System.out.println("delegateId: " +delegateId);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        selectedRadioButton1 = (RadioButton) view.findViewById(selectedRadioButtonID1);
        selectedRadioButton2 = (RadioButton) view.findViewById(selectedRadioButtonID2);
        selectedRadioButton3 = (RadioButton) view.findViewById(selectedRadioButtonID3);
        selectedRadioButton4 = (RadioButton) view.findViewById(selectedRadioButtonID4);
        selectedRadioButton5 = (RadioButton) view.findViewById(selectedRadioButtonID5);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedRadioButtonID1 = mRadioG1.getCheckedRadioButtonId();
                selectedRadioButtonID2= mRadioG2.getCheckedRadioButtonId();
                selectedRadioButtonID3 = mRadioG3.getCheckedRadioButtonId();
                selectedRadioButtonID4 = mRadioG4.getCheckedRadioButtonId();
                selectedRadioButtonID5 = mRadioG5.getCheckedRadioButtonId();

                if (selectedRadioButtonID1 == -1) {
                    Toast.makeText(getContext(), R.string.error_answer, Toast.LENGTH_SHORT).show();
                } else if (selectedRadioButtonID2 == -1) {
                    Toast.makeText(getContext(), R.string.error_answer, Toast.LENGTH_SHORT).show();
                }else if (selectedRadioButtonID3 == -1) {
                    Toast.makeText(getContext(), R.string.error_answer, Toast.LENGTH_SHORT).show();
                } else if (selectedRadioButtonID4 == -1) {
                    Toast.makeText(getContext(), R.string.error_answer, Toast.LENGTH_SHORT).show();
                } else if (selectedRadioButtonID5 == -1) {
                    Toast.makeText(getContext(), R.string.error_answer, Toast.LENGTH_SHORT).show();
                } else {
                View radioButton1 = mRadioG1.findViewById(selectedRadioButtonID1);
                int idx = mRadioG1.indexOfChild(radioButton1);
                RadioButton r1 = (RadioButton)  mRadioG1.getChildAt(idx);
                selectedRadioButtonText1 = r1.getText().toString();

                View radioButton2 = mRadioG2.findViewById(selectedRadioButtonID2);
                int idx2 = mRadioG2.indexOfChild(radioButton2);
                RadioButton r2 = (RadioButton)  mRadioG2.getChildAt(idx2);
                selectedRadioButtonText2 = r2.getText().toString();

                View radioButton3 = mRadioG3.findViewById(selectedRadioButtonID3);
                int idx3 = mRadioG3.indexOfChild(radioButton3);
                RadioButton r3 = (RadioButton)  mRadioG3.getChildAt(idx3);
                selectedRadioButtonText3 = r3.getText().toString();

                View radioButton4 = mRadioG4.findViewById(selectedRadioButtonID4);
                int idx4 = mRadioG4.indexOfChild(radioButton4);
                RadioButton r4 = (RadioButton)  mRadioG4.getChildAt(idx4);
                selectedRadioButtonText4 = r4.getText().toString();

                View radioButton5 = mRadioG5.findViewById(selectedRadioButtonID5);
                int idx5 = mRadioG5.indexOfChild(radioButton5);
                RadioButton r5 = (RadioButton)  mRadioG5.getChildAt(idx5);
                selectedRadioButtonText5 = r5.getText().toString();


                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject1 = new JSONObject();
                    JSONObject jsonObject2 = new JSONObject();
                    JSONObject jsonObject3 = new JSONObject();
                    JSONObject jsonObject4 = new JSONObject();
                    JSONObject jsonObject5 = new JSONObject();
                    try {
                    /*jsonObject1.put("delegateId", delegateId);*/
                        jsonObject1.put("questionId", "" + q1Id);
                        jsonObject1.put("answer", selectedRadioButtonText1);

                    /*jsonObject2.put("delegateId", delegateId);*/
                        jsonObject2.put("questionId", "" + q2Id);
                        jsonObject2.put("answer", selectedRadioButtonText2);

                    /*jsonObject3.put("delegateId", delegateId);*/
                        jsonObject3.put("questionId", "" + q3Id);
                        jsonObject3.put("answer", selectedRadioButtonText3);

                    /*jsonObject4.put("delegateId", delegateId);*/
                        jsonObject4.put("questionId", "" + q4Id);
                        jsonObject4.put("answer", selectedRadioButtonText4);

                    /*jsonObject5.put("delegateId", delegateId);*/
                        jsonObject5.put("questionId", "" + q5Id);
                        jsonObject5.put("answer", selectedRadioButtonText5);

                        jsonArray.put(jsonObject1);
                        jsonArray.put(jsonObject2);
                        jsonArray.put(jsonObject3);
                        jsonArray.put(jsonObject4);
                        jsonArray.put(jsonObject5);

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("delegateId", delegateId);
                        jsonObject.put("questionAnswerList", jsonArray);
                        /*invokeSubmitQuiz(jsonObject);*/
                        System.out.println("JSON Answer inp: " + jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } /*catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        });
        return view;
    }
    private void invokeFetchQuiz(String delegateId) throws UnsupportedEncodingException {

       /* RestClient.httpFetchQuiz(getContext(), getActivity(), "/digitalquizfetchService", null , "application/json", delegateId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, JSONObject obj) {

                try {
                    JSONObject jsonObject1 = new JSONObject(String.valueOf(obj));

                    System.out.println("\nam from quizfrag: " +jsonObject1.toString());

                    String message = jsonObject1.getJSONObject("responseMessage").getString("message");
                    String object = jsonObject1.getJSONObject("responseMessage").getString("object");
                    String score = jsonObject1.getString("userScore");

                    if(message.equals("SUCCESS") || message.equals("Success")){
                        System.out.println("\nam from quizfrag: " +jsonObject1.toString());
                        if (!(object.equals("User has already attended the DiziQuiz"))){
                            //display questions with options
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("questionoptionDetails");
                            digitalQuizList = new ArrayList<>();
                            for (int i=0; i<jsonArray1.length();i++){
                                digitalQuiz = new DigitalQuiz();
                                JSONObject jsonArrayJSONObject = jsonArray1.getJSONObject(i);
                                digitalQuiz.setQuestionId(jsonArrayJSONObject.getInt("questionId"));
                                digitalQuiz.setQuestionName((String) jsonArrayJSONObject.get("questionName"));
                                digitalQuiz.setOption1((String) jsonArrayJSONObject.get("option1"));
                                digitalQuiz.setOption2((String) jsonArrayJSONObject.get("option2"));
                                digitalQuiz.setOption3((String) jsonArrayJSONObject.get("option3"));
                                digitalQuiz.setOption4((String) jsonArrayJSONObject.get("option4"));
                                digitalQuizList.add(digitalQuiz);
                            }
                            System.out.println("\ndigitalQuizList in quizfrag: " +digitalQuizList);
                            for (int i=0; i<digitalQuizList.size();i++){
                                digitalQuiz = digitalQuizList.get(i);
                                q1 = digitalQuizList.get(0);
                                q1Id = q1.getQuestionId();
                                mQidQname1.setText("1: "+q1.getQuestionName());
                                mOption1_1.setText(q1.getOption1());
                                mOption2_1.setText(q1.getOption2());
                                mOption3_1.setText(q1.getOption3());
                                mOption4_1.setText(q1.getOption4());
                                q2 = digitalQuizList.get(1);
                                q2Id = q2.getQuestionId();
                                mQidQname2.setText("2: "+q2.getQuestionName());
                                mOption1_2.setText(q2.getOption1());
                                mOption2_2.setText(q2.getOption2());
                                mOption3_2.setText(q2.getOption3());
                                mOption4_2.setText(q2.getOption4());
                                q3 = digitalQuizList.get(2);
                                q3Id = q3.getQuestionId();
                                mQidQname3.setText("3: "+q3.getQuestionName());
                                mOption1_3.setText(q3.getOption1());
                                mOption2_3.setText(q3.getOption2());
                                mOption3_3.setText(q3.getOption3());
                                mOption4_3.setText(q3.getOption4());
                                q4 = digitalQuizList.get(3);
                                q4Id = q4.getQuestionId();
                                mQidQname4.setText("4: "+q4.getQuestionName());
                                mOption1_4.setText(q4.getOption1());
                                mOption2_4.setText(q4.getOption2());
                                mOption3_4.setText(q4.getOption3());
                                mOption4_4.setText(q4.getOption4());
                                q5 = digitalQuizList.get(4);
                                q5Id = q5.getQuestionId();
                                mQidQname5.setText("5: "+q5.getQuestionName());
                                mOption1_5.setText(q5.getOption1());
                                mOption2_5.setText(q5.getOption2());
                                mOption3_5.setText(q5.getOption3());
                                mOption4_5.setText(q5.getOption4());
                                System.out.println("in loop: " +digitalQuiz.getQuestionId()+": "+digitalQuiz.getQuestionName()+" "+
                                        digitalQuiz.getOption1()+ " " +digitalQuiz.getOption2()+" "+digitalQuiz.getOption3()+" "+ digitalQuiz.getOption4());
                            }

                        } else if (object.equals("User has already attended the DiziQuiz")){
                            //display questions with answers
                            JSONArray questionAnsArray = jsonObject1.getJSONArray("questionAnswerList");
                            QuestionAnswer questionAnswer;
                            questionAnswerArrayList = new ArrayList<>();
                            for (int i=0; i<questionAnsArray.length();i++){
                                questionAnswer = new QuestionAnswer();
                                JSONObject jsonArrayJSONObject = questionAnsArray.getJSONObject(i);
                                questionAnswer.setQuestionId(jsonArrayJSONObject.getInt("questionId"));
                                questionAnswer.setQuestionName((String) jsonArrayJSONObject.get("questionName"));
                                questionAnswer.setAnswer((String) jsonArrayJSONObject.get("answer"));
                                questionAnswerArrayList.add(questionAnswer);
                            }
                            *//*QuestionAnswer qna1 = new QuestionAnswer();
                            QuestionAnswer qna2 = new QuestionAnswer();
                            QuestionAnswer qna3 = new QuestionAnswer();
                            QuestionAnswer qna4 = new QuestionAnswer();
                            QuestionAnswer qna5 = new QuestionAnswer();

                            for (int i=0;i<questionAnswerArrayList.size();i++){
                                qna1 = questionAnswerArrayList.get(0);
                                qna2 = questionAnswerArrayList.get(1);
                                qna3 = questionAnswerArrayList.get(2);
                                qna4 = questionAnswerArrayList.get(3);
                                qna5 = questionAnswerArrayList.get(4);

                            }*//*
                            HomeActivity homeActivity = (HomeActivity) getActivity();
                            homeActivity.loadQnAFragment(questionAnswerArrayList);
                        }
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }  catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getActivity().getApplicationContext(), R.string.response_json, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
                // prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getActivity().getApplicationContext(), "404 - Resource not found!", Toast.LENGTH_SHORT).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getActivity().getApplicationContext(), "500 - Internal server error!", Toast.LENGTH_SHORT).show();
                } else if (statusCode == 403) {
                    Toast.makeText(getActivity().getApplicationContext(), "Forbidden error!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

  /*  private void invokeDigitalQuiz(String delegateId) throws UnsupportedEncodingException {

        //prgDialog.show();
        RestClient.httpFetchQuiz(getContext(), getActivity(), "/digitalquizfetch", null , "application/json", delegateId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, JSONObject obj) {
                //prgDialog.hide();
                try {
                    JSONObject jsonObject1 = new JSONObject(String.valueOf(obj));
                    System.out.println("\n JSON" +String.valueOf(obj));
                    String message = String.valueOf(jsonObject1.get("message"));
                    JSONArray jsonArray = jsonObject1.getJSONArray("object");
                    digitalQuizList = new ArrayList<>();
                    for (int i=0; i<jsonArray.length();i++){
                        digitalQuiz = new DigitalQuiz();
                        JSONObject jsonArrayJSONObject = jsonArray.getJSONObject(i);
                        digitalQuiz.setQuestionId(jsonArrayJSONObject.getInt("questionId"));
                        digitalQuiz.setQuestionName((String) jsonArrayJSONObject.get("questionName"));
                        digitalQuiz.setOption1((String) jsonArrayJSONObject.get("option1"));
                        digitalQuiz.setOption2((String) jsonArrayJSONObject.get("option2"));
                        digitalQuiz.setOption3((String) jsonArrayJSONObject.get("option3"));
                        digitalQuiz.setOption4((String) jsonArrayJSONObject.get("option4"));
                        digitalQuizList.add(digitalQuiz);
                    }
                    if(message.equals("SUCCESS") || message.equals("Success")){
                        System.out.println("\nam from quizfrag: " +jsonObject1.toString());
                        System.out.println("\ndigitalQuizList in quizfrag: " +digitalQuizList);
                        for (int i=0; i<digitalQuizList.size();i++){
                            digitalQuiz = digitalQuizList.get(i);
                            q1 = digitalQuizList.get(0);
                            q1Id = q1.getQuestionId();
                            mQidQname1.setText("1: "+q1.getQuestionName());
                            mOption1_1.setText(q1.getOption1());
                            mOption2_1.setText(q1.getOption2());
                            mOption3_1.setText(q1.getOption3());
                            mOption4_1.setText(q1.getOption4());
                            q2 = digitalQuizList.get(1);
                            q2Id = q2.getQuestionId();
                            mQidQname2.setText("2: "+q2.getQuestionName());
                            mOption1_2.setText(q2.getOption1());
                            mOption2_2.setText(q2.getOption2());
                            mOption3_2.setText(q2.getOption3());
                            mOption4_2.setText(q2.getOption4());
                            q3 = digitalQuizList.get(2);
                            q3Id = q3.getQuestionId();
                            mQidQname3.setText("3: "+q3.getQuestionName());
                            mOption1_3.setText(q3.getOption1());
                            mOption2_3.setText(q3.getOption2());
                            mOption3_3.setText(q3.getOption3());
                            mOption4_3.setText(q3.getOption4());
                            q4 = digitalQuizList.get(3);
                            q4Id = q4.getQuestionId();
                            mQidQname4.setText("4: "+q4.getQuestionName());
                            mOption1_4.setText(q4.getOption1());
                            mOption2_4.setText(q4.getOption2());
                            mOption3_4.setText(q4.getOption3());
                            mOption4_4.setText(q4.getOption4());
                            q5 = digitalQuizList.get(4);
                            q5Id = q5.getQuestionId();
                            mQidQname5.setText("5: "+q5.getQuestionName());
                            mOption1_5.setText(q5.getOption1());
                            mOption2_5.setText(q5.getOption2());
                            mOption3_5.setText(q5.getOption3());
                            mOption4_5.setText(q5.getOption4());
                            System.out.println("in loop: " +digitalQuiz.getQuestionId()+": "+digitalQuiz.getQuestionName()+" "+
                                    digitalQuiz.getOption1()+ " " +digitalQuiz.getOption2()+" "+digitalQuiz.getOption3()+" "+ digitalQuiz.getOption4());
                        }

                        *//*quizAdapter = new QuizAdapter(getContext(), getActivity(), digitalQuizList);
                        mquizView.setAdapter(quizAdapter);
                        quizAdapter.notifyDataSetChanged();
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        mquizView.setLayoutManager(linearLayoutManager);*//*
                    } else {
                        *//*alertMsg(message);*//*
                        *//*Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();*//*
                    }
                }  catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getActivity().getApplicationContext(), R.string.response_json, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
                // prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getActivity().getApplicationContext(), "404 - Resource not found!", Toast.LENGTH_SHORT).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getActivity().getApplicationContext(), "500 - Internal server error!", Toast.LENGTH_SHORT).show();
                } else if (statusCode == 403) {
                    Toast.makeText(getActivity().getApplicationContext(), "Forbidden error!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void invokeSubmitQuiz(JSONObject jsonObject) throws UnsupportedEncodingException {

        RestClient.httpConnectDigi(getContext(),getActivity(), "/digitalQuizAnswerService", jsonObject , "application/json", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, JSONObject obj) {

                try {
                    JSONObject jsonObject1 = new JSONObject(String.valueOf(obj));
                    System.out.println("\n JSON" +String.valueOf(obj));
                    String message = String.valueOf(jsonObject1.get("message"));

                    if(message.equals("SUCCESS") || message.equals("Success")){
                        System.out.println("\nam from quizfrag: " +jsonObject1.toString());
                        *//*Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();*//*
                        *//*alertSubmit();*//*
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }  catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getActivity().getApplicationContext(), R.string.response_json, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
                // prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getActivity().getApplicationContext(), "404 - Resource not found!", Toast.LENGTH_SHORT).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getActivity().getApplicationContext(), "500 - Internal server error!", Toast.LENGTH_SHORT).show();
                } else if (statusCode == 403) {
                    Toast.makeText(getActivity().getApplicationContext(), "Forbidden error!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

    public void alertSubmit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.submit_btn);
        builder.setMessage(R.string.answer_submitted)
                .setCancelable(false)
                .setPositiveButton(R.string.okbtn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       /* HomeActivity homeActivity = (HomeActivity) getActivity();
                        homeActivity.loadFragment("loadHome");*/
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
