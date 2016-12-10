package hu.ait.camdensikes.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import hu.ait.camdensikes.shoppinglist.adapter.ItemAdapter;
import hu.ait.camdensikes.shoppinglist.data.ListItem;
import hu.ait.camdensikes.shoppinglist.touch.ListTouchHelperCallback;


public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_NEW_ITEM = 101;
    public static final int REQUEST_EDIT_ITEM = 102;
    public static final String KEY_EDIT = "KEY_EDIT";
    private ItemAdapter itemAdapter;
    private CoordinatorLayout layoutContent;
    private DrawerLayout drawerLayout;
    private ListItem itemToEditHolder;
    private int itemToEditPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<ListItem> itemList = ListItem.listAll(ListItem.class);

        itemAdapter = new ItemAdapter(itemList, this);
        RecyclerView recyclerViewItems = (RecyclerView) findViewById(
                R.id.recyclerViewItems);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItems.setAdapter(itemAdapter);

        ListTouchHelperCallback touchHelperCallback = new ListTouchHelperCallback(
                itemAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(
                touchHelperCallback);
        touchHelper.attachToRecyclerView(recyclerViewItems);

        layoutContent = (CoordinatorLayout) findViewById(
                R.id.layoutContent);

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateItemActivity();
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.action_add:
                                showCreateItemActivity();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_about:
                                showSnackBarMessage(getString(R.string.txt_about));
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_help:
                                showSnackBarMessage(getString(R.string.txt_help));
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                        }

                        return false;
                    }
                });

        setUpToolBar();
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    private void showCreateItemActivity() {
        Intent intentStart = new Intent(MainActivity.this,
                AddItemActivity.class);
        startActivityForResult(intentStart, REQUEST_NEW_ITEM);
    }

    public void showEditItemActivity(ListItem itemToEdit, int position) {
        Intent intentStart = new Intent(MainActivity.this,
                AddItemActivity.class);
        itemToEditHolder = itemToEdit;
        itemToEditPosition = position;

        intentStart.putExtra(KEY_EDIT, itemToEdit);
        startActivityForResult(intentStart, REQUEST_EDIT_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == REQUEST_NEW_ITEM) {
                    ListItem item = (ListItem) data.getSerializableExtra(
                            AddItemActivity.KEY_ITEM);

                    itemAdapter.addItem(item);
                    showSnackBarMessage(getString(R.string.txt_item_added));
                } else if (requestCode == REQUEST_EDIT_ITEM) {
                    ListItem itemTemp = (ListItem) data.getSerializableExtra(
                            AddItemActivity.KEY_ITEM);

                    itemToEditHolder.setItemName(itemTemp.getItemName());
                    itemToEditHolder.setDescription(itemTemp.getDescription());
                    itemToEditHolder.setItemType(itemTemp.getItemType());

                    if (itemToEditPosition != -1) {
                        itemAdapter.updateItem(itemToEditPosition, itemToEditHolder);
                        itemToEditPosition = -1;
                    }else {
                        itemAdapter.notifyDataSetChanged();
                    }
                    showSnackBarMessage(getString(R.string.txt_item_edited));
                }
                break;
            case RESULT_CANCELED:
                showSnackBarMessage(getString(R.string.txt_add_cancel));
                break;
        }
    }


    private void showSnackBarMessage(String message) {
        Snackbar.make(layoutContent,
                message,
                Snackbar.LENGTH_LONG
        ).setAction(R.string.action_hide, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...
            }
        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                showCreateItemActivity();
                return true;
            case R.id.action_delete_all:
                itemAdapter.clearList();
                return true;
            case R.id.action_delete_checked:
                itemAdapter.clearChecked();
                return true;
            default:
                showCreateItemActivity();
                return true;
        }
    }

    public void update(ListItem listItem, int position) {
        itemAdapter.updateItem(position, listItem);
    }
}

