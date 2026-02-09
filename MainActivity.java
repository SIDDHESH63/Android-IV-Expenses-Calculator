package com.example.expenses;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText etName, etAmount;
    Button btnAdd;
    ListView listExpenses;
    TextView tvTotal;
    DBHelper dbHelper;
    ArrayList<String> expenseList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etExpenseName);
        etAmount = findViewById(R.id.etExpenseAmount);
        btnAdd = findViewById(R.id.btnAddExpense);
        listExpenses = findViewById(R.id.listExpenses);
        tvTotal = findViewById(R.id.tvTotal);

        dbHelper = new DBHelper(this);
        expenseList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseList);
        listExpenses.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String amtStr = etAmount.getText().toString();

            if (!name.isEmpty() && !amtStr.isEmpty()) {
                double amount = Double.parseDouble(amtStr);
                dbHelper.insertExpense(name, amount);
                loadExpenses();
                etName.setText("");
                etAmount.setText("");
            }
        });

        loadExpenses();
    }

    private void loadExpenses() {
        expenseList.clear();
        Cursor cursor = dbHelper.getAllExpenses();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                double amount = cursor.getDouble(2);
                expenseList.add(name + " - ₹" + amount);
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();

        // Update total
        double total = dbHelper.getTotalExpenses();
        tvTotal.setText("Total: ₹" + total);
    }
}
