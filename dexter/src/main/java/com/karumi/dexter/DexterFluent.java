/*
 * Copyright (C) 2016 Karumi.
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

package com.karumi.dexter;

import com.karumi.dexter.listener.EmptyPermissionRequestErrorListener;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.EmptyMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class DexterFluent {

  static SinglePermissionBuilder withPermission(String permission) {
    return new SinglePermissionBuilder(permission);
  }

  static MultiplePermissionsBuilder withPermissions(String... permissions) {
    return new MultiplePermissionsBuilder(permissions);
  }

  public static abstract class BaseBuilder {
    protected Collection<String> permissions;
    protected MultiplePermissionsListener listener = new EmptyMultiplePermissionsListener();
    protected PermissionRequestErrorListener errorListener =
        new EmptyPermissionRequestErrorListener();
    protected boolean shouldExecuteOnSameThread = false;

    public BaseBuilder onSameThread() {
      shouldExecuteOnSameThread = true;
      return this;
    }

    public void check() {
      try {
        if (shouldExecuteOnSameThread) {
          Dexter.checkPermissionsOnSameThread(listener,
              permissions.toArray(new String[permissions.size()]));
        } else {
          Dexter.checkPermissions(listener, permissions);
        }
      } catch (IllegalStateException e) {
        errorListener.onPermissionsAlreadyBeingRequested();
      }
    }
  }

  public static class MultiplePermissionsBuilder extends BaseBuilder {
    private MultiplePermissionsBuilder(String... permissions) {
      this.permissions = Arrays.asList(permissions);
    }

    public BaseBuilder withListener(MultiplePermissionsListener listener) {
      this.listener = listener;
      return this;
    }
  }

  public static class SinglePermissionBuilder extends BaseBuilder {
    private SinglePermissionBuilder(String permission) {
      this.permissions = Collections.singletonList(permission);
    }

    public BaseBuilder withListener(PermissionListener listener) {
      this.listener = new MultiplePermissionsListenerToPermissionListenerAdapter(listener);
      return this;
    }
  }
}
