package com.example.administrator.bicycle.util;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.administrator.bicycle.R;


/**
 * Created by lan on 2016/7/7.
 */
public class Dialog {
    private static Toast toast;
    private static ProgressDialog progressDialog;
    private static AlertDialog alertDialog;
    private static AlertDialog bleDialog;
    private static AlertDialog pushDialog;

    public static void registBroadCast(Context context, BroadcastReceiver b, String ...action) {
        registBroadCast(context, b, 0, action);
    }

    public static void registBroadCast(Context context, BroadcastReceiver b, int priority, String ...action) {
        if (context != null && b != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.setPriority(priority);
            for (String string : action) {
                intentFilter.addAction(string);
            }
            context.registerReceiver(b, intentFilter);
        }
    }

    public static void unRegistBroadCast(Context context, BroadcastReceiver b) {
        if (context != null && b != null) {
            context.unregisterReceiver(b);
        }
    }


    public static void showToast(Context context, String str) {
        showToast(context, str, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, int stringId) {
        showToast(context, context.getString(stringId), Toast.LENGTH_SHORT);
    }

    private static void showToast(Context context, CharSequence str, int duration) {
        if(toast != null) {
            toast.cancel();
            toast = null;
        }


        toast = Toast.makeText(context, str, duration);
        toast.show();
    }

    public static void cancelToast() {
        if(toast != null) {
            // toast.cancel();
            toast = null;
        }
    }

    public static void cancelNotification(Context context, int notificationId) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.cancel(notificationId);
    }

    private static final int NOTIFICATION_ID_ICON = 5;
    public static void showIconStatus(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

/*        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.icon_wifi)
                .setContentTitle("Test")
                .setContentText("test2")
                .setContentIntent(pendingIntent)
                .build();
        // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        // 表明在点击了通知栏中的"清除通知"后，此通知不清除， 经常与FLAG_ONGOING_EVENT一起使用
        notification.flags |= Notification.FLAG_NO_CLEAR;

        mNotificationManager.notify(NOTIFICATION_ID_ICON, notification);*/
    }

    public static void cancelIconStatus(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.cancel(NOTIFICATION_ID_ICON);
    }

    public static void showAlertDialog(Context context, int msgId) {
        if(alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msgId)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showAlertDialog(Context context, String msg) {
        if(alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showAlertDialog(Context context, int msgId,
                                       DialogInterface.OnClickListener confirmListener) {
        if(alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msgId)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, confirmListener)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showAlertDialog(Context context, int msgId,
                                       DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener neutralListener) {
        if(alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msgId)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, positiveListener)
                .setNeutralButton(R.string.never, neutralListener)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showAlertDialog(Context context, int titleId, int msgId,
                                       DialogInterface.OnClickListener confirmListener) {
        if(alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titleId)
                .setMessage(msgId)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, confirmListener)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showAlertDialog(Context context, String title, String msg,
                                       DialogInterface.OnClickListener confirmListener) {
        if(alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, confirmListener)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showBleDialog(Context context, int msgId,
                                     DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener neutralListener, DialogInterface.OnClickListener cancellListener) {
        if(bleDialog != null) {
            bleDialog.dismiss();
            bleDialog = null;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msgId)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, positiveListener)
                .setNeutralButton(R.string.never, neutralListener)
                .setNegativeButton(R.string.cancel, cancellListener);
        bleDialog = builder.create();
        bleDialog.show();
    }

    public static void showPushDialog(Context context, String title, String msg,
                                      DialogInterface.OnClickListener confirmListener,
                                      DialogInterface.OnClickListener cancelListener) {
        if(pushDialog != null) {
            pushDialog.dismiss();
            pushDialog = null;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, confirmListener)
                .setNegativeButton(R.string.cancel, cancelListener);
        pushDialog = builder.create();
        pushDialog.show();
    }










    public static void showListDialog(Context context, String title, String[] array,
                                      DialogInterface.OnClickListener confirmListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setItems(array, confirmListener);
        builder.show();
    }

    public static void cancelAlertDialog() {
        if(alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    public static void cancelBleDialog() {
        if(bleDialog != null) {
            bleDialog.dismiss();
            bleDialog = null;
        }
    }

    public static void cancelPushDialog() {
        if(pushDialog != null) {
            pushDialog.dismiss();
            pushDialog = null;
        }
    }

    public static boolean showProgress(Context context, int strId) {
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }


        String msg = context.getString(strId);
        progressDialog = ProgressDialog.show(context, "", msg, false, false);
        progressDialog.show();

        return true;
    }

    public static void showProgress(Context context, int strId, boolean cancelable) {
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }


        String msg = context.getString(strId);
        progressDialog = ProgressDialog.show(context, "", msg, cancelable, cancelable);
        progressDialog.show();
    }

    public static void showProgress(Context context, String text) {
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }


        progressDialog = ProgressDialog.show(context, "", text, true, true);
        progressDialog.show();
    }

    public static void showProgressValue(Context context, int strId) {
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }


        String msg = context.getString(strId);
        progressDialog = ProgressDialog.show(context, "", msg, true, true);
        progressDialog.show();
    }

    public static void setProgress(int progress) {
        if(progressDialog != null) {
            progressDialog.setMessage(progress + "%");
        }
    }

    public static void dismiss() {
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
