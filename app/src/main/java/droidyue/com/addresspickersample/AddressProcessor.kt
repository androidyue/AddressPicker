package droidyue.com.addresspickersample

import droidyue.com.addresspicker.Address
import droidyue.com.addresspicker.AddressItem

object AddressProcessor {

    fun convertToAddress(secooAddressResponse: MyAddressResponse): Address {
        val address = Address()
        val provinceList = mutableListOf<AddressItem>()
        address.provinceList = provinceList
        secooAddressResponse.address.provinces.forEach {
            province ->
                val provinceItem = AddressItem().apply {
                    code = province.c
                    name = province.n
                }
                provinceList.add(provinceItem)
        }

        val cityList = mutableListOf<AddressItem>()
        address.cityList = cityList
        secooAddressResponse.address.cities.entries.forEach {
            entry ->
                entry.value.forEach {
                    item ->
                    val cityItem = AddressItem().apply {
                        code  = item.c
                        name = item.n
                        parentCode = entry.key
                    }
                    cityList.add(cityItem)
                }

        }

        val districtList = mutableListOf<AddressItem>()
        address.districtList = districtList
        secooAddressResponse.address.areas.entries.forEach {
            entry ->
                entry.value.forEach {
                    val districtItem = AddressItem().apply {
                        code = it.c
                        name = it.n
                        parentCode = entry.key
                    }
                    districtList.add(districtItem)
                }

        }

        return address
    }
}