package hu.ait.camdensikes.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

import hu.ait.camdensikes.shoppinglist.data.ListItem;

import static hu.ait.camdensikes.shoppinglist.R.id.checkbox;
import static hu.ait.camdensikes.shoppinglist.R.id.etItemDesc;

public class AddItemActivity extends AppCompatActivity {

    public static final String KEY_ITEM = "KEY_ITEM";
    private Spinner spinnerItemType;
    private EditText etItem;
    private EditText etItemDesc;
    private EditText etPrice;
    private ListItem itemToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        if (getIntent().getSerializableExtra(MainActivity.KEY_EDIT) != null) {
            itemToEdit = (ListItem) getIntent().getSerializableExtra(MainActivity.KEY_EDIT);
        }

        spinnerItemType = (Spinner) findViewById(R.id.spinnerItemType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.itemtypes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItemType.setAdapter(adapter);

        etItem = (EditText) findViewById(R.id.etItemName);
        etItemDesc = (EditText) findViewById(R.id.etItemDesc);
        etPrice = (EditText) findViewById(R.id.etPrice);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });

        if (itemToEdit != null) {
            etItem.setText(itemToEdit.getItemName());
            etItemDesc.setText(itemToEdit.getDescription());
            etPrice.setText(itemToEdit.getPrice());
            spinnerItemType.setSelection(itemToEdit.getItemType().getValue());
        }
    }

    private void saveItem() {
        if(fieldsComplete()) {
            Intent intentResult = new Intent();
            ListItem itemResult = null;
            if (itemToEdit != null) {
                itemResult = itemToEdit;
                itemResult.setChecked(itemToEdit.isChecked());
            } else {
                itemResult = new ListItem();
            }

            itemResult.setItemName(etItem.getText().toString());
            itemResult.setDescription(etItemDesc.getText().toString());
            itemResult.setPrice(String.format(getString(R.string.price), etPrice.getText().toString()));
            itemResult.setItemType(
                    ListItem.ItemType.fromInt(spinnerItemType.getSelectedItemPosition()));
            intentResult.putExtra(KEY_ITEM, itemResult);
            setResult(RESULT_OK, intentResult);
            finish();
        }
    }

    private boolean fieldsComplete() {
        boolean result = true;
        if(etItem.getText().toString().equals("")){
            etItem.setError(getString(R.string.enter_name));
            result = false;
        }
        if(etPrice.getText().toString().equals("")){
            etPrice.setError(getString(R.string.enter_price));
            result = false;
        }
        return result;
    }
}
