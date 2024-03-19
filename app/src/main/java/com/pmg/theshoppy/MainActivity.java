package com.pmg.theshoppy;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pmg.theshoppy.databinding.ActivityMainBinding;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String selectedComputerType = "";
    String selectedBrand = "";
    String[] provinces = {"Alberta", "British Columbia", "Manitoba", "New Brunswick", "Newfoundland and Labrador", "Nova Scotia", "Ontario", "Prince Edward Island", "Quebec", "Saskatchewan"};
    String[] computerBrands = {"Dell", "HP", "Lenovo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupComputerTypeRadioGroup();
        setupBrandSpinner();
        setupCalculateInvoice();

    }

    private void setupCalculateInvoice() {
        binding.btnInvoice.setOnClickListener(view -> {
            boolean isValid = validateDataAndCalculateInvoice();
        });
    }

    private boolean validateDataAndCalculateInvoice() {
        String name = binding.etCustomerName.getText().toString();
        String province = binding.actProvince.getText().toString();
        String brand = binding.spinnerBrand.getSelectedItem().toString();
        boolean ssd = binding.cbSsd.isChecked();
        boolean printer = binding.cbPrinter.isChecked();
        int peripheralsId = binding.rgPeripherals.getCheckedRadioButtonId();
        RadioButton peripherals = findViewById(peripheralsId);
        String selectedPeripherals = peripherals.getText().toString();

        if (selectedComputerType.isEmpty()) {
            Toast.makeText(this, "Please select Computer type first!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (brand.isEmpty()) {
            Toast.makeText(this, "Please select Brand type first!!", Toast.LENGTH_SHORT).show();
            return false;
        }

        double cost = 0;
        if (selectedComputerType.equals("Desktop")) {
            if (brand.equals("Dell")) {
                cost = 475;
            } else if (brand.equals("HP")) {
                cost = 400;
            } else if (brand.equals("Lenovo")) {
                cost = 450;
            }
            if (ssd) {
                cost += 60;
            }
            if (printer) {
                cost += 100;
            }
            if (selectedPeripherals.equals("Webcam")) {
                cost += 95;
            } else if (selectedPeripherals.equals("External Hard Drive")) {
                cost += 64;
            }
        } else if (selectedComputerType.equals("Laptop")) {
            switch (brand) {
                case "Dell":
                    cost = 1249;
                    break;
                case "HP":
                    cost = 1150;
                    break;
                case "Lenovo":
                    cost = 1549;
                    break;
            }
            if (ssd) {
                cost += 60;
            }
            if (printer) {
                cost += 100;
            }
            switch (selectedPeripherals) {
                case "Cooling Mat":
                    cost += 33;
                    break;
                case "USB C-HUB":
                    cost += 60;
                    break;
                case "Laptop Stand":
                    cost += 139;
                    break;
            }
        }

        double tax = cost * 0.13;
        double totalCost = cost + tax;
        String totalPrice = String.format(Locale.CANADA, "%.2f", totalCost);

        String strBrand = "";
        if (ssd && printer) {
            strBrand = "SSD & Printer";
        } else if (ssd){
            strBrand = "SSD";
        } else if (printer) {
            strBrand = "Printer";
        }

        String formatInvoiceString = "Customer Name: " + name + "\n" + "Province: " + province + "\n" + "Computer: " + selectedComputerType + "\n" + "Brand: " + brand + "\n" + "Additional: " + strBrand + "\n" + "Added Peripherals: " + selectedComputerType + "\n" + "Cost: $" + totalPrice;
        binding.tvInvoice.setText(formatInvoiceString);
        return true;
    }

    private void setupComputerTypeRadioGroup() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, provinces);
        binding.actProvince.setAdapter(adapter);

        // Listener to the RadioGroup to detect changes in the selection of Desktop or Laptop
        binding.rgComputerType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbDesktop) {
                selectedComputerType = "Desktop";
                binding.rbNone.setVisibility(View.VISIBLE);
                binding.rbExternalHardDrive.setVisibility(View.VISIBLE);
                binding.rbWebcam.setVisibility(View.VISIBLE);
                binding.rbLaptopStand.setVisibility(View.GONE);
                binding.rbCoolingMat.setVisibility(View.GONE);
                binding.rbUsbCHub.setVisibility(View.GONE);
            } else if (checkedId == R.id.rbLaptop) {
                selectedComputerType = "Laptop";
                binding.rbNone.setVisibility(View.GONE);
                binding.rbExternalHardDrive.setVisibility(View.GONE);
                binding.rbWebcam.setVisibility(View.GONE);
                binding.rbLaptopStand.setVisibility(View.VISIBLE);
                binding.rbCoolingMat.setVisibility(View.VISIBLE);
                binding.rbUsbCHub.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupBrandSpinner() {

        // Having the list of brands
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, computerBrands);

        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinnerBrand.setAdapter(brandAdapter);

        binding.spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBrand = computerBrands[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}