package com.zurich.mobile.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zurich.mobile.controller.AppStatusListenController;
import com.zurich.mobile.controller.FragmentLifecycleController;
import com.zurich.mobile.controller.FragmentUserVisibleController;

public abstract class AppBaseFragment extends Fragment implements FragmentLifecycleController.LifecycleCallback, FragmentUserVisibleController.UserVisibleCallback{

	private FragmentLifecycleController lifecycleController;
	private FragmentUserVisibleController userVisibleController;
	private AppStatusListenController appStatusListenController;

	public AppBaseFragment() {
		userVisibleController = new FragmentUserVisibleController(this, this);
		lifecycleController = new FragmentLifecycleController(this, this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		lifecycleController.create();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		userVisibleController.activityCreated();
	}

	@Override
	public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return lifecycleController.createView(inflater, container);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		lifecycleController.viewCreated(view, savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		userVisibleController.resume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		userVisibleController.pause();
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		userVisibleController.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void onDestroyView() {
		if(lifecycleController != null){
			lifecycleController.destroyView();
		}
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		if(lifecycleController != null){
			lifecycleController.destroy();
		}
		if(appStatusListenController != null){
			appStatusListenController.close(getContext());
		}
		super.onDestroy();
	}

	@Override
	public void setWaitingShowToUser(boolean waitingShowToUser) {
		userVisibleController.setWaitingShowToUser(waitingShowToUser);
	}

	@Override
	public boolean isWaitingShowToUser() {
		return userVisibleController.isWaitingShowToUser();
	}

	@Override
	public void callSuperSetUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public boolean isVisibleToUser(){
		return userVisibleController.isVisibleToUser();
	}

	@Override
	public void onVisibleToUserChanged(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
		if(lifecycleController != null){
			lifecycleController.visibleToUserChanged(isVisibleToUser);
		}
	}

	public View findViewById(int viewId){
		View rootView = getView();
		return rootView != null ? rootView.findViewById(viewId) : null;
	}

	public void showData(){
		if(lifecycleController != null){
			lifecycleController.showData();
		}
	}

	public void loadData() {
		if(lifecycleController != null){
			lifecycleController.loadData();
		}
	}

	public void setLoading(boolean loading) {
		if(lifecycleController != null){
			lifecycleController.setLoading(loading);
		}
	}

	public void setDisableDelay(boolean banDelay) {
		if(lifecycleController != null){
			lifecycleController.setDisableDelay(banDelay);
		}
	}

	public void openAppStatusAndProgressListener(AppStatusListenController.AppStatusAndProgressListener appStatusAndProgressListener){
		if(appStatusListenController == null){
			appStatusListenController = new AppStatusListenController();
		}
		appStatusListenController.setAppStatusAndProgressListener(appStatusAndProgressListener);
		if(!appStatusListenController.isOpened()){
			appStatusListenController.open(getContext());
		}
	}

	public void openAppStatusListener(AppStatusListenController.AppStatusListener appStatusListener){
		if(appStatusListenController == null){
			appStatusListenController = new AppStatusListenController();
		}
		appStatusListenController.setAppStatusListener(appStatusListener);
		if(!appStatusListenController.isOpened()){
			appStatusListenController.open(getContext());
		}
	}

	public Context getContext() {
		return getActivity();
	}

	public FragmentLifecycleController getLifecycleController() {
		return lifecycleController;
	}

}