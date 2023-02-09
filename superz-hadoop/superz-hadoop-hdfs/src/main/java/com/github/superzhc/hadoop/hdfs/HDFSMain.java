package com.github.superzhc.hadoop.hdfs;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.jackson.JsonUtils;
import org.apache.commons.io.input.BoundedInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.PositionedReadable;
import org.apache.hadoop.fs.Seekable;
import org.apache.hadoop.io.file.tfile.TFile;
import org.apache.hadoop.yarn.logaggregation.LogToolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author superz
 * @create 2022/5/25 14:16
 **/
public class HDFSMain {
    private static Logger logger= LoggerFactory.getLogger(HDFSMain.class);

    static class SeekableByteArrayInputStream extends ByteArrayInputStream implements Seekable, PositionedReadable {

        public SeekableByteArrayInputStream(byte[] buf)
        {
            super(buf);
        }
        @Override
        public long getPos() throws IOException{
            return pos;
        }

        @Override
        public void seek(long pos) throws IOException {
            if (mark != 0)
                throw new IllegalStateException();

            reset();
            long skipped = skip(pos);

            if (skipped != pos)
                throw new IOException();
        }

        @Override
        public boolean seekToNewSource(long targetPos) throws IOException {
            return false;
        }

        @Override
        public int read(long position, byte[] buffer, int offset, int length) throws IOException {

            if (position >= buf.length)
                throw new IllegalArgumentException();
            if (position + length > buf.length)
                throw new IllegalArgumentException();
            if (length > buffer.length)
                throw new IllegalArgumentException();

            System.arraycopy(buf, (int) position, buffer, offset, length);
            return length;
        }

        @Override
        public void readFully(long position, byte[] buffer) throws IOException {
            read(position, buffer, 0, buffer.length);

        }

        @Override
        public void readFully(long position, byte[] buffer, int offset, int length) throws IOException {
            read(position, buffer, offset, length);
        }
    }

    public static void main(String[] args) throws Exception {
//        MyHdfs myHdfs=new MyHdfs("hdfs://flink-01:9000");
//        myHdfs.open("/user/superz/wordcount.txt");

        HdfsRestApi api = new HdfsRestApi("log-platform01", 50070, "root");
        String path = "/tmp/logs/root/logs-tfile/application_1660794657312_0910/log-platform02_35427_1663297014413";
        String result = api.summary(path);
        JsonNode json=JsonUtils.json(result);
        int length= JsonUtils.integer(json,"ContentSummary","length");

        InputStream in=api.open(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int read;

        while ((read = in.read(buf)) > 0)
            baos.write(buf, 0, read);

        byte data[] = baos.toByteArray();
        SeekableByteArrayInputStream bais = new SeekableByteArrayInputStream(data);
        FSDataInputStream in2=new FSDataInputStream(bais);
        final TFile.Reader inputReader = new TFile.Reader(in2, length, new Configuration());
        TFile.Reader.Scanner scanner = inputReader.createScanner();
        String key, value;

        for (; !scanner.atEnd(); scanner.advance()) {
            final TFile.Reader.Scanner.Entry entry = scanner.entry();

            try (DataInputStream keyIn = entry.getKeyStream()) {
                key = keyIn.readUTF();
            }

            // Extract value depending on the type of entry.
            // Based on org.apache.hadoop.yarn.logaggregation.AggregatedLogFormat.LogReader

            try (DataInputStream valueIn = entry.getValueStream()) {
                if (!key.startsWith("container_")) {
                    value = valueIn.readUTF();
                    logger.info("{}={}",  key, value);
                } else {
                    StringBuilder output=new StringBuilder();

                    String logType = null;
                    boolean eod = false;
                    while (true) {
                        try {
                            logType = valueIn.readUTF();
                        } catch (EOFException x) {
                            eod = true;
                        }
                        if (eod)
                            break;
                        // Copy log contents into a separate file
                        final long len = Long.parseLong(valueIn.readUTF());

                        //output.append(containerStr + "\n");
                        output.append("LogAggregationType: " + logType + "\n");
                        // sb.append(StringUtils.repeat("=", containerStr.length()) + "\n");
                        // sb.append("LogType:" + fileName + "\n");
                        // sb.append("LogLastModifiedTime:" + lastModifiedTime + "\n");
                        output.append("LogLength:" + Long.toString(len) + "\n");
                        output.append("LogContents:\n");
                        try (InputStreamReader inputStreamReader = new InputStreamReader(valueIn)) {
                            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    output.append(line+"\n");
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        logger.info(output.toString());
                    }
                }
            }
        }
    }
}
