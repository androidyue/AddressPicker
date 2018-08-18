package droidyue.com.addresspicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddressPickerView extends RelativeLayout implements View.OnClickListener {
    private int listItemSelectedColor =  Color.parseColor("#50AA00");;
    private int listItemUnSelectedColor = Color.parseColor("#262626");
    private int confirmButtonNotReadyTextColor = Color.parseColor("#7F7F7F");
    private int confirmButtonReadyTextColor = Color.parseColor("#50AA00");

    private TabLayout mTabLayout;
    private RecyclerView mAddressListView;
    private String tabProvinceText = "省份";
    private String tabCityText = "城市";
    private String tabDistrictText = "区县";

    private AddressAdapter mAdapter;

    private Address mAddressData;
    private AddressItem mSelectedProvice;
    private AddressItem mSelectedCity;
    private AddressItem mSelectedDistrict;
    private int mSelectedProvicePosition = 0;
    private int mSelectedCityPosition = 0;
    private int mSelectedDistrictPosition = 0;

    public static final int TAB_POSITION_PROVINCE = 0;
    public static final int TAB_POSITION_CITY = 1;
    public static final int TAB_POSITION_DISTRICT = 2;

    private AddressPickerListener mAddressPickerListener;
    private TextView mConfirmTextView;

    public AddressPickerView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public AddressPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public AddressPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        if (attributeSet != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.AddressPickerView, defStyleAttr, 0);
            listItemSelectedColor = typedArray.getColor(R.styleable.AddressPickerView_item_selected_color, listItemSelectedColor);
            listItemUnSelectedColor = typedArray.getColor(R.styleable.AddressPickerView_item_unselected_color, listItemUnSelectedColor);
            confirmButtonNotReadyTextColor = typedArray.getColor(R.styleable.AddressPickerView_confirm_button_not_ready_text_color, confirmButtonNotReadyTextColor);
            confirmButtonReadyTextColor = typedArray.getColor(R.styleable.AddressPickerView_confirm_button_ready_text_color, confirmButtonReadyTextColor);
            String provinceText = typedArray.getString(R.styleable.AddressPickerView_tab_item_province_text);
            if (!TextUtils.isEmpty(provinceText)) {
                tabProvinceText = provinceText;
            }

            String cityText = typedArray.getString(R.styleable.AddressPickerView_tab_item_city_text);
            if (!TextUtils.isEmpty(cityText)) {
                tabCityText = cityText;
            }

            String districtText = typedArray.getString(R.styleable.AddressPickerView_tab_item_district_text);
            if (!TextUtils.isEmpty(districtText)) {
                tabDistrictText = districtText;
            }

            typedArray.recycle();
        }

        View rootView = inflate(context, R.layout.address_picker_view, this);

        mConfirmTextView = rootView.findViewById(R.id.textview_confirm);
        mConfirmTextView.setTextColor(confirmButtonNotReadyTextColor);
        mConfirmTextView.setOnClickListener(this);

        mTabLayout = rootView.findViewById(R.id.tlTabLayout);
        mTabLayout.addTab(mTabLayout.newTab().setText(tabProvinceText));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabCityText));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabDistrictText));
        mTabLayout.addOnTabSelectedListener(tabSelectedListener);

        mAddressListView = rootView.findViewById(R.id.rvList);
        mAddressListView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new AddressAdapter();
        mAddressListView.setAdapter(mAdapter);
    }


    public void setAddressData(Address address) {
        if (address != null) {
            mAddressData = address;
            mAdapter.setData(address.getProvinceList());
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.textview_confirm) {
            if (mSelectedProvice != null && mSelectedCity != null && mSelectedDistrict != null) {
                if (mAddressPickerListener != null) {
                    mAddressPickerListener.onAddressPicked(mSelectedProvice, mSelectedCity, mSelectedDistrict);
                }
            } else {
                boolean handled = false;
                if (mAddressPickerListener != null) {
                    handled = mAddressPickerListener.handleNotReadyError();
                }

                if (!handled) {
                    Toast.makeText(getContext(), "地址还没有选完整哦", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mAdapter.clearData();
            switch (tab.getPosition()) {
                case TAB_POSITION_PROVINCE :
                    mAdapter.setData(mAddressData.getProvinceList());
                    mAddressListView.smoothScrollToPosition(mSelectedProvicePosition);
                    break;

                case TAB_POSITION_CITY :

                    if (mSelectedProvice != null) {
                        for (AddressItem itemBean : mAddressData.getCityList()) {
                            if (itemBean.getParentCode().equals(mSelectedProvice.getCode())) {
                                mAdapter.addData(itemBean);
                            }
                        }
                    } else {
                        boolean handled = false;
                        if (mAddressPickerListener != null) {
                            handled = mAddressPickerListener.handleProvinceNotSelectedError();
                        }
                        if (!handled) {
                            Toast.makeText(getContext(), "请您先选择省份", Toast.LENGTH_SHORT).show();
                        }
                    }

                    mAdapter.notifyDataSetChanged();
                    mAddressListView.smoothScrollToPosition(mSelectedCityPosition);
                    break;

                case TAB_POSITION_DISTRICT :
                    if (mSelectedProvice != null && mSelectedCity != null) {
                        for (AddressItem itemBean : mAddressData.getDistrictList()) {
                            if (itemBean.getParentCode().equals(mSelectedCity.getCode()))
                                mAdapter.addData(itemBean);
                        }
                    } else {
                        boolean handled = false;
                        if (mAddressPickerListener != null) {
                            handled = mAddressPickerListener.handleCityNotSelectedError();
                        }
                        if (!handled) {
                            Toast.makeText(getContext(), "请您先选择省份与城市", Toast.LENGTH_SHORT).show();
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    mAddressListView.smoothScrollToPosition(mSelectedDistrictPosition);
                    break;
            }


        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };


    class AddressAdapter extends RecyclerView.Adapter<AddressItemViewHolder> {
        private List<AddressItem> addressItems = new ArrayList<>(); // 用来在recyclerview显示的数据

        public void clearData() {
            addressItems.clear();
        }


        public void setData(List<AddressItem> addressItems) {
            this.addressItems.clear();
            this.addressItems.addAll(addressItems);
            notifyDataSetChanged();
        }

        public void addData(AddressItem addressItem) {
            addressItems.add(addressItem);
        }

        @Override
        public AddressItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AddressItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_text, parent, false));
        }

        private void updateItemViewColor(TextView textView, int itemPosition, int tabSelectPosition) {
            AddressItem addressItem = addressItems.get(itemPosition);
            if (addressItem == null) {
                return;
            }
            switch (tabSelectPosition) {
                case TAB_POSITION_PROVINCE:
                    if (mSelectedProvice != null && addressItem.getCode().equals(mSelectedProvice.getCode())) {
                        textView.setTextColor(listItemSelectedColor);
                    }
                    break;
                case TAB_POSITION_CITY:
                    if (mSelectedCity != null && addressItem.getCode().equals(mSelectedCity.getCode())) {
                        textView.setTextColor(listItemSelectedColor);
                    }
                    break;
                case TAB_POSITION_DISTRICT:
                    if (mSelectedDistrict != null
                            && addressItem.getCode().equals(mSelectedDistrict.getCode())
                            && addressItem.getName().equals(mSelectedDistrict.getName())) {
                        textView.setTextColor(listItemSelectedColor);
                    }
                    break;
            }
        }


        @Override
        public void onBindViewHolder(final AddressItemViewHolder holder, final int position) {
            final int tabSelectPosition = mTabLayout.getSelectedTabPosition();
            holder.mTitleView.setText(addressItems.get(position).getName());
            holder.mTitleView.setTextColor(listItemUnSelectedColor);
            updateItemViewColor(holder.mTitleView, position, tabSelectPosition);

            holder.mTitleView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (tabSelectPosition) {
                        case TAB_POSITION_PROVINCE:
                            mSelectedProvice = addressItems.get(position);
                            mSelectedCity = null;
                            mSelectedDistrict = null;
                            mSelectedCityPosition = 0;
                            mSelectedDistrictPosition = 0;
                            mSelectedProvicePosition = position;


                            mTabLayout.getTabAt(TAB_POSITION_PROVINCE).setText(mSelectedProvice.getName());
                            mTabLayout.getTabAt(TAB_POSITION_CITY).setText(tabCityText);
                            mTabLayout.getTabAt(TAB_POSITION_DISTRICT).setText(tabDistrictText);
                            mTabLayout.getTabAt(TAB_POSITION_CITY).select();

                            mConfirmTextView.setTextColor(confirmButtonNotReadyTextColor);
                            break;
                        case TAB_POSITION_CITY:
                            mSelectedCity = addressItems.get(position);
                            mSelectedDistrict = null;
                            mSelectedDistrictPosition = 0;
                            mSelectedCityPosition = position;

                            mTabLayout.getTabAt(TAB_POSITION_CITY).setText(mSelectedCity.getName());
                            mTabLayout.getTabAt(TAB_POSITION_DISTRICT).setText(tabDistrictText);
                            mTabLayout.getTabAt(TAB_POSITION_DISTRICT).select();

                            mConfirmTextView.setTextColor(confirmButtonNotReadyTextColor);
                            break;
                        case TAB_POSITION_DISTRICT:
                            mSelectedDistrict = addressItems.get(position);
                            mSelectedDistrictPosition = position;
                            mTabLayout.getTabAt(TAB_POSITION_DISTRICT).setText(mSelectedDistrict.getName());
                            //update the selected item view to be displayed in a selected style
                            notifyDataSetChanged();
                            if (mAddressPickerListener != null) {
                                mAddressPickerListener.onAddressReady();
                            }
                            mConfirmTextView.setTextColor(confirmButtonReadyTextColor);
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return addressItems.size();
        }
    }

    public void setOnAddressPickerListener(AddressPickerListener listener) {
        mAddressPickerListener = listener;
    }

}
