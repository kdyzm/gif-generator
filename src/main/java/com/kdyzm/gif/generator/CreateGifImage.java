package com.kdyzm.gif.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

public class CreateGifImage {

    private static final Logger log = LoggerFactory.getLogger(CreateGifImage.class);

    public synchronized void jpgToGif(File[] pic, String newPic, Map<String, Integer> imageDelayConfig, String defaultDelay) {
        try {
            log.info("一共有" + pic.length + "张图片");
            AnimatedGifEncoder e = new AnimatedGifEncoder();  //请见<a href="http://http://blog.csdn.net/ycb1689/article/details/8071733">本博客文章  </a>           e.setRepeat(0);
            e.start(newPic);
            e.setRepeat(0);
            BufferedImage src[] = new BufferedImage[pic.length];
            log.info("正在生成指定的gif文件！");
            for (int i = 0; i < src.length; i++) {
                String name = pic[i].getName();
                int delay = imageDelayConfig.get(name);
                log.info("正在加载第" + i + "张图片！");
                if(CollectionUtils.isEmpty(imageDelayConfig)){
                    if(StringUtils.isEmpty(defaultDelay)){
                        throw new RuntimeException("delay config can't be null,can config by default.delay properties");
                    }else{
                        e.setDelay(Integer.parseInt(defaultDelay.trim()));
                    }
                }else{
                    e.setDelay((int)(delay*1.4/10.0f)); //设置播放的延迟时间
                }

                src[i] = ImageIO.read(pic[i]); // 读入需要播放的jpg文件
                e.addFrame(src[i]);  //添加到帧中
            }
            log.info("已经成功生成了指定的gif文件！");
            e.finish();
        } catch (Exception e) {
            log.info("jpgToGif Failed:", e);
        }
    }
}
