package com.github.superzhc.data.others;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.data.common.HttpData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 酷云EYE
 * 网址：https://eye.kuyun.com/
 *
 * @author superz
 * @create 2022/2/18 16:39
 */
public class EyeKuYun extends HttpData {
    private final static ObjectMapper mapper = new ObjectMapper();

    public List<List<Object>> douyinHotVideo(String date) {
        List<List<Object>> params = new ArrayList<>();
        try {
            // 示例：https://eye.kuyun.com/api/scene/douyin-hot-video?date=20220217
            String url = String.format("https://eye.kuyun.com/api/scene/douyin-hot-video?date=%s", date);
            String data = get(url).data();
            JsonNode node = mapper.readTree(data);
            JsonNode dataNode = node.get("data");
            for (JsonNode item : dataNode) {
                String title = item.get("t").asText();
                String author = item.get("a").asText();
                // 热词
                String hotWords = item.get("hw").asText();
                // 热度
                Long heat = item.get("hv").asLong();
                // 播放量
                Long playCount = item.get("pc").asLong();
                // 点赞
                Long likeCount = item.get("dc").asLong();
                // 评论
                Long commentCount = item.get("cc").asLong();
                // 视频地址
                String videoUrl = item.get("su").asText();

                List<Object> param = new ArrayList<>();
                param.add(date);
                param.add(title);
                param.add(author);
                param.add(hotWords);
                param.add(heat);
                param.add(playCount);
                param.add(likeCount);
                param.add(commentCount);
                param.add(videoUrl);
                params.add(param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/news_dw?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";
        String sql = "INSERT INTO eye_kuyun_douyin_hot_video(riqi,title,author,hot_words,heat,play_count,like_count,comment_count,video_url) VALUES(?,?,?,?,?,?,?,?,?)";

        EyeKuYun kuYun = new EyeKuYun();
        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            LocalDate start = LocalDate.of(2022, 2, 18);
            LocalDate end = LocalDate.now();
            for (; end.isAfter(start); start = start.plusDays(1)) {
                List<List<Object>> params = kuYun.douyinHotVideo(start.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                jdbc.batchUpdate(sql, params);
            }
        }

    }
}
