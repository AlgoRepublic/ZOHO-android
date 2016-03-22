package com.algorepublic.zoho.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.algorepublic.zoho.ActivitySplash;
import com.algorepublic.zoho.MainActivity;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.ToggleExpandLayout;
import com.androidquery.AQuery;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by android on 3/10/16.
 */
public class MenuSettingsFragment extends BaseFragment {

    BaseClass baseClass;
    static MenuSettingsFragment fragment;
    CheckBox checkBox;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    AQuery aq;

    public static MenuSettingsFragment newInstance() {
        fragment = new MenuSettingsFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_settings, container, false);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        getToolbar().setTitle(getString(R.string.settings));
        aq = new AQuery(view);
checkBox=(CheckBox)view.findViewById(R.id.switch_theme);
        final ToggleExpandLayout layout = (ToggleExpandLayout) view.findViewById(R.id.toogleLayout);
        ((CheckBox) view.findViewById(R.id.switch_theme)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    layout.close();

                } else {
                    layout.open();
                }
            }
        });

        ((RadioGroup) view.findViewById(R.id.radio_theme)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.blue_theme:
                        baseClass.setThemePreference(R.style.AppThemeBlue);
                        break;
                    case R.id.black_theme:
                        baseClass.setThemePreference(R.style.AppTheme);
                        break;
                }

                getActivity().finish();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        aq = new AQuery(view);

        aq.id(R.id.logout).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseClass.setUserId("");
                Intent intent = new Intent(getActivity(), ActivitySplash.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
        aq.id(R.id.edit_profile).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithBackStack(R.id.container, EditProfileFragment.newInstance(), "FragmentEditProfile");
            }
        });
        aq.id(R.id.rest_password).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithBackStack(R.id.container, RestPasswordFragment.newInstance(), "RestPasswordFragment");
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
