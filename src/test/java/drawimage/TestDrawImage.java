package drawimage;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import javax.print.PrintException;

import org.meteoinfo.data.GridData;
import org.meteoinfo.data.meteodata.DrawMeteoData;
import org.meteoinfo.data.meteodata.MeteoDataInfo;
import org.meteoinfo.layer.RasterLayer;
import org.meteoinfo.layout.LayoutMap;
import org.meteoinfo.layout.MapLayout;
import org.meteoinfo.legend.MapFrame;
import org.meteoinfo.map.MapView;

public class TestDrawImage {

	private static String vis = "D:\\四川气象局\\Z_SATE_C_BAWX_20170203115507_P_FY2E_ANI_IR1_R01_20170203_1030.AWX";
	
	public static void main(String[] args) {
		try {
			exportPicture();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportPicture() throws FileNotFoundException, IOException, Exception{
		
		MapLayout mapLayout = new MapLayout();
		LayoutMap layoutMap = mapLayout.getActiveLayoutMap();
		
		MapFrame mapFrame = layoutMap.getMapFrame();
		mapFrame.setBackColor(new Color(0,0,0,0));

		
		MapView mapView = mapFrame.getMapView();
//		---- 设置Layout
		mapLayout.setPaperSize(540, 550);
//		mapLayout.setPaperSize(267, 267);
		
//		---- 设置ActiveLayoutMap（图层显示）
		layoutMap.setDrawGridLine(false);
		layoutMap.setDrawNeatLine(false);
		layoutMap.setDrawGridLabel(false);
		layoutMap.setDrawGridTickLine(false);
		layoutMap.setLeft(10);
		layoutMap.setTop(10);
		layoutMap.setWidth(500);
		layoutMap.setHeight(500);//490

		//---- 设置图例
		String palfn = "D:\\pal\\" + "I-010_1.pal";

		//---- 设置MeteoDataInfo
		MeteoDataInfo mid = new MeteoDataInfo();
		//---- 设置数据文件  
		mid.openAWXData(vis);
		
		//---- 投影
		mapView.projectLayers(mid.getProjectionInfo());
		//---- 获取云图格点数据
		GridData gData = mid.getGridData("var");
		//---- 生成云图图层
		RasterLayer cloudLayer = DrawMeteoData.createRasterLayer(gData, "Cloud", palfn);
		
		cloudLayer.setProjInfo(mid.getProjectionInfo());
		//---- 添加图层
		mapFrame.addLayer(cloudLayer);
		//---- 缩放至云图范围
		mapView.zoomToExtent(cloudLayer.getExtent());
		//---- 输出图形为文件
		String outFile = "Cloud_" + UUID.randomUUID() + ".png";
		System.out.println(outFile);
		try {
			mapLayout.exportToPicture("D:\\data\\" + outFile);
		} catch (PrintException | IOException e) {
			e.printStackTrace();
		}
	}
}
