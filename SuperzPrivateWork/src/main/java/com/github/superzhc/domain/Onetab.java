package com.github.superzhc.domain;

import javax.persistence.*;

/**
 * 实体与数据库的映射关系
 * @Entity 告诉JPA这是一个实体类（一个和数据表映射的类）
 * @Table 配置当前实体类和哪张表对应；可以省略不写，如果省略默认表名就是admin
 * 2021年01月22日 superz add
 */
@Entity
@Table(name = "onetab")
public class Onetab
{

    /**
     * @Id 代表这是主键
     * @GeneratedValue 主键生成规则 IDENTITY 自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 配置属性和数据库表字段的对应
     * name 数据库表的字段名 可以不写 不写就是字段名就是属性名
     */
    @Column(name = "title")
    private String title;

    @Column
    private String url;

    @Column
    private String level;

    @Column
    private String readed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getReaded() {
        return readed;
    }

    public void setReaded(String readed) {
        this.readed = readed;
    }

    @Override
    public String toString() {
        return "Onetab{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", level='" + level + '\'' +
                ", readed='" + readed + '\'' +
                '}';
    }
}
