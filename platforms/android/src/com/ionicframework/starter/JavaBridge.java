package com.ionicframework.starter;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.chukylabs.bible.BookNameLoader;
import com.chukylabs.bible.BookReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JavaBridge extends CordovaPlugin {
	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		// TODO Auto-generated method stub
		switch (action) {
		case "getBooks":
			return getBooks(callbackContext);
		case "getBook":
			return getBook(args.getString(0), args.getString(1), callbackContext);
		default:
			callbackContext.success("No handler found");
			return true;
		}
	}

	private boolean getBooks(final CallbackContext callback) {
		cordova.getThreadPool().execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				GsonBuilder b = new GsonBuilder();
				Gson json = b.create();
				callback.success(json.toJson(BookNameLoader.getInstance()
						.getBookCollection()));
			}
		});
		return true;
	}
	
	private boolean getBook(final String name, final String version, final CallbackContext callback) {
		cordova.getThreadPool().execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub				
				try {
					callback.success(BookReader.getBook(cordova.getActivity(), "bible/" + version + "json", name));
				} catch (IOException | InterruptedException
						| ExecutionException | TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		return true;
	}
}
