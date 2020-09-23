package com.github.superzhc.web.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author superz
 * @since 2020-09-11
 */
@TableName("book")
public class Book implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId("bookId")
    private String bookId;

    private String fullname;

    private String name;

    private String visiturl;

    private String size;

    private String categoryname;

    @TableField("updateDate")
    private String updateDate;

    private String baidu;

    private String address;


    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVisiturl() {
        return visiturl;
    }

    public void setVisiturl(String visiturl) {
        this.visiturl = visiturl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getBaidu() {
        return baidu;
    }

    public void setBaidu(String baidu) {
        this.baidu = baidu;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Book{" +
        "bookId=" + bookId +
        ", fullname=" + fullname +
        ", name=" + name +
        ", visiturl=" + visiturl +
        ", size=" + size +
        ", categoryname=" + categoryname +
        ", updateDate=" + updateDate +
        ", baidu=" + baidu +
        ", address=" + address +
        "}";
    }
}
