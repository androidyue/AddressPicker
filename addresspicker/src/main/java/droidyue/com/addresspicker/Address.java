package droidyue.com.addresspicker;

import java.io.Serializable;
import java.util.List;

public class Address {

    private List<AddressItem> provinceList;
    private List<AddressItem> cityList;
    private List<AddressItem> districtList;

    public List<AddressItem> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<AddressItem> provinceList) {
        this.provinceList = provinceList;
    }

    public List<AddressItem> getCityList() {
        return cityList;
    }

    public void setCityList(List<AddressItem> cityList) {
        this.cityList = cityList;
    }

    public List<AddressItem> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<AddressItem> districtList) {
        this.districtList = districtList;
    }

}
