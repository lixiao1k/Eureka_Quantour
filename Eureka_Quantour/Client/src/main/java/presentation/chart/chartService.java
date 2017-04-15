package presentation.chart;

import javafx.scene.layout.Pane;

/**
 * Created by huihantao on 2017/3/8.
 */
public interface chartService {

	/**
	 * @Description: you can get a pane contains a chart that you can decide its size
	 *               only if width<=0 or height <=0, it will be initial size
	 * @author: hzp
	 * @time: 2017年4月3日
	 * @return: Pane
	 */
    Pane getchart(int width, int height);

    /**
     * 
     * @Description: you can set the chart's title by input name
     * @author: hzp
     * @time: 2017年4月3日
     * @return: void
     * @exception:
     */
    void setName(String name);
}
