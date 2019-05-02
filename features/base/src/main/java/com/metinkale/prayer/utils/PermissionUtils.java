/*
 * Copyright (c) 2013-2019 Metin Kale
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.metinkale.prayer.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.crashlytics.android.Crashlytics;
import com.metinkale.prayer.Preferences;
import com.metinkale.prayer.base.R;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by metin on 14.12.2015.
 */
public class PermissionUtils {

    private static PermissionUtils mInstance;
    public boolean pCalendar;
    public boolean pCamera;
    public boolean pStorage;
    public boolean pLocation;

    private PermissionUtils(@NonNull Context c) {
        checkPermissions(c);
    }

    public static PermissionUtils get(@NonNull Context c) {
        if (mInstance == null) {
            mInstance = new PermissionUtils(c);
        }
        return mInstance;
    }

    private void checkPermissions(@NonNull Context c) {
        pCalendar = ContextCompat.checkSelfPermission(c, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(c, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED;
        pCamera = ContextCompat.checkSelfPermission(c, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        pStorage = ContextCompat.checkSelfPermission(c, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        pLocation = ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        Crashlytics.setBool("pCalendar", pCalendar);
        Crashlytics.setBool("pCamera", pCamera);
        Crashlytics.setBool("pStorage", pStorage);
        Crashlytics.setBool("pLocation", pLocation);

    }


    public void needCamera(@NonNull final Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && act.isDestroyed())
            return;

        if (!pCamera) {
            AlertDialog.Builder builder = new AlertDialog.Builder(act);

            builder.setTitle(R.string.permissionCameraTitle).setMessage(R.string.permissionCameraText)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CAMERA}, 0));

            builder.show();

        }
    }


    public void needLocation(@NonNull final Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && act.isDestroyed())
            return;

        if (!pLocation) {

            AlertDialog.Builder builder = new AlertDialog.Builder(act);

            builder.setTitle(R.string.permissionLocationTitle).setMessage(R.string.permissionLocationText)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0));


            builder.show();

        }
    }


    public void needCalendar(@NonNull final Activity act, boolean force) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && act.isDestroyed())
            return;

        if (!pCalendar && (!"-1".equals(Preferences.CALENDAR_INTEGRATION.get()) || force)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(act);

            builder.setTitle(R.string.permissionCalendarTitle).setMessage(R.string.permissionCalendarText)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, 0));


            builder.show();
        }

    }


    public void needStorage(@NonNull final Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && act.isDestroyed())
            return;

        if (!pStorage) {

            AlertDialog.Builder builder = new AlertDialog.Builder(act);

            builder.setTitle(R.string.permissionStorageTitle).setMessage(R.string.permissionStorageText)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0));


            builder.show();

        }
    }


    public void onRequestPermissionResult(@NonNull String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            switch (permissions[i]) {
                case Manifest.permission.CAMERA:
                    pCamera = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                    break;
                case Manifest.permission.WRITE_CALENDAR:
                    pCalendar = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                    if (!pCalendar)
                        Preferences.CALENDAR_INTEGRATION.set("-1");
                    break;
                case Manifest.permission.ACCESS_FINE_LOCATION:
                    pLocation = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                    break;
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    pStorage = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                    break;
            }
        }
    }

}
