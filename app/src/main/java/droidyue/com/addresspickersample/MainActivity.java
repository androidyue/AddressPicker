package droidyue.com.addresspickersample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import droidyue.com.addresspicker.Address;
import droidyue.com.addresspicker.AddressItem;
import droidyue.com.addresspicker.AddressPickerListener;
import droidyue.com.addresspicker.AddressPickerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final AddressPickerView addressPickerView = findViewById(R.id.address_picker_view);

        Address address = AddressProcessor.INSTANCE.convertToAddress(prepareAddressData());
        addressPickerView.setAddressData(address);

        addressPickerView.setOnAddressPickerListener(new AddressPickerListener() {

            @Override
            public void onAddressPicked(AddressItem province, AddressItem city, AddressItem district) {
                Log.i("MainActivity", "onAddressPicked address=" +
                        ";provinceCode=" + province.getName() + ";cityCode=" + city.getName()
                        + ";districtCode=" + district.getName());
                addressPickerView.setVisibility(View.GONE);
            }

            @Override
            public boolean handleNotReadyError() {
                return false;
            }

            @Override
            public boolean handleProvinceNotSelectedError() {
                return false;
            }

            @Override
            public boolean handleCityNotSelectedError() {
                return false;
            }

            @Override
            public void onAddressReady() {

            }
        });
    }

    private MyAddressResponse prepareAddressData() {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader addressJsonStream = new BufferedReader(new InputStreamReader(getAssets().open("address.json")));
            String line;
            while ((line = addressJsonStream.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(builder.toString(), MyAddressResponse.class);
    }
}
