# NotePad

## 实验要求

阅读NotePad的源代码并做如下扩展：

* NoteList中显示条目增加时间戳显示
* 添加笔记查询功能（根据标题查询）



## 前期准备

要想让源代码在自己的环境运行，要做以下改动：

* 步骤一：这三个文件替换成之前可以运行的项目的同名文件

![](https://github.com/eric-ruhu/MobileApp/blob/master/NotePad/images/%E5%9B%BE%E7%89%871.png?raw=true)

![](https://github.com/eric-ruhu/MobileApp/blob/master/NotePad/images/%E5%9B%BE%E7%89%872.png?raw=true)

![](https://github.com/eric-ruhu/MobileApp/blob/master/NotePad/images/%E5%9B%BE%E7%89%873.png?raw=true)

* 步骤二：删除AndroidMainfest.xml文件中的<uses-sdk android:minSdkVersion="11"/>这句代码

![](https://github.com/eric-ruhu/MobileApp/blob/master/NotePad/images/%E5%9B%BE%E7%89%874.png?raw=true)

![](https://github.com/eric-ruhu/MobileApp/blob/master/NotePad/images/%E5%9B%BE%E7%89%875.png?raw=true)

![](https://github.com/eric-ruhu/MobileApp/blob/master/NotePad/images/%E5%9B%BE%E7%89%876.png?raw=true)

这样就可以在自己的环境上成功运行NotePad源代码！！！



## 应用一——时间戳

* noteslist_item.xml

在noteslist_item.xml布局文件中增加一个TextView来显示时间戳，另一个TextView用于显示笔记标题。因为有两个组件所以要添加一个布局，我这里添加的是LinearLayout（线性布局）。

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">
    <!-- 标题-->
    <TextView
        android:id="@android:id/text1"
        android:layout_width="match_parent"
        android:layout_height="?android:attr/listPreferredItemHeight"
        android:textAppearance="?android:attr/textAppearanceLarge"
        android:gravity="center_vertical"
        android:paddingLeft="5dp"
        android:singleLine="true" />
    <!--时间戳 -->
    <TextView
        android:id="@+id/text2"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:textAppearance="?android:attr/textAppearanceLarge"
        android:textSize="15sp"
        android:gravity="center_vertical"
        android:paddingLeft="10dp"
        android:singleLine="true"
        android:layout_weight="1"
        android:layout_margin="0dp"/>
</LinearLayout>
```

Tip1:

> android:orientation="vertical" ：整体布局一定要是垂直的，水平布局会使笔记标题把时间戳遮住。
>
> android:textAppearance="?android:attr/textAppearanceLarge" ：设置字体外观。



* NoteEditor.java

查看NotePadProvider.java发现数据库中已经存在时间信息：

```
@Override
public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE " + NotePad.Notes.TABLE_NAME + " ("
            + NotePad.Notes._ID + " INTEGER PRIMARY KEY,"
            + NotePad.Notes.COLUMN_NAME_TITLE + " TEXT,"
            + NotePad.Notes.COLUMN_NAME_NOTE + " TEXT,"
            + NotePad.Notes.COLUMN_NAME_CREATE_DATE + " INTEGER,"
            + NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE + " INTEGER"
            + ");");
}
```

在NotePadProvider.java里找到了源代码获取时间的代码：

```
// Gets the current system time in milliseconds
Long now = Long.valueOf(System.currentTimeMillis());
```

本来是想在NotePadProvider.java中修改获取时间的代码，在NoteEditor.java中updateNote()方法进行引用并将其用ContentValues的put方法存入数据库。

但是这样的修改方式非常繁琐！！！

发现了一下直接修改NoteEditor.java中updateNote()方法而NotePadProvider.java无需修改这样的修改方法更为简便！！！

我首先获得了Long类型的now时间，将now转化成Date类型时间，再运用SimpleDateFormat类设置时间类型，dateTime即为转化后的时间格式，最后将其用ContentValues的put方法存入数据库。

***（只粘贴了修改的部分）***

```
private final void updateNote(String text, String title) {

    // Gets the current system time in milliseconds
    Long now = Long.valueOf(System.currentTimeMillis());
    Date date = new Date(now);
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    String dateTime = format.format(date);
    // Sets up a map to contain values to be updated in the provider.
    ContentValues values = new ContentValues();
    values.put(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, dateTime);
		   ...........
  	  (该方法中其他代码无需改变)
}
```



* NotesList.java

为了让时间顺利显示出来我们还要做一些工作。

1、当前Activity所用到的数据被定义在PROJECTION中，所以要将时间显示，首先要在PROJECTION中定义显示的时间的字段。

```
private static final String[] PROJECTION = new String[] {
        NotePad.Notes._ID, // 0
        NotePad.Notes.COLUMN_NAME_TITLE, // 1
        NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE //显示时间
};
```

2、在SimpleCursorAdapter中的参数viewsIDs和dataColumns增加字段描述。

```
// The names of the cursor columns to display in the view, initialized to the title column

String[] dataColumns = { NotePad.Notes.COLUMN_NAME_TITLE , NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE};

// The view IDs that will display the cursor columns, initialized to the TextView in noteslist_item.xml

int[] viewIDs = { android.R.id.text1 , R.id.text2 };
```



* 运行结果

完成以上全部修改就可以显示时间戳了！运行结果如图所示：

![](https://github.com/eric-ruhu/MobileApp/blob/master/NotePad/images/%E5%9B%BE%E7%89%877.png?raw=true)



## 应用二——搜索功能

* list_options_menu.xml

在menu里面添加actionbar的SearchView（搜索框）。

```
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto">
    <!-- 搜索框 -->
    <item
        android:id="@+id/search"
        android:showAsAction="always"
        android:title="@string/search"
        android:actionViewClass="android.widget.SearchView"/>

    <item android:id="@+id/menu_add"
          android:icon="@drawable/ic_menu_compose"
          android:title="@string/menu_add"
          android:alphabeticShortcut='a'
          android:showAsAction="always" />
    <!--  If there is currently data in the clipboard, this adds a PASTE menu item to the menu
          so that the user can paste in the data.. -->
    <item android:id="@+id/menu_paste"
          android:icon="@drawable/ic_menu_compose"
          android:title="@string/menu_paste"
          android:alphabeticShortcut='p' />
</menu>
```

Tip2:

> android:showAsAction="always" ：永远显示在Toolbar中，如果屏幕空间不够则不显示。
>
> android:actionViewClass="android.widget.SearchView" ：定义这个item是一个搜索框。



* NotesList.java

用getActionView方法找到之前定义的item，并设置监听事件。

onQueryTextSubmit和onQueryTextChange两个函数，前者是搜索框点击提交的时候查询，后者是一边输入一边自动查询，这里我选择在onCreateOptionsMenu中对后者重写方法。

***（只粘贴了修改的部分）***

```
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate menu from XML resource
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.list_options_menu, menu);
    // 获取ActionBar上的SearchView
    // 用MenuItem的getActionView()方法获取android:actionViewClass所对应的实例（这里是SerachView）
    SearchView searchview = (SearchView) menu.findItem(R.id.search).getActionView();
    searchview.setSubmitButtonEnabled(true);// 是否显示确认搜索按钮
    searchview.setIconifiedByDefault(true);// 设置展开后图标的样式,这里只有两种,true为图标在搜索框外,false为图标在搜索框内
    searchview.setIconified(true);// 设置
    searchview.setQueryHint("Search");
    searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            Log.i("path location",getIntent().getData().toString());//路径位置
            Log.i("before the judge",s);//判断之前
            Cursor cursor = null;
            String[] dataColumns = {NotePad.Notes.COLUMN_NAME_TITLE, NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE};
            int[] viewIDs = {android.R.id.text1, R.id.text2};
            SimpleCursorAdapter adapter = null;
            if (s.length() != 0 || s != null) {
                Log.i("judge a null", s);//判断空值
                cursor = managedQuery(
                        getIntent().getData(),            // Use the default content URI for the provider.
                        PROJECTION,                       // Return the note ID and title for each note.
                        NotePad.Notes.COLUMN_NAME_TITLE + " LIKE '%" + s + "%'",                             // No where clause, return all records.
                        null,                             // No where clause, therefore no where column values.
                        NotePad.Notes.DEFAULT_SORT_ORDER  // Use the default sort order.
                );
            }
            adapter = new SimpleCursorAdapter(
                    NotesList.this,                             // The Context for the ListView
                    R.layout.noteslist_item,          // Points to the XML for a list item
                    cursor,                           // The cursor to get items from
                    dataColumns,
                    viewIDs
            );
            setListAdapter(adapter);
            return true;
        }
    });

    // Generate any additional actions that can be performed on the
    // overall list.  In a normal install, there are no additional
    // actions found here, but this allows other applications to extend
    // our menu with their own actions.
    Intent intent = new Intent(null, getIntent().getData());
    intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
    menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
            new ComponentName(this, NotesList.class), null, intent, 0, null);
    return super.onCreateOptionsMenu(menu);
}
```



* 运行结果

完成以上全部修改就可以进行搜索了！运行结果如图所示：

![](https://github.com/eric-ruhu/MobileApp/blob/master/NotePad/images/%E5%9B%BE%E7%89%878.png?raw=true)

![](https://github.com/eric-ruhu/MobileApp/blob/master/NotePad/images/%E5%9B%BE%E7%89%879.png?raw=true)

![](https://github.com/eric-ruhu/MobileApp/blob/master/NotePad/images/%E5%9B%BE%E7%89%8710.png?raw=true)