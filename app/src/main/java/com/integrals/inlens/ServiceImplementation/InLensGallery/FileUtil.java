
package com.integrals.inlens.ServiceImplementation.InLensGallery;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;


class FileUtil {

	private static int count;
    private static ContentCheck contentCheck;
    private static List<String> fileList;
    private static  Cursor cursor;
    private static List<String> arrayPath;
    private static String requiredDate;
    private static String albumCreated;
    private static String time;
    private static List<String> timeList;
    private static String replacetime1,replacetime2;
    private static String TAG="InLens";
    private static int x=0;
    private static String albumExpiry;


    public static List<String> findMediaFiles(Context context,
											  String albumCreatedD,
											  String albumExpiryD) {

		 fileList = new ArrayList<>();
		 timeList=new ArrayList<>();
		 arrayPath=new ArrayList<>();

		 final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID,MediaStore.Images.Media.DATE_ADDED };
		 final String orderBy = MediaStore.Images.Media._ID;

		 cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, orderBy);

		 contentCheck=new ContentCheck("",context);
		 albumCreated=albumCreatedD;
		 albumExpiry=albumExpiryD;

		 if (cursor != null) {

		 	 count = cursor.getCount();



			for (int i = (count-1); i >0; i--) {
				cursor.moveToPosition(i);

				int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

				arrayPath.add(cursor.getString(dataColumnIndex));

				if(contentCheck.getImageAddedDate(arrayPath.get(arrayPath.size()-1))==null){
				 	continue;
              	}else{
				}
                time=contentCheck.getImageAddedDate(arrayPath.get(arrayPath.size()-1));
				requiredDate=contentCheck.getImageAddedDate(arrayPath.get(arrayPath.size()-1)).substring(8,10)+"-"
						+contentCheck.getImageAddedDate(arrayPath.get(arrayPath.size()-1)).substring(5,7)+"-"+
						contentCheck.getImageAddedDate(arrayPath.get(arrayPath.size()-1)).substring(0,4)  ;



                if(contentCheck.isImageDateAfterAlbumCreatedDate(requiredDate,albumCreated,albumExpiry)==true){
                	 replacetime1=time.replaceAll(":","-");
					 replacetime2=replacetime1.replaceAll(" ","T");
					 fileList.add(arrayPath.get(arrayPath.size()-1));
					 timeList.add(replacetime2);
 					Log.d("InLens::Date:", "time::" + replacetime2);

				}
                else
                	{
                		break;
                  }


                }
			cursor.close();



		}else{
		 }
		return fileList;
	}





	public static List<String> getTimeList() {
		return timeList;
	}
	private static void showLog(String data){
    	Log.d("InLens:",data);
	}

}