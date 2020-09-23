package com.github.superzhc.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.superzhc.web.model.Onetab;
import com.github.superzhc.web.service.OnetabService;
import com.github.superzhc.web.utils.EmojiUtils;
import com.github.superzhc.web.utils.LayuiUtils;
import com.github.superzhc.web.utils.Result;

/**
 * 2020年09月05日 superz add
 */
@RestController
@RequestMapping("/onetab")
public class OnetabController
{
    @Autowired
    // private OnetabMapper onetabMapper;
    private OnetabService service;

    @GetMapping("/list")
    public Result list(@RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer limit,
            @RequestParam(required = false) String searchParams) {

        Map<String, Object> params = null;
        if (null == searchParams || "".equals(searchParams))
            params = new HashMap<>();
        else
            params = JSON.parseObject(searchParams).getInnerMap();

        QueryWrapper query = new QueryWrapper();
        // query.allEq(params);// 模糊查询实现不了
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if ("title".equalsIgnoreCase(entry.getKey()) || "url".equalsIgnoreCase(entry.getKey()))
                query.like(entry.getKey(), entry.getValue());
            else
                query.eq(entry.getKey(), entry.getValue());
        }

        Page<Onetab> pageplus = new Page<>(page, limit);
        IPage<Onetab> pageinfo = service.page(pageplus, query);
        return LayuiUtils.table_ok(pageinfo);
        // 分页查询
        // PageHelper.startPage(page, limit);
        // List<Onetab> fundInfos = onetabMapper.selectByCondition(params);
        // PageInfo<Onetab> pageInfo = new PageInfo<Onetab>(fundInfos);
        // return LayuiUtils.table_ok(pageInfo);
    }

    @GetMapping("/detail/{id}")
    public Result get(@PathVariable(name = "id") Integer id) {
        Onetab onetab = service.getById(id);
        return LayuiUtils.data(onetab);
    }

    @PostMapping("/add")
    public Result add(Onetab onetab) {
        boolean b = service.save(onetab);
        return b ? LayuiUtils.msg_ok("新增成功!") : LayuiUtils.msg_error("新增失败!");
    }

    @PostMapping("/update")
    public Result update(Onetab onetab) {
        boolean b = service.updateById(onetab);
        return b ? LayuiUtils.msg_ok("修改成功!") : LayuiUtils.msg_error("修改失败!");
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable(name = "id") Integer id) {
        boolean b = service.removeById(id);
        return b ? LayuiUtils.msg_ok("删除成功!") : LayuiUtils.msg_error("删除失败");
    }

    @DeleteMapping("/deletebatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        boolean b = service.removeByIds(ids);
        return b ? LayuiUtils.msg_ok("删除成功!") : LayuiUtils.msg_error("删除失败");
    }

    @PostMapping("/autoupdate/{id}")
    public Result autoUpdate(@PathVariable(name = "id") Integer id) {
        Onetab onetab = service.getById(id);
        try {
            Document document = Jsoup.connect(onetab.getUrl()).get();
            onetab.setTitle(document.title());
            service.updateById(onetab);
            return LayuiUtils.data(onetab.getTitle());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return LayuiUtils.msg_error("更新异常：" + ex.getMessage());
        }
    }

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return LayuiUtils.msg_error("上传失败，请选择文件");
        }

        Reader reader = null;
        String line = null;
        try {
            reader = new InputStreamReader(file.getInputStream());
            BufferedReader br = new BufferedReader(reader);
            while ((line = br.readLine()) != null) {// 一次读入一行数据
                if (!StringUtils.isEmpty(line)) {
                    String[] ss = line.split(" \\| ");

                    QueryWrapper query = new QueryWrapper();
                    query.eq("url", ss[0]);
                    Onetab model = service.getOne(query);
                    boolean exist = false;
                    if (null == model) {
                        model = new Onetab();
                    }
                    else {
                        exist = true;
                    }
                    model.setUrl(ss[0]);
                    model.setReaded(0);
                    model.setLevel(3);
                    if (ss.length == 2) {
                        model.setTitle(EmojiUtils.emojiChange(ss[1]));
                    }
                    else if (ss.length > 2) {
                        String[] ss2 = Arrays.copyOfRange(ss, 1, ss.length - 1);
                        String title = String.join(" \\| ", ss2);
                        model.setTitle(EmojiUtils.emojiChange(title));
                    }

                    if (!exist)
                        service.save(model);
                    else
                        service.updateById(model);
                }
            }
        }
        catch (Exception ex) {
            System.out.println("异常数据行：" + line);
            ex.printStackTrace();
            return LayuiUtils.msg_error("上传失败，失败原因：" + ex.getMessage());
        }
        finally {
            if (null != reader) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return LayuiUtils.msg_ok("上传成功");
    }
}
