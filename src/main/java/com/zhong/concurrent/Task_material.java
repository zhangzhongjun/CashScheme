package com.zhong.concurrent;

import java.util.Collection;

/**
 * 原料的Task
 *
 * @author 张中俊
 **/
public class Task_material {
    private String keyword;
    private Collection<String> filenames;

    public Task_material(String keyword, Collection<String> filenames) {
        this.keyword = keyword;
        this.filenames = filenames;
    }

    public String getKeyword() {
        return keyword;
    }

    public Collection<String> getFilenames() {
        return filenames;
    }
}
