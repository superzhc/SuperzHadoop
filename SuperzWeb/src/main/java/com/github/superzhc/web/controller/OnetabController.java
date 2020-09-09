package com.github.superzhc.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.superzhc.web.mapper.OnetabMapper;
import com.github.superzhc.web.model.Onetab;
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
    private OnetabMapper onetabMapper;

    @GetMapping("/list")
    public Result list(@RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer limit,
            @RequestParam(required = false) String searchParams) {

        Map<String, Object> params = null;
        if (null == searchParams || "".equals(searchParams))
            params = new HashMap<>();
        else
            params = JSON.parseObject(searchParams).getInnerMap();

        // 分页查询
        PageHelper.startPage(page, limit);
        List<Onetab> fundInfos = onetabMapper.selectByCondition(params);
        PageInfo<Onetab> pageInfo = new PageInfo<Onetab>(fundInfos);
        return LayuiUtils.table_ok(pageInfo);
    }

    @GetMapping("/detail/{id}")
    public Result get(@PathVariable(name = "id") Integer id) {
        Onetab onetab = onetabMapper.selectByPrimaryKey(id);
        return LayuiUtils.model_ok(onetab);
    }

    @PostMapping("/add")
    public Result add(Onetab onetab) {
        int i = onetabMapper.insert(onetab);
        if (i > 0)
            return LayuiUtils.form_ok("新增成功!");
        else
            return LayuiUtils.form_error("新增失败!");
    }

    @PostMapping("/update")
    public Result update(Onetab onetab) {
        int i = onetabMapper.updateByPrimaryKey(onetab);
        if (i > 0)
            return LayuiUtils.form_ok("修改成功!");
        else
            return LayuiUtils.form_error("修改失败!");
    }

    @PostMapping("/autoupdate/{id}")
    public Result autoUpdate(@PathVariable(name = "id") Integer id) {
        Onetab onetab = onetabMapper.selectByPrimaryKey(id);
        try {
            Document document = Jsoup.connect(onetab.getUrl()).get();
            onetab.setTitle(document.title());
            onetabMapper.updateByPrimaryKey(onetab);
            return LayuiUtils.form_ok(onetab.getTitle());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return LayuiUtils.form_error("更新异常：" + ex.getMessage());
        }
    }

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return LayuiUtils.form_error("上传失败，请选择文件");
        }

        Reader reader = null;
        String line = null;
        try {
            reader = new InputStreamReader(file.getInputStream());
            BufferedReader br = new BufferedReader(reader);
            while ((line = br.readLine()) != null) {// 一次读入一行数据
                if (!StringUtils.isEmpty(line)) {
                    String[] ss = line.split(" \\| ");

                    Onetab model = onetabMapper.selectByUrl(ss[0]);
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
                        onetabMapper.insert(model);
                    else
                        onetabMapper.updateByPrimaryKey(model);
                }
            }
        }
        catch (Exception ex) {
            System.out.println("异常数据行：" + line);
            ex.printStackTrace();
            return LayuiUtils.form_error("上传失败，失败原因：" + ex.getMessage());
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

        return LayuiUtils.form_ok("上传成功");
    }
}
