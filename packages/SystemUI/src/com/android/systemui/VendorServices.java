/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.android.systemui;

import android.app.AlarmManager;
import android.content.Context;

import com.android.systemui.Dumpable;
import com.android.systemui.R;
import com.android.systemui.VendorServices;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.statusbar.phone.CentralSurfaces;

import com.android.systemui.ambient.AmbientIndicationContainer;
import com.android.systemui.ambient.AmbientIndicationService;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Placeholder for any vendor-specific services.
 */
public class VendorServices extends CoreStartable {

    private final CentralSurfaces mCentralSurfaces;
    private final ArrayList<Object> mServices = new ArrayList<>();
    private final AlarmManager mAlarmManager;

    @Inject
    public VendorServices(Context context, AlarmManager alarmManager, CentralSurfaces centralSurfaces) {
        super(context);
        mAlarmManager = alarmManager;
        mCentralSurfaces = centralSurfaces;
    }
    
    @Override
    public void start() {
        AmbientIndicationContainer ambientIndicationContainer = (AmbientIndicationContainer) mCentralSurfaces.getNotificationShadeWindowView().findViewById(R.id.ambient_indication_container);
        ambientIndicationContainer.initializeView(mCentralSurfaces);
        addService(new AmbientIndicationService(mContext, ambientIndicationContainer, mAlarmManager));
    }

    @Override
    public void dump(PrintWriter printWriter, String[] strArr) {
        for (int i = 0; i < mServices.size(); i++) {
            if (mServices.get(i) instanceof Dumpable) {
                ((Dumpable) mServices.get(i)).dump(printWriter, strArr);
            }
        }
    }

    private void addService(Object obj) {
        if (obj != null) {
            mServices.add(obj);
        }
    }
}
