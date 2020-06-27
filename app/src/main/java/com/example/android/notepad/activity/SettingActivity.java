package com.example.android.notepad.activity;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.example.android.notepad.R;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingActivity extends AppCompatActivity {
    private SharedPreferences settings;
    public static final String THEME_Key = "app_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set theme
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        int theme = sharedPreferences.getInt(MainActivity.THEME_Key, R.style.AppTheme);
        setTheme(theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        List<IDrawerItem<?>> stockyItems = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.edit_note_activity_toolbar);
        setSupportActionBar(toolbar);


        SwitchDrawerItem switchDrawerItem = new SwitchDrawerItem()
                .withName("Dark Theme")
                .withChecked(theme == R.style.AppTheme_Dark)
                .withIcon(R.drawable.ic_menu_delete)
                .withOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(@NotNull IDrawerItem drawerItem, @NotNull CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            settings.edit().putInt(THEME_Key, R.style.AppTheme_Dark).apply();
                        } else {
                            settings.edit().putInt(THEME_Key, R.style.AppTheme).apply();
                        }

                        // recreate app or the activity // if it's not working follow this steps
                        // MainActivity.this.recreate();

                        // this lines means wi want to close the app and open it again to change theme
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            TaskStackBuilder.create(SettingActivity.this)
                                    .addNextIntent(new Intent(SettingActivity.this, SettingActivity.class))
                                    .addNextIntent(getIntent()).startActivities();
                        }
                    }
                });

        SwitchDrawerItem switchDrawerItem2 = new SwitchDrawerItem()
                .withName("Red Theme")
                .withChecked(theme == R.style.AppThemeRed)
                .withIcon(R.drawable.ic_menu_delete)
                .withOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(@NotNull IDrawerItem drawerItem, @NotNull CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            settings.edit().putInt(THEME_Key, R.style.AppThemeRed).apply();
                        } else {
                            settings.edit().putInt(THEME_Key, R.style.AppTheme).apply();
                        }

                        // recreate app or the activity // if it's not working follow this steps
                        // MainActivity.this.recreate();

                        // this lines means wi want to close the app and open it again to change theme
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            TaskStackBuilder.create(SettingActivity.this)
                                    .addNextIntent(new Intent(SettingActivity.this, SettingActivity.class))
                                    .addNextIntent(getIntent()).startActivities();
                        }
                    }
                });
        stockyItems.add(switchDrawerItem);
        stockyItems.add(switchDrawerItem2);
    }

}
