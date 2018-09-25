package com.navigationdrawer.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.navigationdrawer.ApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * {@link Utilities} send an e-mail with some debug information
 * to the developer.
 *
 * @author GladiatoR
 */

public class Utilities {

    private Utilities() {
    }

    private static Utilities utils;

    public static Utilities getInstance() {
        if (utils == null)
            utils = new Utilities();
        return utils;
    }

    /**
     * Method use to hide keyboard.
     *
     * @param ctx context of current activity.
     */
    public void hideKeyboard(Activity ctx) {
        try {
            if (ctx.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ctx.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method use to show keyboard on current screen.
     *
     * @param ctx context of current activity.
     */
    public final void showKeyboard(Activity ctx) {
        try {
            if (ctx.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Show Dialog with Title, Message, Button1, Button2 with Button1 and Button2 Listener
     */
    public AlertDialog showDialog(Context ctx, String title, String msg,
                                  String btn1, String btn2,
                                  OnClickListener listener1,
                                  OnClickListener listener2) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(btn1, listener1);
        if (btn2 != null)
            builder.setNegativeButton(btn2, listener2);

        AlertDialog alert = builder.create();
        return alert;
    }

    /*
     * Show Dialog with Title, Message, Button1, Button2 with Button1 Listener
     * Button2 contains default listener which close dialog box.
     */
    public AlertDialog showDialog(Context ctx, String title, String msg,
                                  String btn1, String btn2, OnClickListener listener) {

        return showDialog(ctx, title, msg, btn1, btn2, listener,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
    }

    /*
     * Show Dialog with Title, Message, with posative button "OK" with its listener
     */
    public AlertDialog showDialog(Context ctx, String title, String msg,
                                  OnClickListener listener) {
        return showDialog(ctx, title, msg, "OK", null, listener, null);
    }

    /*
     * Show Dialog with Title, Message, with posative button "OK" which only Close the dialog box
     */
    public AlertDialog showDialog(Context ctx, String title, String msg) {
        return showDialog(ctx, title, msg,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
    }

    /*
     * Show Failde Dialog with Default Title and message with button "OK" which only Close the dialog box
     */
    public void showFailDialog(final Activity ctx) {
        showDialog(ctx, "Error", "Failed to check the subscription.",
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ctx.finish();
                    }
                }).show();
    }

    /**
     * Method use to check whether user is online or not.
     *
     * @param context context of current activity.
     * @return true if user is online else returns false.
     */
    public final boolean isOnline(Context context) {
        if (context != null) {
            try {
                ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected())
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
//        return true;
    }

    /**
     * Method use to check whether email is valid or not.
     *
     * @param email
     * @return true if email is valid else invalid.
     */
    public boolean isValidEmail(String email) {
        String emailExp = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,10}$";
        Pattern pattern = Pattern.compile(emailExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Method use to check number is numeric or not
     *
     * @param number String number.
     * @return true if number is number else false.
     */
    public boolean isNumeric(String number) {
        String numExp = "^[-+]?[0-9]*\\.?[0-9]+$";
        Pattern pattern = Pattern.compile(numExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    /**
     * Method use to get double in string from number.
     *
     * @param number
     * @return if number is invalid then it returns 0.0.
     */
    public String getDoubleFromString(String number) {
        String numExp = "^[-+]?[0-9]*\\.?[0-9]+$";
        Pattern pattern = Pattern.compile(numExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(number);
        if (matcher.matches())
            return number;
        else
            return "0.0";
    }

    /**
     * Method use to send message using message intent.
     *
     * @param act     Current activity refrence.
     * @param smsBody message body.
     */
    public final void sendMessage(final Activity act, String smsBody) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", smsBody);
        sendIntent.setType("vnd.android-dir/mms-sms");
        act.startActivity(sendIntent);
    }

    /**
     * Method use to send email using mail intent.
     *
     * @param act     current activity refrence.
     * @param emailid to whem you want to send
     * @param body    String body of mail.
     * @param subject String subject of mail.
     */
    public final void sendEmail(final Activity act, String emailid, String body, String subject) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailid});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        act.startActivity(Intent.createChooser(intent, "Send Email"));
    }

    /**
     * Method use to send mail with attachement.
     *
     * @param act      object of current Activity.
     * @param subject  you wants to send in mail
     * @param message  message you wants to send in mail
     * @param filePath file url you wants in attachment.
     */
    public void sendMailWithAttachment(Activity act, String subject, String message, String filePath) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));
        act.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    /**
     * Method use to copy file from source to destination.
     *
     * @param src source file object
     * @param dst destination file object.
     */
    public void copyFile(File src, File dst) {
        try {
            if (!dst.exists())
                dst.createNewFile();
            FileInputStream in = new FileInputStream(src);
            FileOutputStream out = new FileOutputStream(dst);
            int size = (int) src.length();
            byte[] buf = new byte[size];
            in.read(buf);
            out.write(buf);
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method use to send message from application
     *
     * @param address String no on which message needs to send.
     * @param message String text message wants to send.
     */
    public void sendSmsMessage(String address, String message) {
        try {
            SmsManager smsMgr = SmsManager.getDefault();
            smsMgr.sendTextMessage(address, null, message, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method use to call from application
     *
     * @param context     context
     * @param phoneNumber String phoneNumber where to call.
     */
    public void doCall(Context context, String phoneNumber) {
        try {
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method use to open url in external web broswer.
     *
     * @param url     String url needs to open
     * @param context context of activity.
     */
    public void openWebUrl(String url, Context context) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method use to get String date in user readable format
     *
     * @param c context of activity.
     * @return string date in dd-MMM-yyyy format.
     */
    @SuppressLint("SimpleDateFormat")
    public String getUserDateFormat(Calendar c) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = sdf.format(c.getTime());
        return formattedDate;
    }

    /**
     * Method use to get milliseconds from string date.
     *
     * @param date string date in dd-MMM-yyyy format
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public long getDateTimestamp(String date) {
        DateFormat fromFormat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date d = fromFormat.parse(date);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Method use to get ScreenWidth
     *
     * @param activity refrence of current activity.
     * @return width of screen
     */
    public int getScreenWidth(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    /**
     * Method use to get ScreenHeight
     *
     * @param activity refrence of current activity.
     * @return height of screen
     */
    public int getScreenHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    /**
     * Method use to get density pixel from int value.
     *
     * @param value   to which you want it to convert into DP.
     * @param context context of current activity.
     * @return converted value in DP
     */
    public int getValueInDP(int value, Context context) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics());
    }

    /**
     * Method use to get density pixel from int value.
     *
     * @param context       context of current activity.
     * @param selectedImage is Uri of the image, which you want BitMap.
     * @return converted value in Bitmap from URI
     */
    public Bitmap getBitmapFromUri(Context context, Uri selectedImage) {
        InputStream imageStream = null;
        Bitmap photo = null;
        try {
            imageStream = context.getContentResolver().openInputStream(selectedImage);
            photo = BitmapFactory.decodeStream(imageStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photo;
    }

    /**
     * Method use to get IP address of user device.
     *
     * @return
     */
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        @SuppressWarnings("deprecation")
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i("", "***** IP=" + ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return "";
    }

    public String getFacebookUserImage(String facebookUserId) {
//        return "http://www.keenthemes.com/preview/conquer/assets/plugins/jcrop/demos/demo_files/image1.jpg";
        return "http://graph.facebook.com/" + facebookUserId + "/picture?type=" + "large";
    }

    public static Bitmap loadBitmapFromView(LinearLayout layout) {
        try {
            layout.setDrawingCacheEnabled(true);
            layout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            layout.layout(0, 0, layout.getMeasuredWidth(), layout.getMeasuredHeight());
            layout.buildDrawingCache(true);
            Bitmap b = Bitmap.createBitmap(layout.getDrawingCache());
            layout.setDrawingCacheEnabled(false);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }

        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        } else {
            try {
                String filePath = "";
                Cursor cursor;
                int currentapiVersion = Build.VERSION.SDK_INT;
                if (currentapiVersion < 21) {
                    cursor = context.getContentResolver().query(uri, null, null, null, null);
                    if (cursor == null) {
                        filePath = uri.getPath();
                    } else {
                        cursor.moveToFirst();
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        filePath = cursor.getString(index);
                    }
                } else {
                    String wholeID = DocumentsContract.getDocumentId(uri);
                    String[] splited = wholeID.split(":");
                    String id = "";
                    if (splited.length == 2)
                        id = wholeID.split(":")[1];
                    else
                        id = wholeID.split(":")[0];
                    String[] column = {MediaStore.Images.Media.DATA};
                    String sel = MediaStore.Images.Media._ID + "=?";
                    cursor = context.getContentResolver().
                            query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{id}, null);
                    int columnIndex = cursor.getColumnIndex(column[0]);
                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(columnIndex);
                    }
                }
                if (cursor != null && !cursor.isClosed())
                    cursor.close();
                return filePath;
            } catch (Exception e) {
                e.printStackTrace();
                if (uri != null) {
                    return uri.toString();
                }
                return null;
            }
        }
        return null;
    }
//-----------------------commented by Sana-----------------------------------------------
  /*  public void makeSnackToast(Activity context, String message, boolean isSmall) {
        Snackbar snack = Snackbar.make(context.findViewById(android.R.id.content), message, isSmall ? Snackbar.LENGTH_SHORT : Snackbar.LENGTH_LONG);
        View view = snack.getView();
        view.setBackgroundColor(Color.BLACK);
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }*/
//-------------------------------------------

    public boolean saveImageOnExternalData(String filePath, Bitmap bitmap) {
        boolean success = false;
        try {
            File f = new File(filePath);
            if (f.exists())
                f.delete();
            f.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            success = true;
        } catch (FileNotFoundException e) {
            Log.d("ImageCapture", "FileNotFoundException");
            Log.d("ImageCapture", e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.d("ImageCapture", "IOException");
            Log.d("ImageCapture", e.getMessage());
        }
        return success;
    }

    public Bitmap getBitmapFromPath(String filePath) throws FileNotFoundException {
        Bitmap bitmapt = null;
        File f = new File(filePath);
        bitmapt = BitmapFactory.decodeStream(new FileInputStream(f));
        return bitmapt;
    }

    public File getFileFromPath(String filePath) throws FileNotFoundException {
        File f = new File(filePath);
        return f;
    }


    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public String getFormattedDate(Calendar cal) {
//        DAY_OF_MONTH
        return (cal.get(Calendar.DATE) < 10 ? "0" : "") + cal.get(Calendar.DATE) + "/"
                + ((cal.get(Calendar.MONTH) + 1) < 10 ? "0" : "") + (cal.get(Calendar.MONTH) + 1) + "/"
                + cal.get(Calendar.YEAR);
    }

    public String getFormattedTime(Calendar cal) {
//        HOUR_OF_DAY
        return (cal.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + cal.get(Calendar.HOUR_OF_DAY) + ":"
                + (cal.get(Calendar.MINUTE) < 10 ? "0" : "") + cal.get(Calendar.MINUTE);
    }

    public boolean isAppOnForeground(Context context) {
//        Context.ACTIVITY_SERVICE
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        } else {
            String packageName = context.getPackageName();
            Iterator var4 = appProcesses.iterator();
            ActivityManager.RunningAppProcessInfo appProcess;
            do {
                if (!var4.hasNext()) {
                    return false;
                }
                appProcess = (ActivityManager.RunningAppProcessInfo) var4.next();
            } while (appProcess.importance != 100 || !appProcess.processName.equals(packageName));
            return true;
        }
    }

    public boolean isApplicationSentToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static int getBuildVersionCode(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;

            System.out.println("#################### : version " + version);
            System.out.println("#################### : verCode " + verCode);
            return verCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static String getDeviceUDID() {
        return Settings.Secure.getString(ApplicationContext.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    Context activity;
//---------------------commented by sana----------------------------------------------------
   /* public void logoutUser() {

        Intent loginIntent = new Intent(activity, ActivityLogin.class);
        PrefSetup.getInstance().clearPrefSetup();
        DBHelper helper = new DBHelper(activity);
        helper.clearDataBaseTables();
//        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(loginIntent);
    }*/
//------------------------------------------------------------------------------------

}
