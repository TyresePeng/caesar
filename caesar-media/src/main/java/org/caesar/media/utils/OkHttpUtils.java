package org.caesar.media.utils;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author peng.guo
 */
public class OkHttpUtils {

    private static final int TIMEOUT_SECONDS = 100;

    private final OkHttpClient client;

    /***
     * 静态内部类单例实现
     */
    private OkHttpUtils() {
        client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();
    }

    public static OkHttpUtils getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final OkHttpUtils INSTANCE = new OkHttpUtils();
    }

    /**同步 GET 请求，返回响应体字符串*/
    public String getSync(String url, JSONObject params, Map<String, String> headers) throws IOException {
        String fullUrl = buildUrlWithParams(url, params);
        Request.Builder builder = new Request.Builder().url(fullUrl).get();
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(builder::addHeader);
        }
        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response.code() + " with message: " + response.message());
            }
            ResponseBody body = response.body();
            return body != null ? body.string() : null;
        }
    }

    /**同步 POST 请求，返回响应体字符串*/
    public String postSync(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder(StandardCharsets.UTF_8);
        if (params != null) {
            params.forEach(formBuilder::add);
        }
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(formBuilder.build());
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(builder::addHeader);
        }
        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response.code() + " with message: " + response.message());
            }
            ResponseBody body = response.body();
            return body != null ? body.string() : null;
        }
    }

    /**
     * 拼接带参数的URL，自动编码
     */
    private String buildUrlWithParams(String url, JSONObject params) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        sb.append(url.contains("?") ? "&" : "?");
        params.forEach((key, value) -> {
            try {
                sb.append(URLEncoder.encode(key, StandardCharsets.UTF_8.name()))
                        .append("=")
                        .append(URLEncoder.encode(String.valueOf(value), StandardCharsets.UTF_8.name()))
                        .append("&");
            } catch (UnsupportedEncodingException e) {
                // 不应该发生
                throw new RuntimeException(e);
            }
        });
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 拼接带参数的URL，自动编码
     */
    private String buildUrlWithParams(String url, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        sb.append(url.contains("?") ? "&" : "?");
        params.forEach((key, value) -> {
            try {
                sb.append(URLEncoder.encode(key, StandardCharsets.UTF_8.name()))
                  .append("=")
                  .append(URLEncoder.encode(value, StandardCharsets.UTF_8.name()))
                  .append("&");
            } catch (UnsupportedEncodingException e) {
                // 不应该发生
                throw new RuntimeException(e);
            }
        });
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }


    /**GET 请求，支持可选请求头*/
    public void get(String url, Map<String, String> params, Map<String, String> headers, okhttp3.Callback callback) {
        String fullUrl = buildUrlWithParams(url, params);
        Request.Builder builder = new Request.Builder().url(fullUrl).get();
        // 添加请求头
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(builder::addHeader);
        }
        Request request = builder.build();
        client.newCall(request).enqueue(callback);
    }

    /**POST 请求，支持可选请求头*/
    public void post(String url, Map<String, String> params, Map<String, String> headers, okhttp3.Callback callback) {
        FormBody.Builder formBuilder = new FormBody.Builder(StandardCharsets.UTF_8);
        if (params != null) {
            params.forEach(formBuilder::add);
        }
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(formBuilder.build());
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(builder::addHeader);
        }
        Request request = builder.build();
        client.newCall(request).enqueue(callback);
    }

    /***文件上传，支持可选请求头*/
    public void uploadFile(String url, File file, String fileKey, Map<String, String> params, Map<String, String> headers, okhttp3.Callback callback) {
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("File does not exist or is not a file");
        }

        MediaType mediaType = guessMimeType(file.getName());
        RequestBody fileBody = RequestBody.create(file, mediaType);

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multipartBuilder.addFormDataPart(fileKey, file.getName(), fileBody);

        if (params != null) {
            params.forEach(multipartBuilder::addFormDataPart);
        }

        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(multipartBuilder.build());

        if (headers != null && !headers.isEmpty()) {
            headers.forEach(builder::addHeader);
        }

        Request request = builder.build();

        client.newCall(request).enqueue(callback);
    }
    /**
     * GET 请求
     */
    public void get(String url, Map<String, String> params, okhttp3.Callback callback) {
        String fullUrl = buildUrlWithParams(url, params);
        Request request = new Request.Builder()
                .url(fullUrl)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * POST 请求 (application/x-www-form-urlencoded)
     */
    public void post(String url, Map<String, String> params, okhttp3.Callback callback) {
        FormBody.Builder builder = new FormBody.Builder(StandardCharsets.UTF_8);
        if (params != null) {
            params.forEach(builder::add);
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        client.newCall(request).enqueue(callback);
    }


    /**
     * 文件上传（multipart/form-data）
     */
    public void uploadFile(String url, File file, String fileKey, Map<String, String> params, okhttp3.Callback callback) {
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("File does not exist or is not a file");
        }

        MediaType mediaType = guessMimeType(file.getName());
        RequestBody fileBody = RequestBody.create(file, mediaType);

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multipartBuilder.addFormDataPart(fileKey, file.getName(), fileBody);

        if (params != null) {
            params.forEach(multipartBuilder::addFormDataPart);
        }

        Request request = new Request.Builder()
                .url(url)
                .post(multipartBuilder.build())
                .build();

        client.newCall(request).enqueue(callback);
    }

    private MediaType guessMimeType(String filename) {
        String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        switch (ext) {
            case "png": return MediaType.parse("image/png");
            case "jpg":
            case "jpeg": return MediaType.parse("image/jpeg");
            case "gif": return MediaType.parse("image/gif");
            case "txt": return MediaType.parse("text/plain");
            case "pdf": return MediaType.parse("application/pdf");
            // 可扩展更多
            default: return MediaType.parse("application/octet-stream");
        }
    }

    /**
     * 下载文件
     */
    public void downloadFile(String url, String destDir, String fileName, DownloadCallback callback) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    callback.onError(call, response.code(), response.message());
                    return;
                }
                File dir = new File(destDir);
                if (!dir.exists() && !dir.mkdirs()) {
                    callback.onError(call, -1, "Failed to create directory");
                    return;
                }
                File file = new File(dir, fileName);

                try (InputStream is = response.body().byteStream();
                     FileOutputStream fos = new FileOutputStream(file)) {

                    byte[] buffer = new byte[8192];
                    long total = response.body().contentLength();
                    long downloaded = 0;
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        downloaded += len;
                        callback.onProgress(total, downloaded);
                    }
                    fos.flush();
                    callback.onSuccess(call, file);
                } catch (IOException e) {
                    callback.onFailure(call, e);
                }
            }
        });
    }

    /**增加可选的统一请求构造方法*/
    public void request(Consumer<Request.Builder> builderConsumer, okhttp3.Callback callback) {
        Request.Builder builder = new Request.Builder();
        builderConsumer.accept(builder);
        Request request = builder.build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 下载文件回调接口，增加进度
     */
    public interface DownloadCallback {
        void onProgress(long totalBytes, long downloadedBytes);
        void onSuccess(Call call, File file);
        void onFailure(Call call, IOException e);
        void onError(Call call, int code, String errorMsg);
    }
}
