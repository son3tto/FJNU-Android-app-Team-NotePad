package com.example.android.notepad.entity;
import org.jetbrains.annotations.NotNull;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 *  笔记在数据库中存储的实体
 */
@Entity(tableName = "notes")
public class Notes {
    @PrimaryKey(autoGenerate = true)
    private int id; // default value
    @ColumnInfo(name = "title")
    private String noteTitle;
    @ColumnInfo(name = "text")
    private String noteText;
    @ColumnInfo(name = "date")
    private long noteDate;
    @ColumnInfo(name = "create_date")
    private long createDate;
    @ColumnInfo(name = "type")
    private String noteType;

    @Ignore // we don't want to store this value on database so ignore it
    private boolean checked = false;


    public Notes(String noteTitle,String noteText, long noteDate ) {
        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.noteDate = noteDate;

    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public long getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(long noteDate) {
        this.noteDate = noteDate;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "id=" + id +
                ", noteTitle='" + noteTitle + '\'' +
                ", noteText='" + noteText + '\'' +
                ", noteDate=" + noteDate +
                ", createDate=" + createDate +
                ", noteType='" + noteType + '\'' +
                ", checked=" + checked +
                '}';
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
