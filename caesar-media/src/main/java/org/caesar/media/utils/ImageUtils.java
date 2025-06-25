package org.caesar.media.utils;

import lombok.extern.log4j.Log4j2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @author peng.guo
 */
@Log4j2
public class ImageUtils {
    /**
     * 保存 base64 编码的图片为本地文件
     *
     * @param base64Data 含或不含 "data:image/xxx;base64," 前缀的 base64 字符串
     * @param outputPath 要保存的文件路径，如 "qrcode.png"
     */
    public static void saveBase64Image(String base64Data, String outputPath) {
        if (base64Data == null || base64Data.isEmpty()) {
            throw new IllegalArgumentException("Base64 数据为空");
        }

        // 如果有 data:image/png;base64, 前缀，先去掉
        if (base64Data.startsWith("data:image")) {
            base64Data = base64Data.substring(base64Data.indexOf(",") + 1);
        }

        byte[] imageBytes = Base64.getDecoder().decode(base64Data);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outputPath);
            fos.write(imageBytes);
            log.info("base64保存图片成功");
        }catch (IOException e){
            log.error("base64保存图片失败:{}",e.getMessage(),e);
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error("关闭文件输出流失败:{}",e.getMessage(),e);
                }
            }

        }
    }
}
