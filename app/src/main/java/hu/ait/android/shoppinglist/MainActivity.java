package hu.ait.android.shoppinglist;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import hu.ait.android.shoppinglist.adapter.ItemAdapter;
import hu.ait.android.shoppinglist.data.Item;
import hu.ait.android.shoppinglist.touch.ItemsListTouchHelperCallback;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_NEW_ITEM = 111;
    public static final int REQUEST_EDIT_ITEM = 112;
    public static final String KEY_EDIT = "KEY_EDIT";
    private ItemAdapter itemsAdapter;
    private CoordinatorLayout layoutContent;
    private DrawerLayout drawerLayout;
    private Item itemToEditHolder;
    private int itemToEditPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Item> itemsList = Item.listAll(Item.class);

        itemsAdapter = new ItemAdapter(itemsList, this);
        RecyclerView recyclerViewPlaces = (RecyclerView) findViewById(
                R.id.recyclerViewPlaces);
        recyclerViewPlaces.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPlaces.setAdapter(itemsAdapter);

        ItemsListTouchHelperCallback touchHelperCallback = new ItemsListTouchHelperCallback(
                itemsAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(
                touchHelperCallback);
        touchHelper.attachToRecyclerView(recyclerViewPlaces);

        layoutContent = (CoordinatorLayout) findViewById(
                R.id.layoutContent);

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateItemActivity();
            }
        });

        FloatingActionButton fabDelete = (FloatingActionButton) findViewById(R.id.btnDeleteAll);
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllItems();
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
                            case R.id.action_totalcost:
                                showSnackBarMessage(getString(R.string.totalcost_for_snackbar)+Integer.toString(itemsAdapter.findTotalCost()));
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
        toolbar.setNavigationIcon(R.drawable.app_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    private void showCreateItemActivity() {
        Intent intentStart = new Intent(MainActivity.this,
                CreateItemActivity.class);
        startActivityForResult(intentStart, REQUEST_NEW_ITEM);
    }

    private void deleteAllItems() {
        itemsAdapter.removeAllItems();
    }

    public void showEditItemActivity(Item itemToEdit, int position) {
        Intent intentStart = new Intent(MainActivity.this,
                CreateItemActivity.class);
        itemToEditHolder = itemToEdit;
        itemToEditPosition = position;

        intentStart.putExtra(KEY_EDIT, itemToEdit);
        startActivityForResult(intentStart, REQUEST_EDIT_ITEM);
    }

    public void showDescriptionItemActivity(Item itemToEdit) {
        showSnackBarMessage((itemToEdit.getItemDescription()));
    }

    public void showCheckBoxClickedActivity(boolean status) {
        if (status) {
            showSnackBarMessage(getString(R.string.message_if_bought));
        } else {
            showSnackBarMessage(getString(R.string.message_if_not_bought));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == REQUEST_NEW_ITEM) {
                    Item item = (Item) data.getSerializableExtra(
                            CreateItemActivity.KEY_ITEM);

                    itemsAdapter.addItem(item);
                    showSnackBarMessage(getString(R.string.txt_place_added));
                } else if (requestCode == REQUEST_EDIT_ITEM) {
                    Item itemTemp = (Item) data.getSerializableExtra(
                            CreateItemActivity.KEY_ITEM);

                    itemToEditHolder.setItemName(itemTemp.getItemName());
                    itemToEditHolder.setItemDescription(itemTemp.getItemDescription());
                    itemToEditHolder.setItemType(itemTemp.getItemType());
                    itemToEditHolder.setEstimatedPrice(itemTemp.getEstimatedPrice());

                    if (itemToEditPosition != -1) {
                        itemsAdapter.updateItem(itemToEditPosition, itemToEditHolder);
                        itemToEditPosition = -1;
                    } else {
                        itemsAdapter.notifyDataSetChanged();
                    }
                    showSnackBarMessage(getString(R.string.txt_place_edited));
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
            default:
                showCreateItemActivity();
                return true;
        }
    }
}
