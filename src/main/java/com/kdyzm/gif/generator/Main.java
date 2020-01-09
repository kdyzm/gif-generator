package com.kdyzm.gif.generator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/***
 * sourceDir
 *              -   gif_dir1
 *                      -   img1.jpg
 *                      -   img2.jpg
 *                      ...
 *                      -   animation.json
 *                              -   [{"file":"000000.jpg","delay":39},....]
 *              -   gif_dir2
 *              -   gif_dir3
 *
 * targetDir
 *              -   gif_dir1.gif
 *              -   gif_dir2.gif
 *              -   gif_dir3.gif
 */
public class Main {


    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static final String IMAGE_CONFIG_FILE_NAME = "animation.json";

    public static void main(String args[]) throws IOException {

        Properties properties = readInputParams();
        if (Objects.isNull(properties)) {
            return;
        }
        File sourceDir = new File(properties.getProperty("dir.source"));
        File targetDir = new File(properties.getProperty("dir.target"));
        if (!sourceDir.exists()) {
            System.out.println("源文件夹不存在");
            return;
        }
        File[] allImageSourceDir = getAllFile(sourceDir);
        for (File imageSourceDir : allImageSourceDir) {
            String aimFileName = imageSourceDir.getName() + ".gif";
            CreateGifImage cgi = new CreateGifImage();
            String realName = targetDir.getAbsolutePath() + "\\" + aimFileName;
            File temp = new File(realName);
            if (temp.exists()) {
                log.error("目标文件:{}已经存在，跳过生成", temp.getAbsolutePath());
                continue;
            }
            File[] allImages = getAllImages(imageSourceDir);
            Map<String, Integer> imageDelayConfig = getImageConfig(imageSourceDir);
            cgi.jpgToGif(allImages, realName, imageDelayConfig);
        }
    }

    private static Map<String, Integer> getImageConfig(File temp) {
        File file = new File(temp, IMAGE_CONFIG_FILE_NAME);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String str;
            StringBuilder sb = new StringBuilder();
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
            List<DelayConfigItem> data = new Gson().fromJson(sb.toString(), new TypeToken<List<DelayConfigItem>>() {
            }.getType());
            return data.stream()
                    .collect(Collectors.toMap(DelayConfigItem::getFile, DelayConfigItem::getDelay, (k1, k2) -> k2));
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    private static File[] getAllImages(File temp) {
        return Arrays
                .stream(Objects.requireNonNull(temp.listFiles()))
                .filter(file -> !file.getName().equalsIgnoreCase(IMAGE_CONFIG_FILE_NAME))
                .toArray(File[]::new);
    }

    /**
     * 得到所有的图片文件
     *
     * @param file
     * @return
     */
    private static File[] getAllFile(File file) {
        return Arrays
                .stream(Objects.requireNonNull(file.listFiles()))
                .filter(File::isDirectory)
                .toArray(File[]::new);
    }

    private static Properties readInputParams() {

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File("input.properties")));
        } catch (IOException e) {
            log.error("配置文件不存在", e);
            return null;
        }
        return properties;
    }
}
