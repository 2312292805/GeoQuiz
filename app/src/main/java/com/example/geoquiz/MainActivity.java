package com.example.geoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.prefs.AbstractPreferences;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_QUESTION_STATE = "question_state";
    private static final String KEY_CORRECTNUM = "correctnum";
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private ImageButton mImageNextButton;
    private ImageButton mImagePrevButton;
    private TextView mQuestionTextView;
    private boolean[] mQuestionBank_state = new boolean[]{true,true,true,true,true,true,true};
    private  int mCurrentIndex = 0;
    private  int mCorrectNum = 0;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };


    private void checkButtonEnable(){
        mTrueButton.setEnabled(mQuestionBank_state[mCurrentIndex]);
        mFalseButton.setEnabled(mQuestionBank_state[mCurrentIndex]);
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId=0;
        if (userPressedTrue == answerIsTrue){
            messageResId = R.string.correct_toast;
            mCorrectNum=mCorrectNum+1;
        }
        else{
            messageResId = R.string.incorrect_toast;
        }
        mQuestionBank_state[mCurrentIndex]=false;
        checkButtonEnable();
        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
        settleGrade();
    }

    private void settleGrade(){
        if(mCurrentIndex==mQuestionBank.length-1){
            System.out.println("Result,mCorrect:" + mCorrectNum);
            String grade = Float.toString(Float.valueOf(mCorrectNum)/mQuestionBank.length*100)+"%";
            Toast.makeText(this,grade,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        Log.i(TAG,"onSaveInstanceState");
        saveInstanceState.putInt(KEY_INDEX,mCurrentIndex);
        Log.i(TAG,"onSaveInstanceState_Array");
        saveInstanceState.putBooleanArray(KEY_QUESTION_STATE,mQuestionBank_state);
        Log.i(TAG,"onSaveInstanceState");
        saveInstanceState.putInt(KEY_CORRECTNUM,mCorrectNum);
        }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        checkButtonEnable();
        updateQuestion();

        mTrueButton = (Button) findViewById(R.id.ture_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });


        mImageNextButton = (ImageButton) findViewById(R.id.img_next_button);
        mImageNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mImagePrevButton = (ImageButton) findViewById(R.id.img_prev_button);
        mImagePrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                if( mCurrentIndex < 0)mCurrentIndex += mQuestionBank.length;
                updateQuestion();
            }
        });
        updateQuestion();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onState() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause() called");
    }
/*
    @Override
    protected void onSaveInstance(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        Log.i(TAG,"onSaveInstanceState");
        saveInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }*/

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy() called");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }
}