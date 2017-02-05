package drawimage;

import java.util.List;

import org.meteoinfo.data.GridData;
import org.meteoinfo.data.meteodata.MeteoDataInfo;
import org.meteoinfo.data.meteodata.netcdf.NetCDFDataInfo;

import ucar.nc2.Variable;

public class ParseNetCdf {
	
	private static String[] KEY_ARR = {"VIS","IR"};
	private static int WIDTH = 1;
	private static int HEIGHT = 1;
	
    public static GridData parse(String filePath){
        MeteoDataInfo meteoDataInfo = new MeteoDataInfo();
        meteoDataInfo.openNetCDFData(filePath);
        GridData data = meteoDataInfo.getGridData(read(filePath));
        return data;
    }

    public static String read(String str) {
        NetCDFDataInfo netCDFDataInfo = new NetCDFDataInfo();
        netCDFDataInfo.readDataInfo(str);

        List<Variable> dd = netCDFDataInfo.getNCVariables();
        for (Variable vv : dd) {
            for(String key : KEY_ARR){
            	if(key.equals(vv.getName())){
            		return key;
            	}
            }
        }
        return "VIS";
    }
}
