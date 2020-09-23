package com.github.superzhc.web.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * Chrome插件OneTab的标签
 * </p>
 *
 * @author superz
 * @since 2020-09-11
 */
@TableName("onetab")
public class Onetab implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String url;

    private String title;

    /**
     * 文章级别
     */
    private Integer level;

    /**
     * 是否已读
     */
    private Integer readed;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getReaded() {
        return readed;
    }

    public void setReaded(Integer readed) {
        this.readed = readed;
    }

    @Override
    public String toString() {
        return "Onetab{" +
        "id=" + id +
        ", url=" + url +
        ", title=" + title +
        ", level=" + level +
        ", readed=" + readed +
        "}";
    }
}
