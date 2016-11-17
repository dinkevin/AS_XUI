package cn.dinkevin.xui.image.fresco;

import android.content.Context;
import android.text.TextUtils;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import java.io.File;

/**
 * 图片加载库
 * Created by ChengPengFei on 2016/11/17 0017.</br>
 */

public final class ImageDisplay {

    private ImageDisplay(){}

    private static Context g_context;

    /**
     * 初始化图片加载库
     * @param context   上下文环境
     * @param imageCacheDir 图片缓存目录
     */
    public static void initial(Context context,String imageCacheDir){
        g_context = context;
        if(!TextUtils.isEmpty(imageCacheDir))
        {
            File root = new File(imageCacheDir);
            DiskCacheConfig cacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryPath(root)
                    .build();

            ImagePipelineConfig imageConfig = ImagePipelineConfig
                    .newBuilder(context)
                    .setMainDiskCacheConfig(cacheConfig)
                    .build();

            Fresco.initialize(context,imageConfig);
        }
        else {
            Fresco.initialize(context);
        }
    }

    private static GenericDraweeHierarchy g_hierarchy;

    /**
     * 获取图片加载库的 Hierarchy
     * @return
     */
    public static GenericDraweeHierarchy getHierarchy(){

        if(null == g_hierarchy){
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(g_context.getResources());
            g_hierarchy = builder
                    .setFadeDuration(300)
                    .setProgressBarImage(new ProgressBarDrawable())
                    .build();
        }
        return g_hierarchy;
    }
}
