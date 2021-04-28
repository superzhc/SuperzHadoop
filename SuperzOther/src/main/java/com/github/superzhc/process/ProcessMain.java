package com.github.superzhc.process;

import com.github.superzhc.util.ProcessUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author superz
 * @create 2021/4/7 18:20
 */
public class ProcessMain {
    public static void main(String[] args) throws IOException, InterruptedException {
//        Process process=Runtime.getRuntime().exec("ping www.baidu.com");
//        InputStream output= process.getInputStream();
//        InputStream error=process.getErrorStream();

//        Charset charset = Charset.defaultCharset();
//        System.out.println(System.getProperty("file.encoding"));
//        Process process = new ProcessBuilder("ping", "www.baidu.com").redirectErrorStream(true).start();
//        InputStream output = process.getInputStream();
//        BufferedReader outputReader = new BufferedReader(new InputStreamReader(output, "GBK"));
//        String line = null;
//        while ((line = outputReader.readLine()) != null) {
//            System.out.println(line);
//        }
//        int exitCode = process.waitFor();
//        System.out.println(exitCode);

        int exitCode= ProcessUtils.execCommnad("ping www.baidu.com");
        System.out.println(exitCode);
    }
}
