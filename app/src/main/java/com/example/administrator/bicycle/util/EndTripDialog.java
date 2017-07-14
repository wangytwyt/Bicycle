package com.example.administrator.bicycle.util;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.bicycle.R;

import static com.example.administrator.bicycle.R.style.CustomDialog;

/**
 * Created by Administrator on 2017/7/14.
 */

public class EndTripDialog extends android.app.Dialog {
    public EndTripDialog(Context context) {
        super(context);
    }
    public EndTripDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String positiveButtonText = "确认，结束行程";
        private String negativeButtonText = "取  消";
        private View contentView;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }



        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }


        public Builder setPositiveButton(DialogInterface.OnClickListener listener) {

            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(DialogInterface.OnClickListener listener) {

            this.negativeButtonClickListener = listener;
            return this;
        }



        public EndTripDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final EndTripDialog dialog = new EndTripDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_endtrip_layout, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            // set the content message

            dialog.setContentView(layout);
            return dialog;
        }
    }

}
