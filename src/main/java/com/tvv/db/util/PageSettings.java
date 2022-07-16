package com.tvv.db.util;

import java.util.Objects;

public class PageSettings {
    private long page;
    private long size;
    private String sort;

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "PageSettings{" +
                "page=" + page +
                ", size=" + size +
                ", sort='" + sort + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageSettings that = (PageSettings) o;
        return page == that.page && size == that.size && Objects.equals(sort, that.sort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, size, sort);
    }
}
