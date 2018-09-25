package com.navigationdrawer;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import com.navigationdrawer.Activity.BaseAppCompatActivity;
import com.navigationdrawer.Adapter.ExpandListAdapter;
import com.navigationdrawer.GetSet.ExpandListGetSet;
import com.navigationdrawer.webservice.ErrorType;
import com.navigationdrawer.webservice.ResponsePacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseAppCompatActivity
{

    ExpandableListView expandableListView;
    Toolbar toolbar;
    DrawerLayout drawer;
    ArrayList<ExpandListGetSet> listDataHeader = new ArrayList<>();
    HashMap<ExpandListGetSet, List<String>> listDataChild = new HashMap<ExpandListGetSet, List<String>>();
    ExpandListAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setAsAction();
    }

    private void init()
    {
        toolbar = findViewById(R.id.toolbar);
        drawer =  findViewById(R.id.drawer_layout);
        expandableListView =  findViewById(R.id.expandableListView);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        enableExpandableList();

//        getData();

    }

   /* private void getData()
    {
        JSONObject jsonObject=new JSONObject();
        try
        {
            jsonObject.put("","");

            new InteractorImpl(MainActivity.this,MainActivity.this, Interactor.RequestCode_Login,Interactor.Tag_Login).
                    makeJsonPostRequest(Interactor.Method_Login,jsonObject,false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/

    private void enableExpandableList()
    {
            prepareListData(listDataHeader,listDataChild);
            listAdapter=new ExpandListAdapter(this,listDataHeader,listDataChild);
            expandableListView.setAdapter(listAdapter);
    }

    private void prepareListData(ArrayList<ExpandListGetSet> listDataHeader, HashMap<ExpandListGetSet, List<String>> listDataChild)
    {
        listDataHeader.add(new ExpandListGetSet(getResources().getString(R.string.home),R.drawable.ic_menu_camera));
        listDataHeader.add(new ExpandListGetSet(getResources().getString(R.string.caraccess),R.drawable.ic_menu_gallery));
        listDataHeader.add(new ExpandListGetSet(getResources().getString(R.string.dealers),R.drawable.ic_menu_manage));
        listDataHeader.add(new ExpandListGetSet(getResources().getString(R.string.wishlist),R.drawable.ic_menu_send));
        listDataHeader.add(new ExpandListGetSet(getResources().getString(R.string.more),android.R.drawable.ic_menu_more));

        ArrayList<String> more=new ArrayList<>();
        more.add(getResources().getString(R.string.aboutus));
        more.add(getResources().getString(R.string.newsupdate));
        more.add(getResources().getString(R.string.downleaf));
        more.add(getResources().getString(R.string.contactus));

        listDataChild.put(listDataHeader.get(0),new ArrayList<String>());
        listDataChild.put(listDataHeader.get(1),new ArrayList<String>());
        listDataChild.put(listDataHeader.get(2),new ArrayList<String>());
        listDataChild.put(listDataHeader.get(3),new ArrayList<String>());
        listDataChild.put(listDataHeader.get(4),more);

    }

    private void setAsAction()
    {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket)
    {
        super.onSuccess(requestCode, responsePacket);

    }

    @Override
    public void onError(int requestCode, ErrorType errorType)
    {
        super.onError(requestCode, errorType);

    }
}
