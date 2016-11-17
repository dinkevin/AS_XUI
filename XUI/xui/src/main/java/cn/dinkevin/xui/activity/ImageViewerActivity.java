package cn.dinkevin.xui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import java.util.ArrayList;
import java.util.List;
import cn.dinkevin.xui.R;
import cn.dinkevin.xui.adapter.ViewPagerAdapter;
import cn.dinkevin.xui.finder.ViewFinder;
import cn.dinkevin.xui.image.fresco.ImageDisplay;
import cn.dinkevin.xui.image.fresco.zoomable.ZoomableDraweeView;
import cn.dinkevin.xui.widget.container.LooperViewPager;

/**
 * 图片浏览页面，在 Intent 中需要将图片路径数据源装进 {@value #PICTURE_SOURCE} 格式为 String[],</br>
 * 当前显示的索引位置 {@link #SOURCE_INDEX} int
 * @author chengpengfei
 *
 */
@SuppressLint("InflateParams")
public class ImageViewerActivity extends AbstractActivity{

	/**
	 * 图片数据源传递关键字
	 */
	public static final String PICTURE_SOURCE = "source";

	// 源图片路径
	private int index = 0;
	private String[] pictureSource;
	
	/**
	 * 图片数据源索引
	 */
	public static final String SOURCE_INDEX = "index";
	
	private LooperViewPager view_pager;
	
	private List<View> view_source = new ArrayList<>();
	private ViewPagerAdapter view_adapter;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		
		setTitle("");
		
		view_pager = viewFinder.findViewById(R.id.xui_pager);

		pictureSource = getIntent().getStringArrayExtra(PICTURE_SOURCE);
		index = getIntent().getIntExtra(SOURCE_INDEX, 0);
		if(pictureSource != null && pictureSource.length > 0){
			if(index > pictureSource.length - 1) index = pictureSource.length - 1;

			for(int i = 0; i < pictureSource.length; i++)
			{
				View view = layoutInflater.inflate(R.layout.xui_zoomable_image, null);
				ViewFinder finder = new ViewFinder(view);

				ZoomableDraweeView zoomableView = finder.findViewById(R.id.xui_zoomable_view);
				zoomableView.setAllowTouchInterceptionWhileZoomed(true);
				DraweeController controller = Fresco.newDraweeControllerBuilder()
						.setUri(pictureSource[i])
						.build();
				zoomableView.setController(controller);
				zoomableView.setTapListener(new GestureDetector.SimpleOnGestureListener());
				zoomableView.setHierarchy(ImageDisplay.getHierarchy());

				view_source.add(view);
			}
			
			view_adapter = new ViewPagerAdapter(view_source);
			view_pager.setAdapter(view_adapter);
			view_pager.setCurrentItem(index);
		}
	}

	@Override
	protected int getContentLayout() {
		return R.layout.xui_activity_image_viewer;
	}
}
