/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.dexter.listener.single;

import com.karumi.dexter.listener.PermissionDeniedResponse;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * Utility listener that shows a {@link android.app.Dialog} with a minimum configuration when the
 * user rejects some permission
 */
public class DialogOnDeniedPermissionListener extends EmptyPermissionListener {

  protected final Context context;
  protected final String title;
  protected final String message;
  protected final String positiveButtonText;
  protected final Drawable icon;
  protected final DialogFactory dialogFactory;

  protected DialogOnDeniedPermissionListener(Context context, String title, String message,
      String positiveButtonText, Drawable icon, DialogFactory factory) {
    this.context = context;
    this.title = title;
    this.message = message;
    this.positiveButtonText = positiveButtonText;
    this.icon = icon;
    this.dialogFactory = factory;
  }

  @Override public void onPermissionDenied(PermissionDeniedResponse response) {
    super.onPermissionDenied(response);

    showDialog();
  }

  protected void showDialog() {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
              }
            })
            .setIcon(icon);

    if(dialogFactory != null) {
      dialogFactory.showDialog(dialogBuilder);
    } else {
      dialogBuilder.show();
    }
  }

  /**
   * Builder class to configure the displayed dialog.
   * Non set fields will be initialized to an empty string.
   */
  public static class Builder {
    protected final Context context;
    protected String title;
    protected String message;
    protected String buttonText;
    protected Drawable icon;
    protected DialogFactory dialogFactory;

    protected Builder(Context context) {
      this.context = context;
    }

    public static Builder withContext(Context context) {
      return new Builder(context);
    }

    public Builder withTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder withTitle(@StringRes int resId) {
      this.title = context.getString(resId);
      return this;
    }

    public Builder withMessage(String message) {
      this.message = message;
      return this;
    }

    public Builder withMessage(@StringRes int resId) {
      this.message = context.getString(resId);
      return this;
    }

    public Builder withButtonText(String buttonText) {
      this.buttonText = buttonText;
      return this;
    }

    public Builder withButtonText(@StringRes int resId) {
      this.buttonText = context.getString(resId);
      return this;
    }

    public Builder withIcon(Drawable icon) {
      this.icon = icon;
      return this;
    }

    public Builder withIcon(@DrawableRes int resId) {
      this.icon = context.getResources().getDrawable(resId);
      return this;
    }

    public Builder withDialogFactory(DialogFactory factory) {
      this.dialogFactory = factory;
      return this;
    }

    public DialogOnDeniedPermissionListener build() {
      String title = this.title == null ? "" : this.title;
      String message = this.message == null ? "" : this.message;
      String buttonText = this.buttonText == null ? "" : this.buttonText;
      return new DialogOnDeniedPermissionListener(context, title, message, buttonText, icon, dialogFactory);
    }
  }

  public interface DialogFactory {

    void showDialog(AlertDialog.Builder dialogBuilder);
  }
}
