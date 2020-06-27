package com.example.android.notepad.activity;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.notepad.R;
import com.example.android.notepad.adapters.NotesAdapter;
import com.example.android.notepad.database.NotesDB;
import com.example.android.notepad.database.NotesDao;
import com.example.android.notepad.entity.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {
    SearchView mSearchView;
    private RecyclerView recyclerView;
    private ArrayList<Note> notes;
    private NotesAdapter adapter;
    private NotesDao dao;
    private FloatingActionButton fab;
    private SharedPreferences settings;
    private View color_block;
    public static final String THEME_Key = "app_theme";
    public static final String APP_PREFERENCES = "notepad_settings";
    private int theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        theme = settings.getInt(THEME_Key, R.style.AppTheme);
        setTheme(theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupNavigation(savedInstanceState, toolbar);
        // swipe to right or to left te delete
        ItemTouchHelper swipeToDeleteHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                        if (notes != null) {
                            // get swiped note
                            Note swipedNote = notes.get(viewHolder.getAdapterPosition());
                            if (swipedNote != null) {
                                swipeToDelete(swipedNote, viewHolder);

                            }

                        }
                    }
                });

        // init recyclerView
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeToDeleteHelper.attachToRecyclerView(recyclerView);


        // init fab Button
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onAddNewNote();
                onAddNewTitle();
            }

        });

        dao = NotesDB.getInstance(this).notesDao();
    }


    private void swipeToDelete(final Note swipedNote, final RecyclerView.ViewHolder viewHolder) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("确认要删除吗？")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.deleteNote(swipedNote);
                        notes.remove(swipedNote);
                        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        showEmptyView();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Objects.requireNonNull(recyclerView.getAdapter()).notifyItemChanged(viewHolder.getAdapterPosition());


                    }
                })
                .setCancelable(false)
                .create().show();
    }

    /**
     * title
     */
    private void onAddNewTitle() {
        //打开login布局
        LayoutInflater layoutInflater = getLayoutInflater();
        final View login = layoutInflater.inflate(R.layout.title_editor, null);
        //设置dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(login)
                .setTitle("Title");
        //重写一个方法
//        builder.setNegativeButton();
        //直接在函数里面set 不重写 顺便点击确认的时候可以跳转到下一个界面
        //TODO
        setNegativeButton(builder);
        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                TextView textView = login.findViewById(R.id.dialog_title);
                RadioGroup rg = login.findViewById(R.id.login_radiogroup);
                int btn_id = rg.getCheckedRadioButtonId();
                for (int i = 0; i < rg.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) rg.getChildAt(i);
                    if (rb.isChecked()) {
                        intent.putExtra("type", i);
                        Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_LONG).show();
                    }
                }

