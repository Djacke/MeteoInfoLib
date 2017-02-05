package drawimage;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import javax.print.PrintException;

import org.joda.time.DateTime;
import org.meteoinfo.data.GridData;
import org.meteoinfo.data.mapdata.MapDataManage;
import org.meteoinfo.data.meteodata.DrawMeteoData;
import org.meteoinfo.data.meteodata.MeteoDataInfo;
import org.meteoinfo.layer.MapLayer;
import org.meteoinfo.layer.RasterLayer;
import org.meteoinfo.layout.LayoutGraphic;
import org.meteoinfo.layout.LayoutMap;
import org.meteoinfo.layout.MapLayout;
import org.meteoinfo.legend.ColorBreak;
import org.meteoinfo.legend.MapFrame;
import org.meteoinfo.map.MapView;
import org.meteoinfo.projection.ProjectionInfo;

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
		
		//---- 设置路径变量
		String baseDir = "D:\\四川气象局\\MeteoInfo\\";
		String mapDir = baseDir + "map\\";
		
		//---- 打开图层
		MapLayer bou2Layer = MapDataManage.loadLayer(mapDir + "bou2_4p.shp");
		ColorBreak lb2 = bou2Layer.getLegendScheme().getLegendBreaks().get(0);
		lb2.setColor(Color.gray);
//		lb.OutlineColor = Color.Gray
//		lb.OutlineSize = 1
//		lb.DrawFill = False
		MapLayer bou1Layer = MapDataManage.loadLayer(mapDir + "bou1_4l.shp");
		ColorBreak lb1 = bou1Layer.getLegendScheme().getLegendBreaks().get(0);
		lb1.setColor(Color.red);
		
		MapLayer res1Layer = MapDataManage.loadLayer(mapDir + "res1_4m.shp");
		ColorBreak lb3 = res1Layer.getLegendScheme().getLegendBreaks().get(0);
		lb3.setColor(Color.red);
		res1Layer.setLayerName("NAME");
		
		MapLayout mapLayout = new MapLayout();
//		
//		mapLayout.setOpaque(false);
//		mapLayout.setLayout(null);
		LayoutMap layoutMap = mapLayout.getActiveLayoutMap();
		
//		mapLayout.setVisible(false);
		
		MapFrame mapFrame = layoutMap.getMapFrame();
		mapFrame.setBackColor(new Color(0,0,0,0));

		
		MapView mapView = mapFrame.getMapView();
//		mapView.setOpaque(false);
//		---- 设置Layout
		mapLayout.setPaperSize(540, 550);
//		---- 添加地图图层
//		mapFrame.addLayer(bou2Layer);
//		mapFrame.addLayer(bou1Layer);
//		mapFrame.addLayer(res1Layer);
		
//		---- 设置ActiveLayoutMap（图层显示）
		layoutMap.setDrawGridLine(false);
		layoutMap.setDrawNeatLine(false);
		layoutMap.setDrawGridLabel(false);
		layoutMap.setDrawGridTickLine(false);
		layoutMap.setLeft(10);
		layoutMap.setTop(10);
		layoutMap.setWidth(500);
		layoutMap.setHeight(500);//490
		
//		---- 设置图形标题
		LayoutGraphic aText = mapLayout.addText("Temp",250,50,"黑体",12);
		aText.getGraphic().getLegend().setColor(Color.red);
		
		
//		---- 设置时间
		DateTime sTime = DateTime.now();

		//---- 设置图例
//		String palfn = "D:\\pal\\V-09.pal";
		String palfn = "D:\\pal\\" + "I-010_1.pal";

		//---- 设置MeteoDataInfo
		MeteoDataInfo mid = new MeteoDataInfo();
		//---- 设置数据文件  
//		String inFile = sTime.toString("yyMMddHH") + "00.000";
//		System.out.println(inFile);
		//---- 打开MICAPS数据文件
//		mid.openMICAPSData(vis);
//		ProjectionInfo info = mid.getProjectionInfo();
		mid.openAWXData(vis);
		
		//---- 投影
		mapView.projectLayers(mid.getProjectionInfo());

		//---- 获取云图格点数据
		GridData gData = mid.getGridData("var");
//		GridData tempGrid = new GridData();
//		tempGrid.setValue(gData);
		//---- 生成云图图层
		RasterLayer cloudLayer = DrawMeteoData.createRasterLayer(gData, "Cloud", palfn);
		
		cloudLayer.setProjInfo(mid.getProjectionInfo());
		//---- 添加图层
		mapFrame.addLayer(cloudLayer);
		//---- 缩放至云图范围
		mapView.zoomToExtent(cloudLayer.getExtent());


		//---- 设置标题名称
		String title = "可见光云图(" + DateTime.now().toString("yyyy-MM-dd HH:mm:ss") + ")";
		aText.setLabelText(title); 
		//---- 绘制图形
//		mapLayout.setBackground(new Color(0,0,0,0));
//		mapLayout.paintGraphics();
		//---- 输出图形为文件
//		String outFile = "Cloud_" + sTime.toString("yyyyMMddHH") + ".png";
		String outFile = "Cloud_" + UUID.randomUUID() + ".png";
		System.out.println(outFile);
		try {
			mapLayout.exportToPicture("D:\\data\\" + outFile);
		} catch (PrintException | IOException e) {
			e.printStackTrace();
		}
	}
}
