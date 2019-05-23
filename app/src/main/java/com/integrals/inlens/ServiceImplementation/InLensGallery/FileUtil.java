
package com.integrals.inlens.ServiceImplementation.InLensGallery;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.integrals.inlens.Helper.CurrentDatabase;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

class FileUtil {

	private static int count;
    private static ContentCheck contentCheck;
    private static List<String> fileList;
    private static  Cursor cursor;
    private static String[] arrPath;
    private static String requiredDate;
    private static String albumExpiry;
    private static String time;
    private static List<String> timeList;
    private static String replacetime1,replacetime2;
    public static List<String> findMediaFiles(Context context) {

		 fileList = new ArrayList<>();
		 timeList=new ArrayList<>();
		 final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID,MediaStore.Images.Media.DATE_ADDED };
		 final String orderBy = MediaStore.Images.Media._ID;
		 cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, orderBy);

		 contentCheck=new ContentCheck("",context);

		 CurrentDatabase currentDatabase=new CurrentDatabase(context,"",null,1);
		 albumExpiry=currentDatabase.GetAlbumExpiry();

		 if (cursor != null) {
			 count = cursor.getCount();
			 arrPath = new String[count];

			for (int i = (count-1); i >0; i--) {
				cursor.moveToPosition(i);
				int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
				arrPath[i] = cursor.getString(dataColumnIndex);
				if(contentCheck.getImageAddedDate(arrPath[i])==null){
                	continue;
				}
                time=contentCheck.getImageAddedDate(arrPath[i]);
				requiredDate=contentCheck.getImageAddedDate(arrPath[i]).substring(8,10)+"-"
						+contentCheck.getImageAddedDate(arrPath[i]).substring(5,7)+"-"+
				contentCheck.getImageAddedDate(arrPath[i]).substring(0,4)  ;


                if(contentCheck.isImageDateAfterAlbumExpiryDate(requiredDate,albumExpiry)==true){
                	 replacetime1=time.replaceAll(":","-");
					 replacetime2=replacetime1.replaceAll(" ","T");
					 Log.d("Date:", "time::" + replacetime2);
					 fileList.add(arrPath[i]);
					 timeList.add(replacetime2);

				}else {
				break;
                }


                }
			cursor.close();



		}
		return fileList;
	}

	public static List<String> findImageFileInDirectory(String directory, String[] imageTypes) {
		final List<String> tFileList = new ArrayList<>();
		FilenameFilter[] filter = new FilenameFilter[imageTypes.length];

		int i = 0;
		for (final String type : imageTypes) {
			filter[i] = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith("." + type);
				}
			};
			i++;
		}

		File[] allMatchingFiles = listFilesAsArray(
				new File(directory), filter, -1);
		for (File f : allMatchingFiles) {
			tFileList.add(f.getAbsolutePath());
		}
		return tFileList;
	}

	private static File[] listFilesAsArray(File directory, FilenameFilter[] filter,
			int recurse) {
		Collection<File> files = listFiles(directory, filter, recurse);

		File[] arr = new File[files.size()];
		return files.toArray(arr);
	}

	private static Collection<File> listFiles(File directory, FilenameFilter[] filter, int recurse) {

		Vector<File> files = new Vector<>();

		File[] entries = directory.listFiles();

		if (entries != null) {
			for (File entry : entries) {
				for (FilenameFilter filefilter : filter) {
					if (filefilter.accept(directory, entry.getName())) {
						files.add(entry);
					}
				}
				if ((recurse <= -1) || (recurse > 0 && entry.isDirectory())) {
					recurse--;
					files.addAll(listFiles(entry, filter, recurse));
					recurse++;
				}
			}
		}
		return files;
	}


	public static List<String> getTimeList() {
		return timeList;
	}

	public static void setTimeList(List<String> timeList) {
		FileUtil.timeList = timeList;
	}
}