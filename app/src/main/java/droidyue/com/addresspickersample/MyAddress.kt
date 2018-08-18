package droidyue.com.addresspickersample

import android.support.annotation.Keep

@Keep
data class MyAddressResponse(val address: MyAddress)

@Keep
data class MyAddressItem(val c: String, val n: String);

@Keep
data class MyAddress(val cities: Map<String, List<MyAddressItem>>, val provinces: List<MyAddressItem>,
                     val areas: Map<String, List<MyAddressItem>>);