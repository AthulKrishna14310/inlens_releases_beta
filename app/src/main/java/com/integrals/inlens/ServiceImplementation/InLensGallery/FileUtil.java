
package com.integrals.inlens.ServiceImplementation.InLensGallery;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
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
    private static List<String> arrayPath;
    private static String requiredDate;
    private static String albumExpiry;
    private static String time;
    private static List<String> timeList;
    private static String replacetime1,replacetime2;
    private static String TAG="InLens";
    private static int x=0;

    public static List<String> findMediaFiles(Context context,String albumExpiryD) {

		 fileList = new ArrayList<>();
		 timeList=new ArrayList<>();
		 arrayPath=new ArrayList<>();

		 final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID,MediaStore.Images.Media.DATE_ADDED };
		 final String orderBy = MediaStore.Images.Media._ID;

		 cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, orderBy);

		 contentCheck=new ContentCheck("",context);
		 albumExpiry=albumExpiryD;


		 if (cursor != null) {
             showLog("Cursor found");

		 	 count = cursor.getCount();

			 showLog(count+"");


			for (int i = (count-1); i >0; i--) {
				cursor.moveToPosition(i);

				int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

				arrayPath.add(cursor.getString(dataColumnIndex));
				showLog(arrayPath.get(arrayPath.size()-1));

				if(contentCheck.getImageAddedDate(arrayPath.get(arrayPath.size()-1))==null){
				 	continue;
              	}else{
				}
                time=contentCheck.getImageAddedDate(arrayPath.get(arrayPath.size()-1));
				requiredDate=contentCheck.getImageAddedDate(arrayPath.get(arrayPath.size()-1)).substring(8,10)+"-"
						+contentCheck.getImageAddedDate(arrayPath.get(arrayPath.size()-1)).substring(5,7)+"-"+
						contentCheck.getImageAddedDate(arrayPath.get(arrayPath.size()-1)).substring(0,4)  ;



                if(contentCheck.isImageDateAfterAlbumCreatedDate(requiredDate,albumExpiry)==true){
                	 replacetime1=time.replaceAll(":","-");
					 replacetime2=replacetime1.replaceAll(" ","T");
					 fileList.add(arrayPath.get(arrayPath.size()-1));
					 timeList.add(replacetime2);
 					Log.d("InLens::Date:", "time::" + replacetime2);

				}
                else
                	{
                       showLog("Album time expired");
                		break;
                  }


                }
			cursor.close();



		}else{
		 	Log.d("InLens::","Cursor is Null");
		 }
		return fileList;
	}

	private static void showLog(String data) {
     Log.d(TAG,data);
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

}