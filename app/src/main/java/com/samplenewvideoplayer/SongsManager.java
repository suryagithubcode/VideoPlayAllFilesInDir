package com.samplenewvideoplayer;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Environment;
import android.util.Log;




public class SongsManager {
	// SDCard Path
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	MainActivity main = new MainActivity();

	String storagePathPenDrive;


	// Constructor
	public SongsManager() {
	}

	/**
	 * Function to read all mp3 files from sdcard and store the details in
	 * ArrayList
	 * */

	public ArrayList<HashMap<String, String>> getPlayList(Context context) {
		System.out.println("Long ");
		File home = null;
		try {
			 String newhome = Environment.getExternalStorageDirectory()+"/testsrivas";
			home = new File(newhome);


/*

			UsbMassStorageDevice[] devices = UsbMassStorageDevice.getMassStorageDevices(context);

			for(UsbMassStorageDevice device: devices) {

				// before interacting with a device you need to call init()!
				try {
					device.init();
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Only uses the first partition on the device
				FileSystem currentFs = device.getPartitions().get(0).getFileSystem();
				Log.d("mmm", "Capacity: " + currentFs.getCapacity());
				Log.d("mmm", "Occupied Space: " + currentFs.getOccupiedSpace());
				Log.d("mmm", "Free Space: " + currentFs.getFreeSpace());
				Log.d("mmm", "Chunk size: " + currentFs.getChunkSize());

				String dir = device.getUsbDevice().getDeviceName();
				Log.d("mmm dir", "dir: " + dir);

				String currentString =dir;
				String[] separated = currentString.split("/");

				//storagePathPenDrive = "usb://1"+separated[5]+"/PCB/QDSS/";
				//storagePathPenDrive = "1023/PCB/QDSS/";
				Log.d("mmm","Display Storage Path"+storagePathPenDrive);

			}
*/

			//String newhome = storagePathPenDrive;
			//home = new File(newhome);




		//  home = new File("/mnt/usb_storage/USB_DISK0/corpus"+"/test");
		  //  home = new File("/storage/external_storage/usb/usb1_1"+"/Test");
			//home = new File(extDir + "/Movies");
		//home = new File(extDir + "/DCIM/camera");
	//		System.out.println("aaa="+extDir);
		} catch (Exception e) {
			main.writeFile("\n Log file Creation Error : " + e.toString());
		}
		try {
			if (home.listFiles(new FileExtensionFilter()).length > 0) {
				for (File file : home.listFiles(new FileExtensionFilter())) {
					HashMap<String, String> song = new HashMap<String, String>();
					song.put(
							"songTitle",
							file.getName().substring(0,
									(file.getName().length() - 4)));
					song.put("songPath", file.getPath());
					// Adding each song to SongList
					songsList.add(song);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			main.writeFile("\n Get File From Directory " + e.toString());
		}
		// return songs list array
		return songsList;
	}

	/**
	 * Class to filter files which are having .mp3 extension
	 * */	

	
	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			try {
				return (name.endsWith(".mp4") || name.endsWith(".MP4")
						|| name.endsWith(".mkv") || name.endsWith(".MKV")
						|| name.endsWith(".avi") || name.endsWith(".AVI")
						|| name.endsWith(".wmv") || name.endsWith(".WMV"));
			} catch (Exception e) {
				main.writeFile("Get File Format Exception " + e.toString());
				return false;
			}
		}
	}
}
