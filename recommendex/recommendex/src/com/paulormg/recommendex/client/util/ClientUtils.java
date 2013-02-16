package com.paulormg.recommendex.client.util;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class ClientUtils {

	public static AsyncCallback<Void> getStatisticsCallback() {
		return new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// best-effort stats logging: do nothing
			}

			@Override
			public void onSuccess(Void result) {
				// best-effort stats logging: do nothing
			}
		};
	}	
	
}
