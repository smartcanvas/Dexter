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

package com.karumi.dexter.listener.multi;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.List;

public class RationaleDialogOnAnyDeniedMultiplePermissionsListener extends DialogOnAnyDeniedMultiplePermissionsListener {

  protected final Runnable runOnGranted;

  protected RationaleDialogOnAnyDeniedMultiplePermissionsListener(Context context, String title,
      String message, String positiveButtonText, Drawable icon, DialogFactory factory, Runnable runOnGranted) {
    super(context, title, message, positiveButtonText, icon, factory);

    this.runOnGranted = runOnGranted;
  }

  @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
    super.onPermissionsChecked(report);

    if (report.areAllPermissionsGranted()) {
      if (runOnGranted != null) {
        runOnGranted.run();
      }
    } else {
      showDialog();
    }
  }

  @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
    super.onPermissionRationaleShouldBeShown(permissions, token);

    token.continuePermissionRequest();
  }

  public static class Builder extends DialogOnAnyDeniedMultiplePermissionsListener.Builder {
    protected Runnable runOnGranted;

    protected Builder(Context context) {
      super(context);
    }

    public static Builder withContext(Context context) {
      return new Builder(context);
    }

    public Builder withRunnableOnGranted(Runnable runnableOnGranted) {
      this.runOnGranted = runnableOnGranted;
      return this;
    }


    public RationaleDialogOnAnyDeniedMultiplePermissionsListener build() {
      String title = this.title == null ? "" : this.title;
      String message = this.message == null ? "" : this.message;
      String buttonText = this.buttonText == null ? "" : this.buttonText;

      return new RationaleDialogOnAnyDeniedMultiplePermissionsListener(context, title, message, buttonText, icon, dialogFactory, runOnGranted);
    }
  }
}
