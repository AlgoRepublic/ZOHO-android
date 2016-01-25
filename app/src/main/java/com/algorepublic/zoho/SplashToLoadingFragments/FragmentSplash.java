package com.algorepublic.zoho.SplashToLoadingFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.BaseFragment;

import java.lang.reflect.Field;

/**
 * Created by android on 1/25/16.
 */
public class FragmentSplash extends BaseFragment {

    static FragmentSplash fragment;
    View view;
    public FragmentSplash() {
    }

    @SuppressWarnings("unused")
    public static FragmentSplash newInstance() {
        if (fragment == null) {
            fragment = new FragmentSplash();
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_splash_zoho, container, false);
        return view;
    }
    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
