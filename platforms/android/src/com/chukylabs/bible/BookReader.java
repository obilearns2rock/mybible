package com.chukylabs.bible;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.app.Activity;

public class BookReader {
	public static String getBook(final Activity caller, final String loaction,
			final String name) throws IOException, InterruptedException,
			ExecutionException, TimeoutException {
		final FutureTask<String> task = new FutureTask<>(new Callable<String>() {

			@Override
			public String call() throws Exception {
				// TODO Auto-generated method stub
				return getBookTask(caller, loaction, name);
			}
		});
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				task.run();
			}
		}).start();

		return task.get(5, TimeUnit.SECONDS);
	}

	private static String getBookTask(Activity caller, String loaction,
			String name) throws IOException {
		InputStream is = caller.getAssets().open(loaction + "/" + name + ".json");
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream bufferOutput = new ByteArrayOutputStream();
		int bytesRead;
		while ((bytesRead = is.read(buffer)) > 0) {
			bufferOutput.write(buffer, 0, bytesRead);
		}
		return new String(bufferOutput.toByteArray());
	}
}
