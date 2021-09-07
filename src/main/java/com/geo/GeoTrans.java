package com.geo;

import org.gdal.gdal.gdal;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;

public class GeoTrans {
    public static void main(String[] args) {
        gdal.AllRegister();

        String fileName_tif = "D:\\1.tif";
        Dataset dataset = ReadTif(fileName_tif);
        double[] geo = dataset.GetGeoTransform();
        System.out.println(geo[0]);
        System.out.println(geo[1]);
        System.out.println(geo[2]);
        System.out.println(geo[3]);
        System.out.println(geo[4]);
        System.out.println(geo[5]);
        double temp1[]={390621.67938,2660715.99097};
        double temp2[]={390683.11845,2660736.57008};
        double temp3[]={390729.64512,2660701.07858};
        double temp4[]={390741.57504,2660668.86781};
        double temp5[]={390734.11884,2660648.28870};
        double temp6[]={390654.18841,2660620.55165};
        double[] GeozuoBiao1 = CoordTrans(0, 0, geo);
        double[] GeozuoBiao2 = CoordTrans(3277, 3421, geo);
//        int[] PixelZuobiao1 = CoordTransReverse(GeozuoBiao1[0], GeozuoBiao1[1], geo);
//        int[] PixelZuobiao2 = CoordTransReverse(GeozuoBiao2[0], GeozuoBiao2[1], geo);
        int[] PixelZuobiao1 = CoordTransReverse(temp1[0], temp1[1], geo);
        int[] PixelZuobiao2 = CoordTransReverse(temp2[0], temp2[1], geo);
        int[] PixelZuobiao3 = CoordTransReverse(temp3[0], temp3[1], geo);
        int[] PixelZuobiao4 = CoordTransReverse(temp4[0], temp4[1], geo);
        int[] PixelZuobiao5 = CoordTransReverse(temp5[0], temp5[1], geo);
        int[] PixelZuobiao6 = CoordTransReverse(temp6[0], temp6[1], geo);

//        System.out.println(GeozuoBiao1[0] + "," + GeozuoBiao1[1]);
//        System.out.println(GeozuoBiao2[0] + "," + GeozuoBiao2[1]);
//        System.out.println(PixelZuobiao1[0] + "," + PixelZuobiao1[1]);
//        System.out.println(PixelZuobiao2[0] + "," + PixelZuobiao2[1]);
        System.out.println(PixelZuobiao1[0] + "," + PixelZuobiao1[1]);
        System.out.println(PixelZuobiao2[0] + "," + PixelZuobiao2[1]);
        System.out.println(PixelZuobiao3[0] + "," + PixelZuobiao3[1]);
        System.out.println(PixelZuobiao4[0] + "," + PixelZuobiao4[1]);
        System.out.println(PixelZuobiao5[0] + "," + PixelZuobiao5[1]);
        System.out.println(PixelZuobiao6[0] + "," + PixelZuobiao6[1]);

        Band band = dataset.GetRasterBand(1);
        int nXSize = band.GetXSize();
        int nYSize = band.GetYSize();
        System.out.println(nXSize + "," + nYSize);

        dataset.delete();
        gdal.GDALDestroyDriverManager();
    }

    public static Dataset ReadTif(String fileName) {
        Dataset dataset = gdal.Open(fileName);
        if (dataset == null) {
            System.err.println("GDALOpen Failed - " + gdal.GetLastErrorNo());
            System.err.println(gdal.GetLastErrorMsg());
            System.exit(1);
        }
        return dataset;
    }

    public static int[] CoordTransReverse(double XGeo, double YGeo, double[] GeoTransform) {
        double dTemp = GeoTransform[1] * GeoTransform[5] - GeoTransform[2] * GeoTransform[4];
        double dCol = 0.0, dRow = 0.0;
        dCol = (GeoTransform[5] * (XGeo - GeoTransform[0]) -
                GeoTransform[2] * (YGeo - GeoTransform[3])) / dTemp + 0.5;
        dRow = (GeoTransform[1] * (YGeo - GeoTransform[3]) -
                GeoTransform[4] * (XGeo - GeoTransform[0])) / dTemp + 0.5;
        return new int[]{(int)dCol, (int)dRow};
    }

    public static double[] CoordTrans(long Xpixel, long Ypixel, double[] GeoTransform) {
        double XGeo = GeoTransform[0] + GeoTransform[1] * Xpixel + Ypixel * GeoTransform[2];
        double YGeo = GeoTransform[3] + GeoTransform[4] * Xpixel + Ypixel * GeoTransform[5];
        return new double[]{XGeo, YGeo};
    }
}
