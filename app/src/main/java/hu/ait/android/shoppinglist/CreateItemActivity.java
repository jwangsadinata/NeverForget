package hu.ait.android.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import hu.ait.android.shoppinglist.data.Item;

public class CreateItemActivity extends AppCompatActivity {
    public static final String KEY_ITEM = "KEY_ITEM";
    private Spinner spinnerPlaceType;
    private EditText etItemName;
    private EditText etItemDesc;
    private EditText etItemPrice;
    private Item itemToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        if (getIntent().getSerializableExtra(MainActivity.KEY_EDIT) != null) {
            itemToEdit = (Item) getIntent().getSerializableExtra(MainActivity.KEY_EDIT);
        }

        spinnerPlaceType = (Spinner) findViewById(R.id.spinnerItemType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.itemtypes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlaceType.setAdapter(adapter);

        etItemName = (EditText) findViewById(R.id.etItemName);
        etItemDesc = (EditText) findViewById(R.id.etItemDesc);
        etItemPrice = (EditText) findViewById(R.id.etItemPrice);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePlace();
            }
        });

        if (itemToEdit != null) {
            etItemName.setText(itemToEdit.getItemName());
            etItemDesc.setText(itemToEdit.getItemDescription());
            etItemPrice.setText(itemToEdit.getEstimatedPrice());
            spinnerPlaceType.setSelection(itemToEdit.getItemType().getValue());
        }
    }

    private void savePlace() {
        Intent intentResult = new Intent();
        Item itemResult = null;
        if (itemToEdit != null) {
            itemResult = itemToEdit;
        } else {
            itemResult = new Item();
        }

        itemResult.setItemName(etItemName.getText().toString());
        itemResult.setItemDescription(etItemDesc.getText().toString());
        itemResult.setEstimatedPrice(etItemPrice.getText().toString());
        itemResult.setItemType(
                Item.ItemType.fromInt(spinnerPlaceType.getSelectedItemPosition()));

        intentResult.putExtra(KEY_ITEM, itemResult);
        setResult(RESULT_OK, intentResult);
        finish();
    }
}