//                Toast.makeText(getApplicationContext(),String.valueOf(btn_id), Toast.LENGTH_LONG).show();
                intent.putExtra("title", textView.getText().toString());
                onAddNewNote(intent);
            }
        })
                .create()
                .show();


    }

    //重写的setNegativeButton方法
    private void setNegativeButton(AlertDialog.Builder builder) {
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "取消编辑", Toast.LENGTH_SHORT).show();
//                Snackbar.make(, "我是普通的Snackbar", Snackbar.LENGTH_LONG).show();


            }
        });
    }

    //    AppBarLayout
    private void setupNavigation(Bundle savedInstanceState, Toolbar toolbar) {

        List<IDrawerItem<?>> iDrawerItems = new ArrayList<>();
        iDrawerItems.add(new PrimaryDrawerItem()
                .withName("所有笔记")
                .withIcon(R.drawable.ic_menu_compose)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(@Nullable View view, int i, @NotNull IDrawerItem<?> iDrawerItem) {
                        loadNotes();
                        return true;
                    }
                }));
        iDrawerItems.add(new PrimaryDrawerItem().withName("ToDo").withIcon(R.drawable.ic_menu_edit)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(@Nullable View view, int i, @NotNull IDrawerItem<?> iDrawerItem) {
                        loadNotesByType(0);
                        return true;
                    }
                }));
        iDrawerItems.add(new PrimaryDrawerItem().withName("日记").withIcon(R.drawable.ic_menu_edit)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(@Nullable View view, int i, @NotNull IDrawerItem<?> iDrawerItem) {
                        loadNotesByType(1);
                        return true;
                    }
                }));
        iDrawerItems.add(new PrimaryDrawerItem().withName("草稿").withIcon(R.drawable.ic_menu_edit)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(@Nullable View view, int i, @NotNull IDrawerItem<?> iDrawerItem) {
                        loadNotesByType(2);
                        return true;
                    }
                }));

        List<IDrawerItem<?>> stockyItems = new ArrayList<>();

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
                        // MainActivity.this.recreate();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            TaskStackBuilder.create(MainActivity.this)
                                    .addNextIntent(new Intent(MainActivity.this, MainActivity.class))
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

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            TaskStackBuilder.create(MainActivity.this)
                                    .addNextIntent(new Intent(MainActivity.this, MainActivity.class))
                                    .addNextIntent(getIntent()).startActivities();
                        }
                    }
                });
        stockyItems.add(new PrimaryDrawerItem()
                .withName("设置")
                .withIcon(R.drawable.ic_settings_black_24dp)
//                .setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(@Nullable View view, int i, @NotNull IDrawerItem<?> iDrawerItem) {
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                        return true;
                    }
                }));
        stockyItems.add(new ProfileDrawerItem()
                .withName("点击分享给你的朋友！")
//                .withIcon(R.drawable.ic_share_black_24dp)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(@Nullable View view, int i, @NotNull IDrawerItem<?> iDrawerItem) {
                        allShare();
                        return true;
                    }
                }));
        stockyItems.add(switchDrawerItem);
        stockyItems.add(switchDrawerItem2);
        // navigation menu header
        AccountHeader header = new AccountHeaderBuilder().withActivity(this)
                .addProfiles(new ProfileDrawerItem()
                        .withName("FJNU-NotePad")
                        .withIcon(R.mipmap.ic_launcher))
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(R.color.pink_dark)
                .withSelectionListEnabledForSingleProfile(false) // we need just one profile
                .build();

        // Navigation drawer
        new DrawerBuilder()
                .withActivity(this) // activity main
                .withToolbar(toolbar) // toolbar
                .withSavedInstance(savedInstanceState) // saveInstance of activity
                .withDrawerItems(iDrawerItems) // menu items
                .withTranslucentNavigationBar(true)
                .withStickyDrawerItems(stockyItems) // footer items
                .withAccountHeader(header) // header of navigation
                .withOnDrawerItemClickListener(this) // listener for menu items click
                .build();

    }

    private void loadNotesByType(int noteType) {
        this.notes = new ArrayList<>();
        List<Note> list = dao.getNotesByType(noteType);// get All notes from DataBase
        this.notes.addAll(list);
        this.adapter = new NotesAdapter(this, this.notes);
        // set listener to adapter
//        this.adapter.setListener(this);
        this.recyclerView.setAdapter(adapter);
        showEmptyView();

    }

    private void loadNotes() {
        this.notes = new ArrayList<>();
        List<Note> list = dao.getNotes();
        this.notes.addAll(list);
        this.adapter = new NotesAdapter(this, this.notes);

        this.recyclerView.setAdapter(adapter);
        showEmptyView();

    }

    private void searchNotes(String text) {
        this.notes = new ArrayList<>();
        List<Note> list = dao.searchNotes(text);// get All notes from DataBase
        this.notes.addAll(list);
        this.adapter = new NotesAdapter(this, this.notes);
        // set listener to adapter
//        this.adapter.setListener(this);f
        this.recyclerView.setAdapter(adapter);
        showEmptyView();
        // add swipe helper to recyclerView


//        swipeToDeleteHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * when no notes show msg in main_layout
     */
    private void showEmptyView() {
        if (notes.size() == 0) {
            this.recyclerView.setVisibility(View.GONE);
            findViewById(R.id.empty_notes_view).setVisibility(View.VISIBLE);

        } else {
            this.recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.empty_notes_view).setVisibility(View.GONE);
        }

    }

    /**
     * Start EditNoteActivity.class for Create New Note
     */
    private void onAddNewNote(Intent intent) {
//        (new Intent())
        startActivity(intent.setClass(this, EditNoteActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingActivity.class));
            return true;
        }

        else if (id == R.id.menu_search) {
            mSearchView = (SearchView) item.getActionView();
            CharSequence query = mSearchView.getQuery();
//            searchNotes((String) query);
//            Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchNotes(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchNotes(newText);
                    return true;
                }
            });
            return true;
        }
        else if (id == R.id.menu_paste) {
            //TODO
            return true;
        }
        else if(id == R.id.menu_pdf){
            final EditText[] textView = new EditText[1];
            LayoutInflater layoutInflater = getLayoutInflater();
            final View login = layoutInflater.inflate(R.layout.url_editor, null);
            //设置dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setView(login)
                    .setTitle("输入URL");
            builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    textView[0] = login.findViewById(R.id.pdf_url_text);
                    intent.putExtra("com.example.android.notepad.url", textView[0].getText().toString());
//                    Toast.makeText(getApplicationContext(), intent.getStringExtra("com.example.android.notepad.url"), Toast.LENGTH_LONG).show();
                    startActivity(intent.setClass(MainActivity.this, PDFActivity.class));

//                setDownloadListener(mUrl);
                }
            })
                    .create()
                    .show();
            return true;
//            Intent intent = new Intent();
//            intent.putExtra("url", textView[0].getText().toString());

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }



    @Override
    public boolean onItemClick(View view, int position, @NotNull IDrawerItem drawerItem) {

        Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * Android原生分享功能
     * 默认选取手机所有可以分享的APP
     */
    public void allShare() {
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("text/plain");//设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_SUBJECT, "share");//添加分享内容标题
        share_intent.putExtra(Intent.EXTRA_TEXT, "share with you:" + "android");//添加分享内容
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, "share");
        startActivity(share_intent);
    }



}



