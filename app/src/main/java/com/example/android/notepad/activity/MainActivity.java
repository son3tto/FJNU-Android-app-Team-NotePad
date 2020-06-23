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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.notepad.R;
import com.example.android.notepad.adapters.NotesAdapter;
import com.example.android.notepad.database.NotesDB;
import com.example.android.notepad.database.NotesDao;
import com.example.android.notepad.entity.Notes;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {
    SearchView mSearchView;
    private RecyclerView recyclerView;
    private ArrayList<Notes> notes;
    private NotesAdapter adapter;
    private NotesDao dao;

    private FloatingActionButton fab;
    private SharedPreferences settings;
    public static final String THEME_Key = "app_theme";
    public static final String APP_PREFERENCES = "notepad_settings";
    private int theme;
    private int theme1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        theme1 = settings.getInt(THEME_Key, R.style.AppTheme);
        setTheme(theme1);
        theme = getSharedPreferences("theme", MODE_PRIVATE).getInt(THEME_Key,R.style.AppTheme);
        //if(theme1!=theme) {
            if (theme == R.style.AppTheme) {
                setTheme(R.style.AppTheme);
            } else {
                setTheme(R.style.AppThemeRed);
            }
        //}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupNavigation(savedInstanceState, toolbar);
        // init recyclerView
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // init fab Button
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onAddNewNote();
                onAddNewTitle();
            }

        });

        //btn = findViewById(R.id.change);
        //通过点击，切换主题。点击后要重起应用能看到效果
        //btn.setOnClickListener( new View.OnClickListener(){
            //@Override
            //public void onClick(View view) {
                //if( R.style.AppTheme != theme ){
                    //将主题保存到sharedPreference中，以便下次启动设置主题时读取
                    //getSharedPreferences("cons",Context.MODE_PRIVATE).edit().putInt(THEME_Key, R.style.AppThemeOrange2).commit();
                //} else {
                    //getSharedPreferences("cons",Context.MODE_PRIVATE).edit().putInt(THEME_Key, R.style.AppThemePurple).commit();
                //}
                //退出应用
                //android.os.Process.killProcess(android.os.Process.myPid());
            //}
        //});
        dao = NotesDB.getInstance(this).notesDao();
    }
    public void changeTheme(View view) {
        theme = theme == R.style.AppTheme ? 1 : R.style.AppTheme;
        getSharedPreferences("theme", MODE_PRIVATE).edit().putInt(THEME_Key, theme).commit();
        recreate();
        //会丢失当前页面的状态，需要保持的数据做持久化保持
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
                setNegativeButton(builder);
        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                TextView textView = login.findViewById(R.id.dialog_title);
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

    private void setupNavigation(Bundle savedInstanceState, Toolbar toolbar) {

        List<IDrawerItem<?>> iDrawerItems = new ArrayList<>();
        iDrawerItems.add(new PrimaryDrawerItem().withName("Home").withIcon(R.drawable.ic_menu_revert));
        iDrawerItems.add(new PrimaryDrawerItem().withName("Notes").withIcon(R.drawable.ic_menu_edit));

        List<IDrawerItem<?>> stockyItems = new ArrayList<>();

        SwitchDrawerItem switchDrawerItem = new SwitchDrawerItem()
                .withName("Dark Theme")
                .withChecked(theme1 == R.style.AppTheme_Dark)
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
                            TaskStackBuilder.create(MainActivity.this)
                                    .addNextIntent(new Intent(MainActivity.this, MainActivity.class))
                                    .addNextIntent(getIntent()).startActivities();
                        }
                    }
                });

        PrimaryDrawerItem primaryDrawerItem =new PrimaryDrawerItem()
                .withName("Setting")
                .withIcon(R.drawable.ic_settings_black_24dp);
        stockyItems.add(primaryDrawerItem);
        stockyItems.add(switchDrawerItem);

        // navigation menu header
        AccountHeader header = new AccountHeaderBuilder().withActivity(this)
                .addProfiles(new ProfileDrawerItem()
                        .withName("就这就这就这就这？")
                        .withIcon(R.drawable.app_notes))
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(R.color.gray)
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

    private void loadNotes() {
        this.notes = new ArrayList<>();
        List<Notes> list = dao.getNotes();// get All notes from DataBase
        this.notes.addAll(list);
        this.adapter = new NotesAdapter(this, this.notes);
        // set listener to adapter
//        this.adapter.setListener(this);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

//    @Override
//    public void onNoteClick(Note note) {
//        // TODO: 22/07/2018  note clicked : edit note
//        Intent edit = new Intent(this, EditNoteActivity.class);
//        edit.putExtra(NOTE_EXTRA_Key, note.getId());
//        startActivity(edit);
//
//    }
//
//    @Override
//    public void onNoteLongClick(Note note) {
//        // TODO: 22/07/2018 note long clicked : delete , share ..
//        note.setChecked(true);
//        chackedCount = 1;
//        adapter.setMultiCheckMode(true);
//
//        // set new listener to adapter intend off MainActivity listener that we have implement
//        adapter.setListener(new NoteEventListener() {
//            @Override
//            public void onNoteClick(Note note) {
//                note.setChecked(!note.isChecked()); // inverse selected
//                if (note.isChecked())
//                    chackedCount++;
//                else chackedCount--;
//
//                if (chackedCount > 1) {
//                    actionModeCallback.changeShareItemVisible(false);
//                } else actionModeCallback.changeShareItemVisible(true);
//
//                if (chackedCount == 0) {
//                    //  finish multi select mode wen checked count =0
//                    actionModeCallback.getAction().finish();
//                }
//
//                actionModeCallback.setCount(chackedCount + "/" + notes.size());
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onNoteLongClick(Note note) {
//
//            }
//        });
//
//        actionModeCallback = new MainActionModeCallback() {
//            @Override
//            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
//                if (menuItem.getItemId() == R.id.action_delete_notes)
//                    onDeleteMultiNotes();
//                else if (menuItem.getItemId() == R.id.action_share_note)
//                    onShareNote();
//
//                actionMode.finish();
//                return false;
//            }
//
//        };
//
//        // start action mode
//        startActionMode(actionModeCallback);
//        // hide fab button
//        fab.setVisibility(View.GONE);
//        actionModeCallback.setCount(chackedCount + "/" + notes.size());
//    }
//
//    private void onShareNote() {
//        // TODO: 22/07/2018  we need share just one Note not multi
//
//        Note note = adapter.getCheckedNotes().get(0);
//        // TODO: 22/07/2018 do your logic here to share note ; on social or something else
//        Intent share = new Intent(Intent.ACTION_SEND);
//        share.setType("text/plain");
//        String notetext = note.getNoteText() + "\n\n Create on : " +
//                NoteUtils.dateFromLong(note.getNoteDate()) + "\n  By :" +
//                getString(R.string.app_name);
//        share.putExtra(Intent.EXTRA_TEXT, notetext);
//        startActivity(share);
//
//
//    }
//
//    private void onDeleteMultiNotes() {
//        // TODO: 22/07/2018 delete multi notes
//
//        List<Notes> chackedNotes = adapter.getCheckedNotes();
//        if (chackedNotes.size() != 0) {
//            for (Notes note : chackedNotes) {
//                dao.deleteNote(note);
//            }
//            // refresh Notes
//            loadNotes();
//            Toast.makeText(this, chackedNotes.size() + " Note(s) Delete successfully !", Toast.LENGTH_SHORT).show();
//        } else Toast.makeText(this, "No Note(s) selected", Toast.LENGTH_SHORT).show();
//
//        //adapter.setMultiCheckMode(false);
//    }
//
//    @Override
//    public void onActionModeFinished(ActionMode mode) {
//        super.onActionModeFinished(mode);
//
//        adapter.setMultiCheckMode(false); // uncheck the notes
//        adapter.setListener(this); // set back the old listener
//        fab.setVisibility(View.VISIBLE);
//    }
//
//    // swipe to right or to left te delete
//    private ItemTouchHelper swipeToDeleteHelper = new ItemTouchHelper(
//            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//                @Override
//                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                    return false;
//                }
//
//                @Override
//                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                    // TODO: 28/09/2018 delete note when swipe
//
//                    if (notes != null) {
//                        // get swiped note
//                        Note swipedNote = notes.get(viewHolder.getAdapterPosition());
//                        if (swipedNote != null) {
//                            swipeToDelete(swipedNote, viewHolder);
//
//                        }
//
//                    }
//                }
//            });
//
//    private void swipeToDelete(final Note swipedNote, final RecyclerView.ViewHolder viewHolder) {
//        new AlertDialog.Builder(MainActivity.this)
//                .setMessage("Delete Note?")
//                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // TODO: 28/09/2018 delete note
//                        dao.deleteNote(swipedNote);
//                        notes.remove(swipedNote);
//                        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//                        showEmptyView();
//
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // TODO: 28/09/2018  Undo swipe and restore swipedNote
//                        recyclerView.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());
//
//
//                    }
//                })
//                .setCancelable(false)
//                .create().show();
//
//    }

    @Override
    public boolean onItemClick(View view, int position, @NotNull IDrawerItem drawerItem) {

        Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
        return false;
    }
}



