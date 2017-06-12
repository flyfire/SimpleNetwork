package com.solarexsoft.solarexnetwork.entity;

import android.text.TextUtils;

import com.solarexsoft.solarexnetwork.utils.CloseUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 12/06/2017
 *    Desc:
 * </pre>
 */

public class MultiPartEntity implements HttpEntity {
    private final static char[] MULTIPART_CHARS =
            "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    .toCharArray();
    private final String NEW_LINE = "\r\n";
    private final String CONTENT_TYPE = "Content-Type: ";
    private final String CONTENT_DISPOSITION = "Content-Disposition: ";
    /**
     * 文本参数和字符集
     */
    private final String TYPE_TEXT_CHARSET = "text/plain; charset=UTF-8";
    /**
     * 字节流参数
     */
    private final String TYPE_OCTET_STREAM = "application/octet-stream";
    /**
     * 二进制参数
     */
    private final byte[] BINARY_ENCODING = "Content-Transfer-Encoding: binary\r\n\r\n".getBytes();
    /**
     * 文本参数
     */
    private final byte[] BIT_ENCODING = "Content-Transfer-Encoding: 8bit\r\n\r\n".getBytes();
    /**
     * 分隔符
     */
    private String mBoundary = null;
    /**
     * 输出流
     */
    ByteArrayOutputStream mOutputStream = new ByteArrayOutputStream();

    public MultiPartEntity() {
        this.mBoundary = generateBoundary();
    }

    private String generateBoundary() {
        final StringBuilder sb = new StringBuilder();
        final Random random = new Random();
        for (int i = 0; i < 30; i++) {
            sb.append(MULTIPART_CHARS[random.nextInt(MULTIPART_CHARS.length)]);
        }
        return sb.toString();
    }

    public void writeFirstBoundary() throws IOException {
        mOutputStream.write(("--" + mBoundary + "\r\n").getBytes());
    }

    public void addStringPart(final String paramName, final String value) {
        writeToOutputStream(paramName, value.getBytes(), TYPE_TEXT_CHARSET, BIT_ENCODING, "");
    }

    public void addBinaryPart(String paramName, final byte[] rawData) {
        writeToOutputStream(paramName, rawData, TYPE_OCTET_STREAM, BINARY_ENCODING, "no-file");
    }

    public void addFilePart(final String key, final File file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            writeFirstBoundary();
            final String type = CONTENT_TYPE + TYPE_OCTET_STREAM + NEW_LINE;
            mOutputStream.write(getContentDispositionBytes(key, file.getName()));
            mOutputStream.write(type.getBytes());
            mOutputStream.write(BINARY_ENCODING);
            final byte[] tmp = new byte[4096];
            int len = 0;
            while ((len = is.read(tmp)) != -1) {
                mOutputStream.write(tmp, 0, len);
            }
            mOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.close(is);
        }
    }

    private void writeToOutputStream(String paramname, byte[] rawdata, String type_text_charset,
                                     byte[] bit_encoding, String filename) {
        try {
            writeFirstBoundary();
            mOutputStream.write((CONTENT_TYPE + TYPE_TEXT_CHARSET + NEW_LINE).getBytes());
            mOutputStream.write(getContentDispositionBytes(paramname, filename));
            mOutputStream.write(bit_encoding);
            mOutputStream.write(rawdata);
            mOutputStream.write(NEW_LINE.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private byte[] getContentDispositionBytes(String paramname, String filename) {
        StringBuilder sb = new StringBuilder();
        sb.append(CONTENT_DISPOSITION + "form-data; name=\"" + paramname + "\"");
        if (!TextUtils.isEmpty(filename)) {
            sb.append(";filename=\"" + filename + "\"");
        }
        return sb.append(NEW_LINE).toString().getBytes();
    }


    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public long getContentLength() {
        return mOutputStream.toByteArray().length;
    }

    @Override
    public Header getContentType() {
        return new BasicHeader("Content-Type", "multipart/form-data; boundary=" + mBoundary);
    }

    @Override
    public Header getContentEncoding() {
        return null;
    }

    @Override
    public InputStream getContent() throws IOException, IllegalStateException {
        return new ByteArrayInputStream(mOutputStream.toByteArray());
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        final String endString = "--" + mBoundary + "--\r\n";
        mOutputStream.write(endString.getBytes());
        outputStream.write(mOutputStream.toByteArray());
    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    @Override
    public void consumeContent() throws IOException, UnsupportedOperationException {
        if (isStreaming()) {
            throw new UnsupportedOperationException("Streaming entity doesnt implement #consumeContent");
        }

    }
}
