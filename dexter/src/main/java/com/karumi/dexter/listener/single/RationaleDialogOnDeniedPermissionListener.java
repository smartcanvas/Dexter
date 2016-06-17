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

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class RationaleDialogOnDeniedPermissionListener extends DialogOnDeniedPermissionListener {

  protected final Runnable runOnGranted;

  protected RationaleDialogOnDeniedPermissionListener(Context context, String title, String message,
      String positiveButtonText, Drawable icon, DialogFactory factory, Runnable runOnGranted) {
    super(context, title, message, positiveButtonText, icon, factory);

    this.runOnGranted = runOnGranted;
  }

  @Override public void onPermissionGranted(PermissionGrantedResponse response) {
    super.onPermissionGranted(response);

    if (runOnGranted != null) {
      runOnGranted.run();
    }
  }

  @Override public void onPermissionDenied(PermissionDeniedResponse response) {
    super.onPermissionDenied(response);

    showDialog();
  }

  @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
    super.onPermissionRationaleShouldBeShown(permission, token);

    token.continuePermissionRequest();
  }

  public static class Builder extends DialogOnDeniedPermissionListener.Builder {

    protected Runnable runOnGranted;

    protected Builder(Context context) {
      super(context);
    }

    public static Builder withContext(Context context) {
      return new Builder(context);
    }

    public Builder withRunnableOnGranted(Runnable runOnGranted) {
      this.runOnGranted = runOnGranted;
      return this;
    }

    public RationaleDialogOnDeniedPermissionListener build() {
      String title = this.title == null ? "" : this.title;
      String message = this.message == null ? "" : this.message;
      String buttonText = this.buttonText == null ? "" : this.buttonText;
      return new RationaleDialogOnDeniedPermissionListener(context, title, message, buttonText, icon, dialogFactory, runOnGranted);
    }
  }
}
