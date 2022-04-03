package com.byteattackers.byteattackers;

import android.net.Uri;

import com.google.android.gms.tasks.Task;

public class AudioBook {
    private String filename;
    private String filetype;
    private Task<Uri> url;

    public AudioBook() {
    }

    public AudioBook(String filename, String filetype, Task<Uri> url) {
        this.filename = filename;
        this.filetype = filetype;
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public Task<Uri> getUrl() {
        return url;
    }

    public void setUrl(Task<Uri> url) {
        this.url = url;
    }
}
