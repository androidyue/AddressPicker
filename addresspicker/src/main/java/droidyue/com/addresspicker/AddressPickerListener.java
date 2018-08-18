package droidyue.com.addresspicker;

public interface AddressPickerListener {
    void onAddressPicked(AddressItem province, AddressItem city, AddressItem district);
    boolean handleNotReadyError();
    boolean handleProvinceNotSelectedError();
    boolean handleCityNotSelectedError();
    void onAddressReady();
}
