package com.example.android.notepad.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.notepad.R;
import com.example.android.notepad.database.NotesDB;
import com.example.android.notepad.database.NotesDao;
import com.example.android.notepad.entity.Note;
import com.example.android.notepad.utils.DateUtil;
import com.example.android.notepad.utils.LocationService;
import com.example.android.notepad.utils.LocationUtil;
import com.example.android.notepad.utils.PdfUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EditNoteActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String NOTE_EXTRA_Key = "notes_id";
    public static final String NOTE_TITLE_Key = "title";
    public static final String NOTE_TYPE_Key = "type";
    private EditText inputNoteTitle;
    private EditText inputNote;
    private int note_id, note_type;
    private NotesDao dao;
    private Note temp;
    private ImageButton bold, italic, underline, bt_red, bt_blue, bt_green, bt_black, bt_gray, bt_yellow;
    private ImageButton size1, size2, size3, size4;
    private ImageButton bt_photo, bt_music, bt_address, bt_clock, bt_font, bt_background;
    private boolean isShowOrNot = false;
    private boolean isShowOrNot1 = false;
    private boolean isShowOrNot2 = false;
    private boolean isShowOrNot3 = false;
    private boolean isShowOrNot4 = false;
    private LinearLayout ll;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // set theme
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        int theme = sharedPreferences.getInt(MainActivity.THEME_Key, R.style.AppTheme);
        setTheme(theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_note);

        Toolbar toolbar = findViewById(R.id.edit_note_activity_toolbar);
        setSupportActionBar(toolbar);

        TextView inputNoteType = findViewById(R.id.input_note_type);
        inputNoteTitle = findViewById(R.id.input_note_title);
        inputNote = findViewById(R.id.input_note);

        dao = NotesDB.getInstance(this).notesDao();

        note_id = getIntent().getIntExtra(NOTE_EXTRA_Key, 0);

        temp = dao.getNoteById(note_id);
        if (note_id != 0) {
            note_type = dao.getNoteById(note_id).getNoteType();
//            if (Objects.requireNonNull(getIntent().getExtras()).getInt(NOTE_EXTRA_Key, 0) != 0) {
            inputNoteTitle.setText(temp.getNoteTitle());
//            int id = Objects.requireNonNull(getIntent().getExtras()).getInt(NOTE_EXTRA_Key, 0);
            temp = dao.getNoteById(note_id);
            inputNote.setText(temp.getNoteText());
//            Toast.makeText(getApplicationContext(), String.valueOf(inputNoteType), Toast.LENGTH_LONG).show();
            String input_type_and_date = choseType(note_type)+" "+DateUtil.StringToDateExactly(temp.getNoteDate());
            inputNoteType.setText(input_type_and_date);
        } else {
            //新的note
            note_type = getIntent().getIntExtra(NOTE_TYPE_Key, 0);
//            Toast.makeText(getApplicationContext(), String.valueOf(inputNoteType), Toast.LENGTH_LONG).show();
            String input_type_and_date = choseType(note_type)+" "+DateUtil.StringToDateExactly(System.currentTimeMillis());
            inputNoteType.setText(input_type_and_date);
            inputNoteTitle.setText(getIntent().getStringExtra(NOTE_TITLE_Key));
            inputNote.setFocusable(true);
        }
        //吴宇佳添加
        ll = findViewById(R.id.ll);
        ll.setVisibility(View.GONE);
        inputNote = findViewById(R.id.input_note);
        bt_photo = findViewById(R.id.bt_photo);
        bt_font = findViewById(R.id.bt_font);
        bold = findViewById(R.id.bold);
        italic = findViewById(R.id.italic);
        underline = findViewById(R.id.underline);
        bt_black = findViewById(R.id.bt_black);
        bt_blue = findViewById(R.id.bt_blue);
        bt_green = findViewById(R.id.bt_green);
        bt_gray = findViewById(R.id.bt_gray);
        bt_red = findViewById(R.id.bt_red);
        bt_yellow = findViewById(R.id.bt_yellow);
        size1 = findViewById(R.id.size1);
        size2 = findViewById(R.id.size2);
        size3 = findViewById(R.id.size3);
        size4 = findViewById(R.id.size4);
        //    button.setOnClickListener(this);
        bold.setOnClickListener(this);
        italic.setOnClickListener(this);
        underline.setOnClickListener(this);
        bt_yellow.setOnClickListener(this);
        bt_red.setOnClickListener(this);
        bt_gray.setOnClickListener(this);
        bt_green.setOnClickListener(this);
        bt_blue.setOnClickListener(this);
        bt_black.setOnClickListener(this);
        bt_font.setOnClickListener(this);
        bt_photo.setOnClickListener(this);
        size1.setOnClickListener(this);
        size2.setOnClickListener(this);
        size3.setOnClickListener(this);
        size4.setOnClickListener(this);

        bt_address = findViewById(R.id.bt_address);
        bt_address.setOnClickListener(this);
    }

    String choseType(int i) {
        switch (i) {
            case 0:
//                Toast.makeText(getApplicationContext(), "Todo", Toast.LENGTH_LONG).show();
                return "ToDo";
            case 1:
//                Toast.makeText(getApplicationContext(), "日记", Toast.LENGTH_LONG).show();
                return "日记";
            case 2:
                return "草稿";
            default:
                return "";
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edite_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void onShareNote() {
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);
        share_intent.setType("text/plain");
        share_intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        share_intent.putExtra(Intent.EXTRA_TEXT, "推荐您使用一款软件:" + R.string.app_name);
        share_intent = Intent.createChooser(share_intent, "分享");
        startActivity(share_intent);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_export:
                PdfUtil pdfUtil = new PdfUtil();
                RelativeLayout view = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.activity_edite_note, null);
                try {
                    pdfUtil.pdfModel(getApplicationContext(), view);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.save_note:
                onSaveNote();
                return true;
            case R.id.menu_share:
                onShareNote();
                return true;
            case R.id.menu_delete:
                dao.deleteNoteById(note_id);
                //回到主页面
                finish();
        }
//        int id = item.getItemId();
//        if (id == R.id.save_note)
//            onSaveNote();
        return super.onOptionsItemSelected(item);
    }

    //与dao交互的部分
    private void onSaveNote() {
        String title = inputNoteTitle.getText().toString();
        String text = inputNote.getText().toString();
        int type = note_type;
        if (!text.isEmpty()) {
            long date = new Date().getTime(); // get  system time
            // if  exist update els crete new

            if (temp == null) {
                // 以Notes的形式插入dao
                temp = new Note(title, text, date, type);
                dao.insertNote(temp); // create new note and inserted to database
            } else {
                temp.setNoteTitle(title);
                temp.setNoteText(text);
                temp.setNoteDate(date);
                temp.setNoteType(note_type);
                dao.updateNote(temp); // change text and date and update note on database
            }

            finish(); // return to the MainActivity
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_photo:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                //设置action
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //选中相片后返回本Activity
                startActivityForResult(intent, 2);
                break;
            case R.id.bt_address:
//                Toast.makeText(getApplicationContext(), "bt+address", Toast.LENGTH_LONG).show();
                LocationUtil locationUtil = new LocationUtil();
                String result_location = null;
                try {
                    result_location = locationUtil.getLastKnownLocation(getApplicationContext());
                    inputNote.append(result_location);
                    inputNote.append("  ");

//                    dao.updateNote(temp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                    String result_location = locationService.
                Toast.makeText(getApplicationContext(), result_location, Toast.LENGTH_LONG).show();

                break;
            case  R.id.bt_font:
                if (!isShowOrNot) {
                    ll.setVisibility(View.VISIBLE);// 设置显示

                    isShowOrNot = true;
                } else {
                    ll.setVisibility(View.GONE); // 设置隐藏
                    isShowOrNot = false;
                }
                break;
            case R.id.bold:
                if (!isShowOrNot1) {
                    inputNote.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));// 设置加粗

                    isShowOrNot1 = true;
                } else {
                    inputNote.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));// 设置不加粗
                    isShowOrNot1 = false;
                }
                //       inputNote.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                Toast toast = Toast.makeText(EditNoteActivity.this, "点击了加粗", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case R.id.italic:
                // inputNote.setTextSize(16);
                if (!isShowOrNot2) {
                    inputNote.setTypeface(inputNote.getTypeface(), Typeface.ITALIC);;// 设置斜体

                    isShowOrNot2 = true;
                } else {
                    inputNote.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));// 设置不斜体
                    isShowOrNot2 = false;
                }
                Toast toast1 = Toast.makeText(EditNoteActivity.this, "点击了斜体", Toast.LENGTH_SHORT);
                toast1.show();
                break;
            case R.id.underline:
                //  inputNote.setTextSize(20);
                if (!isShowOrNot3) {
                    inputNote.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); // 设置中划线
                    // inputNote.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    isShowOrNot3 = true;
                } else {
                    inputNote.getPaint().setFlags(0);// 设置不设线
                    // inputNote.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    isShowOrNot3 = false;
                }
                Toast toast2 = Toast.makeText(EditNoteActivity.this, "点击了下划线", Toast.LENGTH_SHORT);
                toast2.show();
                break;
            case R.id.size1:
                inputNote.setTextSize(10);
                break;
            case R.id.size2:
                inputNote.setTextSize(16);
                break;
            case R.id.size3:
                inputNote.setTextSize(24);
                break;
            case R.id.size4:
                inputNote.setTextSize(30);
                break;
            case R.id.bt_black:
                inputNote.setTextColor(Color.BLACK);
                Toast toast3 = Toast.makeText(EditNoteActivity.this, "选择黑色", Toast.LENGTH_SHORT);
                toast3.show();
                break;
            case R.id.bt_red:
                inputNote.setTextColor(Color.RED);
                Toast toast4 = Toast.makeText(EditNoteActivity.this, "选择红色", Toast.LENGTH_SHORT);
                toast4.show();
                break;
            case R.id.bt_yellow:
                inputNote.setTextColor(Color.YELLOW);
                Toast toast5 = Toast.makeText(EditNoteActivity.this, "选择黄色", Toast.LENGTH_SHORT);
                toast5.show();
                break;
            case R.id.bt_blue:
                inputNote.setTextColor(Color.BLUE);
                Toast toast6 = Toast.makeText(EditNoteActivity.this, "选择蓝色", Toast.LENGTH_SHORT);
                toast6.show();
                break;
            case R.id.bt_gray:
                inputNote.setTextColor(Color.GRAY);
                Toast toast7 = Toast.makeText(EditNoteActivity.this, "选择灰色", Toast.LENGTH_SHORT);
                toast7.show();
                break;
            case R.id.bt_green:
                inputNote.setTextColor(Color.GREEN);
                Toast toast8 = Toast.makeText(EditNoteActivity.this, "选择绿色", Toast.LENGTH_SHORT);
                toast8.show();
                break;

        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /* if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                imageView.setImageURI(uri);
            }
        }
    }
*/
        if (resultCode == RESULT_OK) {
            //取得数据
            Uri uri = data.getData();
            ContentResolver cr = EditNoteActivity.this.getContentResolver();
            Bitmap bitmap = null;
            Bundle extras = null;
            //如果是选择照片
            if (requestCode == 2) {
                //将对象存入Bitmap中
                try {
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            int imgWidth = bitmap.getWidth();
            int imgHeight = bitmap.getHeight();
            double partion = imgWidth * 1.0 / imgHeight;
            double sqrtLength = Math.sqrt(partion * partion + 1);
            //新的缩略图大小
            double newImgW = 480 * (partion / sqrtLength);
            double newImgH = 480 * (1 / sqrtLength);
            float scaleW = (float) (newImgW / imgWidth);
            float scaleH = (float) (newImgH / imgHeight);

            Matrix mx = new Matrix();
            //对原图片进行缩放
            mx.postScale(scaleW, scaleH);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, mx, true);
            final ImageSpan imageSpan = new ImageSpan(this, bitmap);
            SpannableString spannableString = new SpannableString("test");
            spannableString.setSpan(imageSpan, 0, spannableString.length(), SpannableString.SPAN_MARK_MARK);
            //光标移到下一行
            inputNote.append("\n");
            Editable editable = inputNote.getEditableText();
            int selectionIndex = inputNote.getSelectionStart();
            spannableString.getSpans(0, spannableString.length(), ImageSpan.class);

            //将图片添加进EditText中
            editable.insert(selectionIndex, spannableString);
            //添加图片后自动空出两行
            inputNote.append("\n\n");
        }
    }


}
