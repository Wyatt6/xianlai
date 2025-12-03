package fun.xianlai.app.common.service.impl;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import fun.xianlai.app.common.service.CaptchaService;
import fun.xianlai.app.common.service.OptionService;
import fun.xianlai.core.annotation.ServiceLog;
import fun.xianlai.core.annotation.SimpleServiceLog;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.response.DataMap;
import fun.xianlai.core.utils.file.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private OptionService optionService;
    @Lazy
    @Autowired
    private CaptchaService self;

    private Producer getKaptchaProducer(String length) {
//        kaptcha.border 是否有边框 默认为true 我们可以自己设置yes，no
//        kaptcha.border.color   边框颜色   默认为Color.BLACK
//        kaptcha.border.thickness  边框粗细度  默认为1
//        kaptcha.producer.impl   验证码生成器  默认为DefaultKaptcha
//        kaptcha.textproducer.impl   验证码文本生成器  默认为DefaultTextCreator
//        kaptcha.textproducer.char.string   验证码文本字符内容范围  默认为abcde2345678gfynmnpwx
//        kaptcha.textproducer.char.length   验证码文本字符长度  默认为5
//        kaptcha.textproducer.char.space  验证码文本字符间距  默认为2
//        kaptcha.textproducer.font.names    验证码文本字体样式  默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
//        kaptcha.textproducer.font.size   验证码文本字符大小  默认为40
//        kaptcha.textproducer.font.color  验证码文本字符颜色  默认为Color.BLACK
//        kaptcha.noise.impl    验证码噪点生成对象  默认为DefaultNoise
//        kaptcha.noise.color   验证码噪点颜色   默认为Color.BLACK
//        kaptcha.obscurificator.impl   验证码样式引擎  默认为WaterRipple
//        kaptcha.word.impl   验证码文本字符渲染   默认为DefaultWordRenderer
//        kaptcha.background.impl   验证码背景生成器   默认为DefaultBackground
//        kaptcha.background.clear.from   验证码背景颜色渐进   默认为Color.LIGHT_GRAY
//        kaptcha.background.clear.to   验证码背景颜色渐进   默认为Color.WHITE
//        kaptcha.image.width   验证码图片宽度  默认为200
//        kaptcha.image.height  验证码图片高度  默认为50
        Properties props = new Properties();
        props.put("kaptcha.border", "no");
        props.put("kaptcha.textproducer.char.string", "23456789acdefhjkmnprstwxyEFGHJKMNPRSTWXY");
        props.put("kaptcha.textproducer.char.length", length);
        Config config = new Config(props);
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);
        return kaptcha;
    }

    @Override
    @ServiceLog("生成验证码")
    public DataMap generateCaptcha() {
        try {
            Integer length = optionService.readValueInInteger("captcha.length").orElse(5);
            Long expireSeconds = optionService.readValueInLong("captcha.expireSeconds").orElse(60L);

            Producer producer = getKaptchaProducer(String.valueOf(length));
            // 生成验证码KEY、验证码文本，并缓存到Redis
            String captchaKey = UUID.randomUUID().toString().replaceAll("-", "");
            String captchaText = producer.createText();
            redis.opsForValue().set("captcha:" + captchaKey, captchaText, Duration.ofSeconds(expireSeconds));
            // 根据文本生成图片并转成Base64格式
            BufferedImage captchaImage = producer.createImage(captchaText);
            String captchaImageBase64 = null;
            captchaImageBase64 = ImageUtils.bufferedImageToBase64(captchaImage, "jpeg");

            log.info("验证码KEY: {}", captchaKey);
            log.info("验证码: {}", captchaText);
            log.info("过期时长: {}s", expireSeconds);

            DataMap data = new DataMap();
            data.put("captchaKey", captchaKey);
            data.put("captchaImage", captchaImageBase64);
            return data;
        } catch (IOException e) {
            throw new SysException("验证码生成失败");
        }
    }

    @Override
    @SimpleServiceLog("校验验证码")
    public void verifyCaptcha(String captchaKey, String captcha) {
        Object captchaText = redis.opsForValue().get("captcha:" + captchaKey);
        if (captchaText == null) {
            throw new SysException("验证码已失效");
        } else {
            if (captcha.equalsIgnoreCase(String.valueOf(captchaText))) {
                redis.delete("captcha:" + captchaKey);
            } else {
                throw new SysException("验证码错误");
            }
        }
    }
}
