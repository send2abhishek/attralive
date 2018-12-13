package com.attra.attralive.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.attra.attralive.R;
import com.attra.attralive.adapter.BlogMainCategoryAdapter;
import com.attra.attralive.adapter.BlogTopCategoryAdapter;
import com.attra.attralive.model.AnimationInterface;
import com.attra.attralive.model.BlogAllCategories;
import com.attra.attralive.model.BlogTopCategories;

import java.util.ArrayList;


public class MainBlogCategoryActivity extends AppCompatActivity implements AnimationInterface {
    RecyclerView topCatregories,allCategories;
    LinearLayoutManager linearLayoutManager1,linearLayoutManager2;
    ArrayList<BlogTopCategories> topList;
    ArrayList<BlogAllCategories>allList;
    BlogTopCategories blogTopCategories;
    BlogAllCategories blogAllCategories;
    BlogTopCategoryAdapter topadapter;
    BlogMainCategoryAdapter allAdapter;
    public String titleBlog,blogId;
    //Intent in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_blog_category);
        /*in =getIntent();
        titleBlog=in.getStringExtra("title");
        blogId=in.getStringExtra("blogId");*/
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Categories");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        topCatregories=findViewById(R.id.rv_topcategories);
        allCategories=findViewById(R.id.rv_allcategories);
        linearLayoutManager1=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManager2=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        topList=new ArrayList<BlogTopCategories>();
        allList=new ArrayList<BlogAllCategories>();
        prepareTopCategories();
        topadapter=new BlogTopCategoryAdapter(this,topList);
        topCatregories.setLayoutManager(linearLayoutManager1);
        topCatregories.setAdapter(topadapter);
        prepareAllCategories();
        allAdapter=new BlogMainCategoryAdapter(this,allList);
        allCategories.setLayoutManager(linearLayoutManager2);
        allCategories.setAdapter(allAdapter);


    }
    private void prepareAllCategories()
    {
        blogAllCategories=new BlogAllCategories(R.drawable.ic_dropbox,"Technology");
        allList.add(blogAllCategories);
        blogAllCategories=new BlogAllCategories(R.drawable.ic_foursquare,"Finance");
        allList.add(blogAllCategories);
        blogAllCategories=new BlogAllCategories(R.drawable.ic_behance,"Infrastructor");
        allList.add(blogAllCategories);
        blogAllCategories=new BlogAllCategories(R.drawable.ic_man,"Human Resource");
        allList.add(blogAllCategories);
        blogAllCategories=new BlogAllCategories(R.drawable.ic_quora,"L & D");
        allList.add(blogAllCategories);
    }
    private void prepareTopCategories()
    {
        blogTopCategories=new BlogTopCategories(R.drawable.ic_bank_building);
        topList.add(blogTopCategories);
        blogTopCategories=new BlogTopCategories(R.drawable.ic_vimeo);
        topList.add(blogTopCategories);
        blogTopCategories=new BlogTopCategories(R.drawable.ic_vimeo);
        topList.add(blogTopCategories);
        blogTopCategories=new BlogTopCategories(R.drawable.ic_vimeo);
        topList.add(blogTopCategories);
        blogTopCategories=new BlogTopCategories(R.drawable.ic_vimeo);
        topList.add(blogTopCategories);
        blogTopCategories=new BlogTopCategories(R.drawable.ic_vimeo);
        topList.add(blogTopCategories);
        blogTopCategories=new BlogTopCategories(R.drawable.ic_vimeo);
        topList.add(blogTopCategories);
        blogTopCategories=new BlogTopCategories(R.drawable.ic_dropbox);
        topList.add(blogTopCategories);
        blogTopCategories=new BlogTopCategories(R.drawable.ic_foursquare);
        topList.add(blogTopCategories);
    }

    @Override
    public void callupdateActivity() {
        Log.d("gayathry","hjghj");

        overridePendingTransition(R.anim.slide_from_right,R.anim.slide_left_out);
    }
}
