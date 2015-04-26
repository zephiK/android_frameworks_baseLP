/*
 * Copyright (C) 2015 The CyanogenMod Project
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
 * limitations under the License.
 */

package com.android.systemui.qs.tiles;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.provider.Settings.Global;

import com.android.systemui.qs.GlobalSetting;
import com.android.systemui.qs.QSTile;
import com.android.systemui.R;

import android.widget.Toast;

/** Quick settings tile: Heads up **/
public class HeadsUpTile extends QSTile<QSTile.BooleanState> {

    private static final Intent NOTIFICATION_SETTINGS = new Intent("android.settings.NOTIFICATION_SETTINGS");

    private final GlobalSetting mSetting;

    public HeadsUpTile(Host host) {
        super(host);

        mSetting = new GlobalSetting(mContext, mHandler, Global.HEADS_UP_NOTIFICATIONS_ENABLED) {
            @Override
            protected void handleValueChanged(int value) {
                handleRefreshState(value);
            }
        };
    }

    @Override
    protected BooleanState newTileState() {
        return new BooleanState();
    }

    @Override
    protected void handleClick() {
        toggleState();
        refreshState();
	mHost.collapsePanels(); /* dismissShade */
	toast();
    }

    @Override
    protected void handleLongClick() {
        mHost.startSettingsActivity(NOTIFICATION_SETTINGS);
    }

    protected void toggleState() {
         Settings.Global.putInt(mContext.getContentResolver(),
                        Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED, !headsupEnabled() ? 1 : 0);
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        state.visible = true;
	if (headsupEnabled()) {
        state.icon = ResourceIcon.get(R.drawable.ic_qs_heads_up_on);
        state.label = mContext.getString(R.string.accessibility_quick_settings_heads_up_on);
	} else {
        state.icon = ResourceIcon.get(R.drawable.ic_qs_heads_up_off);
        state.label = mContext.getString(R.string.accessibility_quick_settings_heads_up_off);
	    }
	}

    private boolean headsupEnabled() {
        return Settings.Global.getInt(mContext.getContentResolver(),
                Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED, 1) == 1;
    }

    @Override
    public void setListening(boolean listening) {
        // Do nothing
    }

    protected void toast() {
  	/* show a toast */
        String enabled = mContext.getString(R.string.accessibility_quick_settings_heads_up_on);
        String disabled = mContext.getString(R.string.accessibility_quick_settings_heads_up_off);
        int duration = Toast.LENGTH_SHORT;
        if (headsupEnabled()) {
            Toast toast = Toast.makeText(mContext, enabled, duration);
            toast.show();
        } else {
            Toast toast = Toast.makeText(mContext, disabled, duration);
            toast.show();
        	}
	}
}

