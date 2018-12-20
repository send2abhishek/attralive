//package com.attra.attralive.activity;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.GestureDetector;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.Toast;
//
//import com.attra.attralive.R;
//import com.attra.attralive.adapter.WidgetAdapter;
//
//import java.util.ArrayList;
//
//public class WidgetRecyclerViewAdapter extends AppCompatActivity{
//
//    RecyclerView recyclerView;
//    ArrayList<String> Number;
//    RecyclerView.LayoutManager RecyclerViewLayoutManager;
//    WidgetAdapter RecyclerViewHorizontalAdapter;
//    LinearLayoutManager HorizontalLayout ;
//    View ChildView ;
//    int RecyclerViewItemPosition ;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_home);
//
//        recyclerView = findViewById(R.id.recyclerview1);
//
//        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
//
//        recyclerView.setLayoutManager(RecyclerViewLayoutManager);
//
//
//        // Adding items to RecyclerView.
//        AddItemsToRecyclerViewArrayList();
//
//        RecyclerViewHorizontalAdapter = new WidgetAdapter(Number);
//
//        HorizontalLayout = new LinearLayoutManager(WidgetRecyclerViewAdapter.this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(HorizontalLayout);
//
//        recyclerView.setAdapter(RecyclerViewHorizontalAdapter);
//
//
//        // Adding on item click listener to RecyclerView.
//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//
//            GestureDetector gestureDetector = new GestureDetector(WidgetRecyclerViewAdapter.this, new GestureDetector.SimpleOnGestureListener() {
//
//                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
//
//                    return true;
//                }
//
//            });
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
//
//                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
//
//                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {
//
//                    //Getting clicked value.
//                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(ChildView);
//
//                    // Showing clicked item value on screen using toast message.
//                    Toast.makeText(WidgetRecyclerViewAdapter.this, Number.get(RecyclerViewItemPosition), Toast.LENGTH_LONG).show();
//
//                }
//
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });
//
//    }
//
//    // function to add items in RecyclerView.
//    public void AddItemsToRecyclerViewArrayList(){
//
//        Number = new ArrayList<>();
//        Number.add("ONE");
//        Number.add("TWO");
//        Number.add("THREE");
//        Number.add("FOUR");
//        Number.add("FIVE");
//        Number.add("SIX");
//        Number.add("SEVEN");
//        Number.add("EIGHT");
//        Number.add("NINE");
//        Number.add("TEN");
//
//    }
//}
